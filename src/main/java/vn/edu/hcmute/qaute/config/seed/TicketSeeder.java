package vn.edu.hcmute.qaute.config.seed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ActorType;
import vn.edu.hcmute.qaute.common.constant.AssignmentType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketAssignment;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;
import vn.edu.hcmute.qaute.entity.ticket.TicketStatusHistory;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAssignmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketReviewRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketStatusHistoryRepository;
import vn.edu.hcmute.qaute.service.ticket.TicketCodeGenerator;

@Component
public class TicketSeeder implements DemoSeeder {

    private final TicketRepository ticketRepository;
    private final TicketMessageRepository messageRepository;
    private final TicketStatusHistoryRepository historyRepository;
    private final TicketAssignmentRepository assignmentRepository;
    private final TicketReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TicketCodeGenerator codeGenerator;

    public TicketSeeder(TicketRepository ticketRepository,
                        TicketMessageRepository messageRepository,
                        TicketStatusHistoryRepository historyRepository,
                        TicketAssignmentRepository assignmentRepository,
                        TicketReviewRepository reviewRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository,
                        TicketCodeGenerator codeGenerator) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.historyRepository = historyRepository;
        this.assignmentRepository = assignmentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public int order() {
        return 40;
    }

    @Override
    @Transactional
    public void seed() {
        if (ticketRepository.count() > 0) {
            return;
        }

        List<User> students = userRepository.searchByRoleAndKeyword(RoleCode.STUDENT, null);
        if (students.isEmpty()) {
            throw new IllegalStateException("Chưa có tài khoản sinh viên demo để seed ticket");
        }

        User manager1 = userRepository.findByEmailIgnoreCase("manager1@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager1 demo"));
        User manager2 = userRepository.findByEmailIgnoreCase("manager2@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager2 demo"));
        User manager3 = userRepository.findByEmailIgnoreCase("manager3@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager3 demo"));
        User manager4 = userRepository.findByEmailIgnoreCase("manager4@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager4 demo"));

        Map<String, Category> catMap = new HashMap<>();
        String[] catCodes = {
            "DT_DKHP", "DT_HOCVU", "DT_TOTNGHIEP",
            "CT_HOCBONG", "CT_RENLUYEN",
            "TC_HOCPHI", "TC_MIENGIAM",
            "KTX_DANGKY", "KTX_DICHVU"
        };
        for (String code : catCodes) {
            Category cat = categoryRepository.findByCode(code)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên mục demo: " + code));
            catMap.put(code, cat);
        }

        LocalDateTime now = LocalDateTime.now();
        List<TicketTemplate> templates = buildTemplates();

        for (int i = 0; i < templates.size(); i++) {
            TicketTemplate t = templates.get(i);
            User student = students.get(i % students.size());
            Category category = catMap.get(t.categoryCode);
            User manager = getManagerForCategory(t.categoryCode, manager1, manager2, manager3, manager4);

            LocalDateTime created = now.minusDays(t.daysAgo).minusHours(t.hoursAgo);
            LocalDateTime firstAssigned = null;
            LocalDateTime firstResponse = null;
            LocalDateTime resolved = null;
            LocalDateTime closed = null;
            LocalDateTime lastActivity = created;

            Long assigneeId = null;
            if (t.status != TicketStatus.NEW) {
                if (t.status != TicketStatus.CANCELLED || t.assignedBeforeCancel) {
                    assigneeId = manager.getId();
                    firstAssigned = created.plusHours(1).plusMinutes(10);
                    lastActivity = firstAssigned;
                }
            }

            if (t.status == TicketStatus.IN_PROGRESS || t.status == TicketStatus.WAITING_STUDENT
                    || t.status == TicketStatus.RESOLVED || t.status == TicketStatus.CLOSED) {
                firstResponse = created.plusHours(2).plusMinutes(25);
                lastActivity = firstResponse;
            }

            if (t.status == TicketStatus.WAITING_STUDENT) {
                lastActivity = firstResponse.plusHours(1);
            }

            if (t.status == TicketStatus.RESOLVED || t.status == TicketStatus.CLOSED) {
                resolved = created.plusHours(8).plusMinutes(40);
                lastActivity = resolved;
            }

            if (t.status == TicketStatus.CLOSED) {
                closed = resolved.plusHours(12);
                lastActivity = closed;
            }

            if (t.status == TicketStatus.CANCELLED) {
                lastActivity = created.plusHours(3);
            }

            String code = codeGenerator.next();
            String submitToken = UUID.randomUUID().toString();

            Ticket ticket = Ticket.builder()
                    .code(code)
                    .studentId(student.getId())
                    .categoryId(category.getId())
                    .title(t.title)
                    .descriptionHtml("<p>" + t.studentQuestion + "</p>")
                    .descriptionText(t.studentQuestion)
                    .priority(t.priority)
                    .status(t.status)
                    .urgentRequested(t.urgentRequested)
                    .urgentReason(t.urgentReason)
                    .allowPublicFaq(t.allowPublicFaq)
                    .currentAssigneeId(assigneeId)
                    .firstAssignedAt(firstAssigned)
                    .firstResponseAt(firstResponse)
                    .resolvedAt(resolved)
                    .closedAt(closed)
                    .closeReason(t.status == TicketStatus.CANCELLED ? "Sinh viên tự hủy yêu cầu tư vấn." : (t.status == TicketStatus.CLOSED ? "Sinh viên xác nhận hài lòng và đóng yêu cầu." : null))
                    .resolutionNote(resolved != null ? "Vấn đề đã được tư vấn viên giải quyết và hướng dẫn chi tiết cho sinh viên." : null)
                    .reopenCount(0)
                    .waitingReminderCount(t.status == TicketStatus.WAITING_STUDENT ? 1 : 0)
                    .slaFirstResponseMinutes(480)
                    .slaResolutionMinutes(2880)
                    .slaFirstResponseDueAt(created.plusHours(8))
                    .slaResolutionDueAt(created.plusHours(48))
                    .slaPausedAt(null)
                    .slaPausedSeconds(0L)
                    .slaStatus(t.slaStatus)
                    .lastActivityAt(lastActivity)
                    .submitToken(submitToken)
                    .version(0L)
                    .build();

            ticket = ticketRepository.save(ticket);
            ticketRepository.updateTimestamps(ticket.getId(), created, lastActivity);

            // 1. TicketStatusHistory
            historyRepository.save(TicketStatusHistory.builder()
                    .ticketId(ticket.getId())
                    .fromStatus(null)
                    .toStatus(TicketStatus.NEW)
                    .actorId(student.getId())
                    .actorType(ActorType.STUDENT)
                    .reason("Sinh viên tạo yêu cầu tư vấn")
                    .build());

            if (assigneeId != null) {
                historyRepository.save(TicketStatusHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(TicketStatus.NEW)
                        .toStatus(TicketStatus.ASSIGNED)
                        .actorId(manager.getId())
                        .actorType(ActorType.MANAGER)
                        .reason("Tiếp nhận xử lý yêu cầu")
                        .build());

                // Assignment
                assignmentRepository.save(TicketAssignment.builder()
                        .ticketId(ticket.getId())
                        .fromManagerId(null)
                        .toManagerId(manager.getId())
                        .assignedBy(manager.getId())
                        .type(AssignmentType.CLAIM)
                        .reason("Tư vấn viên nhận xử lý từ hàng chờ")
                        .assignedAt(firstAssigned)
                        .build());
            }

            if (t.status == TicketStatus.IN_PROGRESS || t.status == TicketStatus.WAITING_STUDENT
                    || t.status == TicketStatus.RESOLVED || t.status == TicketStatus.CLOSED) {
                historyRepository.save(TicketStatusHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(TicketStatus.ASSIGNED)
                        .toStatus(TicketStatus.IN_PROGRESS)
                        .actorId(manager.getId())
                        .actorType(ActorType.MANAGER)
                        .reason("Bắt đầu xử lý và phản hồi")
                        .build());
            }

            if (t.status == TicketStatus.WAITING_STUDENT) {
                historyRepository.save(TicketStatusHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(TicketStatus.IN_PROGRESS)
                        .toStatus(TicketStatus.WAITING_STUDENT)
                        .actorId(manager.getId())
                        .actorType(ActorType.MANAGER)
                        .reason("Chờ sinh viên bổ sung thông tin minh chứng")
                        .build());
            }

            if (t.status == TicketStatus.RESOLVED || t.status == TicketStatus.CLOSED) {
                historyRepository.save(TicketStatusHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(TicketStatus.IN_PROGRESS)
                        .toStatus(TicketStatus.RESOLVED)
                        .actorId(manager.getId())
                        .actorType(ActorType.MANAGER)
                        .reason("Đã hướng dẫn và xử lý hoàn tất")
                        .build());
            }

            if (t.status == TicketStatus.CLOSED) {
                historyRepository.save(TicketStatusHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(TicketStatus.RESOLVED)
                        .toStatus(TicketStatus.CLOSED)
                        .actorId(student.getId())
                        .actorType(ActorType.STUDENT)
                        .reason("Sinh viên xác nhận hài lòng")
                        .build());
            }

            if (t.status == TicketStatus.CANCELLED) {
                historyRepository.save(TicketStatusHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(assigneeId != null ? TicketStatus.ASSIGNED : TicketStatus.NEW)
                        .toStatus(TicketStatus.CANCELLED)
                        .actorId(student.getId())
                        .actorType(ActorType.STUDENT)
                        .reason("Sinh viên tự hủy yêu cầu tư vấn")
                        .build());
            }

            // 2. TicketMessage
            // Message 1: Sinh viên
            messageRepository.save(TicketMessage.builder()
                    .ticketId(ticket.getId())
                    .senderId(student.getId())
                    .senderType(SenderType.STUDENT)
                    .contentHtml("<p>" + t.studentQuestion + "</p>")
                    .contentText(t.studentQuestion)
                    .build());

            // Message 2: Tư vấn viên (nếu có)
            if (firstResponse != null) {
                String reply = t.managerReply != null ? t.managerReply : "Chào em, Thầy/Cô đã kiểm tra hệ thống và tiếp nhận thông tin yêu cầu của em.";
                messageRepository.save(TicketMessage.builder()
                        .ticketId(ticket.getId())
                        .senderId(manager.getId())
                        .senderType(SenderType.MANAGER)
                        .contentHtml("<p>" + reply + "</p>")
                        .contentText(reply)
                        .build());
            }

            // Message 3: Sinh viên phản hồi tiếp (nếu có)
            if (t.studentReply != null) {
                messageRepository.save(TicketMessage.builder()
                        .ticketId(ticket.getId())
                        .senderId(student.getId())
                        .senderType(SenderType.STUDENT)
                        .contentHtml("<p>" + t.studentReply + "</p>")
                        .contentText(t.studentReply)
                        .build());
            }

            // Message 4: Tư vấn viên giải đáp kết luận (nếu có)
            if (t.managerConclusion != null) {
                messageRepository.save(TicketMessage.builder()
                        .ticketId(ticket.getId())
                        .senderId(manager.getId())
                        .senderType(SenderType.MANAGER)
                        .contentHtml("<p>" + t.managerConclusion + "</p>")
                        .contentText(t.managerConclusion)
                        .build());
            }

            // 3. TicketReview (nếu có)
            if (t.reviewRating != null) {
                reviewRepository.save(TicketReview.builder()
                        .ticketId(ticket.getId())
                        .studentId(student.getId())
                        .rating(t.reviewRating)
                        .reasonCodes(t.reviewRating >= 4 ? "FAST_RESPONSE,HELPFUL" : "SLOW_RESPONSE")
                        .comment(t.reviewComment)
                        .build());
            }
        }
    }

    private User getManagerForCategory(String code, User m1, User m2, User m3, User m4) {
        if (code.startsWith("DT_")) return m1;
        if (code.startsWith("CT_")) return m2;
        if (code.startsWith("TC_")) return m3;
        return m4;
    }

    private record TicketTemplate(
            String categoryCode,
            String title,
            String studentQuestion,
            TicketStatus status,
            TicketPriority priority,
            int daysAgo,
            int hoursAgo,
            boolean urgentRequested,
            String urgentReason,
            boolean allowPublicFaq,
            SlaStatus slaStatus,
            boolean assignedBeforeCancel,
            String managerReply,
            String studentReply,
            String managerConclusion,
            Integer reviewRating,
            String reviewComment
    ) {}

    private List<TicketTemplate> buildTemplates() {
        List<TicketTemplate> list = new ArrayList<>();

        // 1. DT_DKHP (5 tickets)
        list.add(new TicketTemplate(
                "DT_DKHP", "Xin đăng ký bổ sung học phần Tiếng Anh chuyên ngành",
                "Em chào Thầy/Cô, do đợt đăng ký vừa rồi lớp bị kín chỗ quá nhanh nên em chưa kịp đăng ký môn Tiếng Anh chuyên ngành. Mong Thầy/Cô hỗ trợ mở thêm sĩ số giúp em.",
                TicketStatus.NEW, TicketPriority.NORMAL, 1, 3, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP", "Bị trùng lịch thi giữa kỳ hai môn học phần cùng một buổi",
                "Dạ em xem lịch thi giữa kỳ trên portal thì thấy môn Lập trình mạng và An toàn thông tin bị trùng khung giờ 8h sáng thứ Bảy tuần tới ạ.",
                TicketStatus.IN_PROGRESS, TicketPriority.HIGH, 4, 2, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Phòng Đào tạo đã ghi nhận việc trùng lịch thi của 2 học phần này. Hiện tại khoa CNTT đang xếp lại lịch thi ca 2 cho môn An toàn thông tin.",
                "Dạ em cảm ơn Thầy/Cô nhiều, khi nào có lịch cụ thể Thầy/Cô cập nhật giúp em ạ.", null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP", "Đề nghị mở thêm lớp học phần Đồ án môn học trong kỳ hè",
                "Kính gửi Phòng Đào tạo, hiện tại nhóm sinh viên chúng em gồm 15 bạn có nguyện vọng học môn Đồ án môn học trong học kỳ hè để kịp tiến độ tốt nghiệp.",
                TicketStatus.WAITING_STUDENT, TicketPriority.NORMAL, 8, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Phòng Đào tạo yêu cầu đại diện nhóm lập danh sách gồm: Họ tên, Mã SV, Lớp, Ngành và chữ ký của các bạn gửi đính kèm vào yêu cầu này để Khoa xem xét mở lớp.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP", "Xin hủy đăng ký học phần Kỹ năng mềm do bị trùng lịch thực tập",
                "Em đã đăng ký môn Kỹ năng mềm vào thứ 4 nhưng do vừa nhận được lịch thực tập tại doanh nghiệp cả ngày nên em xin được hủy môn học này.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 20, 5, false, null, true, SlaStatus.ON_TRACK, false,
                "Chào em, do thời hạn hủy môn học trực tuyến đã qua, Thầy đã kiểm tra giấy xác nhận thực tập của em và hỗ trợ hủy học phần trên hệ thống.",
                "Dạ em đã kiểm tra portal và thấy môn học đã được hủy thành công. Em cảm ơn Thầy rất nhiều ạ!",
                "Chúc em có kỳ thực tập hiệu quả và đạt kết quả cao nhé.", 5, "Thầy hỗ trợ giải quyết rất nhanh và nhiệt tình!"
        ));
        list.add(new TicketTemplate(
                "DT_DKHP", "Học phần Đồ án tốt nghiệp bị lỗi không hiển thị trên danh sách đăng ký",
                "Em đã tích lũy đủ 130 tín chỉ theo quy định nhưng khi vào trang đăng ký học phần thì không thấy môn Đồ án tốt nghiệp để chọn ạ.",
                TicketStatus.CLOSED, TicketPriority.NORMAL, 35, 6, false, null, true, SlaStatus.ON_TRACK, false,
                "Chào em, hệ thống vừa cập nhật danh sách xét điều kiện làm Đồ án tốt nghiệp. Thầy đã cấp quyền đăng ký cho tài khoản của em trên hệ thống portal.",
                "Dạ em đã đăng ký thành công rồi ạ, em cảm ơn Thầy ạ.",
                "Yêu cầu đã được hoàn tất. Chúc em bảo vệ đồ án tốt.", 5, "Giải đáp thắc mắc rõ ràng, thao tác kịp thời."
        ));

        // 2. DT_HOCVU (5 tickets)
        list.add(new TicketTemplate(
                "DT_HOCVU", "Xin phúc khảo bài thi kết thúc học phần Xác suất thống kê",
                "Em làm bài thi cuối kỳ môn Xác suất thống kê khá tự tin nhưng điểm công bố chỉ được 4.0. Em xin phép được nộp đơn phúc khảo bài thi ạ.",
                TicketStatus.ASSIGNED, TicketPriority.NORMAL, 2, 5, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU", "Điểm kiểm tra giữa kỳ môn Cơ sở dữ liệu bị nhập sai",
                "Dạ trên portal điểm thi giữa kỳ của em bị ghi 0 điểm, trong khi bài làm của em được Thầy giảng viên chấm 8.5 điểm và đã trả bài trên lớp.",
                TicketStatus.IN_PROGRESS, TicketPriority.NORMAL, 7, 1, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy đã liên hệ với giảng viên phụ trách môn học để đối chiếu bảng điểm gốc. Giảng viên xác nhận có sai sót trong quá trình nhập liệu.",
                "Dạ vâng, em gửi kèm ảnh chụp bài thi có chữ ký và điểm của Thầy giáo ở đây ạ.", null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU", "Hỏi về thủ tục xin tạm hoãn thi học kỳ do ốm nằm viện",
                "Em bị sốt xuất huyết phải nhập viện điều trị từ ngày 12 đến 17, trùng với 2 môn thi cuối kỳ. Em cần chuẩn bị những giấy tờ gì để hoãn thi?",
                TicketStatus.WAITING_STUDENT, TicketPriority.NORMAL, 15, 2, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, em cần chuẩn bị: Giấy ra viện có dấu mộc tròn của bệnh viện tuyến huyện trở lên và Đơn xin hoãn thi theo mẫu của trường.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU", "Xin đính chính thông tin nơi sinh trên bảng điểm tích lũy",
                "Trên bảng điểm tốt nghiệp của em phần nơi sinh ghi là Bình Định, nhưng trong CCCD và giấy khai sinh của em là Quảng Ngãi.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 28, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy đã đối chiếu với hồ sơ nhập học và bản scan CCCD của em. Thông tin nơi sinh đã được đính chính lại trên hệ thống đào tạo.",
                "Dạ em xem trên portal thấy đã sửa đúng thành Quảng Ngãi rồi ạ. Em cảm ơn Thầy!",
                "Không có gì em nhé. Nhớ kiểm tra kỹ lại trước khi in bằng.", 5, "Hỗ trợ sửa thông tin nhanh chóng."
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU", "Không đăng nhập được portal đào tạo sau khi đổi mật khẩu",
                "Em đổi mật khẩu tài khoản sinh viên tối qua nhưng sáng nay đăng nhập portal luôn báo 'Tài khoản hoặc mật khẩu không chính xác'.",
                TicketStatus.CANCELLED, TicketPriority.NORMAL, 10, 1, false, null, false, SlaStatus.ON_TRACK, true,
                null, null, null, null, null
        ));

        // 3. DT_TOTNGHIEP (5 tickets)
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP", "Nộp chứng chỉ TOEIC quốc tế để xét chuẩn đầu ra tốt nghiệp",
                "Em vừa thi đạt chứng chỉ TOEIC 650 điểm tại IIG Việt Nam ngày 10/10. Em gửi hồ sơ scan phiếu điểm để xin xét chuẩn đầu ra đợt tháng 12.",
                TicketStatus.IN_PROGRESS, TicketPriority.NORMAL, 5, 3, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy đã tiếp nhận phiếu điểm TOEIC của em. Phòng Đào tạo sẽ gửi xác thực sang IIG Việt Nam trong đợt đối soát tuần tới.",
                "Dạ thời gian xác thực thường mất bao lâu vậy Thầy?", null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP", "Chưa có tên trong danh sách xét tốt nghiệp đợt tháng 9",
                "Dạ em đã hoàn thành đủ số tín chỉ và nộp đủ các chứng chỉ tin học, ngoại ngữ nhưng trong danh sách công nhận tốt nghiệp dự kiến lại không có tên em.",
                TicketStatus.WAITING_STUDENT, TicketPriority.HIGH, 18, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy kiểm tra thấy em chưa hoàn thành nghĩa vụ học phí học kỳ hè vừa qua nên hệ thống tạm dừng xét tốt nghiệp. Em đã đóng học phí chưa?",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP", "Xin giấy chứng nhận tốt nghiệp tạm thời để bổ sung hồ sơ xin việc",
                "Em đã có tên trong danh sách công nhận tốt nghiệp đợt 3. Do công ty yêu cầu nộp hồ sơ gấp trong tuần này nên em xin cấp giấy chứng nhận tạm thời trước ạ.",
                TicketStatus.RESOLVED, TicketPriority.HIGH, 32, 2, false, null, true, SlaStatus.ON_TRACK, false,
                "Chào em, giấy chứng nhận tốt nghiệp tạm thời của em đã được ký duyệt. Em có thể đến bàn số 2 Phòng Đào tạo để nhận bản chính có mộc đỏ nhé.",
                "Dạ chiều nay em ghé nhận luôn ạ, em cảm ơn Thầy nhiều!",
                "Chúc mừng em đã tốt nghiệp và chúc em thành công trong công việc.", 5, "Xử lý hỗ trợ kịp thời cho sinh viên đi làm."
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP", "Thắc mắc về việc tính điểm xếp loại tốt nghiệp đại học",
                "Điểm tích lũy toàn khóa của em là 3.19, em muốn hỏi trường có quy định làm tròn thành 3.2 để được xếp loại Giỏi hay không ạ?",
                TicketStatus.CLOSED, TicketPriority.LOW, 45, 5, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, theo quy chế đào tạo của Bộ GD&ĐT và nhà trường, điểm trung bình tích lũy toàn khóa lấy chính xác đến 2 chữ số thập phân và không làm tròn.",
                "Dạ em hiểu rồi ạ, em cảm ơn Thầy đã giải thích cặn kẽ.",
                "Chúc em luôn nỗ lực và gặt hái nhiều thành công.", 4, "Tư vấn viên giải thích quy định rõ ràng."
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP", "Xin hoãn nhận bằng tốt nghiệp tại buổi lễ tốt nghiệp",
                "Dạ em có lịch công tác đột xuất vào ngày trường tổ chức Lễ trao bằng tốt nghiệp nên xin phép vắng mặt và đến nhận bằng sau được không ạ?",
                TicketStatus.CLOSED, TicketPriority.LOW, 52, 2, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, hoàn toàn được nhé. Sau đợt lễ tốt nghiệp, em có thể mang CCCD bản gốc đến Phòng Đào tạo (Tòa A) vào các ngày làm việc để nhận bằng.",
                "Dạ em cảm ơn Thầy ạ.", null, 5, "Hài lòng với giải đáp của nhà trường."
        ));

        // 4. CT_HOCBONG (5 tickets)
        list.add(new TicketTemplate(
                "CT_HOCBONG", "Hỏi về điều kiện xét học bổng khuyến khích học tập kỳ 1",
                "Dạ Thầy/Cô cho em hỏi điểm học tập của em là 3.42, điểm rèn luyện là 85 thì có cơ hội nhận học bổng khuyến khích học tập không ạ?",
                TicketStatus.NEW, TicketPriority.HIGH, 2, 1, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG", "Chưa nhận được tiền học bổng doanh nghiệp tài trợ",
                "Em có tên trong danh sách nhận học bổng Doanh nghiệp đồng hành đợt tháng 9 nhưng đến nay tài khoản ngân hàng của em vẫn chưa nhận được tiền.",
                TicketStatus.ASSIGNED, TicketPriority.LOW, 6, 2, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG", "Thay đổi số tài khoản ngân hàng nhận học bổng khuyến khích",
                "Em vừa khóa thẻ ngân hàng cũ do bị mất và mở tài khoản mới tại VietinBank. Em gửi số tài khoản mới để cập nhật nhận học bổng ạ.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 22, 3, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Cô đã cập nhật số tài khoản mới của em vào danh sách chi trả học bổng của đợt này rồi nhé.",
                "Dạ em cảm ơn Cô nhiều lắm ạ!",
                "Em nhớ kiểm tra tài khoản khi có thông báo giải ngân nhé.", 4, "Giải quyết nhanh nhẹn, chu đáo."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG", "Nộp hồ sơ trợ cấp xã hội cho sinh viên mồ côi cả cha lẫn mẹ",
                "Em gửi kèm bản sao công chứng giấy khai sinh, giấy chứng tử của cha mẹ và đơn xin trợ cấp xã hội học kỳ này ạ.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 38, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Cô đã nhận đủ hồ sơ hợp lệ của em. Nhà trường đã duyệt em vào danh sách hưởng trợ cấp xã hội theo học kỳ.",
                "Dạ em cảm ơn Cô và Ban Giám hiệu nhà trường đã luôn tạo điều kiện cho em.",
                "Chúc em luôn vững bước và học tập thật tốt nhé.", 5, "Nhà trường hỗ trợ sinh viên rất tận tình."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG", "Khiếu nại danh sách học bổng khuyến khích học tập bị tính thiếu điểm",
                "Dạ điểm rèn luyện của em được duyệt là 88 điểm nhưng danh sách học bổng dự kiến lại ghi nhầm thành 68 điểm nên em bị rớt học bổng.",
                TicketStatus.CLOSED, TicketPriority.URGENT, 50, 7, true, "Cần đính chính trước khi chốt danh sách trao học bổng", false, SlaStatus.ON_TRACK, false,
                "Chào em, Cô đã kiểm tra với bộ phận quản lý điểm rèn luyện và phát hiện sai sót do nhập liệu. Cô đã cập nhật lại đúng 88 điểm và đưa em vào danh sách học bổng loại Giỏi.",
                "Dạ em mừng quá, em cảm ơn Cô rất nhiều vì đã xử lý gấp cho em ạ!",
                "Yêu cầu đã được đóng thành công. Chúc mừng em nhé.", 5, "Xử lý sự cố rất nhanh và công tâm."
        ));

        // 5. CT_RENLUYEN (5 tickets)
        list.add(new TicketTemplate(
                "CT_RENLUYEN", "Điểm rèn luyện bị thiếu điểm tham gia Ngày hội việc làm",
                "Em có tham gia check-in sự kiện Ngày hội việc làm sinh viên HCMUTE tuần trước nhưng bảng điểm rèn luyện tạm thời chưa được cộng điểm.",
                TicketStatus.IN_PROGRESS, TicketPriority.NORMAL, 3, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Ban tổ chức sự kiện đang tổng hợp file quét mã QR check-in. Cô sẽ đối chiếu danh sách trong ngày mai.",
                "Dạ em gửi ảnh chụp màn hình lúc quét mã check-in thành công tại quầy ạ.", null, null, null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN", "Xin cộng điểm rèn luyện cho hoạt động hiến máu nhân đạo",
                "Em vừa tham gia đợt hiến máu tình nguyện do Hội Chữ thập đỏ trường tổ chức sáng nay. Em gửi kèm giấy chứng nhận hiến máu ạ.",
                TicketStatus.WAITING_STUDENT, TicketPriority.LOW, 9, 3, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, ảnh chụp giấy chứng nhận hiến máu em đính kèm bị mờ không thấy rõ họ tên và ngày tháng. Em vui lòng chụp lại rõ nét hơn giúp Cô nhé.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN", "Không truy cập được trang tự chấm điểm rèn luyện của lớp",
                "Khi em bấm vào link tự đánh giá điểm rèn luyện thì hệ thống báo lỗi 403 Forbidden không có quyền truy cập.",
                TicketStatus.RESOLVED, TicketPriority.HIGH, 25, 2, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, do tài khoản của em chưa được đồng bộ vào lớp sinh hoạt mới. Cô đã phân quyền lại cho em vào lớp 241CTTA rồi nhé.",
                "Dạ em đã vào tự chấm điểm được rồi ạ. Em cảm ơn Cô!",
                "Em nhớ tự chấm và nhắc các bạn trong lớp hoàn thành đúng hạn nhé.", 2, "Hệ thống hay bị lỗi phân quyền, cần khắc phục."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN", "Xin cấp giấy xác nhận điểm rèn luyện để nộp học bổng ngoài trường",
                "Em đang làm hồ sơ học bổng Vallet và cần giấy xác nhận điểm rèn luyện 2 học kỳ gần nhất có mộc của Phòng CTSV ạ.",
                TicketStatus.CLOSED, TicketPriority.NORMAL, 42, 5, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, giấy xác nhận điểm rèn luyện của em đã được in và ký duyệt. Em ghé phòng A201 trong giờ hành chính để nhận nhé.",
                "Dạ sáng nay em đã đến nhận giấy rồi ạ. Em cảm ơn Thầy/Cô nhiều.",
                "Chúc em may mắn và đạt được học bổng nhé.", 5, "Cấp giấy tờ rất nhanh và thuận tiện."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN", "Thắc mắc về tiêu chí đánh giá hoạt động nghiên cứu khoa học",
                "Em có đề tài nghiên cứu khoa học sinh viên được nghiệm thu cấp Khoa loại Tốt thì được cộng bao nhiêu điểm vào mục rèn luyện?",
                TicketStatus.CLOSED, TicketPriority.LOW, 55, 1, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, đề tài NCKH sinh viên nghiệm thu cấp Khoa đạt từ loại Khá trở lên được cộng 08 điểm vào Tiêu chí 2 (Ý thức học tập và nghiên cứu).",
                "Dạ vâng em cảm ơn Cô đã giải đáp rõ ràng.", null, 4, "Hướng dẫn dễ hiểu."
        ));

        // 6. TC_HOCPHI (5 tickets)
        list.add(new TicketTemplate(
                "TC_HOCPHI", "Đã thanh toán học phí qua ngân hàng nhưng portal chưa cập nhật gạch nợ",
                "Em chuyển khoản học phí 8.500.000 VNĐ qua app ngân hàng từ sáng hôm qua nhưng trên cổng sinh viên vẫn báo nợ học phí.",
                TicketStatus.IN_PROGRESS, TicketPriority.URGENT, 3, 2, true, "Sắp hết hạn đóng học phí sợ bị khóa môn học", false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy kiểm tra thấy nội dung chuyển khoản của em bị thiếu số sinh viên nên hệ thống không tự động đối soát được.",
                "Dạ em sơ ý quá, em gửi ảnh chụp biên lai chuyển tiền thành công có mã giao dịch ở đây ạ.", null, null, null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI", "Xin gia hạn thời gian nộp học phí do gia đình gặp khó khăn",
                "Do bố em vừa phẫu thuật nên gia đình em chưa kịp chuẩn bị tiền học phí kỳ này. Em xin được gia hạn thêm 2 tuần để đóng ạ.",
                TicketStatus.WAITING_STUDENT, TicketPriority.NORMAL, 12, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, nhà trường đồng ý hỗ trợ gia hạn. Em cần làm đơn xin gia hạn học phí có chữ ký của phụ huynh và gửi đính kèm vào yêu cầu này.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI", "Xuất hóa đơn điện tử học phí theo mã số thuế của công ty phụ huynh",
                "Em muốn nhờ Phòng Kế hoạch - Tài chính xuất hóa đơn VAT học phí học kỳ này theo tên và mã số thuế công ty của mẹ em để công ty thanh toán.",
                TicketStatus.RESOLVED, TicketPriority.URGENT, 24, 6, true, "Cần hóa đơn để thanh toán công ty trước ngày cuối tháng", false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy đã phát hành hóa đơn điện tử theo thông tin công ty em cung cấp và gửi mã tra cứu hóa đơn về email của em rồi nhé.",
                "Dạ em đã tải được hóa đơn hợp lệ rồi ạ, em cảm ơn Thầy rất nhiều!",
                "Không có gì em nhé. Chúc em học tốt.", 5, "Xử lý hỗ trợ rất chu đáo và đúng hạn."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI", "Thắc mắc về khoản phí bảo hiểm y tế và bảo hiểm tai nạn",
                "Em đã có thẻ BHYT diện hộ nghèo do địa phương cấp thì có được trừ tiền BHYT trong thông báo thu học phí đầu năm không ạ?",
                TicketStatus.CLOSED, TicketPriority.HIGH, 36, 1, false, null, true, SlaStatus.ON_TRACK, false,
                "Chào em, sinh viên đã có thẻ BHYT theo diện chính sách chỉ cần nộp bản photo thẻ BHYT về trường, nhà trường sẽ hoàn lại 100% tiền BHYT đã thu.",
                "Dạ em đã nộp bản photo và nhận được tiền hoàn lại qua tài khoản rồi ạ. Em cảm ơn Thầy!",
                "Yêu cầu đã được đóng. Chúc em sức khỏe.", 4, "Tư vấn nhiệt tình, giải quyết thỏa đáng."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI", "Sai sót số tiền học phí môn thí nghiệm thực hành",
                "Em học môn Thí nghiệm Vật lý 1 tín chỉ nhưng trên bảng học phí lại tính thành học phí của môn 2 tín chỉ.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 44, 3, false, null, false, SlaStatus.BREACHED, false,
                "Chào em, Thầy kiểm tra và thấy có sự nhầm lẫn mã học phần giữa lớp đại trà và chất lượng cao. Thầy đã điều chỉnh lại công nợ về đúng mức học phí.",
                "Dạ em kiểm tra thấy số tiền đã đúng rồi ạ. Em cảm ơn Thầy.",
                "Thành thật xin lỗi em vì sự chậm trễ trong đợt đối soát vừa qua.", 3, "Xử lý được vấn đề nhưng hơi lâu."
        ));

        // 7. TC_MIENGIAM (5 tickets)
        list.add(new TicketTemplate(
                "TC_MIENGIAM", "Hỏi về hồ sơ xin miễn giảm học phí cho con thương binh",
                "Dạ Thầy/Cô cho em hỏi ba em là thương binh hạng 3/4 thì em được miễn giảm bao nhiêu phần trăm học phí và hồ sơ cần những gì ạ?",
                TicketStatus.NEW, TicketPriority.LOW, 1, 6, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM", "Nộp đơn xin rút môn học và hoàn lại tiền học phí",
                "Do em bị tai nạn gãy chân phải nghỉ học 1 tháng nên em làm đơn xin rút bớt 2 môn lý thuyết và xin hoàn phí kỳ này ạ.",
                TicketStatus.ASSIGNED, TicketPriority.NORMAL, 5, 2, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM", "Bổ sung sổ hộ nghèo chứng thực điện tử để xét miễn học phí",
                "Em xin gửi bổ sung bản sao chứng thực điện tử sổ hộ nghèo năm 2026 của UBND xã để hoàn thiện hồ sơ miễn giảm học phí.",
                TicketStatus.IN_PROGRESS, TicketPriority.LOW, 11, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Thầy đã xem file đính kèm. Bản chứng thực điện tử có chữ ký số hợp lệ của UBND xã, hồ sơ của em đã đầy đủ.",
                "Dạ em cảm ơn Thầy nhiều ạ, khi nào có quyết định chính thức ạ?", null, null, null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM", "Thắc mắc về thời gian nhận tiền trợ cấp miễn giảm học phí",
                "Em thuộc diện miễn 100% học phí theo Nghị định 81, em đã nộp hồ sơ từ đầu kỳ nhưng chưa biết khi nào trường chi trả tiền cấp bù?",
                TicketStatus.WAITING_STUDENT, TicketPriority.NORMAL, 26, 3, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, tiền cấp bù học phí do ngân sách Nhà nước rót về theo từng quý. Em gửi lại số tài khoản ngân hàng để đối chiếu danh sách chi trả nhé.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM", "Xin giảm học phí cho 2 anh em ruột cùng học đại học chính quy",
                "Nhà em có 2 anh em ruột cùng học tại trường: anh trai học năm 3 ngành Cơ điện tử và em học năm 1 ngành CNTT. Em xin hướng dẫn thủ tục giảm học phí.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 39, 5, false, null, true, SlaStatus.ON_TRACK, false,
                "Chào em, nhà trường có chính sách giảm 10% học phí cho người em. Thầy đã tiếp nhận giấy khai sinh và duyệt giảm trừ vào học phí kỳ tới cho em rồi nhé.",
                "Dạ em cảm ơn Thầy và nhà trường rất nhiều ạ!",
                "Chúc hai anh em học tập thật tốt nhé.", 5, "Chính sách rất nhân văn, giải quyết nhiệt tình."
        ));

        // 8. KTX_DANGKY (5 tickets)
        list.add(new TicketTemplate(
                "KTX_DANGKY", "Xin đổi phòng ký túc xá do phòng hiện tại bị thấm dột",
                "Em ở phòng 402 KTX khu A, đợt mưa lớn vừa qua trần nhà bị thấm nước ẩm ướt ảnh hưởng sinh hoạt. Em xin được chuyển sang phòng khác ạ.",
                TicketStatus.NEW, TicketPriority.NORMAL, 2, 4, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY", "Tân sinh viên xin đăng ký ở ký túc xá bổ sung đợt 2",
                "Em nhập học đợt bổ sung nên chưa kịp đăng ký KTX đợt 1. Em có hoàn cảnh ở xa (tỉnh Gia Lai), em xin được xét duyệt chỗ ở KTX khu B ạ.",
                TicketStatus.ASSIGNED, TicketPriority.HIGH, 4, 6, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY", "Hỏi về thủ tục trả phòng ký túc xá và nhận lại tiền cọc",
                "Em chuẩn bị đi thực tập tốt nghiệp ở Bình Dương nên cần làm thủ tục trả phòng KTX vào cuối tháng này. Em xin hỏi quy trình trả phòng và nhận lại tiền cọc tài sản.",
                TicketStatus.IN_PROGRESS, TicketPriority.NORMAL, 14, 2, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, em cần báo trước cho trưởng nhà KTX tối thiểu 05 ngày, dọn dẹp phòng sạch sẽ và bàn giao lại chìa khóa, trang thiết bị phòng.",
                "Dạ tiền cọc sẽ được hoàn trả bằng tiền mặt hay chuyển khoản vậy Cô?", null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY", "Đăng ký tạm trú cho sinh viên nội trú ký túc xá",
                "Em cần giấy xác nhận tạm trú tại KTX để làm thủ tục thi bằng lái xe máy tại TP.HCM. Em xin hướng dẫn cách xin giấy ạ.",
                TicketStatus.WAITING_STUDENT, TicketPriority.NORMAL, 21, 1, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, em mang theo CCCD bản gốc và 01 ảnh 3x4 đến văn phòng Ban Quản lý KTX (tầng trệt nhà A1) để Cô làm giấy xác nhận nhé.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY", "Xin gia hạn thời gian đóng tiền phòng ký túc xá học kỳ 2",
                "Em đang đợi tiền phụ cấp từ quê gửi lên nên xin phép được dời lịch đóng tiền phòng KTX thêm 5 ngày so với thông báo.",
                TicketStatus.CLOSED, TicketPriority.NORMAL, 48, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, Ban Quản lý KTX đã ghi nhận và đồng ý cho em gia hạn đóng tiền phòng đến ngày 25 của tháng này.",
                "Dạ em cảm ơn Cô quản lý đã thông cảm cho em ạ.",
                "Em nhớ hoàn thành đúng ngày hẹn nhé.", 5, "Cô quản lý KTX rất thân thiện và thấu hiểu sinh viên."
        ));

        // 9. KTX_DICHVU (5 tickets)
        list.add(new TicketTemplate(
                "KTX_DICHVU", "Phòng 215 KTX khu B bị hỏng vòi nước và mất đèn chiếu sáng",
                "Phòng chúng em bị hỏng công tắc đèn tuýp chính từ tối qua và vòi rửa mặt bị rò rỉ nước liên tục. Mong các chú kỹ thuật hỗ trợ sửa giúp phòng em.",
                TicketStatus.ASSIGNED, TicketPriority.NORMAL, 1, 2, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU", "Mạng wifi tầng 3 nhà A2 KTX bị mất kết nối liên tục",
                "Từ sáng nay wifi sinh viên tại tầng 3 nhà A2 cứ 10 phút lại bị ngắt mạng một lần, ảnh hưởng đến việc học trực tuyến của chúng em.",
                TicketStatus.IN_PROGRESS, TicketPriority.NORMAL, 4, 5, false, null, false, SlaStatus.AT_RISK, false,
                "Chào các em, đội kỹ thuật mạng đã phát hiện cục phát access point tầng 3 bị lỗi nguồn và đang tiến hành thay thế thiết bị mới.",
                "Dạ các chú kỹ thuật đang lên kiểm tra rồi ạ, em cảm ơn Thầy/Cô.", null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU", "Máy giặt tự động tầng trệt nuốt tiền nhưng không chạy",
                "Em quét mã thanh toán 20.000 VNĐ cho máy giặt số 3 tại nhà giặt chung nhưng máy không hoạt động và app đã trừ tiền.",
                TicketStatus.WAITING_STUDENT, TicketPriority.HIGH, 16, 3, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, em gửi giúp Cô ảnh chụp màn hình lịch sử trừ tiền trên app ngân hàng có mã giao dịch để kỹ thuật kiểm tra và hoàn tiền nhé.",
                null, null, null, null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU", "Sửa chữa khóa cửa ban công phòng 510 KTX khu A",
                "Khóa cửa ra ban công phòng em bị kẹt cứng không mở được, gió giật mạnh gây va đập rất nguy hiểm.",
                TicketStatus.RESOLVED, TicketPriority.NORMAL, 30, 4, false, null, false, SlaStatus.ON_TRACK, false,
                "Chào em, chú bảo trì đã đến thay ổ khóa mới cho phòng 510 chiều nay. Em kiểm tra lại xem khóa đã mở êm chưa nhé.",
                "Dạ chú thợ thay khóa rất nhanh và chắc chắn rồi ạ. Em cảm ơn Ban quản lý KTX!",
                "Yêu cầu đã được xử lý xong. Chúc các em sinh hoạt vui vẻ.", 5, "Sửa chữa trang thiết bị rất nhanh chóng."
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU", "Báo mất thẻ xe ký túc xá và xin cấp lại thẻ mới",
                "Em sơ ý làm rơi mất thẻ gửi xe tháng của KTX trong lúc đi học. Em xin hỏi thủ tục làm lại thẻ xe và có bị phạt tiền không ạ?",
                TicketStatus.CANCELLED, TicketPriority.LOW, 19, 2, false, null, false, SlaStatus.ON_TRACK, false,
                null, null, null, null, null
        ));

        return list;
    }
}

