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

    public TicketSeeder(TicketRepository ticketRepository, TicketMessageRepository messageRepository,
            TicketStatusHistoryRepository historyRepository, TicketAssignmentRepository assignmentRepository,
            TicketReviewRepository reviewRepository, UserRepository userRepository,
            CategoryRepository categoryRepository, TicketCodeGenerator codeGenerator) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.historyRepository = historyRepository;
        this.assignmentRepository = assignmentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override public int order() { return 40; }

    @Override
    @Transactional
    public void seed() {
        if (ticketRepository.count() > 0) return;
        List<User> students = userRepository.searchByRoleAndKeyword(RoleCode.STUDENT, null);
        if (students.isEmpty()) throw new IllegalStateException("Chưa có tài khoản sinh viên demo để seed ticket");

        User managerDaoTao = userRepository.findByEmailIgnoreCase("manager.daotao@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Đào tạo"));
        User managerCtsv = userRepository.findByEmailIgnoreCase("manager.ctsv@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Công tác sinh viên"));
        User managerTaiChinh = userRepository.findByEmailIgnoreCase("manager.taichinh@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Kế hoạch - Tài chính"));
        User managerKtx = userRepository.findByEmailIgnoreCase("manager.ktx@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Ban Quản lý Ký túc xá"));
        User managerCntt = userRepository.findByEmailIgnoreCase("manager.cntt@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Trung tâm Thông tin - Máy tính"));
        User managerThuVien = userRepository.findByEmailIgnoreCase("manager.thuvien@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Trung tâm Thông tin - Thư viện"));
        User managerQhdn = userRepository.findByEmailIgnoreCase("manager.qhdn@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Quan hệ Doanh nghiệp"));
        User managerKhcn = userRepository.findByEmailIgnoreCase("manager.khcn@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Khoa học Công nghệ"));
        User managerYte = userRepository.findByEmailIgnoreCase("manager.yte@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Trạm Y tế"));
        User managerCsvc = userRepository.findByEmailIgnoreCase("manager.csvc@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Quản trị Cơ sở vật chất"));
        User managerQlcl = userRepository.findByEmailIgnoreCase("manager.qlcl@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Phòng Khảo thí và Đảm bảo chất lượng"));
        User managerSauDh = userRepository.findByEmailIgnoreCase("manager.saudh@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy quản lý Viện Sau đại học"));

        String[] catCodes = {
            "DT_DKHP", "DT_HOCVU", "DT_TOTNGHIEP", "CT_HOCBONG", "CT_RENLUYEN", "TC_HOCPHI", "TC_MIENGIAM", "KTX_DANGKY", "KTX_DICHVU",
            "DT_DKHP_SAI", "DT_DKHP_HUY", "DT_HOCVU_DIEM", "DT_HOCVU_BAOLUU", "DT_TOTNGHIEP_HOSO",
            "CT_XACNHAN", "CT_HOTRO", "CT_HOCBONG_HOSO", "CT_HOCBONG_KETQUA", "CT_RENLUYEN_DIEM", "CT_RENLUYEN_MINHCHUNG", "CT_XACNHAN_GIAYTO",
            "TC_CHUNGTU", "TC_THANHTOAN", "TC_HOCPHI_DOISOAT", "TC_HOCPHI_GIAHAN", "TC_MIENGIAM_HOSO", "TC_THANHTOAN_LOI",
            "KTX_DANGKY_HOSO", "KTX_DANGKY_GIAHAN", "KTX_DICHVU_SUA", "KTX_DICHVU_DIENNUOC",
            "CNTT_TAIKHOAN", "CNTT_PORTAL", "CNTT_BAOMAT", "CNTT_TAIKHOAN_KHOIPHUC", "CNTT_PORTAL_LOI",
            "TV_MUONTRA", "TV_TRACUU", "TV_TAIKHOAN", "TV_MUONTRA_GIAHAN", "TV_TRACUU_TAILIEU",
            "QHDN_THUCTAP", "QHDN_TUYENDUNG", "QHDN_CV", "QHDN_THUCTAP_HOSO", "QHDN_TUYENDUNG_HOSO",
            "KHCN_DETAI", "KHCN_HOITHAO", "KHCN_NGHIENCUU", "KHCN_DETAI_HOSO", "KHCN_HOITHAO_DANGKY",
            "YTE_BHYT", "YTE_SUCKHOE", "YTE_HOTRO", "YTE_BHYT_HOSO", "YTE_SUCKHOE_TUVAN",
            "CSVC_SUACHUA", "CSVC_PHONGHOC", "CSVC_THIETBI", "CSVC_SUACHUA_BAOHONG", "CSVC_PHONGHOC_DANGKY",
            "QLCL_LICHTHI", "QLCL_PHUCKHAO", "QLCL_KHAOSAT", "QLCL_LICHTHI_TRUNGLICH", "QLCL_PHUCKHAO_HOSO",
            "SDH_TUYENSINH", "SDH_HOSO", "SDH_HOCVU", "SDH_TUYENSINH_HOSO", "SDH_HOSO_BOSUNG"
        };
        Map<String, Category> catMap = new HashMap<>();
        for (String catCode : catCodes) {
            Category category = categoryRepository.findByCode(catCode)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên mục mới: " + catCode));
            catMap.put(catCode, category);
        }

        LocalDateTime now = LocalDateTime.now();
        List<TicketTemplate> templates = buildTemplates();
        for (int i = 0; i < templates.size(); i++) {
            TicketTemplate t = templates.get(i);
            User student = students.get(i % students.size());
            Category category = catMap.get(t.categoryCode());
            User manager = getManagerForCategory(t.categoryCode(), managerDaoTao, managerCtsv, managerTaiChinh,
                    managerKtx, managerCntt, managerThuVien, managerQhdn, managerKhcn, managerYte,
                    managerCsvc, managerQlcl, managerSauDh);
            LocalDateTime created = now.minusDays(t.daysAgo()).minusHours(t.hoursAgo());
            LocalDateTime firstAssigned = null, firstResponse = null, resolved = null, closed = null;
            LocalDateTime lastActivity = created;
            Long assigneeId = null;

            if (t.status() != TicketStatus.NEW && (t.status() != TicketStatus.CANCELLED || t.assignedBeforeCancel())) {
                assigneeId = manager.getId();
                firstAssigned = created.plusHours(1).plusMinutes(10);
                lastActivity = firstAssigned;
            }
            if (t.status() == TicketStatus.IN_PROGRESS || t.status() == TicketStatus.WAITING_STUDENT
                    || t.status() == TicketStatus.RESOLVED || t.status() == TicketStatus.CLOSED) {
                firstResponse = created.plusHours(2).plusMinutes(25);
                lastActivity = firstResponse;
            }
            if (t.status() == TicketStatus.WAITING_STUDENT) lastActivity = firstResponse.plusHours(1);
            if (t.status() == TicketStatus.RESOLVED || t.status() == TicketStatus.CLOSED) {
                resolved = created.plusHours(8).plusMinutes(40);
                lastActivity = resolved;
            }
            if (t.status() == TicketStatus.CLOSED) {
                closed = resolved.plusHours(12);
                lastActivity = closed;
            }
            if (t.status() == TicketStatus.CANCELLED) lastActivity = created.plusHours(3);

            String code = codeGenerator.next();
            String submitToken = UUID.randomUUID().toString();
            Ticket ticket = Ticket.builder()
                    .code(code)
                    .studentId(student.getId())
                    .categoryId(category.getId())
                    .title(t.title())
                    .descriptionHtml("<p>" + t.studentQuestion() + "</p>")
                    .descriptionText(t.studentQuestion())
                    .priority(t.priority())
                    .status(t.status())
                    .urgentRequested(t.urgentRequested())
                    .urgentReason(t.urgentReason())
                    .allowPublicFaq(t.allowPublicFaq())
                    .currentAssigneeId(assigneeId)
                    .firstAssignedAt(firstAssigned)
                    .firstResponseAt(firstResponse)
                    .resolvedAt(resolved)
                    .closedAt(closed)
                    .closeReason(t.status() == TicketStatus.CANCELLED ? "Sinh viên tự hủy yêu cầu tư vấn."
                            : (t.status() == TicketStatus.CLOSED ? "Sinh viên xác nhận hài lòng và đóng yêu cầu." : null))
                    .resolutionNote(resolved != null
                            ? "Vấn đề đã được tư vấn viên giải quyết và hướng dẫn chi tiết cho sinh viên."
                            : null)
                    .reopenCount(0)
                    .waitingReminderCount(t.status() == TicketStatus.WAITING_STUDENT ? 1 : 0)
                    .slaFirstResponseMinutes(480)
                    .slaResolutionMinutes(2880)
                    .slaFirstResponseDueAt(created.plusHours(8))
                    .slaResolutionDueAt(created.plusHours(48))
                    .slaPausedAt(null)
                    .slaPausedSeconds(0L)
                    .slaStatus(t.slaStatus())
                    .lastActivityAt(lastActivity)
                    .submitToken(submitToken)
                    .version(0L)
                    .build();
            ticket = ticketRepository.save(ticket);
            ticketRepository.updateTimestamps(ticket.getId(), created, lastActivity);

            historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId()).fromStatus(null)
                    .toStatus(TicketStatus.NEW).actorId(student.getId()).actorType(ActorType.STUDENT)
                    .reason("Sinh viên tạo yêu cầu tư vấn").build());
            if (assigneeId != null) {
                historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId())
                        .fromStatus(TicketStatus.NEW).toStatus(TicketStatus.ASSIGNED).actorId(manager.getId())
                        .actorType(ActorType.MANAGER).reason("Tiếp nhận xử lý yêu cầu").build());
                assignmentRepository.save(TicketAssignment.builder().ticketId(ticket.getId()).fromManagerId(null)
                        .toManagerId(manager.getId()).assignedBy(manager.getId()).type(AssignmentType.CLAIM)
                        .reason("Tư vấn viên nhận xử lý từ hàng chờ").assignedAt(firstAssigned).build());
            }
            if (t.status() == TicketStatus.IN_PROGRESS || t.status() == TicketStatus.WAITING_STUDENT
                    || t.status() == TicketStatus.RESOLVED || t.status() == TicketStatus.CLOSED) {
                historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId())
                        .fromStatus(TicketStatus.ASSIGNED).toStatus(TicketStatus.IN_PROGRESS).actorId(manager.getId())
                        .actorType(ActorType.MANAGER).reason("Bắt đầu xử lý và phản hồi").build());
            }
            if (t.status() == TicketStatus.WAITING_STUDENT) {
                historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId())
                        .fromStatus(TicketStatus.IN_PROGRESS).toStatus(TicketStatus.WAITING_STUDENT).actorId(manager.getId())
                        .actorType(ActorType.MANAGER).reason("Chờ sinh viên bổ sung thông tin minh chứng").build());
            }
            if (t.status() == TicketStatus.RESOLVED || t.status() == TicketStatus.CLOSED) {
                historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId())
                        .fromStatus(TicketStatus.IN_PROGRESS).toStatus(TicketStatus.RESOLVED).actorId(manager.getId())
                        .actorType(ActorType.MANAGER).reason("Đã hướng dẫn và xử lý hoàn tất").build());
            }
            if (t.status() == TicketStatus.CLOSED) {
                historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId())
                        .fromStatus(TicketStatus.RESOLVED).toStatus(TicketStatus.CLOSED).actorId(student.getId())
                        .actorType(ActorType.STUDENT).reason("Sinh viên xác nhận hài lòng").build());
            }
            if (t.status() == TicketStatus.CANCELLED) {
                historyRepository.save(TicketStatusHistory.builder().ticketId(ticket.getId())
                        .fromStatus(assigneeId != null ? TicketStatus.ASSIGNED : TicketStatus.NEW)
                        .toStatus(TicketStatus.CANCELLED).actorId(student.getId()).actorType(ActorType.STUDENT)
                        .reason("Sinh viên tự hủy yêu cầu tư vấn").build());
            }

            messageRepository.save(TicketMessage.builder().ticketId(ticket.getId()).senderId(student.getId())
                    .senderType(SenderType.STUDENT).contentHtml("<p>" + t.studentQuestion() + "</p>")
                    .contentText(t.studentQuestion()).build());
            if (t.managerReply() != null) {
                messageRepository.save(TicketMessage.builder().ticketId(ticket.getId()).senderId(manager.getId())
                        .senderType(SenderType.MANAGER).contentHtml("<p>" + t.managerReply() + "</p>")
                        .contentText(t.managerReply()).build());
            }
            if (t.studentReply() != null) {
                messageRepository.save(TicketMessage.builder().ticketId(ticket.getId()).senderId(student.getId())
                        .senderType(SenderType.STUDENT).contentHtml("<p>" + t.studentReply() + "</p>")
                        .contentText(t.studentReply()).build());
            }
            if (t.managerConclusion() != null) {
                messageRepository.save(TicketMessage.builder().ticketId(ticket.getId()).senderId(manager.getId())
                        .senderType(SenderType.MANAGER).contentHtml("<p>" + t.managerConclusion() + "</p>")
                        .contentText(t.managerConclusion()).build());
            }
            if (t.reviewRating() != null) {
                reviewRepository.save(TicketReview.builder().ticketId(ticket.getId()).studentId(student.getId())
                        .rating(t.reviewRating()).reasonCodes(t.reviewRating() >= 4 ? "FAST_RESPONSE,HELPFUL" : "SLOW_RESPONSE")
                        .comment(t.reviewComment()).build());
            }
        }
    }


    private User getManagerForCategory(String code,
            User managerDaoTao, User managerCtsv, User managerTaiChinh, User managerKtx,
            User managerCntt, User managerThuVien, User managerQhdn, User managerKhcn,
            User managerYte, User managerCsvc, User managerQlcl, User managerSauDh) {
        if (code.startsWith("DT_")) return managerDaoTao;
        if (code.startsWith("CT_")) return managerCtsv;
        if (code.startsWith("TC_")) return managerTaiChinh;
        if (code.startsWith("KTX_")) return managerKtx;
        if (code.startsWith("CNTT_")) return managerCntt;
        if (code.startsWith("TV_")) return managerThuVien;
        if (code.startsWith("QHDN_")) return managerQhdn;
        if (code.startsWith("KHCN_")) return managerKhcn;
        if (code.startsWith("YTE_")) return managerYte;
        if (code.startsWith("CSVC_")) return managerCsvc;
        if (code.startsWith("QLCL_")) return managerQlcl;
        if (code.startsWith("SDH_")) return managerSauDh;
        throw new IllegalStateException("Không ánh xạ được quản lý cho chuyên mục: " + code);
    }

    private record TicketTemplate(String categoryCode, String title, String studentQuestion, TicketStatus status,
            TicketPriority priority, int daysAgo, int hoursAgo, boolean urgentRequested, String urgentReason,
            boolean allowPublicFaq, SlaStatus slaStatus, boolean assignedBeforeCancel, String managerReply,
            String studentReply, String managerConclusion, Integer reviewRating, String reviewComment) {}



    private List<TicketTemplate> buildTemplates() {
        List<TicketTemplate> list = new ArrayList<>();

        // 45 kịch bản gốc còn phù hợp, giữ nguyên nội dung từng tình huống.
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Xin đăng ký bổ sung học phần Tiếng Anh chuyên ngành",
                "Em chào Thầy/Cô, do đợt đăng ký vừa rồi lớp bị kín chỗ quá nhanh nên em chưa kịp đăng ký môn Tiếng Anh chuyên ngành. Mong Thầy/Cô hỗ trợ mở thêm sĩ số giúp em.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Bị trùng lịch thi giữa kỳ hai môn học phần cùng một buổi",
                "Dạ em xem lịch thi giữa kỳ trên portal thì thấy môn Lập trình mạng và An toàn thông tin bị trùng khung giờ 8h sáng thứ Bảy tuần tới ạ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                4,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Phòng Đào tạo đã ghi nhận việc trùng lịch thi của 2 học phần này. Hiện tại khoa CNTT đang xếp lại lịch thi ca 2 cho môn An toàn thông tin.",
                "Dạ em cảm ơn Thầy/Cô nhiều, khi nào có lịch cụ thể Thầy/Cô cập nhật giúp em ạ.",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Đề nghị mở thêm lớp học phần Đồ án môn học trong kỳ hè",
                "Kính gửi Phòng Đào tạo, hiện tại nhóm sinh viên chúng em gồm 15 bạn có nguyện vọng học môn Đồ án môn học trong học kỳ hè để kịp tiến độ tốt nghiệp.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                8,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Phòng Đào tạo yêu cầu đại diện nhóm lập danh sách gồm: Họ tên, Mã SV, Lớp, Ngành và chữ ký của các bạn gửi đính kèm vào yêu cầu này để Khoa xem xét mở lớp.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Xin hủy đăng ký học phần Kỹ năng mềm do bị trùng lịch thực tập",
                "Em đã đăng ký môn Kỹ năng mềm vào thứ 4 nhưng do vừa nhận được lịch thực tập tại doanh nghiệp cả ngày nên em xin được hủy môn học này.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                20,
                5,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, do thời hạn hủy môn học trực tuyến đã qua, Thầy đã kiểm tra giấy xác nhận thực tập của em và hỗ trợ hủy học phần trên hệ thống.",
                "Dạ em đã kiểm tra portal và thấy môn học đã được hủy thành công. Em cảm ơn Thầy rất nhiều ạ!",
                "Chúc em có kỳ thực tập hiệu quả và đạt kết quả cao nhé.",
                5,
                "Thầy hỗ trợ giải quyết rất nhanh và nhiệt tình!"
        ));
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Học phần Đồ án tốt nghiệp bị lỗi không hiển thị trên danh sách đăng ký",
                "Em đã tích lũy đủ 130 tín chỉ theo quy định nhưng khi vào trang đăng ký học phần thì không thấy môn Đồ án tốt nghiệp để chọn ạ.",
                TicketStatus.CLOSED,
                TicketPriority.NORMAL,
                35,
                6,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, hệ thống vừa cập nhật danh sách xét điều kiện làm Đồ án tốt nghiệp. Thầy đã cấp quyền đăng ký cho tài khoản của em trên hệ thống portal.",
                "Dạ em đã đăng ký thành công rồi ạ, em cảm ơn Thầy ạ.",
                "Yêu cầu đã được hoàn tất. Chúc em bảo vệ đồ án tốt.",
                5,
                "Giải đáp thắc mắc rõ ràng, thao tác kịp thời."
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Xin phúc khảo bài thi kết thúc học phần Xác suất thống kê",
                "Em làm bài thi cuối kỳ môn Xác suất thống kê khá tự tin nhưng điểm công bố chỉ được 4.0. Em xin phép được nộp đơn phúc khảo bài thi ạ.",
                TicketStatus.ASSIGNED,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Điểm kiểm tra giữa kỳ môn Cơ sở dữ liệu bị nhập sai",
                "Dạ trên portal điểm thi giữa kỳ của em bị ghi 0 điểm, trong khi bài làm của em được Thầy giảng viên chấm 8.5 điểm và đã trả bài trên lớp.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                7,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy đã liên hệ với giảng viên phụ trách môn học để đối chiếu bảng điểm gốc. Giảng viên xác nhận có sai sót trong quá trình nhập liệu.",
                "Dạ vâng, em gửi kèm ảnh chụp bài thi có chữ ký và điểm của Thầy giáo ở đây ạ.",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Hỏi về thủ tục xin tạm hoãn thi học kỳ do ốm nằm viện",
                "Em bị sốt xuất huyết phải nhập viện điều trị từ ngày 12 đến 17, trùng với 2 môn thi cuối kỳ. Em cần chuẩn bị những giấy tờ gì để hoãn thi?",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                15,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, em cần chuẩn bị: Giấy ra viện có dấu mộc tròn của bệnh viện tuyến huyện trở lên và Đơn xin hoãn thi theo mẫu của trường.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Xin đính chính thông tin nơi sinh trên bảng điểm tích lũy",
                "Trên bảng điểm tốt nghiệp của em phần nơi sinh ghi là Bình Định, nhưng trong CCCD và giấy khai sinh của em là Quảng Ngãi.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                28,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy đã đối chiếu với hồ sơ nhập học và bản scan CCCD của em. Thông tin nơi sinh đã được đính chính lại trên hệ thống đào tạo.",
                "Dạ em xem trên portal thấy đã sửa đúng thành Quảng Ngãi rồi ạ. Em cảm ơn Thầy!",
                "Không có gì em nhé. Nhớ kiểm tra kỹ lại trước khi in bằng.",
                5,
                "Hỗ trợ sửa thông tin nhanh chóng."
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Không đăng nhập được portal đào tạo sau khi đổi mật khẩu",
                "Em đổi mật khẩu tài khoản sinh viên tối qua nhưng sáng nay đăng nhập portal luôn báo 'Tài khoản hoặc mật khẩu không chính xác'.",
                TicketStatus.CANCELLED,
                TicketPriority.NORMAL,
                10,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                true,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Nộp chứng chỉ TOEIC quốc tế để xét chuẩn đầu ra tốt nghiệp",
                "Em vừa thi đạt chứng chỉ TOEIC 650 điểm tại IIG Việt Nam ngày 10/10. Em gửi hồ sơ scan phiếu điểm để xin xét chuẩn đầu ra đợt tháng 12.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy đã tiếp nhận phiếu điểm TOEIC của em. Phòng Đào tạo sẽ gửi xác thực sang IIG Việt Nam trong đợt đối soát tuần tới.",
                "Dạ thời gian xác thực thường mất bao lâu vậy Thầy?",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Chưa có tên trong danh sách xét tốt nghiệp đợt tháng 9",
                "Dạ em đã hoàn thành đủ số tín chỉ và nộp đủ các chứng chỉ tin học, ngoại ngữ nhưng trong danh sách công nhận tốt nghiệp dự kiến lại không có tên em.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.HIGH,
                18,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy kiểm tra thấy em chưa hoàn thành nghĩa vụ học phí học kỳ hè vừa qua nên hệ thống tạm dừng xét tốt nghiệp. Em đã đóng học phí chưa?",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Xin giấy chứng nhận tốt nghiệp tạm thời để bổ sung hồ sơ xin việc",
                "Em đã có tên trong danh sách công nhận tốt nghiệp đợt 3. Do công ty yêu cầu nộp hồ sơ gấp trong tuần này nên em xin cấp giấy chứng nhận tạm thời trước ạ.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                32,
                2,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, giấy chứng nhận tốt nghiệp tạm thời của em đã được ký duyệt. Em có thể đến bàn số 2 Phòng Đào tạo để nhận bản chính có mộc đỏ nhé.",
                "Dạ chiều nay em ghé nhận luôn ạ, em cảm ơn Thầy nhiều!",
                "Chúc mừng em đã tốt nghiệp và chúc em thành công trong công việc.",
                5,
                "Xử lý hỗ trợ kịp thời cho sinh viên đi làm."
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Thắc mắc về việc tính điểm xếp loại tốt nghiệp đại học",
                "Điểm tích lũy toàn khóa của em là 3.19, em muốn hỏi trường có quy định làm tròn thành 3.2 để được xếp loại Giỏi hay không ạ?",
                TicketStatus.CLOSED,
                TicketPriority.LOW,
                45,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, theo quy chế đào tạo của Bộ GD&ĐT và nhà trường, điểm trung bình tích lũy toàn khóa lấy chính xác đến 2 chữ số thập phân và không làm tròn.",
                "Dạ em hiểu rồi ạ, em cảm ơn Thầy đã giải thích cặn kẽ.",
                "Chúc em luôn nỗ lực và gặt hái nhiều thành công.",
                4,
                "Tư vấn viên giải thích quy định rõ ràng."
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Xin hoãn nhận bằng tốt nghiệp tại buổi lễ tốt nghiệp",
                "Dạ em có lịch công tác đột xuất vào ngày trường tổ chức Lễ trao bằng tốt nghiệp nên xin phép vắng mặt và đến nhận bằng sau được không ạ?",
                TicketStatus.CLOSED,
                TicketPriority.LOW,
                52,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, hoàn toàn được nhé. Sau đợt lễ tốt nghiệp, em có thể mang CCCD bản gốc đến Phòng Đào tạo (Tòa A) vào các ngày làm việc để nhận bằng.",
                "Dạ em cảm ơn Thầy ạ.",
                null,
                5,
                "Hài lòng với giải đáp của nhà trường."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Hỏi về điều kiện xét học bổng khuyến khích học tập kỳ 1",
                "Dạ Thầy/Cô cho em hỏi điểm học tập của em là 3.42, điểm rèn luyện là 85 thì có cơ hội nhận học bổng khuyến khích học tập không ạ?",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Chưa nhận được tiền học bổng doanh nghiệp tài trợ",
                "Em có tên trong danh sách nhận học bổng Doanh nghiệp đồng hành đợt tháng 9 nhưng đến nay tài khoản ngân hàng của em vẫn chưa nhận được tiền.",
                TicketStatus.ASSIGNED,
                TicketPriority.LOW,
                6,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Thay đổi số tài khoản ngân hàng nhận học bổng khuyến khích",
                "Em vừa khóa thẻ ngân hàng cũ do bị mất và mở tài khoản mới tại VietinBank. Em gửi số tài khoản mới để cập nhật nhận học bổng ạ.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                22,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Cô đã cập nhật số tài khoản mới của em vào danh sách chi trả học bổng của đợt này rồi nhé.",
                "Dạ em cảm ơn Cô nhiều lắm ạ!",
                "Em nhớ kiểm tra tài khoản khi có thông báo giải ngân nhé.",
                4,
                "Giải quyết nhanh nhẹn, chu đáo."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Nộp hồ sơ trợ cấp xã hội cho sinh viên mồ côi cả cha lẫn mẹ",
                "Em gửi kèm bản sao công chứng giấy khai sinh, giấy chứng tử của cha mẹ và đơn xin trợ cấp xã hội học kỳ này ạ.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                38,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Cô đã nhận đủ hồ sơ hợp lệ của em. Nhà trường đã duyệt em vào danh sách hưởng trợ cấp xã hội theo học kỳ.",
                "Dạ em cảm ơn Cô và Ban Giám hiệu nhà trường đã luôn tạo điều kiện cho em.",
                "Chúc em luôn vững bước và học tập thật tốt nhé.",
                5,
                "Nhà trường hỗ trợ sinh viên rất tận tình."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Khiếu nại danh sách học bổng khuyến khích học tập bị tính thiếu điểm",
                "Dạ điểm rèn luyện của em được duyệt là 88 điểm nhưng danh sách học bổng dự kiến lại ghi nhầm thành 68 điểm nên em bị rớt học bổng.",
                TicketStatus.CLOSED,
                TicketPriority.URGENT,
                50,
                7,
                true,
                "Cần đính chính trước khi chốt danh sách trao học bổng",
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Cô đã kiểm tra với bộ phận quản lý điểm rèn luyện và phát hiện sai sót do nhập liệu. Cô đã cập nhật lại đúng 88 điểm và đưa em vào danh sách học bổng loại Giỏi.",
                "Dạ em mừng quá, em cảm ơn Cô rất nhiều vì đã xử lý gấp cho em ạ!",
                "Yêu cầu đã được đóng thành công. Chúc mừng em nhé.",
                5,
                "Xử lý sự cố rất nhanh và công tâm."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Điểm rèn luyện bị thiếu điểm tham gia Ngày hội việc làm",
                "Em có tham gia check-in sự kiện Ngày hội việc làm sinh viên HCMUTE tuần trước nhưng bảng điểm rèn luyện tạm thời chưa được cộng điểm.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                3,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Ban tổ chức sự kiện đang tổng hợp file quét mã QR check-in. Cô sẽ đối chiếu danh sách trong ngày mai.",
                "Dạ em gửi ảnh chụp màn hình lúc quét mã check-in thành công tại quầy ạ.",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Xin cộng điểm rèn luyện cho hoạt động hiến máu nhân đạo",
                "Em vừa tham gia đợt hiến máu tình nguyện do Hội Chữ thập đỏ trường tổ chức sáng nay. Em gửi kèm giấy chứng nhận hiến máu ạ.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.LOW,
                9,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, ảnh chụp giấy chứng nhận hiến máu em đính kèm bị mờ không thấy rõ họ tên và ngày tháng. Em vui lòng chụp lại rõ nét hơn giúp Cô nhé.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Không truy cập được trang tự chấm điểm rèn luyện của lớp",
                "Khi em bấm vào link tự đánh giá điểm rèn luyện thì hệ thống báo lỗi 403 Forbidden không có quyền truy cập.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                25,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, do tài khoản của em chưa được đồng bộ vào lớp sinh hoạt mới. Cô đã phân quyền lại cho em vào lớp 241CTTA rồi nhé.",
                "Dạ em đã vào tự chấm điểm được rồi ạ. Em cảm ơn Cô!",
                "Em nhớ tự chấm và nhắc các bạn trong lớp hoàn thành đúng hạn nhé.",
                2,
                "Hệ thống hay bị lỗi phân quyền, cần khắc phục."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Xin cấp giấy xác nhận điểm rèn luyện để nộp học bổng ngoài trường",
                "Em đang làm hồ sơ học bổng Vallet và cần giấy xác nhận điểm rèn luyện 2 học kỳ gần nhất có mộc của Phòng CTSV ạ.",
                TicketStatus.CLOSED,
                TicketPriority.NORMAL,
                42,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, giấy xác nhận điểm rèn luyện của em đã được in và ký duyệt. Em ghé phòng A201 trong giờ hành chính để nhận nhé.",
                "Dạ sáng nay em đã đến nhận giấy rồi ạ. Em cảm ơn Thầy/Cô nhiều.",
                "Chúc em may mắn và đạt được học bổng nhé.",
                5,
                "Cấp giấy tờ rất nhanh và thuận tiện."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Thắc mắc về tiêu chí đánh giá hoạt động nghiên cứu khoa học",
                "Em có đề tài nghiên cứu khoa học sinh viên được nghiệm thu cấp Khoa loại Tốt thì được cộng bao nhiêu điểm vào mục rèn luyện?",
                TicketStatus.CLOSED,
                TicketPriority.LOW,
                55,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, đề tài NCKH sinh viên nghiệm thu cấp Khoa đạt từ loại Khá trở lên được cộng 08 điểm vào Tiêu chí 2 (Ý thức học tập và nghiên cứu).",
                "Dạ vâng em cảm ơn Cô đã giải đáp rõ ràng.",
                null,
                4,
                "Hướng dẫn dễ hiểu."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Đã thanh toán học phí qua ngân hàng nhưng portal chưa cập nhật gạch nợ",
                "Em chuyển khoản học phí 8.500.000 VNĐ qua app ngân hàng từ sáng hôm qua nhưng trên cổng sinh viên vẫn báo nợ học phí.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.URGENT,
                3,
                2,
                true,
                "Sắp hết hạn đóng học phí sợ bị khóa môn học",
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy kiểm tra thấy nội dung chuyển khoản của em bị thiếu số sinh viên nên hệ thống không tự động đối soát được.",
                "Dạ em sơ ý quá, em gửi ảnh chụp biên lai chuyển tiền thành công có mã giao dịch ở đây ạ.",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Xin gia hạn thời gian nộp học phí do gia đình gặp khó khăn",
                "Do bố em vừa phẫu thuật nên gia đình em chưa kịp chuẩn bị tiền học phí kỳ này. Em xin được gia hạn thêm 2 tuần để đóng ạ.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                12,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, nhà trường đồng ý hỗ trợ gia hạn. Em cần làm đơn xin gia hạn học phí có chữ ký của phụ huynh và gửi đính kèm vào yêu cầu này.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Xuất hóa đơn điện tử học phí theo mã số thuế của công ty phụ huynh",
                "Em muốn nhờ Phòng Kế hoạch - Tài chính xuất hóa đơn VAT học phí học kỳ này theo tên và mã số thuế công ty của mẹ em để công ty thanh toán.",
                TicketStatus.RESOLVED,
                TicketPriority.URGENT,
                24,
                6,
                true,
                "Cần hóa đơn để thanh toán công ty trước ngày cuối tháng",
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy đã phát hành hóa đơn điện tử theo thông tin công ty em cung cấp và gửi mã tra cứu hóa đơn về email của em rồi nhé.",
                "Dạ em đã tải được hóa đơn hợp lệ rồi ạ, em cảm ơn Thầy rất nhiều!",
                "Không có gì em nhé. Chúc em học tốt.",
                5,
                "Xử lý hỗ trợ rất chu đáo và đúng hạn."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Thắc mắc về khoản phí bảo hiểm y tế và bảo hiểm tai nạn",
                "Em đã có thẻ BHYT diện hộ nghèo do địa phương cấp thì có được trừ tiền BHYT trong thông báo thu học phí đầu năm không ạ?",
                TicketStatus.CLOSED,
                TicketPriority.HIGH,
                36,
                1,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, sinh viên đã có thẻ BHYT theo diện chính sách chỉ cần nộp bản photo thẻ BHYT về trường, nhà trường sẽ hoàn lại 100% tiền BHYT đã thu.",
                "Dạ em đã nộp bản photo và nhận được tiền hoàn lại qua tài khoản rồi ạ. Em cảm ơn Thầy!",
                "Yêu cầu đã được đóng. Chúc em sức khỏe.",
                4,
                "Tư vấn nhiệt tình, giải quyết thỏa đáng."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Sai sót số tiền học phí môn thí nghiệm thực hành",
                "Em học môn Thí nghiệm Vật lý 1 tín chỉ nhưng trên bảng học phí lại tính thành học phí của môn 2 tín chỉ.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                44,
                3,
                false,
                null,
                false,
                SlaStatus.BREACHED,
                false,
                "Chào em, Thầy kiểm tra và thấy có sự nhầm lẫn mã học phần giữa lớp đại trà và chất lượng cao. Thầy đã điều chỉnh lại công nợ về đúng mức học phí.",
                "Dạ em kiểm tra thấy số tiền đã đúng rồi ạ. Em cảm ơn Thầy.",
                "Thành thật xin lỗi em vì sự chậm trễ trong đợt đối soát vừa qua.",
                3,
                "Xử lý được vấn đề nhưng hơi lâu."
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Hỏi về hồ sơ xin miễn giảm học phí cho con thương binh",
                "Dạ Thầy/Cô cho em hỏi ba em là thương binh hạng 3/4 thì em được miễn giảm bao nhiêu phần trăm học phí và hồ sơ cần những gì ạ?",
                TicketStatus.NEW,
                TicketPriority.LOW,
                1,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Nộp đơn xin rút môn học và hoàn lại tiền học phí",
                "Do em bị tai nạn gãy chân phải nghỉ học 1 tháng nên em làm đơn xin rút bớt 2 môn lý thuyết và xin hoàn phí kỳ này ạ.",
                TicketStatus.ASSIGNED,
                TicketPriority.NORMAL,
                5,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Bổ sung sổ hộ nghèo chứng thực điện tử để xét miễn học phí",
                "Em xin gửi bổ sung bản sao chứng thực điện tử sổ hộ nghèo năm 2026 của UBND xã để hoàn thiện hồ sơ miễn giảm học phí.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.LOW,
                11,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Thầy đã xem file đính kèm. Bản chứng thực điện tử có chữ ký số hợp lệ của UBND xã, hồ sơ của em đã đầy đủ.",
                "Dạ em cảm ơn Thầy nhiều ạ, khi nào có quyết định chính thức ạ?",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Thắc mắc về thời gian nhận tiền trợ cấp miễn giảm học phí",
                "Em thuộc diện miễn 100% học phí theo Nghị định 81, em đã nộp hồ sơ từ đầu kỳ nhưng chưa biết khi nào trường chi trả tiền cấp bù?",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                26,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, tiền cấp bù học phí do ngân sách Nhà nước rót về theo từng quý. Em gửi lại số tài khoản ngân hàng để đối chiếu danh sách chi trả nhé.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Xin giảm học phí cho 2 anh em ruột cùng học đại học chính quy",
                "Nhà em có 2 anh em ruột cùng học tại trường: anh trai học năm 3 ngành Cơ điện tử và em học năm 1 ngành CNTT. Em xin hướng dẫn thủ tục giảm học phí.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                39,
                5,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, nhà trường có chính sách giảm 10% học phí cho người em. Thầy đã tiếp nhận giấy khai sinh và duyệt giảm trừ vào học phí kỳ tới cho em rồi nhé.",
                "Dạ em cảm ơn Thầy và nhà trường rất nhiều ạ!",
                "Chúc hai anh em học tập thật tốt nhé.",
                5,
                "Chính sách rất nhân văn, giải quyết nhiệt tình."
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Xin đổi phòng ký túc xá do phòng hiện tại bị thấm dột",
                "Em ở phòng 402 KTX khu A, đợt mưa lớn vừa qua trần nhà bị thấm nước ẩm ướt ảnh hưởng sinh hoạt. Em xin được chuyển sang phòng khác ạ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Tân sinh viên xin đăng ký ở ký túc xá bổ sung đợt 2",
                "Em nhập học đợt bổ sung nên chưa kịp đăng ký KTX đợt 1. Em có hoàn cảnh ở xa (tỉnh Gia Lai), em xin được xét duyệt chỗ ở KTX khu B ạ.",
                TicketStatus.ASSIGNED,
                TicketPriority.HIGH,
                4,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Hỏi về thủ tục trả phòng ký túc xá và nhận lại tiền cọc",
                "Em chuẩn bị đi thực tập tốt nghiệp ở Bình Dương nên cần làm thủ tục trả phòng KTX vào cuối tháng này. Em xin hỏi quy trình trả phòng và nhận lại tiền cọc tài sản.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                14,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, em cần báo trước cho trưởng nhà KTX tối thiểu 05 ngày, dọn dẹp phòng sạch sẽ và bàn giao lại chìa khóa, trang thiết bị phòng.",
                "Dạ tiền cọc sẽ được hoàn trả bằng tiền mặt hay chuyển khoản vậy Cô?",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Đăng ký tạm trú cho sinh viên nội trú ký túc xá",
                "Em cần giấy xác nhận tạm trú tại KTX để làm thủ tục thi bằng lái xe máy tại TP.HCM. Em xin hướng dẫn cách xin giấy ạ.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                21,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, em mang theo CCCD bản gốc và 01 ảnh 3x4 đến văn phòng Ban Quản lý KTX (tầng trệt nhà A1) để Cô làm giấy xác nhận nhé.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Xin gia hạn thời gian đóng tiền phòng ký túc xá học kỳ 2",
                "Em đang đợi tiền phụ cấp từ quê gửi lên nên xin phép được dời lịch đóng tiền phòng KTX thêm 5 ngày so với thông báo.",
                TicketStatus.CLOSED,
                TicketPriority.NORMAL,
                48,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, Ban Quản lý KTX đã ghi nhận và đồng ý cho em gia hạn đóng tiền phòng đến ngày 25 của tháng này.",
                "Dạ em cảm ơn Cô quản lý đã thông cảm cho em ạ.",
                "Em nhớ hoàn thành đúng ngày hẹn nhé.",
                5,
                "Cô quản lý KTX rất thân thiện và thấu hiểu sinh viên."
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Phòng 215 KTX khu B bị hỏng vòi nước và mất đèn chiếu sáng",
                "Phòng chúng em bị hỏng công tắc đèn tuýp chính từ tối qua và vòi rửa mặt bị rò rỉ nước liên tục. Mong các chú kỹ thuật hỗ trợ sửa giúp phòng em.",
                TicketStatus.ASSIGNED,
                TicketPriority.NORMAL,
                1,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Mạng wifi tầng 3 nhà A2 KTX bị mất kết nối liên tục",
                "Từ sáng nay wifi sinh viên tại tầng 3 nhà A2 cứ 10 phút lại bị ngắt mạng một lần, ảnh hưởng đến việc học trực tuyến của chúng em.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                5,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Chào các em, đội kỹ thuật mạng đã phát hiện cục phát access point tầng 3 bị lỗi nguồn và đang tiến hành thay thế thiết bị mới.",
                "Dạ các chú kỹ thuật đang lên kiểm tra rồi ạ, em cảm ơn Thầy/Cô.",
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Máy giặt tự động tầng trệt nuốt tiền nhưng không chạy",
                "Em quét mã thanh toán 20.000 VNĐ cho máy giặt số 3 tại nhà giặt chung nhưng máy không hoạt động và app đã trừ tiền.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.HIGH,
                16,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, em gửi giúp Cô ảnh chụp màn hình lịch sử trừ tiền trên app ngân hàng có mã giao dịch để kỹ thuật kiểm tra và hoàn tiền nhé.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Sửa chữa khóa cửa ban công phòng 510 KTX khu A",
                "Khóa cửa ra ban công phòng em bị kẹt cứng không mở được, gió giật mạnh gây va đập rất nguy hiểm.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                30,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Chào em, chú bảo trì đã đến thay ổ khóa mới cho phòng 510 chiều nay. Em kiểm tra lại xem khóa đã mở êm chưa nhé.",
                "Dạ chú thợ thay khóa rất nhanh và chắc chắn rồi ạ. Em cảm ơn Ban quản lý KTX!",
                "Yêu cầu đã được xử lý xong. Chúc các em sinh hoạt vui vẻ.",
                5,
                "Sửa chữa trang thiết bị rất nhanh chóng."
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Báo mất thẻ xe ký túc xá và xin cấp lại thẻ mới",
                "Em sơ ý làm rơi mất thẻ gửi xe tháng của KTX trong lúc đi học. Em xin hỏi thủ tục làm lại thẻ xe và có bị phạt tiền không ạ?",
                TicketStatus.CANCELLED,
                TicketPriority.LOW,
                19,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));

        // Các kịch bản mở rộng được khai báo riêng, đủ các chuyên mục của hệ thống mới.
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Đề nghị mở nhóm học phần An toàn ứng dụng web",
                "Em cần học An toàn ứng dụng web trong kỳ này để đủ điều kiện đăng ký đồ án chuyên ngành nhưng toàn bộ nhóm lớp đều kín chỗ trước khi em hoàn tất đăng ký.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Lớp học phần hiện sai sĩ số sau khi hủy đăng ký",
                "Một số bạn đã hủy môn nhưng sĩ số trong portal không giảm, khiến em không biết nhóm lớp đã có chỗ trống hay chưa. Em nhờ kiểm tra cập nhật sĩ số.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng đã ghi nhận mã lớp học phần và đang đối chiếu số lượng đăng ký hiện tại với các giao dịch hủy gần nhất. Em không cần gửi lại yêu cầu đăng ký trong lúc bộ phận kiểm tra dữ liệu.",
                "Em gửi mã lớp và thời điểm em thấy hai bạn thông báo đã hủy môn.",
                "Sĩ số đã được đồng bộ sau khi đối soát giao dịch hủy. Em tải lại danh sách lớp trước khi tiếp tục đăng ký.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Xin xác nhận điểm học phần được miễn khi chuyển ngành",
                "Sau khi được duyệt chuyển ngành, một số học phần chung của em chưa hiện trạng thái miễn trong tiến độ. Em muốn biết cần nộp bảng điểm hay quyết định chuyển ngành để cập nhật.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Không tải được bảng điểm có dấu xác nhận điện tử",
                "Khi mở bản bảng điểm có xác nhận điện tử, trình duyệt chỉ hiển thị trang trắng nên em không kiểm tra được chữ ký. Em cần bản hợp lệ để nộp cho đơn vị thực tập.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng đã phát hành lại bản bảng điểm điện tử với liên kết tải mới và kiểm tra chữ ký số trên tài liệu. Em đăng nhập lại rồi tải tệp trực tiếp từ trang hồ sơ học vụ.",
                "Em tải được file mới và phần chữ ký số hiển thị bình thường rồi ạ.",
                "Bản bảng điểm đã được cung cấp lại. Em lưu file PDF gốc và tránh chuyển đổi sang định dạng khác nếu cần xác minh chữ ký.",
                5,
                "Có hướng dẫn rõ và bản điện tử hợp lệ."
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Xin kiểm tra số tín chỉ tự chọn trước khi đăng ký xét tốt nghiệp",
                "Tiến độ của em báo thiếu hai tín chỉ tự chọn, trong khi em đã hoàn thành học phần tự chọn ở học kỳ hè. Em muốn đối chiếu nhóm tín chỉ trước khi gửi đơn xét tốt nghiệp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Giấy xác nhận hoàn thành chương trình bị sai ngày công nhận",
                "Giấy xác nhận hoàn thành chương trình của em ghi ngày công nhận khác ngày trên quyết định tốt nghiệp. Em cần chỉnh lại trước khi nộp hồ sơ tuyển dụng.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                5,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng cần mã sinh viên và bản scan giấy xác nhận hiện tại cùng phần quyết định liên quan để đối chiếu ngày công nhận. Em gửi hai tài liệu đó, có thể che thông tin không liên quan đến yêu cầu điều chỉnh.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Xin tra cứu điểm xét học bổng sau khi kết thúc đợt tuyển chọn",
                "Em đã nộp hồ sơ học bổng và muốn xem điểm hoặc tiêu chí nào khiến hồ sơ chưa được chọn. Em xin biết kết quả chi tiết được công bố ở đâu.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Thông báo nhận học bổng không ghi thời hạn xác nhận",
                "Em nhận thông báo đủ điều kiện học bổng nhưng không thấy hạn cuối xác nhận nhận học bổng. Em muốn biết cần hoàn tất xác nhận trước ngày nào để không mất quyền nhận.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                6,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận phụ trách đang xác minh hạn xác nhận với ban xét chọn của đợt học bổng. Em theo dõi mục thông báo chính thức và chưa cần gửi lại hồ sơ nếu trạng thái đã ghi đủ điều kiện.",
                "Em cảm ơn, em sẽ theo dõi thông báo trên portal và email sinh viên.",
                "Hạn xác nhận đã được bổ sung vào thông báo kết quả. Em hoàn tất xác nhận trước mốc thời gian được hiển thị trên trang học bổng.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Hoạt động tập huấn cán bộ lớp chưa xuất hiện trong điểm rèn luyện",
                "Em tham gia tập huấn cán bộ lớp theo danh sách của khoa nhưng hoạt động chưa hiện trong phần minh chứng rèn luyện. Em muốn kiểm tra danh sách điểm danh đã được chuyển sang CTSV chưa.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Không thể bổ sung minh chứng sau khi lớp đã xác nhận kết quả",
                "Lớp đã thống nhất kết quả rèn luyện nhưng em mới phát hiện thiếu giấy chứng nhận cuộc thi học thuật. Em muốn hỏi có thể gửi yêu cầu điều chỉnh kèm minh chứng hay phải đợi kỳ sau.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                7,
                1,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Bộ phận cần biết kết quả của lớp đang ở giai đoạn tạm thời hay đã được phê duyệt chính thức. Em gửi kỳ đánh giá và loại hoạt động bị thiếu để xác định đúng quy trình bổ sung.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Khoản thu học phí hiển thị khác số tiền trong thông báo kỳ học",
                "Thông báo thu học phí của em ghi một số tiền, nhưng bảng công nợ trên portal hiển thị cao hơn sau khi đăng ký thêm học phần. Em muốn xem chi tiết từng khoản cấu thành tổng tiền.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Biên nhận thanh toán bị ghi sai học kỳ",
                "Em thanh toán học phí cho học kỳ hiện tại nhưng biên nhận điện tử ghi kỳ trước. Em cần xác nhận giao dịch đã được hạch toán đúng và xin điều chỉnh thông tin trên biên nhận.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                3,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đã nhận mã giao dịch và đang đối chiếu kỳ học trong yêu cầu thanh toán với bút toán đã ghi nhận. Em giữ lại biên nhận hiện tại để đối chiếu cho đến khi có kết quả.",
                "Em gửi mã giao dịch và ảnh biên nhận theo hướng dẫn.",
                "Biên nhận đã được kiểm tra và dữ liệu kỳ học được chuyển để điều chỉnh theo giao dịch thực tế. Em tải bản cập nhật sau khi nhận thông báo hoàn tất.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Xin biết kết quả hồ sơ hỗ trợ học phí diện chính sách",
                "Em đã nộp hồ sơ diện chính sách trong thời gian tiếp nhận nhưng chưa nhận được thông báo kết quả. Em muốn biết hồ sơ đang thẩm định hay bị yêu cầu bổ sung giấy tờ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Đơn miễn giảm học phí không đính kèm được nhiều trang minh chứng",
                "Đơn trực tuyến chỉ nhận một tệp minh chứng trong khi hồ sơ của em có nhiều trang giấy xác nhận. Em muốn biết có thể gộp các trang vào PDF hay cần nộp thành nhiều tệp riêng.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã xác nhận biểu mẫu tiếp nhận một tệp PDF tổng hợp cho bộ minh chứng nhiều trang. Em ghép các trang theo đúng thứ tự, kiểm tra rõ chữ và tải lên một lần trong giới hạn dung lượng hiển thị.",
                "Em đã gộp thành một file PDF và biểu mẫu tiếp nhận thành công.",
                "Hồ sơ đã được bổ sung đúng định dạng. Em kiểm tra trạng thái tiếp nhận sau khi gửi và giữ bản gốc của từng giấy tờ để đối chiếu khi cần.",
                5,
                "Hướng dẫn cụ thể cách nộp bộ hồ sơ nhiều trang."
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Xin xác nhận đã hoàn tất đăng ký nội trú trước ngày nhận phòng",
                "Em đã chọn phòng và gửi đơn nội trú nhưng trang chỉ hiển thị đã lưu bản nháp. Em muốn xác nhận hồ sơ đã vào danh sách xét duyệt trước ngày nhận phòng.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Đơn đăng ký KTX bị trùng do em bấm gửi hai lần",
                "Do trang phản hồi chậm, em bấm gửi đơn hai lần và hiện thấy hai hồ sơ giống nhau. Em muốn hủy bản trùng nhưng giữ lại hồ sơ hợp lệ đang chờ xét duyệt.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý đã ghi nhận hai mã hồ sơ có thông tin giống nhau và đang đối chiếu thời điểm tạo để giữ đúng bản gửi hợp lệ. Em không cần gửi thêm đơn mới trong lúc xử lý trùng lặp.",
                "Em đã cung cấp cả hai mã hồ sơ và xác nhận chỉ muốn giữ bản gửi đầu tiên.",
                "Bản trùng đã được đánh dấu hủy, còn hồ sơ chính vẫn nằm trong danh sách xét duyệt. Em theo dõi mã hồ sơ đang hoạt động để tránh nhầm lẫn.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Bình nóng lạnh khu vệ sinh chung tự ngắt giữa lúc sử dụng",
                "Thiết bị nước nóng ở khu vệ sinh chung thường tự ngắt sau vài phút rồi mới bật lại. Em muốn báo đội kỹ thuật kiểm tra bộ bảo vệ nhiệt và lịch bảo trì.",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                1,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Cửa ra vào nhà ở đóng quá mạnh và không tự chốt",
                "Cửa chính nhà ở va mạnh khi đóng và đôi lúc không chốt kín nếu không kéo lại. Em muốn bộ phận bảo trì điều chỉnh tay co cửa để bảo đảm an toàn và giảm tiếng ồn ban đêm.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                8,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Đội bảo trì đã điều chỉnh tay co cửa và kiểm tra chốt khóa ở nhiều góc đóng khác nhau. Cửa hiện đóng êm hơn và chốt hoạt động ổn định trong lần thử nghiệm sau sửa chữa.",
                "Em thử đóng mở vài lần và thấy cửa không còn va mạnh như trước.",
                "Thiết bị đóng cửa đã được điều chỉnh. Em báo lại bộ phận trực nếu cửa có dấu hiệu không tự chốt trong những ngày tiếp theo.",
                5,
                "Xử lý cả tiếng ồn và tình trạng chốt cửa."
        ));
        list.add(new TicketTemplate(
                "CNTT_TAIKHOAN",
                "Email sinh viên không nhận được thư xác minh địa chỉ mới",
                "Em đổi email liên hệ trong tài khoản nhưng hệ thống không gửi thư xác minh đến hộp thư mới. Em đã kiểm tra thư rác và địa chỉ nhập vào không có lỗi chính tả.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_TAIKHOAN",
                "Tên đăng nhập hiển thị khác MSSV sau khi đồng bộ hồ sơ",
                "Sau khi cập nhật hồ sơ cá nhân, phần tên đăng nhập hiển thị một chuỗi ký tự khác với MSSV. Em vẫn truy cập được nhưng muốn xác nhận đây chỉ là tên hiển thị hay thay đổi tài khoản thực tế.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Kỹ thuật đã kiểm tra trường tên hiển thị và xác nhận mã tài khoản đăng nhập không thay đổi. Giao diện đang lấy nhầm trường định danh để trình bày, phần hiển thị đã được sửa về thông tin chuẩn.",
                "Em đã đăng nhập lại và phần tài khoản hiển thị đúng MSSV.",
                "Lỗi hiển thị đã được xử lý, không ảnh hưởng thông tin xác thực. Em tiếp tục sử dụng tài khoản theo mã sinh viên hiện hành.",
                5,
                "Giải thích rõ phạm vi ảnh hưởng và khắc phục giao diện."
        ));
        list.add(new TicketTemplate(
                "CNTT_PORTAL",
                "Bộ lọc thông báo theo loại không giữ lựa chọn sau khi chuyển trang",
                "Em chọn chỉ xem thông báo học vụ rồi chuyển sang trang hai, hệ thống lại hiển thị tất cả các loại thông báo. Em cần lọc ổn định để tìm lại thông báo cũ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_PORTAL",
                "Trang hồ sơ tải ảnh đại diện nhưng ảnh mới không xuất hiện",
                "Em thay ảnh đại diện và thấy thông báo cập nhật thành công, nhưng trang cá nhân vẫn hiển thị ảnh cũ ngay cả sau khi tải lại. Em muốn biết cần chờ bộ nhớ đệm làm mới hay thao tác lại.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Kỹ thuật cần biết trình duyệt và định dạng ảnh để kiểm tra việc lưu tệp và bộ nhớ đệm. Em gửi thời điểm cập nhật cùng phần hiển thị hiện tại, không cần tải lại cùng ảnh nhiều lần khi chưa xác minh.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_BAOMAT",
                "Phiên đăng nhập vẫn còn sau khi em chọn đăng xuất trên máy chung",
                "Em chọn đăng xuất khỏi portal tại máy tính phòng thực hành, nhưng khi bấm nút quay lại của trình duyệt vẫn nhìn thấy trang thông tin cũ. Em muốn biết phiên đã được kết thúc an toàn chưa.",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                1,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_BAOMAT",
                "Không nhận được cảnh báo khi có thay đổi thông tin khôi phục",
                "Em đổi email khôi phục thành công nhưng không nhận cảnh báo tại email trường. Em muốn xác nhận thay đổi có được ghi nhận và hệ thống có gửi thông báo bảo mật cho mỗi lần thay đổi hay không.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra nhật ký cập nhật và xác nhận email khôi phục mới đã được lưu. Kênh thông báo bảo mật của tài khoản đang được rà soát riêng; em có thể kiểm tra mục bảo mật để xác nhận giá trị hiện tại.",
                "Em mở phần bảo mật và thấy email khôi phục mới đã đúng.",
                "Thông tin khôi phục đã được xác nhận. Bộ phận tiếp tục kiểm tra luồng gửi cảnh báo; em không cần đổi lại email nếu dữ liệu đang chính xác.",
                5,
                "Xác nhận được trạng thái tài khoản và tách bạch lỗi thông báo."
        ));
        list.add(new TicketTemplate(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Yêu cầu khôi phục báo không tồn tại tài khoản dù email là email trường",
                "Em nhập email sinh viên vào trang khôi phục nhưng hệ thống báo không có tài khoản tương ứng. Em vẫn nhận được thư trường ở địa chỉ này nên muốn kiểm tra thông tin email đăng nhập đã được lưu thế nào.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_PORTAL_LOI",
                "Trang thông báo bị treo khi mở nội dung có tệp đính kèm",
                "Danh sách thông báo tải bình thường nhưng khi mở một thông báo có file PDF đính kèm, trang đứng ở trạng thái đang tải vô hạn. Những thông báo không có file vẫn xem được.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Kỹ thuật đã ghi nhận mã thông báo và đang kiểm tra việc tải metadata cùng đường dẫn tệp. Em gửi tên thông báo, trình duyệt và thời điểm lỗi để xác định tệp cụ thể gây treo trang.",
                "Em xác nhận lỗi chỉ xảy ra với thông báo về lịch đăng ký thực tập có tệp PDF.",
                "Lỗi xử lý tệp đính kèm đã được sửa cho thông báo này. Em mở lại trang và dùng liên kết tải tệp riêng nếu trình xem trong trang vẫn bị giới hạn bởi trình duyệt.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_MUONTRA",
                "Máy mượn sách tự phục vụ không đọc được mã thẻ sinh viên",
                "Em đưa thẻ vào máy mượn tự phục vụ nhưng thiết bị báo không nhận dạng dù thẻ vẫn dùng được ở cổng thư viện. Em cần biết có thể mượn tại quầy hay phải cấp lại thẻ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_MUONTRA",
                "Sách trả nhầm ngăn của máy tự phục vụ và cần xác nhận giao dịch",
                "Em nhận ra mình bỏ sách vào ngăn trả dành cho khu vực khác của máy tự phục vụ. Em muốn báo ngay để nhân viên tìm đúng tài liệu trước khi giao dịch ghi nhận quá hạn.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                3,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện đã ghi nhận thời điểm và máy trả sách em sử dụng. Nhân viên trực sẽ kiểm tra các ngăn thu hồi tương ứng và xác minh mã tài liệu trước khi cập nhật giao dịch trả.",
                "Em đã gửi mã sách trên phiếu máy in và vị trí máy ở sảnh tầng trệt.",
                "Tài liệu đã được tìm thấy trong ngăn thu hồi và giao dịch trả được cập nhật sau khi kiểm tra mã sách. Em theo dõi danh sách mượn để xác nhận trạng thái thay đổi.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TRACUU",
                "Kết quả tra cứu sách điện tử nhầm sang bản tóm tắt thay vì toàn văn",
                "Em tìm giáo trình tiếng Anh theo tên và kết quả đầu tiên chỉ mở được phần giới thiệu ngắn. Em muốn biết có bản toàn văn được thư viện cấp quyền truy cập cho sinh viên hay không.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TRACUU",
                "Trang thông tin tài liệu hiển thị sai tên tác giả thứ hai",
                "Bản ghi sách có tên tác giả chính xác nhưng phần tác giả đồng biên soạn hiển thị một tên khác. Em báo để thư viện chỉnh metadata vì nhóm em cần trích dẫn đúng trong báo cáo.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                6,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện cần ảnh trang tên sách hoặc trang bản quyền thể hiện danh sách tác giả cùng mã bản ghi thư mục. Em gửi tài liệu minh chứng để tránh chỉnh sửa dựa trên kết quả tìm kiếm không đầy đủ.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TAIKHOAN",
                "Không thể đăng ký nhận thông báo khi sách đã đặt trước có sẵn",
                "Em đặt trước một giáo trình nhưng mục nhận thông báo khi sách sẵn sàng bị vô hiệu hóa. Em muốn biết có thể bật thông báo qua email hay cần kiểm tra trang đặt trước hằng ngày.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TAIKHOAN",
                "Tài khoản thư viện hiển thị lớp cũ sau khi sinh viên chuyển ngành",
                "Em đã chuyển ngành và hồ sơ portal mới hiển thị lớp hiện tại, nhưng thư viện số vẫn dùng lớp cũ để phân loại tài khoản. Em muốn cập nhật quyền sử dụng tài nguyên theo chương trình mới.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                14,
                4,
                false,
                null,
                true,
                SlaStatus.AT_RISK,
                false,
                "Thư viện đã đối chiếu quyết định chuyển ngành với dữ liệu đồng bộ người dùng và cập nhật thông tin lớp trong tài khoản thư viện. Quyền truy cập tài nguyên được kiểm tra lại theo nhóm người dùng hiện tại.",
                "Em đã đăng nhập lại thư viện số và thấy lớp mới đã hiển thị.",
                "Thông tin tài khoản thư viện đã được đồng bộ. Em thử mở lại tài nguyên được cấp quyền theo chương trình hiện tại và báo nếu còn mục nào bị từ chối.",
                5,
                "Giải quyết đúng lệch dữ liệu giữa portal và thư viện."
        ));
        list.add(new TicketTemplate(
                "TV_MUONTRA_GIAHAN",
                "Xin gia hạn sách vì lịch nghỉ lễ làm quầy trả đóng cửa",
                "Ngày trả sách của em trùng ngày thư viện nghỉ lễ theo thông báo, trong khi chức năng gia hạn không cho thao tác vì đã hết lượt. Em muốn biết cách trả đúng hạn trong tình huống này.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện cần mã tài liệu, ngày trả hiện tại và thông báo nghỉ lễ liên quan để kiểm tra quy định áp dụng cho ngày nghỉ. Em gửi thông tin trên tài khoản mượn để bộ phận xác định hướng xử lý phù hợp.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_MUONTRA_GIAHAN",
                "Đã gia hạn thành công nhưng giấy nhắc trả tại quầy vẫn in ngày cũ",
                "Em gia hạn sách trên portal và ngày trả mới đã hiện đúng, nhưng phiếu nhắc trả in tại máy thư viện vẫn hiển thị ngày cũ. Em muốn kiểm tra nguồn dữ liệu của phiếu in.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện đã cập nhật mẫu phiếu để lấy ngày trả hiện hành từ giao dịch mượn mới nhất. Phiếu cũ đã in trước thời điểm gia hạn không được tự thay đổi, em cần in lại nếu muốn dùng bản giấy cập nhật.",
                "Em đã in lại phiếu và ngày trả đã khớp với portal.",
                "Mẫu phiếu đã được cập nhật. Em dùng bản in mới nhất và không căn cứ vào phiếu được in trước khi gia hạn.",
                5,
                "Phân biệt rõ phiếu cũ và thông tin gia hạn mới."
        ));
        list.add(new TicketTemplate(
                "TV_TRACUU_TAILIEU",
                "Liên kết kho luận văn yêu cầu chứng chỉ bảo mật không hợp lệ",
                "Khi mở liên kết kho luận văn từ thư viện số, trình duyệt cảnh báo chứng chỉ bảo mật và không cho xem nội dung. Em cần truy cập tài liệu cho buổi thảo luận ngày mai.",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                1,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_THUCTAP",
                "Xin điều chỉnh thời gian thực tập do lịch nhận việc thay đổi từ doanh nghiệp",
                "Doanh nghiệp dời ngày nhận việc thực tập sớm hơn một tuần so với kế hoạch đã đăng ký. Em muốn cập nhật lại ngày bắt đầu để báo cáo và giấy xác nhận cuối kỳ khớp nhau.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_THUCTAP",
                "Nhật ký thực tập bị khóa khi em cần sửa lỗi đánh máy trong tuần đã nộp",
                "Em đã gửi nhật ký tuần trước nhưng phát hiện sai tên công nghệ ở một đoạn mô tả công việc. Em muốn sửa lỗi nhỏ mà không làm thay đổi nội dung đánh giá của người hướng dẫn.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã mở chức năng gửi bản chỉnh sửa cho nhật ký đang chờ đánh giá. Em sửa lỗi cần thiết, ghi chú nội dung cập nhật và gửi lại để người hướng dẫn xem đúng phiên bản mới.",
                "Em đã nộp bản sửa có ghi chú và thấy trạng thái chờ xác nhận.",
                "Bản chỉnh sửa đã được lưu thành phiên bản mới. Em giữ nội dung nhật ký trung thực với công việc thực tế và không xóa lịch sử các bản đã gửi.",
                5,
                "Có thể sửa lỗi nhỏ mà vẫn giữ lịch sử bản nộp."
        ));
        list.add(new TicketTemplate(
                "QHDN_TUYENDUNG",
                "Xin kiểm tra tin tuyển dụng có chấp nhận sinh viên chưa hoàn thành ngoại ngữ",
                "Tin thực tập yêu cầu ngoại ngữ nhưng không nêu mức điểm tối thiểu. Em đang học chứng chỉ và muốn xác minh có thể ứng tuyển trước khi hoàn thành bài thi hay không.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận sẽ xác minh trực tiếp với đơn vị tuyển dụng vì điều kiện có thể khác nhau giữa các vị trí cùng tên. Em gửi mã tin tuyển dụng và tên vị trí để tránh nhầm thông báo.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_TUYENDUNG",
                "Thông tin hỗ trợ thực tập trên tin đăng khác với thư mời phỏng vấn",
                "Tin đăng ghi hỗ trợ chi phí theo tháng nhưng thư mời phỏng vấn không đề cập. Em muốn xác minh chính sách áp dụng trước khi xác nhận tham gia phỏng vấn.",
                TicketStatus.CLOSED,
                TicketPriority.NORMAL,
                13,
                5,
                false,
                null,
                false,
                SlaStatus.BREACHED,
                false,
                "Bộ phận đã yêu cầu doanh nghiệp xác nhận nội dung chính thức và cập nhật tin đăng để thống nhất với thông tin tuyển dụng. Mức hỗ trợ được ghi rõ trong phần thông tin vị trí sau khi xác minh.",
                "Em đã kiểm tra tin đăng đã chỉnh sửa và xác nhận lịch phỏng vấn.",
                "Thông tin vị trí đã được làm rõ. Em dựa vào thư mời mới nhất và nội dung tin tuyển dụng đã cập nhật khi trao đổi với doanh nghiệp.",
                5,
                "Giúp làm rõ quyền lợi trước buổi phỏng vấn."
        ));
        list.add(new TicketTemplate(
                "QHDN_CV",
                "Xin góp ý cách giải thích khoảng trống thời gian trong CV",
                "Em có một học kỳ tạm dừng học để xử lý việc gia đình và không biết có nên ghi chi tiết khoảng thời gian này trong CV. Em mong được tư vấn cách trình bày ngắn gọn, trung thực và tập trung vào năng lực.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_CV",
                "Liên kết portfolio không mở được khi dùng mạng ngoài trường",
                "Em gửi CV có liên kết portfolio lưu trên máy chủ cá nhân, nhưng khi thử từ mạng di động thì trang không mở được. Em muốn kiểm tra cách chia sẻ để nhà tuyển dụng truy cập thuận tiện.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                9,
                1,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra liên kết và phát hiện portfolio chỉ cho phép truy cập từ mạng nội bộ. Em cập nhật quyền xem công khai hoặc dùng nền tảng chia sẻ phù hợp, đồng thời thử mở liên kết bằng cửa sổ ẩn danh.",
                "Em đã đổi quyền truy cập và người khác mở được portfolio từ mạng ngoài trường.",
                "Liên kết portfolio đã có thể truy cập từ bên ngoài. Em kiểm tra lại quyền riêng tư của nội dung để chỉ công khai những sản phẩm em được phép chia sẻ.",
                5,
                "Chỉ rõ nguyên nhân quyền truy cập và cách kiểm tra."
        ));
        list.add(new TicketTemplate(
                "QHDN_THUCTAP_HOSO",
                "Không tìm thấy biểu mẫu thỏa thuận bảo mật thông tin trong hồ sơ thực tập",
                "Doanh nghiệp yêu cầu ký thỏa thuận bảo mật trước khi nhận dự án thực tập, nhưng bộ biểu mẫu trên cổng trường chỉ có kế hoạch và nhật ký. Em muốn biết mẫu riêng có cần khoa xác nhận không.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_TUYENDUNG_HOSO",
                "Trang hồ sơ ứng tuyển không nhận file portfolio dạng ZIP",
                "Em muốn gửi portfolio gồm nhiều sản phẩm nhưng biểu mẫu chỉ nhận PDF và DOCX, không chấp nhận tệp ZIP. Em cần biết có thể gửi liên kết kho mã nguồn hoặc gộp hình ảnh thành PDF không.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                5,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã vị trí tuyển dụng và phần mô tả định dạng tệp cho phép để tư vấn cách gửi phù hợp. Em ưu tiên liên kết portfolio có quyền xem rõ ràng hoặc một tài liệu PDF tổng hợp thay vì chia sẻ tệp thực thi không cần thiết.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_DETAI",
                "Xin đổi người hướng dẫn khoa học sau khi điều chỉnh hướng nghiên cứu",
                "Sau buổi góp ý đề cương, nhóm em chuyển từ đề tài phân tích dữ liệu sang hướng an toàn mạng, phù hợp chuyên môn một giảng viên khác hơn. Em muốn biết thủ tục xin thay đổi người hướng dẫn.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_HOITHAO",
                "Tên tác giả trong chương trình hội thảo thiếu một đồng tác giả",
                "Bản chương trình hội thảo đăng trên trang trường chỉ nêu tên tác giả trình bày, thiếu tên đồng tác giả của bài báo nhóm em. Em xin chỉnh thông tin để hồ sơ hội thảo chính xác.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban tổ chức đã so sánh bản chương trình với biểu mẫu đăng ký bài báo và cập nhật danh sách đồng tác giả theo hồ sơ đã xác nhận. Bản chương trình điện tử mới đã được thay thế trên trang sự kiện.",
                "Em xem lại trang sự kiện và thấy tên đồng tác giả đã được bổ sung.",
                "Thông tin tác giả đã được đính chính trên chương trình điện tử. Em dùng bản cập nhật khi trích dẫn hoặc bổ sung minh chứng tham dự.",
                5,
                "Đối chiếu nguồn đăng ký và sửa đúng thông tin tác giả."
        ));
        list.add(new TicketTemplate(
                "KHCN_NGHIENCUU",
                "Xin tư vấn quy trình xin cấp quyền truy cập máy chủ phòng thí nghiệm",
                "Nhóm em cần chạy thử nghiệm trên máy chủ phòng thí nghiệm nhưng chưa rõ quy trình xin tài khoản, giới hạn tài nguyên và thời hạn sử dụng. Em muốn chuẩn bị đề xuất đúng yêu cầu trước khi gửi.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần tên đề tài, giảng viên phụ trách và mô tả khối lượng tính toán dự kiến để chuyển đề nghị đến đơn vị quản lý máy chủ. Em không gửi mật khẩu, khóa truy cập hay dữ liệu nghiên cứu hạn chế trong phiếu hỗ trợ.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_DETAI_HOSO",
                "Không thêm được quyết định phê duyệt vào mục tài liệu bắt buộc",
                "Nhóm em đã nhận quyết định phê duyệt đề tài nhưng mục tài liệu bắt buộc vẫn chưa cho tải lên vì trạng thái hồ sơ đã chuyển sang đang thực hiện. Em muốn bổ sung văn bản vào đúng nơi lưu trữ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_HOITHAO_DANGKY",
                "Đăng ký hội thảo bằng email trường nhưng danh sách dùng email cá nhân",
                "Em đăng ký hội thảo bằng email sinh viên nhưng danh sách xác nhận gửi đến email cá nhân cũ. Em muốn thống nhất địa chỉ nhận thông báo và giấy chứng nhận với hồ sơ trường.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                14,
                2,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Ban tổ chức đã đối chiếu mã đăng ký và cập nhật địa chỉ nhận thông báo theo email trường em xác nhận. Các thông báo tiếp theo và bản xác nhận tham dự sẽ sử dụng địa chỉ đã điều chỉnh.",
                "Em nhận được email xác nhận cập nhật rồi ạ.",
                "Địa chỉ liên hệ của đăng ký hội thảo đã được sửa. Em kiểm tra hộp thư đến và mục thư rác của email trường trong thời gian trước sự kiện.",
                5,
                "Điều chỉnh đúng kênh liên hệ và xác nhận lại."
        ));
        list.add(new TicketTemplate(
                "YTE_BHYT",
                "Hồ sơ BHYT không cập nhật trạng thái sau khi nộp ảnh thẻ mới",
                "Em tải ảnh thẻ BHYT mới lên trang hồ sơ nhưng trạng thái vẫn hiển thị thông tin thẻ cũ. Em muốn biết hồ sơ mới đã được tiếp nhận hay còn chờ xác minh.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_SUCKHOE",
                "Xin hướng dẫn vị trí tiếp nhận sơ cứu trong khuôn viên trường",
                "Em đang tham gia hoạt động ở khu nhà học mới và muốn biết phòng y tế gần nhất cũng như số liên hệ trong trường hợp cần hỗ trợ sơ cứu. Trang thông tin sinh viên chưa hiển thị bản đồ vị trí.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                8,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận y tế đã gửi vị trí phòng tiếp nhận gần khu nhà học và thông tin liên hệ trong giờ hoạt động. Với trường hợp khẩn cấp, em cần liên hệ nhân viên trực tại khu vực theo hướng dẫn được niêm yết.",
                "Em đã lưu thông tin liên hệ và biết vị trí phòng y tế gần nhất.",
                "Thông tin vị trí tiếp nhận đã được gửi. Em xem bảng hướng dẫn tại khu nhà học để biết thêm lối đi và đầu mối hỗ trợ tại chỗ.",
                5,
                "Thông tin định vị rõ ràng, hữu ích cho tình huống thực tế."
        ));
        list.add(new TicketTemplate(
                "YTE_HOTRO",
                "Xin tư vấn quy trình trở lại học sau thời gian nghỉ vì sức khỏe",
                "Em đã nghỉ học một thời gian theo hướng dẫn y tế và muốn phối hợp với cố vấn học tập để sắp xếp kế hoạch quay lại lớp. Em cần biết đầu mối nào tiếp nhận thông tin và tài liệu nào là cần thiết.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_BHYT_HOSO",
                "Không thể xóa ảnh BHYT cũ sau khi thay bằng thẻ mới",
                "Hồ sơ bảo hiểm vẫn hiển thị ảnh thẻ đã hết hạn bên cạnh ảnh thẻ mới. Em muốn bảo đảm bản đang dùng để đối chiếu là thẻ mới nhất, tránh nhân viên lấy nhầm thông tin cũ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã ghi nhận yêu cầu cập nhật phiên bản thẻ và đang kiểm tra cách lưu lịch sử minh chứng. Em xác nhận ngày hết hạn trên thẻ mới và giữ bản cũ trong lịch sử nếu hệ thống yêu cầu phục vụ đối chiếu.",
                "Em xác nhận thẻ mới có thời hạn đến cuối năm sau và đã tải rõ cả mặt trước lẫn mặt sau.",
                "Hồ sơ đã đánh dấu thẻ mới là phiên bản hiện hành, bản cũ được lưu trong lịch sử theo quy định. Em kiểm tra lại trạng thái hiệu lực để bảo đảm dữ liệu đang sử dụng là đúng.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_SUCKHOE_TUVAN",
                "Lịch tư vấn được đặt vào khung giờ phòng y tế không tiếp nhận",
                "Hệ thống cho em chọn lịch vào buổi trưa nhưng khi đến thì phòng tiếp nhận đóng cửa theo lịch trực. Em muốn xác nhận lịch hợp lệ và đề nghị cập nhật khung giờ đặt lịch để người khác không gặp tình trạng tương tự.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_SUACHUA",
                "Mái che lối đi bị bung vít sau mưa lớn",
                "Sau trận mưa lớn, một phần mái che nối giữa hai khu nhà bị bung vít và rung khi có gió. Em báo để đội cơ sở vật chất kiểm tra, đặt cảnh báo và khắc phục trước giờ sinh viên đi học đông.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.URGENT,
                1,
                6,
                true,
                "Cần ưu tiên theo yêu cầu: Mái che lối đi bị bung vít sau mưa lớn",
                false,
                SlaStatus.ON_TRACK,
                false,
                "Đội cơ sở vật chất đã ghi nhận vị trí và đang bố trí kiểm tra cố định mái che. Khu vực bên dưới cần tránh đi qua cho đến khi nhân viên xác nhận an toàn và tháo cảnh báo.",
                "Em đã báo nhân viên trực tòa nhà và mọi người đang đi theo lối khác.",
                "Mái che đã được gia cố và kiểm tra sau sửa chữa. Lối đi chỉ được mở lại sau khi nhân viên phụ trách xác nhận không còn bộ phận lỏng.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_PHONGHOC",
                "Âm thanh phòng học bị vọng khi kết nối máy tính cá nhân",
                "Khi giảng viên kết nối máy tính cá nhân vào hệ thống âm thanh phòng học, tiếng nói bị vọng và lặp lại. Em muốn báo kỹ thuật kiểm tra thiết lập micro và loa trước buổi giảng kế tiếp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_THIETBI",
                "Thiết bị đo trong phòng thực hành hiển thị cảnh báo hiệu chuẩn",
                "Trong buổi thực hành, thiết bị đo hiển thị cảnh báo đến hạn hiệu chuẩn và không cho lưu kết quả. Em muốn xác nhận có được tiếp tục sử dụng hay cần đổi thiết bị khác trong buổi học.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.HIGH,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận thiết bị cần mã tài sản hoặc nhãn hiệu chuẩn và mã phòng thực hành để xác minh trạng thái thiết bị. Em không tự bỏ qua cảnh báo hoặc tiếp tục dùng thiết bị khi chưa có xác nhận từ người phụ trách phòng.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_SUACHUA_BAOHONG",
                "Ảnh đính kèm phiếu báo hỏng không hiển thị sau khi đã gửi",
                "Em gửi phiếu báo hỏng kèm ảnh vị trí thiết bị, trang báo tiếp nhận thành công nhưng khi mở lại phiếu không thấy ảnh. Em muốn kiểm tra nhân viên bảo trì có nhận được minh chứng không.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                9,
                1,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã xác nhận ảnh không được liên kết vào phiếu do lỗi tải tệp ở bước cuối. Quyền bổ sung ảnh đã được mở và phiếu sẽ giữ cùng mã yêu cầu để nhân viên bảo trì theo dõi.",
                "Em đã tải lại ảnh và thấy hình hiển thị trong chi tiết phiếu.",
                "Minh chứng ảnh đã được gắn vào phiếu hiện tại. Em không tạo phiếu mới và kiểm tra bản xem trước để chắc chắn ảnh có thể mở được.",
                5,
                "Giữ nguyên mã phiếu và bổ sung được hình ảnh cần thiết."
        ));
        list.add(new TicketTemplate(
                "CSVC_PHONGHOC_DANGKY",
                "Xin đặt phòng có thiết bị ghi hình cho buổi báo cáo trực tuyến",
                "Nhóm em tổ chức buổi báo cáo có người phản biện tham dự từ xa nên cần camera, micro và kết nối ổn định. Em muốn biết phòng nào có đủ thiết bị và cần đăng ký trước bao lâu.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_LICHTHI",
                "Xin xác nhận giờ có mặt khi lịch thi ghi hai mốc thời gian",
                "Thông báo thi ghi giờ mở cửa phòng là 7 giờ 15 và giờ bắt đầu bài thi là 7 giờ 45. Em muốn xác nhận sinh viên cần có mặt trước bao lâu để làm thủ tục vào phòng.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận khảo thí cần mã học phần và ca thi để kiểm tra hướng dẫn có mặt áp dụng cho buổi thi đó. Em gửi thông tin ca thi đang hiển thị để chúng tôi trả lời đúng quy định của buổi thi.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_PHUCKHAO",
                "Xin xác nhận lệ phí phúc khảo đã được hoàn sau khi điểm thay đổi",
                "Kết quả phúc khảo làm điểm của em tăng nhưng tài khoản vẫn không thấy khoản hoàn lệ phí. Em muốn biết khoản hoàn được chuyển về ngân hàng hay trừ vào công nợ học kỳ kế tiếp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_KHAOSAT",
                "Khảo sát học phần ghi nhầm tên giảng viên phụ trách",
                "Khảo sát môn học của em hiển thị tên giảng viên khác người trực tiếp dạy lớp. Em muốn báo để phản hồi được gắn đúng học phần và người phụ trách trước khi hết hạn khảo sát.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                3,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đảm bảo chất lượng đang đối chiếu mã lớp học phần với danh sách phân công giảng dạy của kỳ này. Em cung cấp mã lớp để xác định bản khảo sát đang gắn sai thông tin.",
                "Em gửi mã lớp và ảnh phần tên giảng viên trên khảo sát.",
                "Thông tin giảng viên khảo sát đã được đối chiếu và chuyển sang bản ghi đúng lớp học phần. Em mở lại khảo sát để kiểm tra trước khi gửi phản hồi.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_LICHTHI_TRUNGLICH",
                "Sinh viên học hai chương trình bị xếp lịch thi trùng nhau",
                "Em theo học song ngành và hai học phần thuộc hai chương trình có lịch thi chung một buổi. Em muốn biết quy trình đề nghị đổi ca và cần xác nhận của đơn vị nào trước khi gửi yêu cầu.",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                1,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_PHUCKHAO_HOSO",
                "Không thể sửa thông tin liên hệ trong hồ sơ phúc khảo đang xử lý",
                "Email liên hệ trong đơn phúc khảo bị gõ nhầm một ký tự nên em không thể nhận thông báo kết quả. Hồ sơ đã chuyển sang đang xử lý và trang không còn cho sửa thông tin.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã cập nhật email liên hệ sau khi đối chiếu mã đơn và hồ sơ sinh viên. Thay đổi chỉ tác động kênh nhận thông báo, không làm thay đổi nội dung yêu cầu phúc khảo đang được xử lý.",
                "Em đã nhận email xác nhận từ địa chỉ mới và kiểm tra đúng mã đơn.",
                "Email liên hệ của hồ sơ đã được điều chỉnh. Em dùng mã đơn hiện tại để theo dõi tiến độ thay vì tạo yêu cầu phúc khảo mới.",
                5,
                "Cập nhật liên hệ mà không làm gián đoạn quá trình xử lý."
        ));
        list.add(new TicketTemplate(
                "SDH_TUYENSINH",
                "Xin hỏi điều kiện nộp hồ sơ khi văn bằng nước ngoài chưa hoàn tất công nhận",
                "Em tốt nghiệp ở nước ngoài và đang chờ hoàn tất thủ tục công nhận văn bằng. Em muốn biết có thể tạo hồ sơ dự tuyển trước rồi bổ sung giấy công nhận sau không, hay phải có đầy đủ ngay lúc nộp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOSO",
                "Bảng kê công trình khoa học không cho thêm đường dẫn bài báo",
                "Em khai báo công trình khoa học đã công bố nhưng phần biểu mẫu không cho nhập URL bài báo thứ hai. Em muốn bổ sung đầy đủ danh mục công trình mà không làm mất dữ liệu đã nhập ở bản đầu.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận kỹ thuật đã ghi nhận lỗi giới hạn trường nhập công trình khoa học. Em giữ lại danh sách DOI hoặc liên kết bài báo và gửi mã hồ sơ để chúng tôi hướng dẫn cập nhật đúng phiên bản biểu mẫu.",
                "Em đã lưu danh sách DOI vào tài liệu riêng và chờ hướng dẫn cập nhật trên trang.",
                "Biểu mẫu đã được điều chỉnh để lưu nhiều công trình khoa học trong hồ sơ. Em thêm từng công trình và kiểm tra nội dung đã lưu sau mỗi lần cập nhật.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOCVU",
                "Xin xác nhận yêu cầu bảo lưu kết quả học tập của học viên cao học",
                "Em cần tạm dừng học một học kỳ do lịch công tác kéo dài và muốn tìm hiểu thủ tục bảo lưu, thời hạn nộp đơn và tác động đến thời gian đào tạo tối đa của chương trình.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đào tạo sau đại học cần mã chương trình, khóa học và thời điểm em dự định bảo lưu để hướng dẫn theo quy chế áp dụng. Em gửi thông tin chương trình và kỳ học dự kiến tạm dừng.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_TUYENSINH_HOSO",
                "Chứng chỉ ngoại ngữ tải lên bị cắt mất mặt sau có thông tin xác thực",
                "Bản scan chứng chỉ ngoại ngữ của em có đủ hai mặt nhưng file tải lên chỉ hiển thị mặt trước sau khi gửi. Em muốn thay lại bản đầy đủ trước khi hội đồng thẩm tra hồ sơ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOSO_BOSUNG",
                "Hồ sơ bổ sung bị đóng trước hạn ghi trong email yêu cầu",
                "Email yêu cầu bổ sung ghi hạn cuối là ngày 28, nhưng cổng tuyển sinh đã khóa mục tải tệp từ ngày 26. Em muốn kiểm tra thời hạn nào áp dụng và xin mở lại chức năng để hoàn thiện hồ sơ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tuyển sinh đã nhận mã hồ sơ và đang đối chiếu thời điểm khóa biểu mẫu với hạn trong email. Em gửi email thông báo nguyên bản cùng ảnh trang hồ sơ hiện tại để xác minh chênh lệch.",
                "Em đã gửi email thông báo có ghi hạn ngày 28 như yêu cầu.",
                "Sau khi kiểm tra thông báo và cấu hình đợt tuyển sinh, quyền bổ sung được mở lại đến thời hạn xác nhận chính thức. Em tải tài liệu còn thiếu và kiểm tra trạng thái gửi trước khi kết thúc phiên.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Email khôi phục tài khoản không còn sử dụng",
                "Em đã đổi địa chỉ email cá nhân nên không nhận được mã xác minh khi khôi phục tài khoản cổng sinh viên. Em muốn cập nhật kênh khôi phục sau khi xác minh mình là chủ tài khoản.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận kỹ thuật cần xác minh danh tính qua mã sinh viên và thông tin liên hệ đã đăng ký trước đó. Em không gửi mật khẩu hay mã OTP hiện tại; hãy làm theo hướng dẫn xác minh được gửi qua kênh chính thức.",
                "Em đã cung cấp mã sinh viên và xác nhận vẫn truy cập được email sinh viên do trường cấp.",
                "Kênh khôi phục đã được hướng dẫn cập nhật sau bước xác minh. Em kiểm tra cả hộp thư rác và chỉ dùng đường dẫn khôi phục từ thông báo chính thức.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_PORTAL_LOI",
                "Trang tra cứu học phí tự đăng xuất khi tải biên lai",
                "Mỗi lần em bấm tải biên lai trên cổng sinh viên thì hệ thống đưa về trang đăng nhập, dù các mục khác vẫn sử dụng bình thường. Em cần lấy biên lai để hoàn thiện hồ sơ.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                7,
                3,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Đội kỹ thuật đã kiểm tra phiên đăng nhập tại chức năng tải biên lai và điều chỉnh xử lý phiên khi tạo tệp PDF. Em đăng nhập lại và thử tải một biên lai gần nhất để xác nhận.",
                "Em đã tải được biên lai PDF và không bị chuyển về trang đăng nhập nữa.",
                "Lỗi phiên tại chức năng tải biên lai đã được xử lý. Em có thể dùng mã giao dịch trên biên lai để đối chiếu nếu cần hỗ trợ tiếp.",
                5,
                "Lỗi được xác định đúng chức năng và đã khắc phục."
        ));
        list.add(new TicketTemplate(
                "TV_TRACUU_TAILIEU",
                "Kết quả tra cứu không hiện luận văn lưu tại kho số",
                "Em tìm theo tên luận văn và tên tác giả nhưng không thấy tài liệu trong kho số thư viện, dù giảng viên hướng dẫn xác nhận đã nộp bản điện tử. Em muốn kiểm tra tình trạng mô tả tài liệu.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện sẽ đối chiếu kho số theo tên tác giả, năm bảo vệ và đơn vị quản lý tài liệu. Em cung cấp năm bảo vệ cùng tên khoa hoặc chương trình để khoanh vùng bản ghi, không cần tải lại toàn bộ luận văn lên yêu cầu này.",
                "Em gửi tên khoa, năm bảo vệ và tiêu đề đầy đủ của luận văn.",
                "Bản ghi được chuyển cho phụ trách kho số kiểm tra metadata và quyền hiển thị. Sau khi điều chỉnh, em thử tìm bằng cả tiêu đề và từ khóa tác giả.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_THUCTAP_HOSO",
                "Hồ sơ thực tập thiếu xác nhận tiếp nhận của doanh nghiệp",
                "Em đã tải thư mời thực tập nhưng cổng thông tin vẫn đánh dấu hồ sơ chưa đủ giấy xác nhận tiếp nhận. Em muốn biết thư có chữ ký điện tử của doanh nghiệp có được chấp nhận không.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần xem loại thư tiếp nhận, đơn vị ký và nội dung thời gian thực tập để xác định hồ sơ đáp ứng yêu cầu hay cần mẫu xác nhận riêng. Em tải lên văn bản đầy đủ trang có thông tin doanh nghiệp và chữ ký, đồng thời che dữ liệu cá nhân không liên quan.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_TUYENDUNG_HOSO",
                "Hồ sơ ứng tuyển bị đánh dấu thiếu bảng điểm dù đã tải bản PDF",
                "Em đã tải bảng điểm điện tử do trường cấp nhưng hồ sơ ứng tuyển trên cổng việc làm vẫn báo chưa có bảng điểm. Em muốn kiểm tra định dạng hoặc trạng thái xác minh của tệp.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                9,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra hồ sơ và phát hiện tệp bảng điểm được tải lên mục minh chứng khác nên hệ thống chưa ghi nhận tại trường bắt buộc. Em đã được hướng dẫn chuyển tệp sang đúng mục tài liệu học tập.",
                "Em đã chuyển bảng điểm sang đúng mục và trạng thái hồ sơ hiện đầy đủ rồi ạ.",
                "Hồ sơ ứng tuyển đã ghi nhận bảng điểm tại trường đúng. Em kiểm tra lại danh sách tài liệu trước khi gửi hồ sơ đến doanh nghiệp.",
                5,
                "Hướng dẫn đúng vị trí tài liệu cần bổ sung."
        ));
        list.add(new TicketTemplate(
                "KHCN_DETAI",
                "Xin điều chỉnh tên thành viên trong thuyết minh đề tài đã nộp",
                "Nhóm em phát hiện tên một thành viên bị viết sai dấu trong thuyết minh đề tài đã gửi. Em muốn chỉnh chính tả nhưng không thay đổi thành viên hoặc nội dung chuyên môn đã đăng ký.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận nghiên cứu cần mã đề tài và trang thuyết minh có tên cần chỉnh để xác minh đây là lỗi chính tả. Em gửi bản đã nộp cùng phần tên chính xác để được hướng dẫn phiên bản cập nhật phù hợp.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_HOITHAO",
                "Thông báo hội thảo ghi sai hình thức tham dự trực tuyến",
                "Em đăng ký hội thảo trực tuyến nhưng email xác nhận lại ghi tham dự trực tiếp tại hội trường. Em muốn xác minh hình thức tham dự để chủ động sắp xếp lịch học.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                6,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban tổ chức đã đối chiếu biểu mẫu đăng ký và cập nhật hình thức tham dự theo lựa chọn trực tuyến của em. Thông báo xác nhận mới được gửi lại qua email đăng ký.",
                "Em đã nhận thư mới ghi đúng hình thức trực tuyến.",
                "Hình thức tham dự đã được chỉnh theo đăng ký ban đầu. Em dùng liên kết trong email xác nhận mới khi vào hội thảo.",
                5,
                "Thông tin tham dự được sửa nhanh."
        ));
        list.add(new TicketTemplate(
                "KHCN_NGHIENCUU",
                "Thiết bị phòng thí nghiệm chưa có lịch hiệu chuẩn trước ngày thí nghiệm",
                "Nhóm em đặt lịch sử dụng thiết bị cho đợt đo mẫu nhưng không tìm thấy thông tin hiệu chuẩn còn hiệu lực. Em muốn xác nhận thiết bị có đủ điều kiện dùng cho nghiên cứu hay cần đổi lịch.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                3,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận quản lý phòng thí nghiệm đang xác minh hồ sơ hiệu chuẩn và tình trạng bảo trì của thiết bị. Em gửi mã thiết bị, phòng sử dụng và ngày dự kiến thí nghiệm để kiểm tra đúng tài sản.",
                "Em đã gửi mã thiết bị trên tem tài sản và lịch đặt phòng của nhóm.",
                "Thiết bị được yêu cầu kiểm tra trước khi sử dụng cho số liệu nghiên cứu. Nhóm em chờ xác nhận hợp lệ hoặc phương án thay thế từ cán bộ phụ trách phòng thí nghiệm.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_DETAI_HOSO",
                "Tệp thuyết minh đề tài tải lên bị đảo thứ tự trang",
                "Em mở bản xem trước hồ sơ đề tài thì phần kinh phí xuất hiện trước mục mục tiêu nghiên cứu, khác với tệp PDF gốc. Em muốn xác định hệ thống có lưu sai thứ tự trang hay chỉ hiển thị xem trước không chính xác.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                8,
                4,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận hỗ trợ đã kiểm tra tệp gốc và phát hiện bản xem trước được dựng từ tệp cũ đã lưu trong phiên trước. Em tải lại PDF đúng thứ tự trang và hệ thống đã cập nhật bản xem trước mới.",
                "Em đã mở lại hồ sơ và thấy mục tiêu nghiên cứu đứng trước phần kinh phí như bản gốc.",
                "Bản thuyết minh hiển thị đúng thứ tự trang. Em lưu bản PDF cuối cùng ở máy và kiểm tra xem trước một lần nữa trước khi gửi hội đồng.",
                5,
                "Đã xác định đúng lỗi phiên bản tệp."
        ));
        list.add(new TicketTemplate(
                "KHCN_HOITHAO_DANGKY",
                "Đăng ký hội thảo không chọn được suất trình bày poster",
                "Em muốn đăng ký trình bày poster nhưng biểu mẫu hội thảo chỉ cho chọn tham dự nghe báo cáo. Em cần biết suất poster đã hết hay biểu mẫu chưa bật lựa chọn cho nhóm nghiên cứu sinh viên.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_BHYT",
                "Mã thẻ bảo hiểm y tế hiển thị thiếu ký tự trên hồ sơ sinh viên",
                "Mã thẻ BHYT trong hồ sơ điện tử của em thiếu một ký tự so với thẻ do cơ quan bảo hiểm cấp. Em muốn sửa trước khi trường lập danh sách đối chiếu bảo hiểm.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận y tế cần đối chiếu mã thẻ trên thẻ còn hiệu lực với dữ liệu hồ sơ sinh viên. Em cung cấp ảnh phần mã thẻ và thời hạn sử dụng qua kênh bảo mật, che các thông tin không cần thiết khác.",
                "Em đã gửi ảnh mặt trước thẻ, còn thời hạn sử dụng đến cuối năm.",
                "Thông tin được chuyển đối chiếu với dữ liệu bảo hiểm. Em kiểm tra lại mã sau khi có thông báo cập nhật và báo ngay nếu vẫn còn sai ký tự.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_SUCKHOE",
                "Xin đổi lịch khám sức khỏe định kỳ do trùng lịch thi",
                "Lịch khám sức khỏe của em trùng với buổi thi đã được công bố chính thức. Em xin đổi sang buổi khác nhưng trang đăng ký không còn hiển thị các khung giờ còn chỗ.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                3,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận y tế cần mã sinh viên, lịch khám hiện tại và ca thi bị trùng để kiểm tra phương án đổi lịch. Em gửi thông tin hai mốc thời gian, không cần cung cấp hồ sơ sức khỏe chi tiết trong yêu cầu đổi lịch.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_HOTRO",
                "Xin hướng dẫn tiếp cận phòng tư vấn sức khỏe cho người cần hỗ trợ di chuyển",
                "Em cần đến phòng tư vấn sức khỏe nhưng đang gặp khó khăn khi di chuyển cầu thang. Em muốn biết lối tiếp cận phù hợp và có cần đặt lịch trước để được hỗ trợ không.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                6,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận y tế đã hướng dẫn em liên hệ quầy tiếp nhận trước giờ đến để bố trí lối đi thuận tiện và nhân sự hỗ trợ. Thông tin về thời điểm tư vấn đã được gửi kèm để em chủ động sắp xếp.",
                "Em đã gọi quầy tiếp nhận và xác nhận được thời gian đến phù hợp.",
                "Phương án tiếp cận và thời gian tiếp nhận đã được xác nhận. Em liên hệ lại quầy nếu thời gian đến thay đổi để bộ phận sắp xếp hỗ trợ.",
                5,
                "Hướng dẫn cụ thể, tôn trọng nhu cầu tiếp cận."
        ));
        list.add(new TicketTemplate(
                "YTE_BHYT_HOSO",
                "Ảnh thẻ bảo hiểm y tế bị hệ thống từ chối vì dung lượng",
                "Em tải ảnh thẻ BHYT theo yêu cầu nhưng biểu mẫu báo tệp quá lớn dù ảnh rõ và đúng định dạng. Em muốn biết ngưỡng dung lượng và cách nộp mà không làm giảm khả năng đọc mã thẻ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_SUCKHOE_TUVAN",
                "Xin tư vấn cách đặt lịch trao đổi riêng về kết quả khám định kỳ",
                "Em đã nhận thông báo kết quả khám định kỳ và muốn đặt buổi trao đổi riêng với cán bộ y tế để hiểu hướng dẫn tiếp theo. Trang đặt lịch hiện không hiển thị mục tư vấn cá nhân.",
                TicketStatus.ASSIGNED,
                TicketPriority.NORMAL,
                2,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_SUACHUA",
                "Máy chiếu phòng học chập chờn khi kết nối cổng HDMI",
                "Trong giờ báo cáo nhóm, máy chiếu phòng học liên tục mất hình khi kết nối laptop qua HDMI. Em muốn báo kiểm tra thiết bị trước khi lớp khác sử dụng cùng phòng.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cơ sở vật chất đã ghi nhận mã phòng và đang kiểm tra cáp HDMI, bộ chuyển tín hiệu cùng cổng kết nối của máy chiếu. Em cho biết khung giờ lớp bị lỗi để kỹ thuật xác định thiết bị đã được dùng lúc nào.",
                "Lỗi xuất hiện trong ca học chiều tại phòng B304.",
                "Yêu cầu kiểm tra được chuyển cho kỹ thuật thiết bị trình chiếu. Em thử kết nối lại sau khi có thông báo hoàn tất và báo mã phòng nếu lỗi tái diễn.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_PHONGHOC",
                "Phòng học được phân công không đủ chỗ cho sĩ số lớp",
                "Lớp em có 65 sinh viên nhưng phòng được xếp chỉ có 45 chỗ ngồi. Lớp muốn xác nhận có phải lịch chuyển phòng chưa được cập nhật hay cần điều chỉnh địa điểm học.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                5,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu sĩ số lớp với sức chứa phòng và cập nhật yêu cầu đổi phòng trong lịch sử dụng cơ sở vật chất. Giảng viên được thông báo địa điểm mới qua kênh điều phối lớp.",
                "Lớp em đã nhận được thông báo chuyển sang phòng có đủ chỗ.",
                "Lịch phòng học đã được điều chỉnh theo sức chứa. Em theo dõi thông báo mới nhất trên cổng lớp để tránh sử dụng thông tin phòng cũ.",
                5,
                "Điều chỉnh kịp thời, tránh ảnh hưởng buổi học."
        ));
        list.add(new TicketTemplate(
                "CSVC_THIETBI",
                "Máy tính thực hành không nhận được tai nghe tại bàn số 18",
                "Máy tính ở bàn số 18 trong phòng thực hành không nhận tai nghe dù tai nghe vẫn hoạt động với thiết bị khác. Nhóm em cần dùng âm thanh cho bài thực hành nên xin kiểm tra cổng kết nối.",
                TicketStatus.ASSIGNED,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_SUACHUA_BAOHONG",
                "Báo cửa sổ phòng học không chốt được sau giờ mưa",
                "Cửa sổ phòng học không chốt kín được sau cơn mưa, nước có thể tạt vào khu vực bàn gần cửa. Em muốn báo đội bảo trì kiểm tra khóa và bản lề trước buổi học kế tiếp.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận bảo trì cần xác định chính xác phòng và vị trí cửa sổ bị lỗi để chuẩn bị phụ tùng phù hợp. Em gửi mã phòng, dãy cửa và ảnh hiện trạng nếu có thể chụp an toàn từ vị trí khô ráo.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_PHONGHOC_DANGKY",
                "Đề nghị đặt phòng học nhóm có bảng viết ngoài giờ hành chính",
                "Nhóm em cần phòng có bảng viết để chuẩn bị bài thuyết trình vào buổi tối sau giờ học. Em muốn biết quy trình đăng ký phòng học nhóm và giới hạn thời gian sử dụng.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_LICHTHI",
                "Thông báo phòng thi trên cổng khảo thí chưa hiển thị số tầng",
                "Thông báo ca thi của em có ngày giờ và số phòng nhưng không ghi tầng, trong khi tòa nhà có nhiều khu vực thi. Em muốn xác nhận vị trí chính xác để không đến nhầm phòng.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận khảo thí đang đối chiếu sơ đồ phòng và danh sách bố trí của ca thi này. Em gửi mã học phần cùng ca thi hiển thị để xác nhận tầng áp dụng cho đúng địa điểm.",
                "Em đã gửi mã học phần và ảnh thông báo ca thi hiện tại.",
                "Thông tin vị trí phòng thi đã được kiểm tra và cập nhật kèm tầng sử dụng. Em mở lại thông báo mới nhất trước ngày thi để dùng đúng địa điểm.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_PHUCKHAO",
                "Đơn phúc khảo đã tiếp nhận nhưng chưa có mã theo dõi",
                "Em gửi đơn phúc khảo và nhận thông báo nộp thành công nhưng danh sách yêu cầu chưa hiển thị mã hồ sơ. Em muốn xác nhận đơn đã vào hàng chờ xử lý hay cần gửi lại.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã sinh viên, học phần và thời điểm gửi đơn để tìm bản ghi tiếp nhận trên hệ thống. Em không gửi lại đơn mới khi chưa có kết quả đối chiếu để tránh phát sinh yêu cầu trùng.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_KHAOSAT",
                "Khảo sát môn học không mở được trên điện thoại nhưng chạy trên máy tính",
                "Em mở khảo sát học phần từ điện thoại thì trang liên tục quay về trang chủ, trong khi máy tính vẫn vào được. Em muốn báo lỗi tương thích để hoàn thành khảo sát bằng thiết bị di động.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần loại điện thoại, trình duyệt và đường dẫn khảo sát đang dùng để xác định vấn đề phiên đăng nhập hay bố cục di động. Em gửi thông tin thiết bị và thời điểm lỗi, không gửi thông tin tài khoản nhạy cảm.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_LICHTHI_TRUNGLICH",
                "Lịch thi bù trùng với lịch bảo vệ đồ án đã được hội đồng xác nhận",
                "Em được phân lịch thi bù đúng buổi hội đồng bảo vệ đồ án đã gửi thư xác nhận trước đó. Em muốn biết cần ưu tiên thông báo nào và quy trình đề nghị đổi lịch thi ra sao.",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                1,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_PHUCKHAO_HOSO",
                "Hồ sơ phúc khảo không lưu được số điện thoại liên hệ mới",
                "Em đổi số điện thoại sau khi nộp đơn phúc khảo và muốn cập nhật để bộ phận có thể liên hệ khi cần. Trường nhập số điện thoại trong hồ sơ đang bị khóa nên em không tự sửa được.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã xác minh mã đơn và cập nhật số liên hệ mới vào hồ sơ phúc khảo đang xử lý. Thay đổi được ghi nhận riêng ở thông tin liên hệ và không làm thay đổi nội dung yêu cầu.",
                "Em đã nhận cuộc gọi xác nhận từ số mới rồi ạ.",
                "Thông tin liên hệ mới đã được lưu. Em giữ điện thoại hoạt động trong thời gian xử lý và theo dõi mã đơn để nhận thông báo tiến độ.",
                5,
                "Cập nhật thông tin liên hệ mà không ảnh hưởng hồ sơ đang xử lý."
        ));
        list.add(new TicketTemplate(
                "SDH_TUYENSINH",
                "Xin hỏi lịch nhận hồ sơ bản giấy cho chương trình sau đại học",
                "Em đã hoàn tất hồ sơ điện tử và muốn biết có cần nộp thêm bản giấy tại trường hay mọi tài liệu sẽ được thẩm tra qua hệ thống. Em cần chuẩn bị trước thời điểm hết hạn đăng ký.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOSO",
                "Không thể cập nhật địa chỉ liên hệ sau khi đã lưu hồ sơ dự tuyển",
                "Em phát hiện địa chỉ liên hệ ghi thiếu số nhà sau khi lưu hồ sơ. Trang không cho chỉnh trường này nữa dù hồ sơ vẫn đang chờ thẩm tra. Em xin cập nhật để nhận thư đúng địa chỉ.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                5,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã hồ sơ và địa chỉ liên hệ chính xác em muốn ghi nhận để xác minh trước khi cập nhật. Em gửi riêng thông tin cần thiết qua kênh hỗ trợ bảo mật theo hướng dẫn của bộ phận tiếp nhận.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOCVU",
                "Xin xác nhận điều kiện dự thi kết thúc học phần khi còn thiếu học phí",
                "Em đã hoàn thành yêu cầu chuyên cần nhưng tài khoản học phí còn một khoản đang đối soát. Em muốn biết khoản đó có làm thay đổi trạng thái đủ điều kiện dự thi hay cần xin xác nhận riêng từ bộ phận tài chính.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_TUYENSINH_HOSO",
                "Bản dịch văn bằng chưa được đánh dấu đã xác thực trong hồ sơ",
                "Em đã nộp bản dịch văn bằng theo yêu cầu nhưng trạng thái hồ sơ vẫn báo chưa xác thực bản dịch. Em muốn biết có cần nộp thêm chứng thực bản dịch hay phải gửi lại cả bản gốc và bản dịch trong cùng một tệp.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tuyển sinh đang đối chiếu loại bản dịch và chứng thực được nộp theo hướng dẫn của chương trình. Em gửi mã hồ sơ và tên tài liệu đang bị đánh dấu để xác định chính xác phần cần bổ sung.",
                "Em đã gửi ảnh thông báo và xác nhận tệp hiện có gồm bản dịch công chứng.",
                "Hồ sơ đã được kiểm tra và cập nhật hướng dẫn phần chứng thực còn thiếu nếu áp dụng. Em dùng danh mục tài liệu trong thông báo mới để hoàn tất hồ sơ.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOSO_BOSUNG",
                "Không có nút xác nhận hoàn tất sau khi tải đủ tài liệu bổ sung",
                "Em đã tải tất cả tài liệu được yêu cầu, mỗi mục đều hiển thị tên tệp, nhưng không tìm thấy nút gửi hoàn tất. Em muốn xác nhận hồ sơ đã được chuyển sang bước thẩm tra hay mới lưu bản nháp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP",
                "Môn học thay thế chưa được công nhận trong kế hoạch đăng ký",
                "Em đã hoàn thành môn tương đương ở chương trình trước nhưng hệ thống vẫn yêu cầu đăng ký lại môn bắt buộc này. Em muốn kiểm tra quyết định công nhận tương đương đã được đồng bộ vào kế hoạch học tập chưa.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                6,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng đào tạo đang đối chiếu quyết định công nhận học phần với cấu trúc chương trình hiện tại. Em gửi mã môn đã học và mã môn đang bị yêu cầu đăng ký để kiểm tra chính xác cặp học phần tương đương.",
                "Em gửi mã môn cũ cùng quyết định công nhận học phần đã nhận từ khoa.",
                "Kế hoạch học tập đã được đối chiếu và yêu cầu đăng ký lại được chuyển xử lý theo kết quả công nhận. Em tải lại tiến độ sau khi hệ thống cập nhật.",
                5,
                "Đối chiếu đúng cặp học phần và tránh đăng ký trùng."
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU",
                "Điểm chuyên cần không khớp bảng theo dõi của giảng viên",
                "Bảng điểm trên portal ghi chuyên cần của em thấp hơn bảng theo dõi đã được giảng viên công bố trên lớp. Em muốn biết cần xin xác nhận từ giảng viên hay nộp minh chứng trực tiếp cho Phòng Đào tạo.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                3,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng cần mã lớp học phần và xác nhận của giảng viên phụ trách trước khi điều chỉnh điểm thành phần. Em gửi ảnh phần điểm hiển thị và đề nghị giảng viên xác nhận bảng theo dõi chính thức.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP",
                "Tên tiếng Anh trên dự thảo bằng tốt nghiệp khác hộ chiếu",
                "Dự thảo thông tin bằng tốt nghiệp của em không trùng thứ tự họ tên tiếng Anh trên hộ chiếu. Em muốn chỉnh trước khi nhà trường chốt dữ liệu in bằng để dùng hồ sơ học tập ở nước ngoài.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                8,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng đã đối chiếu bản hộ chiếu và hồ sơ cá nhân, sau đó cập nhật thứ tự tên tiếng Anh trong dữ liệu dự thảo. Em kiểm tra kỹ từng ký tự trên trang xác nhận trước khi đồng ý chốt thông tin in bằng.",
                "Em đã xác nhận thứ tự họ tên hiện đã khớp hộ chiếu.",
                "Thông tin dự thảo đã được cập nhật theo giấy tờ em cung cấp. Em lưu lại bản xác nhận và phản hồi ngay nếu còn sai dấu cách hoặc thứ tự tên.",
                5,
                "Điều chỉnh thông tin quan trọng trước khi in bằng."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG",
                "Tài khoản ngân hàng nhận học bổng đứng tên không khớp hồ sơ sinh viên",
                "Thông báo chi trả học bổng yêu cầu tài khoản chính chủ nhưng tài khoản em đăng ký trước đây là tài khoản gia đình. Em muốn đổi thông tin trước khi danh sách chuyển khoản được chốt.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần xác nhận tài khoản nhận học bổng thuộc chính sinh viên theo yêu cầu chi trả của đợt này. Em gửi giấy xác nhận thông tin chủ tài khoản hoặc tài liệu được hướng dẫn qua kênh bảo mật, không ghi đầy đủ số tài khoản trong phần mô tả công khai.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN",
                "Minh chứng hoạt động tình nguyện bị gắn nhầm sang học kỳ trước",
                "Em tải giấy xác nhận tình nguyện trong học kỳ hiện tại nhưng trang điểm rèn luyện lại ghi minh chứng vào kỳ trước. Em muốn điều chỉnh học kỳ ghi nhận để kết quả phản ánh đúng thời gian tham gia.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                9,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Cán bộ đã kiểm tra ngày tổ chức trên giấy xác nhận và chuyển minh chứng sang kỳ đánh giá hiện tại. Lịch sử xử lý vẫn lưu thông tin ban đầu để đối chiếu khi cần.",
                "Em đã mở lại phần minh chứng và thấy hoạt động nằm đúng học kỳ.",
                "Minh chứng đã được chuyển sang đúng kỳ đánh giá. Em kiểm tra ngày sự kiện trước khi gửi các minh chứng khác để tránh nhầm kỳ.",
                5,
                "Đã điều chỉnh đúng kỳ và giữ được lịch sử đối chiếu."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI",
                "Thanh toán chia thành hai giao dịch nhưng portal chỉ ghi nhận một khoản",
                "Em nộp học phí qua hai giao dịch do giới hạn chuyển khoản một lần, nhưng công nợ mới giảm theo giao dịch đầu tiên. Em muốn đối chiếu cả hai mã giao dịch để tránh bị ghi nhận còn thiếu tiền.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.URGENT,
                2,
                6,
                true,
                "Cần ưu tiên theo yêu cầu: Thanh toán chia thành hai giao dịch nhưng portal chỉ ghi nhận một khoản",
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đã tiếp nhận hai mã giao dịch và đang đối chiếu với tài khoản thu cùng thời điểm hạch toán. Em giữ cả hai biên lai và không chuyển khoản lần ba cho đến khi có kết quả kiểm tra.",
                "Em đã gửi hai mã giao dịch cùng số tiền tương ứng trong kênh hỗ trợ bảo mật.",
                "Kết quả đối soát được cập nhật theo từng giao dịch riêng. Em kiểm tra số dư công nợ sau khi nhận thông báo hoàn tất và lưu cả hai biên lai để tham chiếu.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM",
                "Hồ sơ miễn giảm bị yêu cầu giấy xác nhận còn hiệu lực dù đã nộp bản mới",
                "Em đã nộp giấy xác nhận diện chính sách cấp trong tháng này nhưng trạng thái hồ sơ vẫn báo giấy tờ hết hạn. Em muốn kiểm tra ngày cấp và trường dữ liệu nào khiến hệ thống từ chối tài liệu.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra tệp tải lên và phát hiện trang hiển thị thiếu phần ngày cấp do tệp bị cắt mép. Em gửi lại bản quét đầy đủ trang có cơ quan xác nhận để hồ sơ được cập nhật hợp lệ.",
                "Em đã tải lại bản scan đầy đủ và trạng thái hiện đã chuyển sang đủ hồ sơ.",
                "Hồ sơ đã được tiếp nhận sau khi thay tài liệu hiển thị đầy đủ ngày cấp. Em giữ bản gốc để xuất trình nếu cơ quan thẩm định yêu cầu đối chiếu.",
                5,
                "Nêu rõ lý do từ chối và cách khắc phục."
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY",
                "Bạn cùng phòng rời ký túc xá nhưng danh sách phòng chưa cập nhật",
                "Bạn cùng phòng của em đã hoàn tất trả phòng nhưng danh sách người ở trên portal vẫn hiển thị tên bạn ấy. Em muốn xác nhận việc bàn giao đã được chốt để tránh ảnh hưởng đến thống kê phòng.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU",
                "Đèn hành lang tầng bốn nhấp nháy nhiều lần trong buổi tối",
                "Đèn hành lang tầng bốn liên tục chớp tắt từ tối qua, gây khó quan sát khi sinh viên đi lại. Em đề nghị đội kỹ thuật kiểm tra nguồn điện và bộ đèn tại đoạn hành lang gần cầu thang.",
                TicketStatus.ASSIGNED,
                TicketPriority.HIGH,
                1,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_TAIKHOAN",
                "Tài khoản sinh viên mới nhận thông báo chưa kích hoạt email trường",
                "Em đã được cấp tài khoản sau khi nhập học nhưng email trường vẫn báo chưa kích hoạt, nên em không nhận được thông báo học vụ. Em muốn xác nhận quy trình kích hoạt đang thiếu bước nào.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                3,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận kỹ thuật cần mã sinh viên và ảnh trạng thái kích hoạt để phân biệt tài khoản chưa khởi tạo với phiên xác minh chưa hoàn tất. Em gửi ảnh trang thông báo lỗi, không gửi mật khẩu hay mã xác minh.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_PORTAL",
                "Danh sách lịch học bị mất bộ lọc học kỳ sau khi quay lại trang",
                "Em chọn học kỳ hiện tại và lọc các lớp có lịch thực hành, sau khi mở chi tiết một lớp rồi quay lại danh sách thì bộ lọc bị xóa. Em muốn giữ điều kiện lọc để tiếp tục kiểm tra các lớp còn lại.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                8,
                4,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Kỹ thuật đã điều chỉnh trạng thái bộ lọc để giữ lựa chọn khi người dùng quay lại danh sách từ trang chi tiết. Em thử chuyển qua lại giữa hai lớp để kiểm tra các điều kiện lọc còn được giữ nguyên.",
                "Em đã quay lại danh sách và không cần chọn lại học kỳ nữa.",
                "Bộ lọc lịch học đã giữ được điều kiện tìm kiếm trong luồng quay lại. Em tải lại trang nếu phiên trình duyệt còn giữ trạng thái trước bản cập nhật.",
                5,
                "Khắc phục đúng thao tác gây mất bộ lọc."
        ));
        list.add(new TicketTemplate(
                "CNTT_BAOMAT",
                "Nhận email đặt lại mật khẩu dù em không thực hiện yêu cầu",
                "Em nhận được thư thông báo đặt lại mật khẩu nhưng không tự gửi yêu cầu. Em muốn biết cần làm gì để bảo vệ tài khoản và kiểm tra có phiên đăng nhập lạ hay không.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.URGENT,
                1,
                5,
                true,
                "Cần ưu tiên theo yêu cầu: Nhận email đặt lại mật khẩu dù em không thực hiện yêu cầu",
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã ghi nhận sự kiện và hướng dẫn em đổi mật khẩu từ trang chính thức, kết thúc các phiên đăng nhập khác và không bấm liên kết trong thư nếu không chắc nguồn gửi. Em xác nhận thời điểm nhận thư để kiểm tra nhật ký bảo mật.",
                "Em đã tự mở portal từ địa chỉ quen thuộc, đổi mật khẩu và đăng xuất các phiên khác.",
                "Các bước bảo vệ tài khoản đã được hoàn thành. Bộ phận tiếp tục rà soát sự kiện gửi yêu cầu; em không chia sẻ mã xác minh cho bất kỳ ai.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Mã xác minh khôi phục đến sau khi yêu cầu đã hết hạn",
                "Em nhận được mã xác minh qua email sau vài phút nhưng khi nhập thì hệ thống báo mã hết hạn. Em muốn biết có thể gửi yêu cầu mới hay cần đợi lượt cũ hết hiệu lực hoàn toàn.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                6,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Kỹ thuật đã kiểm tra độ trễ gửi thư và điều chỉnh thông báo khi mã đã hết thời hạn. Em tạo một yêu cầu khôi phục mới từ trang chính thức, chỉ sử dụng mã mới nhất và bỏ qua các mã đã nhận trước đó.",
                "Em đã gửi yêu cầu mới và mã mới được chấp nhận ngay khi nhập.",
                "Yêu cầu khôi phục đã hoàn tất bằng mã còn hiệu lực. Em tránh bấm gửi mã nhiều lần liên tiếp để không nhầm thứ tự các thư xác minh.",
                5,
                "Hướng dẫn rõ cách xử lý mã hết hạn."
        ));
        list.add(new TicketTemplate(
                "CNTT_PORTAL_LOI",
                "Trang lịch học hiển thị ngày Chủ nhật thành ngày thứ Hai",
                "Trên trang lịch học, một số buổi học Chủ nhật bị hiển thị sang cột thứ Hai sau khi em đổi múi giờ trên thiết bị. Em muốn xác định lỗi ở lịch lưu trên hệ thống hay chỉ ở cách hiển thị.",
                TicketStatus.CANCELLED,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_MUONTRA",
                "Phiếu mượn sách ghi sai ngày hạn trả sau khi đổi lịch nghỉ",
                "Em mượn sách trước khi thư viện thông báo đổi lịch hoạt động và phiếu mượn vẫn ghi ngày trả cũ. Em muốn xác nhận hạn trả chính thức để tránh bị tính quá hạn do lịch mở cửa thay đổi.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện đang đối chiếu giao dịch mượn với lịch hoạt động áp dụng trong thời gian điều chỉnh. Em cung cấp mã tài liệu và ngày mượn ghi trên tài khoản để xác nhận hạn trả hiện hành.",
                "Em đã gửi mã tài liệu của hai cuốn sách và ngày mượn trên portal.",
                "Hạn trả được xác minh theo lịch hoạt động đã công bố. Em kiểm tra ngày cập nhật trên tài khoản mượn và dùng ngày mới nhất khi sắp xếp trả sách.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TRACUU",
                "Bản ghi sách có hai mã phân loại khác nhau",
                "Em thấy cùng một đầu sách xuất hiện hai kết quả với mã phân loại khác nhau và vị trí giá sách không giống nhau. Em muốn biết nên dùng bản ghi nào để tìm sách bản giấy tại thư viện.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện cần ảnh bìa hoặc trang thông tin xuất bản cùng năm xuất bản để phân biệt các ấn bản khác nhau. Em gửi tên tác giả và năm xuất bản hiện có trên hai bản ghi để kiểm tra chính xác.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TAIKHOAN",
                "Tài khoản thư viện bị khóa sau nhiều lần nhập sai mã PIN mượn sách",
                "Em quên mã PIN tại máy tự phục vụ và nhập sai vài lần nên tài khoản thư viện bị khóa chức năng mượn. Em muốn biết cách xác minh để mở lại quyền sử dụng mà không mất danh sách sách đang giữ.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                7,
                6,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Thư viện đã xác minh tài khoản và hướng dẫn đặt lại mã PIN qua kênh chính thức. Các giao dịch mượn đang hoạt động vẫn được giữ nguyên, không cần tạo lại yêu cầu mượn sách.",
                "Em đã đặt lại mã PIN và kiểm tra danh sách sách đang mượn vẫn còn đầy đủ.",
                "Quyền sử dụng máy tự phục vụ đã được khôi phục. Em lưu mã PIN an toàn và liên hệ quầy nếu cần hỗ trợ thay vì thử nhập liên tục.",
                5,
                "Mở lại chức năng mà không ảnh hưởng giao dịch hiện tại."
        ));
        list.add(new TicketTemplate(
                "TV_MUONTRA_GIAHAN",
                "Nút gia hạn chỉ hiển thị với một trong ba cuốn đang mượn",
                "Em đang mượn ba cuốn sách, nhưng chỉ một cuốn có nút gia hạn. Em muốn biết các cuốn còn lại đang có người đặt trước hay đã đạt số lần gia hạn tối đa.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TV_TRACUU_TAILIEU",
                "Tài liệu điện tử chỉ cho đọc trực tuyến dù thông báo ghi có thể tải",
                "Em mở giáo trình điện tử trong thư viện số và chỉ thấy trình đọc trực tuyến, không có nút tải như mô tả ở trang danh mục. Em muốn xác nhận quyền truy cập theo loại tài khoản sinh viên.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Thư viện đã kiểm tra hợp đồng cấp quyền của tài liệu và xác nhận tài liệu này chỉ hỗ trợ đọc trực tuyến trong phạm vi giấy phép hiện tại. Mô tả ở danh mục đã được chuyển chỉnh sửa để không gây hiểu nhầm về chức năng tải.",
                "Em đã đọc được toàn bộ nội dung trên trình xem trực tuyến.",
                "Quyền truy cập của tài liệu đã được làm rõ trên danh mục. Em dùng trình xem trực tuyến theo điều khoản cấp phép và tìm giáo trình thay thế nếu cần bản tải được phép.",
                5,
                "Làm rõ giới hạn bản quyền và cập nhật mô tả."
        ));
        list.add(new TicketTemplate(
                "QHDN_THUCTAP",
                "Người hướng dẫn doanh nghiệp chưa xác nhận nhật ký tuần cuối",
                "Em đã hoàn tất nhật ký tuần cuối và báo cáo thực tập nhưng trạng thái xác nhận từ người hướng dẫn doanh nghiệp vẫn đang chờ. Em muốn biết cần nhắc doanh nghiệp trên hệ thống hay xin xác nhận qua email để hoàn tất môn thực tập.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                5,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã hồ sơ thực tập và thông tin người hướng dẫn được khai báo trong thỏa thuận thực tập để kiểm tra luồng gửi thông báo. Em xác nhận email người hướng dẫn hiện tại và ngày em gửi nhật ký cuối cùng.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_TUYENDUNG",
                "Lịch phỏng vấn được gửi sau thời hạn ứng tuyển của tin tuyển dụng",
                "Em nộp hồ sơ trước hạn nhưng thư mời phỏng vấn đến sau ngày hết hạn hiển thị trên tin. Em muốn xác minh lịch mời là chính thức và có cần xác nhận lại với nhà tuyển dụng.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                6,
                4,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu mã tin và xác nhận doanh nghiệp chủ động gia hạn quy trình tuyển chọn đối với hồ sơ đã tiếp nhận. Thông báo cập nhật đã được bổ sung vào tin để ứng viên biết lịch phỏng vấn vẫn hợp lệ.",
                "Em đã xác nhận lịch với doanh nghiệp theo thông tin mới.",
                "Lịch phỏng vấn của hồ sơ em đã được xác nhận. Em theo dõi email từ địa chỉ doanh nghiệp đã đăng ký và giữ lại thư mời cập nhật.",
                5,
                "Giải đáp được khác biệt giữa hạn tin đăng và lịch phỏng vấn."
        ));
        list.add(new TicketTemplate(
                "QHDN_CV",
                "Mẫu CV không cho thêm dự án cá nhân thứ ba",
                "Em có ba dự án lập trình muốn trình bày nhưng công cụ tạo CV chỉ cho nhập hai dự án. Em muốn biết có thể đính kèm portfolio riêng hoặc bổ sung dự án vào mục kinh nghiệm nào để nhà tuyển dụng thấy đầy đủ thông tin.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_THUCTAP_HOSO",
                "Giấy xác nhận thực tập chưa hiển thị người ký trong bản tải về",
                "Bản PDF xác nhận thực tập tải từ portal có thông tin doanh nghiệp và thời gian nhưng phần người ký bị trống. Em cần bản hoàn chỉnh để nộp cho bộ môn trước hạn báo cáo.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                4,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đang kiểm tra trạng thái phê duyệt của giấy xác nhận và dữ liệu người ký được gắn trong mẫu in. Em gửi mã hồ sơ và thời điểm tải bản PDF để đối chiếu phiên bản biểu mẫu.",
                "Em đã gửi mã hồ sơ và bản PDF hiện tại để kiểm tra.",
                "Bản xác nhận đã được phát hành lại sau khi thông tin người ký được bổ sung. Em tải lại từ hồ sơ đang hoạt động thay vì dùng bản PDF đã lưu trước đó.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QHDN_TUYENDUNG_HOSO",
                "Thông tin chuyên ngành trong hồ sơ ứng tuyển bị chuyển thành ngành học cũ",
                "Em cập nhật ngành học trên hồ sơ sinh viên nhưng hồ sơ ứng tuyển đã gửi vẫn hiện ngành cũ. Em muốn sửa thông tin hồ sơ ứng tuyển trước khi doanh nghiệp xem xét mà không phải tạo đơn ứng tuyển thứ hai.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã ứng tuyển và vị trí tuyển dụng để xác định doanh nghiệp đã mở hồ sơ hay chưa. Em gửi mã hồ sơ ứng tuyển và ngành học đúng theo hồ sơ sinh viên hiện tại để được hướng dẫn sửa trong phạm vi cho phép.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_DETAI",
                "Kết quả đánh giá giữa kỳ đề tài chưa cập nhật nhận xét của phản biện",
                "Nhóm em đã tham dự đánh giá giữa kỳ và hội đồng có đưa ra nhận xét, nhưng trang quản lý đề tài chỉ hiện điểm mà không có nội dung phản biện. Em muốn xem nhận xét để chỉnh kế hoạch nghiên cứu.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận nghiên cứu đang đối chiếu biên bản đánh giá và phần nhận xét được phép công bố cho nhóm thực hiện. Em gửi mã đề tài và ngày đánh giá để tìm đúng hồ sơ hội đồng.",
                "Em đã cung cấp mã đề tài và ngày hội đồng đánh giá giữa kỳ.",
                "Nhận xét được phép chia sẻ đã được bổ sung vào hồ sơ đề tài. Nhóm em cập nhật kế hoạch theo góp ý và giữ phiên bản trước để đối chiếu thay đổi.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_HOITHAO",
                "Email xác nhận đăng ký hội thảo thiếu đường dẫn truy cập",
                "Em nhận thư xác nhận có mã đăng ký và giờ bắt đầu nhưng không có đường dẫn vào hội thảo trực tuyến. Em muốn xin lại liên kết chính thức và xác nhận tài khoản dùng để tham dự.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban tổ chức đã kiểm tra trạng thái đăng ký và gửi lại đường dẫn tham dự đến địa chỉ email đã xác nhận. Đường dẫn được kèm theo mã phiên để người tham gia mở đúng buổi hội thảo.",
                "Em đã nhận được liên kết và mở trang chờ hội thảo thành công.",
                "Đường dẫn truy cập đã được gửi lại. Em kiểm tra giờ mở phòng và đăng nhập bằng email đã dùng khi đăng ký.",
                5,
                "Gửi lại đúng thông tin tham dự."
        ));
        list.add(new TicketTemplate(
                "KHCN_NGHIENCUU",
                "Mẫu ghi nhận dữ liệu nghiên cứu không có trường mô tả thiết bị đo",
                "Nhóm em cần lưu lại model thiết bị và điều kiện đo trong hồ sơ dữ liệu để có thể tái lập thí nghiệm. Biểu mẫu hiện tại không có trường mô tả thiết bị nên em muốn đề xuất cách bổ sung thông tin chuẩn hóa.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KHCN_DETAI_HOSO",
                "Tệp dự toán kinh phí bị từ chối vì tên file có ký tự tiếng Việt",
                "Em tải bảng dự toán kinh phí với tên file tiếng Việt có dấu nhưng hệ thống báo tên tệp không hợp lệ. Em muốn biết quy tắc đặt tên tệp được chấp nhận để nộp đúng hạn mà không phải thay đổi nội dung tài liệu.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                7,
                5,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Bộ phận hỗ trợ đã xác nhận trường tên tệp đang từ chối một số ký tự đặc biệt. Em đổi tên tệp bằng chữ không dấu, chữ số và dấu gạch dưới rồi tải lại; nội dung bảng dự toán không cần chỉnh sửa.",
                "Em đã đổi tên tệp theo hướng dẫn và tải lên thành công.",
                "Tệp dự toán đã được tiếp nhận với tên hợp lệ. Em dùng quy tắc đặt tên được hướng dẫn cho các tài liệu tiếp theo để tránh lỗi tương tự.",
                5,
                "Chỉ rõ cách sửa tên tệp mà không làm đổi nội dung."
        ));
        list.add(new TicketTemplate(
                "KHCN_HOITHAO_DANGKY",
                "Không thể hủy đăng ký hội thảo sau khi lịch học thay đổi",
                "Lịch học của em vừa được điều chỉnh trùng với hội thảo đã đăng ký, nhưng trang sự kiện không còn nút hủy. Em muốn thông báo không tham dự để ban tổ chức cập nhật số lượng chỗ ngồi.",
                TicketStatus.CANCELLED,
                TicketPriority.NORMAL,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                true,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_BHYT",
                "Thẻ BHYT được gia hạn nhưng hệ thống trường vẫn ghi ngày hết hạn cũ",
                "Em vừa kiểm tra ứng dụng bảo hiểm và thấy thẻ đã được gia hạn, nhưng hồ sơ tại trường vẫn báo thẻ sắp hết hiệu lực. Em muốn cập nhật ngày mới để tránh sai lệch khi lập danh sách đối chiếu.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận y tế đang so sánh dữ liệu thẻ mới với bản ghi bảo hiểm được đồng bộ từ đợt trước. Em cung cấp thời hạn mới và mã thẻ qua kênh bảo mật để xác minh chính xác bản ghi cần cập nhật.",
                "Em đã gửi ảnh màn hình thời hạn mới và xác nhận mã thẻ.",
                "Thông tin thời hạn đã được chuyển đối chiếu với dữ liệu bảo hiểm. Em kiểm tra lại trạng thái sau khi cập nhật và báo nếu cổng trường vẫn hiển thị ngày cũ.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_SUCKHOE",
                "Phiếu khám định kỳ bị ghi nhầm khoa tiếp nhận",
                "Em đăng ký khám sức khỏe định kỳ nhưng phiếu xác nhận lại ghi khoa tiếp nhận khác lựa chọn ban đầu. Em muốn kiểm tra để đến đúng nơi và tránh phải đăng ký lại khi tới buổi khám.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                8,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận y tế đã đối chiếu mã đặt lịch và cập nhật đúng khoa tiếp nhận theo lựa chọn hợp lệ của em. Phiếu mới được cấp lại trong mục lịch khám.",
                "Em đã tải phiếu mới và thông tin khoa tiếp nhận đã đúng.",
                "Phiếu lịch khám đã được chỉnh. Em sử dụng bản mới nhất và kiểm tra ngày giờ hẹn trước khi đến.",
                5,
                "Điều chỉnh thông tin lịch khám rõ ràng."
        ));
        list.add(new TicketTemplate(
                "YTE_HOTRO",
                "Yêu cầu hỗ trợ tiếp cận bị chuyển nhầm sang bộ phận hành chính",
                "Em gửi yêu cầu cần hỗ trợ di chuyển khi đến khu khám, nhưng trạng thái lại chuyển sang một bộ phận không phụ trách. Em muốn nhờ chuyển đúng đầu mối mà không phải tạo lại nội dung từ đầu.",
                TicketStatus.ASSIGNED,
                TicketPriority.NORMAL,
                2,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_BHYT_HOSO",
                "Hồ sơ BHYT tải lên bị xoay ngang khiến mã thẻ khó đọc",
                "Ảnh thẻ BHYT của em bị tự xoay ngang trong bản xem trước dù trên điện thoại ảnh đang đứng đúng chiều. Em muốn thay bản xem trước hoặc tải lại mà không làm phát sinh hai bộ hồ sơ.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "YTE_SUCKHOE_TUVAN",
                "Lịch tư vấn sức khỏe đã xác nhận nhưng không có thông báo nhắc lịch",
                "Em đặt lịch trao đổi riêng và trang hiển thị đã xác nhận, nhưng không có thư nhắc trước giờ hẹn. Em muốn kiểm tra lịch vẫn còn hiệu lực và biết phải đến đâu khi tới buổi tư vấn.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                3,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã lịch hẹn và địa chỉ email nhận thông báo để kiểm tra hệ thống nhắc lịch. Em gửi mã lịch, không cung cấp nội dung khám hoặc thông tin sức khỏe chi tiết trong yêu cầu này.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_SUACHUA",
                "Ổ cắm trong phòng học bị lỏng và không giữ được phích cắm",
                "Ổ cắm gần bàn giảng viên bị lỏng, dây nguồn dễ tuột khi di chuyển nhẹ. Em muốn báo sửa trước khi sử dụng thiết bị trình chiếu ở buổi học tiếp theo.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                1,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận kỹ thuật đã ghi nhận vị trí và yêu cầu kiểm tra ổ cắm, nguồn điện cùng biện pháp bảo đảm an toàn trước khi tiếp tục sử dụng. Em không cắm thử nhiều lần vào ổ đang lỏng và báo vị trí chính xác để kỹ thuật tiếp cận.",
                "Em gửi mã phòng và vị trí ổ cắm phía bục giảng.",
                "Thiết bị được chuyển kiểm tra an toàn điện trước khi đưa vào sử dụng lại. Em dùng ổ cắm khác đã được xác nhận an toàn cho đến khi có thông báo sửa chữa hoàn tất.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_PHONGHOC",
                "Phòng học đổi địa điểm nhưng bảng chỉ dẫn ở tầng trệt chưa cập nhật",
                "Lịch học trên portal ghi phòng mới nhưng bảng chỉ dẫn tại sảnh vẫn ghi phòng cũ, khiến lớp em đi nhầm địa điểm. Em muốn báo cập nhật bảng chỉ dẫn trước ca học kế tiếp.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                5,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã xác nhận lịch phòng mới và gửi yêu cầu cập nhật bảng chỉ dẫn tại sảnh. Lớp và bộ phận trực tòa nhà được thông báo cùng mã phòng mới để đồng bộ thông tin.",
                "Em đã thấy bảng chỉ dẫn mới được dán đúng địa điểm.",
                "Thông tin phòng đã được thống nhất trên portal và bảng chỉ dẫn. Em theo dõi thông báo từ lớp trong trường hợp có điều chỉnh tiếp theo.",
                5,
                "Đồng bộ thông tin số phòng giữa các kênh."
        ));
        list.add(new TicketTemplate(
                "CSVC_THIETBI",
                "Bàn phím tại máy tính phòng thực hành bị liệt nhiều phím",
                "Bàn phím của máy số 24 trong phòng thực hành không nhận một số phím, ảnh hưởng đến bài thực hành nhập lệnh. Em muốn ghi nhận đúng thiết bị để đội kỹ thuật thay hoặc kiểm tra trước tiết học sau.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                1,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_SUACHUA_BAOHONG",
                "Bản lề cửa nhà vệ sinh tầng hai phát tiếng kêu lớn",
                "Cửa nhà vệ sinh tầng hai phát tiếng kêu lớn và khó đóng nhẹ nhàng. Em muốn báo bảo trì kiểm tra bản lề để tránh ảnh hưởng các lớp học gần đó.",
                TicketStatus.CANCELLED,
                TicketPriority.LOW,
                3,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CSVC_PHONGHOC_DANGKY",
                "Biểu mẫu đăng ký phòng nhóm không có lựa chọn sử dụng cuối tuần",
                "Nhóm em cần phòng cho buổi chuẩn bị báo cáo vào sáng Chủ nhật, nhưng biểu mẫu chỉ hiển thị ngày trong tuần. Em muốn biết có thể gửi yêu cầu đặt phòng cuối tuần bằng kênh nào.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_LICHTHI",
                "Phòng thi hiển thị trùng cho hai học phần cùng ca",
                "Hai học phần em đăng ký có cùng số phòng và ca thi trên lịch cá nhân, trong khi đây là hai môn khác nhau. Em muốn xác minh có lỗi gán phòng hoặc lịch thi chưa cập nhật bản mới nhất.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận khảo thí đang so sánh lịch thi cá nhân với danh sách phân phòng chính thức của hai học phần. Em gửi mã hai môn và ca thi để xác minh xung đột trước khi có thông báo điều chỉnh.",
                "Em đã gửi mã học phần và ảnh lịch thi thể hiện cùng phòng, cùng ca.",
                "Bộ phận đã đối chiếu hai lịch và chuyển xử lý bản ghi phân phòng không nhất quán. Em kiểm tra lịch cá nhân mới nhất trước ngày thi và không dựa vào ảnh chụp cũ.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_PHUCKHAO",
                "Đơn phúc khảo bị trả lại nhưng không có lý do trong thông báo",
                "Em nhận thông báo hồ sơ phúc khảo được trả về nhưng phần lý do hiển thị trống. Em không biết cần sửa nội dung đơn hay bổ sung minh chứng nào để nộp lại đúng hạn.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                4,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã đơn phúc khảo và học phần liên quan để kiểm tra lý do trả hồ sơ trong lịch sử xử lý. Em gửi mã đơn và ảnh thông báo trạng thái hiện tại để xác định thông tin đang thiếu.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_KHAOSAT",
                "Kết quả gửi khảo sát bị báo thành công nhưng không có trong lịch sử",
                "Em hoàn thành khảo sát học phần và nhận thông báo gửi thành công, nhưng khi mở lịch sử khảo sát thì không thấy bản ghi. Em muốn xác nhận câu trả lời đã được lưu trước khi khảo sát đóng.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                6,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu thời điểm gửi và trạng thái khảo sát, sau đó đồng bộ bản ghi phản hồi vào lịch sử. Em không cần gửi lại khảo sát nếu hệ thống đã ghi nhận kết quả hợp lệ.",
                "Em đã mở lịch sử và thấy khảo sát ở trạng thái đã hoàn thành.",
                "Bản ghi phản hồi đã được đồng bộ vào lịch sử. Em kiểm tra trạng thái hoàn thành và chỉ liên hệ lại nếu bản ghi biến mất hoặc bị đổi trạng thái.",
                5,
                "Xử lý dữ liệu khảo sát mà không làm trùng phản hồi."
        ));
        list.add(new TicketTemplate(
                "QLCL_LICHTHI_TRUNGLICH",
                "Yêu cầu đổi lịch thi do trùng lịch thi chứng chỉ ngoài trường",
                "Lịch thi cuối kỳ của em trùng buổi thi chứng chỉ đã đăng ký trước, nhưng em không rõ lịch chứng chỉ ngoài trường có thuộc trường hợp được xem xét điều chỉnh không. Em muốn được hướng dẫn quy trình và loại minh chứng cần nộp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "QLCL_PHUCKHAO_HOSO",
                "Tệp bài làm đính kèm đơn phúc khảo không mở được ở phía tiếp nhận",
                "Em tải bản scan bài làm cùng đơn phúc khảo và đã xem lại tệp trên máy bình thường, nhưng trạng thái tiếp nhận ghi tệp minh chứng không đọc được. Em muốn gửi lại đúng định dạng mà không làm mất ngày nộp đơn ban đầu.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đang kiểm tra định dạng, dung lượng và cách hệ thống lưu tệp bài làm. Em giữ mã đơn hiện tại và gửi lại bản PDF rõ trang theo hướng dẫn, không xóa đơn đã nộp trước khi được xác nhận.",
                "Em đã chuyển bản scan sang PDF tiêu chuẩn và gửi lại theo yêu cầu.",
                "Tệp minh chứng mới đã được gắn với hồ sơ đang xử lý. Em theo dõi mã đơn hiện tại để giữ đúng lịch sử tiếp nhận ban đầu.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_TUYENSINH",
                "Cổng dự tuyển hiển thị chương trình đào tạo sai đợt tuyển sinh",
                "Em chọn chương trình cao học trong đợt tuyển sinh hiện tại nhưng trang xác nhận lại hiển thị tên đợt của kỳ trước. Em muốn xác nhận hồ sơ sẽ được xét vào đúng đợt đã đăng ký.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOSO",
                "Thông tin người giới thiệu khoa học bị mất sau khi lưu hồ sơ",
                "Em đã nhập thông tin người giới thiệu và lưu hồ sơ dự tuyển, nhưng khi mở lại trang thì các trường này để trống. Em muốn kiểm tra liệu dữ liệu đã lưu hay cần khai báo lại trước khi gửi hồ sơ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tuyển sinh đang kiểm tra bản ghi hồ sơ và nhật ký lưu các trường người giới thiệu. Em gửi mã hồ sơ và thời điểm lưu lần gần nhất để đối chiếu; không cần tạo hồ sơ dự tuyển mới.",
                "Em đã cung cấp mã hồ sơ và thời điểm em nhấn lưu.",
                "Bộ phận sẽ hướng dẫn nhập lại các trường nếu dữ liệu chưa được ghi nhận và xác nhận khi thông tin đã lưu. Em kiểm tra lại trang tóm tắt trước khi gửi hồ sơ cuối cùng.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_HOCVU",
                "Đăng ký học phần cao học bị chặn do trạng thái học vụ chưa đồng bộ",
                "Em đã hoàn tất các điều kiện học vụ của chương trình nhưng trang đăng ký học phần vẫn báo chưa đủ điều kiện. Em muốn kiểm tra trạng thái của khóa học và thời điểm dữ liệu được đồng bộ sang cổng đăng ký.",
                TicketStatus.WAITING_STUDENT,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận cần mã học viên, chương trình và học kỳ dự kiến đăng ký để kiểm tra trạng thái học vụ cùng điều kiện mở đăng ký. Em gửi ảnh thông báo hệ thống và danh sách điều kiện đã hoàn tất.",
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "SDH_TUYENSINH_HOSO",
                "Bằng tốt nghiệp đính kèm bị thiếu trang có thông tin xếp loại",
                "Em đã tải bản scan bằng tốt nghiệp nhưng sau khi xem lại, tệp chỉ có mặt trước và thiếu trang thể hiện xếp loại hoặc phụ lục liên quan. Em muốn bổ sung đúng tài liệu vào hồ sơ hiện tại.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                7,
                3,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Bộ phận tuyển sinh đã mở lại mục bổ sung cho hồ sơ và hướng dẫn em tải bản scan đầy đủ cả hai mặt cùng phụ lục cần thiết. Bản tài liệu cũ vẫn được lưu trong lịch sử để đối chiếu phiên bản.",
                "Em đã tải lại đủ các trang và trang hồ sơ đã nhận tài liệu mới.",
                "Bộ hồ sơ đã được bổ sung bản scan đầy đủ. Em kiểm tra thứ tự trang và độ rõ thông tin trước khi gửi các tài liệu còn lại.",
                5,
                "Hướng dẫn đúng phần giấy tờ còn thiếu."
        ));
        list.add(new TicketTemplate(
                "SDH_HOSO_BOSUNG",
                "Yêu cầu bổ sung không cho phép thay tệp đã nộp nhầm phiên bản",
                "Em đã gửi nhầm bản kế hoạch nghiên cứu cũ trong mục bổ sung. Hệ thống không còn nút thay tệp dù hồ sơ vẫn trong thời hạn hoàn thiện. Em muốn cập nhật đúng bản cuối cùng mà không tạo hồ sơ khác.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP_SAI",
                "Yêu cầu đổi nhóm sau khi xác nhận đăng ký nhầm mã lớp",
                "Em đã xác nhận đăng ký đúng môn nhưng chọn nhầm nhóm lớp. Cổng đăng ký vẫn đang mở điều chỉnh và em muốn biết có cần gửi yêu cầu cho Phòng Đào tạo hay có thể tự đổi trực tiếp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP_SAI",
                "Không thể thay nhóm vì hệ thống báo trùng lịch không chính xác",
                "Em chọn nhóm lớp không trùng lịch theo thời khóa biểu, nhưng portal vẫn báo trùng với một học phần em đã rút trước đó. Em gửi thông tin để kiểm tra dữ liệu lịch học.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                4,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đang đối chiếu trạng thái học phần đã rút với dữ liệu kiểm tra trùng lịch. Em gửi mã học phần, nhóm lớp và ảnh cảnh báo ở cùng yêu cầu để tránh nhầm phiên đăng ký.",
                "Dạ em đã gửi mã môn và ảnh cảnh báo, học phần cũ đã có trạng thái đã rút.",
                "Bộ phận đang xử lý dữ liệu trùng lịch; em theo dõi yêu cầu hiện tại và không tạo thêm đăng ký trùng trong lúc kiểm tra.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP_SAI",
                "Đã lưu nhóm thay thế nhưng phiếu xác nhận vẫn giữ nhóm cũ",
                "Sau khi đổi nhóm lớp, lịch học hiển thị nhóm mới nhưng phiếu đăng ký tải về vẫn ghi nhóm cũ. Em cần xác nhận dữ liệu chính thức trước khi bắt đầu học.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                9,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đã kiểm tra giao dịch đăng ký và đồng bộ lại phiếu xác nhận với nhóm lớp đang có hiệu lực. Em tải lại phiếu mới từ trang đăng ký thay vì dùng bản đã tải trước đó.",
                "Dạ em đã tải lại và phiếu mới hiển thị đúng nhóm lớp.",
                "Phiếu xác nhận đã cập nhật theo dữ liệu đăng ký hiện tại. Em giữ bản mới để sử dụng khi cần đối chiếu.",
                5,
                "Đối chiếu đúng dữ liệu và hướng dẫn tải lại phiếu."
        ));
        list.add(new TicketTemplate(
                "DT_DKHP_HUY",
                "Muốn rút học phần tự chọn trước hạn điều chỉnh",
                "Em dự định rút một học phần tự chọn vì lịch làm thêm bị thay đổi. Em muốn xác nhận thao tác này có thể tự thực hiện trên portal trong đợt điều chỉnh hiện tại không.",
                TicketStatus.NEW,
                TicketPriority.LOW,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP_HUY",
                "Đã gửi đề nghị rút môn nhưng chưa thấy trạng thái tiếp nhận",
                "Em gửi yêu cầu rút học phần kèm lý do từ hôm qua, nhưng trang cá nhân chưa hiện mã hồ sơ hoặc trạng thái tiếp nhận. Em muốn biết yêu cầu đã được ghi nhận chưa.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đang kiểm tra nhật ký tiếp nhận theo thời điểm em gửi. Em cung cấp tên học phần, mã lớp và thời gian thao tác gần đúng để đối chiếu chính xác.",
                "Dạ em gửi yêu cầu khoảng 15 giờ hôm qua, học phần là Mạng máy tính nâng cao.",
                "Yêu cầu đã được tìm thấy trong nhật ký tiếp nhận và đang chờ xử lý theo quy trình. Em theo dõi cùng mã yêu cầu này để tránh tạo hồ sơ trùng.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_DKHP_HUY",
                "Cần xác nhận học phần đã rút không còn tính vào tải học kỳ",
                "Em đã rút một học phần trong thời gian cho phép nhưng trang tóm tắt tải học kỳ vẫn tính số tín chỉ cũ. Em cần kiểm tra để biết tải học tập chính thức hiện tại.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đã đối chiếu danh sách đăng ký và cập nhật trạng thái học phần đã rút. Em tải mới bản tóm tắt tải học kỳ để xem số tín chỉ hiện hành.",
                "Dạ em kiểm tra lại thì tổng tín chỉ đã giảm đúng theo học phần rút.",
                "Trạng thái rút học phần và tải học kỳ đã đồng bộ. Nếu bảng tổng hợp kỳ sau vẫn sai, em gửi ảnh và mã môn để kiểm tra tiếp.",
                5,
                "Giải thích rõ cách xác minh trạng thái sau khi rút môn."
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU_DIEM",
                "Điểm thi trên portal khác điểm giảng viên công bố trên lớp",
                "Giảng viên thông báo điểm cuối kỳ của em là 7,5 nhưng portal đang hiển thị 6,5. Em muốn biết cách đề nghị đối chiếu điểm mà không gửi thiếu thông tin.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU_DIEM",
                "Điểm thành phần bị ghi nhầm sang mã sinh viên khác",
                "Em xem bảng điểm thấy điểm thuyết trình của mình không khớp kết quả đã nhận, trong khi một mục khác có vẻ bị gán nhầm. Em muốn bộ phận học vụ hướng dẫn đối chiếu dữ liệu gốc.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                4,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận học vụ đã ghi nhận phản ánh và cần mã lớp học phần, tên thành phần điểm cùng bằng chứng công bố của giảng viên. Em gửi thông tin liên quan của chính mình, không cần cung cấp dữ liệu của sinh viên khác.",
                "Dạ em gửi mã lớp và ảnh phần điểm thuyết trình được công bố trên hệ thống lớp học.",
                "Yêu cầu đang được đối chiếu với bảng điểm gốc và danh sách nhập điểm. Em theo dõi phản hồi trong luồng hiện tại.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU_DIEM",
                "Điểm tích lũy cập nhật sau khi có kết quả học lại",
                "Sau khi kết quả học lại được công bố, điểm tích lũy trên portal vẫn giữ số liệu trước đó. Em muốn xác nhận thời điểm đồng bộ và quy tắc ghi nhận lần học lại.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đã kiểm tra kết quả học lại đã được ghi nhận và làm mới dữ liệu tổng hợp điểm tích lũy. Cách tính được áp dụng theo quy chế cho khóa của em.",
                "Dạ em đăng nhập lại và đã thấy điểm tích lũy thay đổi đúng.",
                "Kết quả học lại đã phản ánh trong bảng điểm tích lũy. Em kiểm tra lại bảng điểm chính thức trước khi dùng cho hồ sơ khác.",
                4,
                "Có phản hồi và hướng dẫn kiểm tra bảng điểm sau cập nhật."
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU_BAOLUU",
                "Tìm hiểu điều kiện bảo lưu khi đã hoàn thành một phần học kỳ",
                "Em đang cân nhắc tạm dừng học vì lý do cá nhân và đã hoàn thành một số học phần trong kỳ. Em muốn biết đơn vị nào hướng dẫn điều kiện và thời điểm bắt đầu bảo lưu.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU_BAOLUU",
                "Trạng thái bảo lưu đã duyệt nhưng portal vẫn báo đang học",
                "Em đã nhận thông báo duyệt bảo lưu nhưng portal vẫn cho đăng ký học phần kỳ tiếp theo và hiển thị trạng thái đang học. Em muốn tránh thao tác ảnh hưởng hồ sơ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                5,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đang đối chiếu quyết định bảo lưu với trạng thái tài khoản đào tạo. Em gửi mã hồ sơ hoặc ngày ban hành quyết định để kiểm tra đúng bản ghi.",
                "Dạ em gửi mã quyết định và ảnh trạng thái portal hiện tại.",
                "Bộ phận đã tiếp nhận căn cứ bảo lưu để cập nhật trạng thái. Em tạm thời không xác nhận đăng ký học phần mới cho đến khi thông tin được kiểm tra xong.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_HOCVU_BAOLUU",
                "Xác nhận thời điểm được đăng ký học trở lại sau bảo lưu",
                "Em đã bảo lưu trong thời gian trước và dự kiến quay lại học kỳ tới. Em muốn biết cần làm thủ tục tiếp nhận lại vào thời điểm nào để không bỏ lỡ đợt đăng ký môn.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                14,
                5,
                false,
                null,
                false,
                SlaStatus.AT_RISK,
                false,
                "Phòng Đào tạo đã đối chiếu thời hạn trong quyết định bảo lưu và hướng dẫn em gửi đề nghị tiếp nhận trở lại theo lịch thông báo của học kỳ tới.",
                "Dạ em đã nắm được mốc cần liên hệ và sẽ gửi hồ sơ trước đợt đăng ký.",
                "Em thực hiện theo mốc tiếp nhận của quyết định và thông báo học kỳ. Sau khi trạng thái được cập nhật, hãy kiểm tra lại quyền đăng ký môn.",
                5,
                "Hướng dẫn đúng trình tự quay lại học."
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP_HOSO",
                "Hồ sơ tốt nghiệp thiếu xác nhận hoàn thành chương trình",
                "Em đang rà soát hồ sơ xét tốt nghiệp và thấy mục xác nhận hoàn thành chương trình chưa được đánh dấu. Em muốn biết cần bổ sung tài liệu nào và nộp ở đâu.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP_HOSO",
                "Đã nộp minh chứng chuẩn đầu ra nhưng hồ sơ vẫn báo thiếu",
                "Em đã tải chứng chỉ ngoại ngữ và nhận thông báo gửi thành công, nhưng danh sách điều kiện tốt nghiệp vẫn báo chưa đủ. Em muốn kiểm tra tệp đã gắn vào đúng mục chưa.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                5,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đang rà soát mục minh chứng và trạng thái kiểm tra chứng chỉ. Em gửi mã hồ sơ, tên chứng chỉ và ngày tải tệp để đối chiếu đúng bản ghi.",
                "Dạ em gửi mã hồ sơ và chứng chỉ em tải ngày đầu tuần.",
                "Bộ phận sẽ cập nhật trạng thái sau khi kiểm tra tính hợp lệ của minh chứng. Em giữ nguyên hồ sơ hiện tại và theo dõi yêu cầu bổ sung nếu có.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "DT_TOTNGHIEP_HOSO",
                "Sai thông tin địa chỉ nhận văn bằng trong hồ sơ tốt nghiệp",
                "Em phát hiện địa chỉ liên hệ trong hồ sơ tốt nghiệp đã cũ và muốn điều chỉnh trước khi hoàn tất thủ tục nhận bằng. Em cần xác nhận thông tin nào được phép cập nhật.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Phòng Đào tạo đã mở mục điều chỉnh thông tin liên hệ trong hồ sơ và hướng dẫn em cập nhật địa chỉ đang sử dụng. Việc thay đổi không làm thay đổi thông tin học vụ trên văn bằng.",
                "Dạ em đã cập nhật và xác nhận lại địa chỉ mới trong bản tóm tắt.",
                "Thông tin liên hệ đã được cập nhật. Em kiểm tra lại bản tóm tắt hồ sơ trước khi hoàn tất các bước nhận văn bằng.",
                5,
                "Hướng dẫn rõ phạm vi được điều chỉnh."
        ));
        list.add(new TicketTemplate(
                "CT_XACNHAN",
                "Xin xác nhận đang học để hoàn thiện hồ sơ thực tập doanh nghiệp",
                "Doanh nghiệp tiếp nhận thực tập yêu cầu giấy xác nhận em đang là sinh viên. Em muốn biết cần ghi rõ tên đơn vị thực tập và ngày dự kiến nhận văn bản không.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_XACNHAN",
                "Giấy xác nhận đã được cấp nhưng không tải được bản điện tử",
                "Em thấy yêu cầu xác nhận chuyển sang đã hoàn tất nhưng nút tải tệp bị lỗi và không lưu được giấy xác nhận. Em muốn nhận lại bản điện tử gắn với hồ sơ hiện có.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận Công tác sinh viên đang kiểm tra liên kết tệp và quyền truy cập của hồ sơ đã cấp. Em gửi mã yêu cầu và trình duyệt đang sử dụng để xác định lỗi tải.",
                "Dạ em thử trên hai trình duyệt đều không mở được, mã yêu cầu em đã gửi kèm.",
                "Yêu cầu đã được chuyển kiểm tra tệp đính kèm. Em dùng mã hồ sơ hiện tại để nhận bản xác nhận, không cần gửi lại đơn mới.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_XACNHAN",
                "Xác nhận thông tin lớp và ngành bị sai trên văn bản đã cấp",
                "Giấy xác nhận sinh viên em nhận được ghi sai lớp chuyên ngành so với portal. Em muốn đề nghị kiểm tra lại dữ liệu trước khi nộp cho bên tiếp nhận.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu hồ sơ sinh viên và phát hiện thông tin lớp trên văn bản chưa cập nhật phiên bản mới nhất. Bản xác nhận đã được điều chỉnh theo dữ liệu hiện hành.",
                "Dạ em đã nhận bản thay thế và thông tin lớp đã đúng.",
                "Văn bản thay thế đã được cấp theo hồ sơ đã đối chiếu. Em sử dụng bản mới và không chỉnh sửa bản PDF đã phát hành.",
                5,
                "Xác minh và cấp lại văn bản chính xác."
        ));
        list.add(new TicketTemplate(
                "CT_HOTRO",
                "Muốn được hướng dẫn dịch vụ hỗ trợ sinh viên ngoài giờ hành chính",
                "Em có thắc mắc về một chương trình hỗ trợ nhưng chỉ có thể liên hệ sau giờ học buổi tối. Em muốn biết kênh tiếp nhận nào cho phép gửi thông tin ngoài giờ.",
                TicketStatus.NEW,
                TicketPriority.LOW,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOTRO",
                "Yêu cầu hỗ trợ bị chuyển qua nhiều đơn vị mà chưa rõ đầu mối",
                "Em gửi đề nghị hỗ trợ về một vấn đề liên quan đến sinh hoạt và học tập, nhưng nhận phản hồi chuyển đơn vị nhiều lần. Em muốn biết bộ phận nào chịu trách nhiệm cập nhật kết quả cuối cùng.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận Công tác sinh viên đang rà soát nội dung và xác định đầu mối chính để theo dõi xuyên suốt. Em cho biết mã yêu cầu đã được chuyển và các phản hồi gần nhất để tổng hợp đúng lịch sử.",
                "Dạ em gửi mã yêu cầu cùng hai thông báo chuyển bộ phận em nhận được.",
                "Yêu cầu được giữ trong một luồng theo dõi và bộ phận phụ trách sẽ cập nhật đầu mối xử lý. Em tiếp tục phản hồi tại mã hiện tại để tránh phân tán lịch sử.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOTRO",
                "Cần biết kết quả xét hỗ trợ sau khi hồ sơ chuyển sang đang xử lý",
                "Em đã nộp hồ sơ hỗ trợ và trạng thái chuyển sang đang xử lý khá lâu. Em muốn biết cách kiểm tra mốc phản hồi dự kiến mà không gửi lại hồ sơ nhiều lần.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra hồ sơ và xác nhận trạng thái xử lý đang được cập nhật theo đợt xét công bố. Em có thể dùng mã hồ sơ để tra cứu và sẽ nhận thông báo nếu cần bổ sung.",
                "Dạ em đã tra cứu lại và thấy thông báo về đợt xét.",
                "Hồ sơ có kênh theo dõi rõ ràng; em chỉ cần bổ sung khi hệ thống hoặc đơn vị phụ trách có yêu cầu cụ thể.",
                4,
                "Giải đáp cách theo dõi mà không tạo hồ sơ trùng."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG_HOSO",
                "Biểu mẫu học bổng không lưu phần thông tin thành tích",
                "Em điền thành tích học tập và hoạt động trong biểu mẫu học bổng nhưng khi lưu lại, phần thành tích bị trống. Em muốn giữ nội dung đã nhập và nộp hồ sơ đúng hạn.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG_HOSO",
                "Không tải được file minh chứng vì vượt giới hạn dung lượng",
                "Hồ sơ học bổng yêu cầu bản scan nhiều trang nhưng portal báo tệp vượt dung lượng cho phép. Em muốn biết định dạng hoặc cách chia tệp phù hợp để không thiếu giấy tờ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận phụ trách đang kiểm tra giới hạn tệp của biểu mẫu hiện hành. Em ưu tiên lưu PDF rõ nét, chỉ gộp các trang cùng một loại minh chứng và kiểm tra giới hạn dung lượng hiển thị trên cổng.",
                "Dạ em đã nén PDF nhưng vẫn giữ đủ chữ và trang tài liệu.",
                "Em có thể tải lại tệp theo hướng dẫn dung lượng trên biểu mẫu. Kiểm tra tệp sau khi tải để bảo đảm trang không bị thiếu hoặc mờ.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG_HOSO",
                "Thông tin tài khoản nhận học bổng không khớp tên sinh viên",
                "Em phát hiện tên chủ tài khoản nhận học bổng bị viết thiếu dấu so với hồ sơ sinh viên. Em muốn xác nhận cần cập nhật thông tin nào để tránh lỗi chi trả.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                9,
                1,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã ghi nhận yêu cầu hiệu chỉnh thông tin người thụ hưởng và hướng dẫn em gửi xác nhận tài khoản theo mẫu chính thức. Dữ liệu sẽ được đối chiếu trước khi chuyển bước chi trả.",
                "Dạ em đã nộp lại thông tin theo mẫu và nhận xác nhận bổ sung thành công.",
                "Thông tin người thụ hưởng đã được gửi để đối chiếu. Em kiểm tra thông báo kết quả và không gửi dữ liệu tài khoản đầy đủ qua kênh công khai.",
                5,
                "Xử lý đúng thông tin người thụ hưởng và nhắc bảo mật."
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG_KETQUA",
                "Không mở được thông báo kết quả xét học bổng trên portal",
                "Em nhận thông báo có kết quả xét học bổng nhưng đường dẫn mở trang trắng. Em muốn tra cứu kết quả chính thức và xác định hồ sơ của mình đã có kết luận chưa.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG_KETQUA",
                "Trạng thái hồ sơ đủ điều kiện nhưng kết quả vẫn đang chờ",
                "Hồ sơ học bổng của em đã đủ các mục tài liệu và không còn thông báo bổ sung, nhưng kết quả vẫn là chờ xét. Em muốn biết có cần thao tác xác nhận nào nữa không.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                6,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận phụ trách đang kiểm tra trạng thái phê duyệt của đợt xét. Hoàn tất tài liệu không đồng nghĩa kết quả đã được quyết định; em không cần nộp lại bộ hồ sơ nếu chưa có yêu cầu mới.",
                "Dạ em hiểu, em sẽ theo dõi mã hồ sơ hiện tại.",
                "Trạng thái đã được ghi nhận để đối chiếu với tiến độ xét của đợt. Kết quả cuối cùng sẽ được công bố qua kênh chính thức.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_HOCBONG_KETQUA",
                "Tên trên thông báo học bổng không khớp hồ sơ cá nhân",
                "Trong danh sách kết quả học bổng, tên em hiển thị sai một ký tự so với hồ sơ sinh viên. Em muốn yêu cầu điều chỉnh trước khi có quyết định chi trả.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                13,
                5,
                false,
                null,
                false,
                SlaStatus.BREACHED,
                false,
                "Bộ phận đã đối chiếu mã sinh viên và hồ sơ gốc, xác nhận lỗi hiển thị tên và chuyển cập nhật danh sách kết quả. Em dùng mã sinh viên để theo dõi vì tên có thể cần đồng bộ lại trên văn bản.",
                "Dạ em đã kiểm tra bản danh sách cập nhật và tên đã chính xác.",
                "Thông tin tên đã được điều chỉnh theo hồ sơ gốc. Em kiểm tra lại văn bản chính thức trước khi sử dụng cho thủ tục khác.",
                5,
                "Đối chiếu bằng mã sinh viên và sửa đúng thông tin."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN_DIEM",
                "Không xem được tổng điểm rèn luyện của học kỳ hiện tại",
                "Em đã tự đánh giá điểm rèn luyện nhưng mục tổng hợp chưa hiện kết quả. Em muốn biết đây là trạng thái bình thường trước khi lớp xét duyệt hay lỗi tài khoản.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN_DIEM",
                "Điểm rèn luyện chưa tính một tiêu chí đã được lớp xác nhận",
                "Lớp đã xác nhận tiêu chí tham gia hoạt động của em nhưng kết quả tổng hợp vẫn để trống phần đó. Em muốn kiểm tra bản ghi xác nhận đã được gửi sang hệ thống chưa.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đang đối chiếu bản tổng hợp với danh sách tiêu chí do lớp gửi. Em cung cấp học kỳ, tiêu chí và thông tin xác nhận tương ứng; không cần gửi danh sách điểm của các bạn khác.",
                "Dạ em gửi tên tiêu chí và xác nhận của lớp trưởng trong học kỳ này.",
                "Yêu cầu đã được chuyển rà soát dữ liệu tổng hợp của tiêu chí. Em theo dõi trạng thái trong mã yêu cầu hiện tại.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN_DIEM",
                "Điểm rèn luyện bị thay đổi sau khi tải lại trang",
                "Em đã lưu bản tự đánh giá và thấy tổng điểm thay đổi sau khi đăng xuất rồi đăng nhập lại. Em muốn kiểm tra bản nào được ghi nhận chính thức và có cần nhập lại không.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                4,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra lịch sử đánh giá và xác nhận điểm đang hiển thị là bản được lưu gần nhất theo trạng thái xử lý của học kỳ. Em xem phần chi tiết tiêu chí để biết mục nào đã được cập nhật.",
                "Dạ em mở chi tiết và thấy các tiêu chí khớp với kết quả lớp đã thông báo.",
                "Điểm rèn luyện đã hiển thị theo bản ghi hiện hành. Em lưu kết quả sau khi kiểm tra các tiêu chí thành phần.",
                4,
                "Hướng dẫn kiểm tra lịch sử và từng tiêu chí."
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN_MINHCHUNG",
                "Tệp minh chứng điểm rèn luyện bị xoay ngang sau khi tải",
                "Em tải ảnh minh chứng lên nhưng khi xem trước, trang bị xoay ngang và khó đọc. Em muốn thay tệp cho đúng chiều trong khi biểu mẫu vẫn cho chỉnh sửa.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                3,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN_MINHCHUNG",
                "Hệ thống từ chối minh chứng dù tệp vẫn mở bình thường",
                "Em gửi bản PDF xác nhận hoạt động, máy em mở được nhưng portal báo định dạng không hợp lệ. Em muốn kiểm tra định dạng tệp được cho phép và cách gửi lại đúng quy cách.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đang rà soát định dạng được chấp nhận trong biểu mẫu. Em xuất lại PDF chuẩn, dùng tên tệp không có ký tự đặc biệt và thử tải trong phiên đăng nhập mới; giữ bản gốc để đối chiếu.",
                "Dạ em đã xuất lại PDF và tải thành công vào đúng tiêu chí.",
                "Minh chứng đã được tiếp nhận trong biểu mẫu. Em mở xem trước lần cuối để xác nhận tài liệu đọc được và đủ trang.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_RENLUYEN_MINHCHUNG",
                "Minh chứng bị gắn nhầm vào tiêu chí hoạt động khác",
                "Em đã tải chứng nhận tham gia hội thảo nhưng lỡ gắn vào tiêu chí tình nguyện. Em muốn chuyển đúng mục mà không làm mất xác nhận đã nộp.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã hướng dẫn gỡ tệp khỏi tiêu chí nhập nhầm và tải lại vào mục hội thảo tương ứng trong cùng bản tự đánh giá.",
                "Dạ em đã chuyển sang đúng mục và kiểm tra tên hoạt động đã khớp.",
                "Minh chứng đã được gắn đúng tiêu chí. Em kiểm tra lại danh sách tài liệu trước khi gửi hoặc khóa bản đánh giá.",
                5,
                "Hướng dẫn chuyển minh chứng rõ ràng."
        ));
        list.add(new TicketTemplate(
                "CT_XACNHAN_GIAYTO",
                "Cần bản sao có xác nhận để nộp hồ sơ thi tuyển",
                "Đơn vị tuyển dụng yêu cầu bảng điểm có xác nhận của trường thay vì bản tải từ portal. Em muốn biết phải ghi cụ thể loại văn bản nào và số bản cần cấp.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_XACNHAN_GIAYTO",
                "Yêu cầu cấp lại giấy tờ bị trả vì thiếu trang đính kèm",
                "Em nhận phản hồi hồ sơ giấy tờ chưa đủ trang phụ lục nhưng trên portal chỉ có nút xem trạng thái, không có hướng dẫn bổ sung. Em muốn xác nhận cách hoàn thiện hồ sơ cũ.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đang kiểm tra mã hồ sơ và nội dung bị trả để xác định trang phụ lục cần bổ sung. Em gửi ảnh phản hồi hoặc mã yêu cầu, không cần tạo đề nghị cấp lại mới trước khi được hướng dẫn.",
                "Dạ em gửi thông báo bị trả hồ sơ và mã yêu cầu.",
                "Hồ sơ đang được rà soát phần tài liệu thiếu. Em bổ sung theo yêu cầu trong cùng mã hồ sơ để giữ lịch sử tiếp nhận.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "CT_XACNHAN_GIAYTO",
                "Bản xác nhận điện tử không hiển thị mã tra cứu",
                "Em tải văn bản điện tử đã cấp nhưng không nhìn thấy mã tra cứu hoặc thông tin xác minh mà nơi tiếp nhận yêu cầu. Em muốn xác nhận bản hiện tại có đầy đủ thành phần không.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra mẫu văn bản áp dụng và hướng dẫn em tải bản xác nhận mới nhất từ trang chi tiết yêu cầu. Nếu bên tiếp nhận yêu cầu xác thực riêng, em gửi đúng thông tin đó để được cấp hướng dẫn phù hợp.",
                "Dạ bản mới đã có thông tin xác minh ở cuối trang.",
                "Văn bản hiện có đủ thông tin theo mẫu được cấp. Em dùng bản mới tải từ portal và không chỉnh sửa nội dung tệp.",
                5,
                "Hướng dẫn xác minh văn bản đúng quy trình."
        ));
        list.add(new TicketTemplate(
                "TC_CHUNGTU",
                "Cần chứng từ xác nhận đã hoàn thành học phí toàn kỳ",
                "Em đã thanh toán các khoản học phí được yêu cầu và cần giấy xác nhận để hoàn thiện hồ sơ cá nhân. Em muốn biết có thể tải chứng từ tổng hợp hay phải xin xác nhận riêng.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_CHUNGTU",
                "Biên nhận không hiển thị mã tham chiếu để đối soát",
                "Em tải biên nhận từ portal nhưng không thấy mã tham chiếu như trong hướng dẫn của bộ phận tài chính. Em muốn xác nhận đây là lỗi hiển thị hay cần tải một loại chứng từ khác.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đang so sánh mẫu biên nhận với mã giao dịch đã được hạch toán. Em gửi mã sinh viên, kỳ học và thời điểm thanh toán để tìm đúng chứng từ.",
                "Dạ em cung cấp mã sinh viên và biên nhận hiện tại trong yêu cầu.",
                "Chứng từ đang được đối chiếu với giao dịch gốc. Em giữ lại cả biên nhận hiện có và mã giao dịch để sử dụng khi cần tra soát.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_CHUNGTU",
                "Xin điều chỉnh kỳ ghi trên chứng từ đã phát hành",
                "Chứng từ học phí em tải được ghi kỳ học không đúng với khoản đã thanh toán. Em muốn xác nhận thông tin giao dịch trước khi yêu cầu cấp lại bản chính xác.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã kiểm tra khoản thanh toán và phát hiện kỳ hiển thị trên chứng từ không trùng kỳ được phân bổ. Chứng từ đã được xử lý theo dữ liệu giao dịch đã đối chiếu.",
                "Dạ em tải lại chứng từ và kỳ học đã đúng.",
                "Chứng từ thay thế đã phản ánh đúng kỳ được xác minh. Em dùng bản cập nhật và không chỉnh tay bản trước.",
                5,
                "Đối soát rõ dữ liệu trước khi cấp lại."
        ));
        list.add(new TicketTemplate(
                "TC_THANHTOAN",
                "Không rõ thứ tự thanh toán khi có cả học phí và phí dịch vụ",
                "Portal hiển thị học phí học kỳ cùng một khoản dịch vụ khác, nhưng em chưa rõ có thể trả chung một giao dịch hay phải thanh toán riêng từng khoản.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_THANHTOAN",
                "Cổng thanh toán không hiển thị phương thức đã được thông báo",
                "Em mở trang thanh toán nhưng chỉ thấy một phương thức, khác với thông báo của trường có nêu thêm cổng thanh toán trực tuyến. Em muốn xác định có phải tài khoản em bị thiếu quyền hay không.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đang đối chiếu cấu hình phương thức thanh toán cho kỳ hiện tại. Em gửi ảnh trang thanh toán, thời gian truy cập và loại khoản phí để kiểm tra chính xác.",
                "Dạ em gửi thông tin trang thanh toán của học kỳ này.",
                "Yêu cầu đã được chuyển kiểm tra cấu hình phương thức theo kỳ. Em chỉ sử dụng phương thức hiển thị trên kênh chính thức và kiểm tra giao dịch trước khi trả lại.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_THANHTOAN",
                "Muốn kiểm tra khoản đã thanh toán trước khi trả phần còn lại",
                "Em đã đóng một phần học phí và muốn xác nhận khoản đó được ghi nhận trước khi thanh toán số dư còn lại. Portal đang hiển thị tổng công nợ nhưng chưa rõ từng khoản.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                9,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu lịch sử giao dịch và chi tiết công nợ. Khoản thanh toán trước đã được ghi nhận, phần số dư còn lại hiển thị riêng theo kỳ học.",
                "Dạ em đã thấy dòng thanh toán trước và số dư còn lại đã giảm.",
                "Dữ liệu công nợ đã được đối chiếu. Em kiểm tra số tiền và mã tham chiếu của phần còn lại trước khi tạo giao dịch tiếp theo.",
                5,
                "Làm rõ phần đã trả và phần còn lại."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI_DOISOAT",
                "Giao dịch bị treo lâu hơn thời gian xử lý được thông báo",
                "Em thanh toán qua cổng trực tuyến, ngân hàng đã báo thành công nhưng portal vẫn để giao dịch đang xử lý sau thời gian dự kiến. Em muốn tra soát mà không phát sinh khoản thanh toán thứ hai.",
                TicketStatus.NEW,
                TicketPriority.HIGH,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI_DOISOAT",
                "Một giao dịch bị ghi nhận thành hai dòng thanh toán",
                "Lịch sử học phí của em hiển thị hai dòng có cùng số tiền và thời điểm gần nhau, trong khi ngân hàng chỉ có một giao dịch. Em muốn tài chính kiểm tra có phát sinh ghi nhận trùng không.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                5,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đang so khớp mã giao dịch và các bút toán hạch toán. Em gửi biên nhận ngân hàng và ảnh hai dòng lịch sử đang hiển thị để phân biệt giao dịch thực với bản ghi trùng.",
                "Dạ em gửi cả biên nhận và ảnh lịch sử portal.",
                "Yêu cầu đang được đối chiếu trên mã giao dịch gốc. Em không thực hiện thanh toán bổ sung cho đến khi nhận được kết quả xác minh.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI_DOISOAT",
                "Khoản thanh toán đã được phân bổ lại sau khi tra soát",
                "Em từng báo khoản học phí bị gắn sai kỳ và muốn biết kết quả điều chỉnh đã hoàn tất chưa vì bảng công nợ mới đã thay đổi nhưng chưa có thông báo.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                13,
                6,
                false,
                null,
                false,
                SlaStatus.BREACHED,
                false,
                "Bộ phận tài chính đã hoàn tất đối chiếu và chuyển khoản thanh toán sang kỳ đúng theo hồ sơ. Bảng công nợ đã được cập nhật theo kết quả xử lý.",
                "Dạ em kiểm tra lại và công nợ kỳ cần thanh toán đã đúng.",
                "Kết quả phân bổ đã được cập nhật. Em lưu mã yêu cầu tra soát cùng biên nhận để tham chiếu khi cần.",
                5,
                "Đóng vòng xử lý và xác nhận kết quả đối soát."
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI_GIAHAN",
                "Xin gia hạn vì thời điểm nhận hỗ trợ tài chính bị lùi",
                "Em dự kiến nhận khoản hỗ trợ để thanh toán học phí nhưng lịch giải ngân bị thay đổi. Em muốn hỏi có thể nộp đề nghị gia hạn trước hạn và cần minh chứng nào.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI_GIAHAN",
                "Đơn gia hạn đã gửi nhưng không có thông báo tiếp nhận",
                "Em gửi đơn gia hạn học phí trên portal và nhận trang xác nhận nhưng email không có thông báo, còn lịch sử yêu cầu chưa hiện trạng thái. Em muốn kiểm tra đơn đã vào hệ thống chưa.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đang tra cứu nhật ký tiếp nhận theo tài khoản và thời điểm gửi. Em cung cấp kỳ học và khoảng thời gian nộp; không gửi lại đơn mới nếu trạng thái chưa được xác minh.",
                "Dạ em nộp đơn khoảng 10 giờ sáng hôm qua cho học kỳ hiện tại.",
                "Hồ sơ đã được tiếp nhận để kiểm tra trạng thái. Em theo dõi mã yêu cầu này và chờ thông báo chính thức về quyết định gia hạn.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_HOCPHI_GIAHAN",
                "Cần biết khoản nào vẫn phải nộp trong thời gian chờ xét gia hạn",
                "Em đã gửi đề nghị gia hạn cho một phần học phí nhưng portal còn một số khoản đến hạn. Em muốn xác định phạm vi khoản được đề nghị gia hạn để tránh hiểu nhầm toàn bộ công nợ đều được lùi hạn.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                1,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu nội dung đơn và hướng dẫn em phân biệt khoản được đề nghị với các khoản không thuộc phạm vi đơn. Thời hạn chỉ thay đổi khi có quyết định chấp thuận tương ứng.",
                "Dạ em đã kiểm tra lại các khoản và hiểu phần nào nằm trong đề nghị.",
                "Em căn cứ quyết định chính thức để xác định hạn mới; các khoản khác vẫn theo thông báo thu phí đang có hiệu lực.",
                4,
                "Làm rõ phạm vi gia hạn và tránh hiểu nhầm hạn thanh toán."
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM_HOSO",
                "Hồ sơ miễn giảm học phí thiếu xác nhận thời hạn còn hiệu lực",
                "Em nộp giấy xác nhận đối tượng nhưng tài liệu đã qua ngày ghi hiệu lực trong hướng dẫn của đợt hiện tại. Em muốn biết cần xin lại giấy nào trước khi hồ sơ bị từ chối.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM_HOSO",
                "Hệ thống báo thiếu tệp dù hồ sơ đã tải đủ giấy tờ",
                "Em đã tải giấy tờ theo danh sách hồ sơ miễn giảm nhưng trang kiểm tra vẫn báo còn thiếu một mục. Em muốn biết mục nào chưa được ghi nhận và cách tải lại mà không tạo đơn mới.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                5,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận tài chính đang kiểm tra trạng thái từng tài liệu theo mã hồ sơ. Em gửi ảnh danh sách tài liệu đã tải và tên mục đang báo thiếu để đối chiếu; giữ nguyên mã hồ sơ hiện tại.",
                "Dạ em gửi ảnh danh sách và tên mục bị báo thiếu là giấy xác nhận đối tượng.",
                "Hồ sơ được tiếp tục kiểm tra theo từng mục tài liệu. Em chỉ tải lại phần được xác định chưa được ghi nhận, tránh gửi nhiều bản giống nhau.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_MIENGIAM_HOSO",
                "Kết quả miễn giảm chưa phản ánh trên khoản học phí kỳ mới",
                "Em đã có thông báo chấp thuận miễn giảm nhưng số phải nộp trong portal chưa thay đổi. Em muốn xác nhận thời điểm áp dụng quyết định và cách tra cứu khoản đã được điều chỉnh.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã đối chiếu quyết định miễn giảm với khoản phải nộp của kỳ mới và cập nhật kết quả vào bảng công nợ. Em tải lại chi tiết học phí để xem dòng điều chỉnh được ghi nhận.",
                "Dạ số phải nộp đã cập nhật sau khi em đăng nhập lại.",
                "Kết quả miễn giảm đã được phản ánh trong công nợ hiện tại. Em lưu thông báo chấp thuận và kiểm tra phần số dư còn lại trước khi thanh toán.",
                5,
                "Đối chiếu quyết định và xác nhận số phải nộp mới."
        ));
        list.add(new TicketTemplate(
                "TC_THANHTOAN_LOI",
                "Cổng thanh toán báo phiên hết hạn sau khi xác nhận số tiền",
                "Em chọn khoản cần đóng và xác nhận số tiền, nhưng khi chuyển sang cổng thanh toán thì thông báo phiên hết hạn. Em muốn biết giao dịch có được tạo hay cần bắt đầu lại.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_THANHTOAN_LOI",
                "Trang quay về portal nhưng không có kết quả giao dịch",
                "Sau khi xác nhận thanh toán, trình duyệt quay về portal nhưng không có thông báo thành công hay thất bại. Em muốn kiểm tra trạng thái để tránh tạo lệnh trùng.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đang đối chiếu mã đơn hàng với lịch sử trả về từ cổng thanh toán. Em kiểm tra lịch sử ngân hàng trước, gửi thời điểm, số tiền và mã đơn nếu được hiển thị; không nhập lại lệnh khi kết quả chưa rõ.",
                "Dạ ngân hàng đang hiển thị giao dịch chờ xử lý, em chưa thực hiện thanh toán lại.",
                "Đang kiểm tra trạng thái cuối cùng từ cổng thanh toán. Em chờ kết quả tra soát và chỉ thực hiện lại khi giao dịch đầu tiên được xác nhận không thành công.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "TC_THANHTOAN_LOI",
                "Thông báo lỗi xác thực xuất hiện sau khi ngân hàng đã trừ tiền",
                "Em nhận thông báo lỗi sau bước xác thực, nhưng lịch sử ngân hàng ghi nhận tiền đã bị trừ. Em muốn xử lý đúng cách và cần cung cấp thông tin nào cho bộ phận tài chính.",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                9,
                6,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Bộ phận đã tra soát mã giao dịch với cổng thanh toán và xác nhận giao dịch được ghi nhận. Trạng thái công nợ đã được cập nhật; không cần thanh toán lại khoản này.",
                "Dạ em kiểm tra portal và đã thấy khoản thanh toán hoàn tất.",
                "Giao dịch đã được xác minh và công nợ cập nhật. Em lưu mã giao dịch, không chia sẻ OTP hoặc thông tin đăng nhập ngân hàng trong trao đổi tiếp theo.",
                5,
                "Tra soát giao dịch rõ ràng và tránh thanh toán trùng."
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY_HOSO",
                "Ảnh giấy tờ nội trú bị mờ khi xem trước trên hệ thống",
                "Em tải giấy tờ đăng ký ký túc xá nhưng phần xem trước bị mờ, không đọc rõ thông tin. Em muốn thay bản scan rõ hơn trong hồ sơ hiện tại.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY_HOSO",
                "Hồ sơ nội trú thiếu xác nhận sau khi đã tải giấy tờ",
                "Em đã điền biểu mẫu đăng ký và tải đủ giấy tờ theo danh sách, nhưng hồ sơ vẫn báo chưa hoàn tất. Em muốn xác định thao tác xác nhận cuối cùng có bị bỏ sót không.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đang đối chiếu trạng thái biểu mẫu và từng tệp đính kèm. Em gửi mã hồ sơ và ảnh trang tóm tắt; kiểm tra xem còn nút xác nhận cuối cùng trước khi thoát trang hay không.",
                "Dạ em gửi mã hồ sơ, trang tóm tắt đang báo thiếu bước xác nhận.",
                "Hồ sơ được tiếp tục kiểm tra theo trạng thái đã lưu. Em chờ xác nhận tiếp nhận chính thức trước khi đăng ký lại.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY_HOSO",
                "Thông tin người liên hệ khẩn cấp bị lưu sai trong hồ sơ nội trú",
                "Em xem lại hồ sơ ký túc xá và phát hiện số điện thoại liên hệ khẩn cấp bị nhập sai một chữ số. Em muốn cập nhật thông tin trước khi hồ sơ được duyệt.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                10,
                4,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đã mở mục thông tin liên hệ cho hồ sơ để em chỉnh sửa và xác nhận lại. Thông tin đã cập nhật được hiển thị trong bản tóm tắt mới nhất.",
                "Dạ em đã sửa số điện thoại và xem lại bản tóm tắt.",
                "Thông tin liên hệ khẩn cấp đã được cập nhật. Em kiểm tra kỹ số điện thoại và tên người liên hệ trước khi rời trang.",
                5,
                "Sửa đúng dữ liệu liên hệ quan trọng."
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY_GIAHAN",
                "Muốn gia hạn thời gian nội trú cho kỳ tiếp theo",
                "Em đang ở ký túc xá và muốn tiếp tục ở trong kỳ sau. Em muốn biết khi nào mở đợt gia hạn và cần xác nhận lại loại phòng hiện tại không.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                3,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY_GIAHAN",
                "Đã gửi yêu cầu gia hạn nhưng chưa có kết quả xếp chỗ",
                "Em gửi đăng ký gia hạn nội trú nhưng hồ sơ vẫn ở trạng thái chờ trong khi ngày kết thúc kỳ hiện tại gần đến. Em muốn biết có cần bổ sung thông tin phòng ở hay không.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                5,
                5,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đang đối chiếu danh sách gia hạn và tình trạng phòng theo đợt. Em gửi mã hồ sơ, khu nhà và loại phòng đăng ký để kiểm tra đúng bản ghi.",
                "Dạ em gửi mã hồ sơ và thông tin phòng hiện tại.",
                "Hồ sơ gia hạn đang được xử lý theo đợt đăng ký. Em theo dõi thông báo chính thức về kết quả và thời gian bàn giao phòng nếu có thay đổi.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DANGKY_GIAHAN",
                "Kết quả gia hạn đã duyệt nhưng thời hạn trên portal chưa đổi",
                "Em nhận thông báo được tiếp tục ở ký túc xá kỳ sau nhưng trang nội trú vẫn hiện ngày kết thúc cũ. Em muốn xác nhận hạn mới để chủ động sắp xếp sinh hoạt.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                11,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đã đối chiếu kết quả duyệt và cập nhật thời gian lưu trú trong hồ sơ. Em đăng nhập lại và kiểm tra mục thông tin hợp đồng hoặc thời hạn nội trú.",
                "Dạ ngày kết thúc đã được cập nhật đúng sau khi em tải mới trang.",
                "Thời hạn nội trú đã phản ánh kết quả gia hạn. Em lưu thông báo và liên hệ lại nếu ngày trên hợp đồng khác với portal.",
                5,
                "Xác nhận kết quả gia hạn và thông tin thời hạn."
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU_SUA",
                "Bàn học trong phòng bị lỏng chân, cần kiểm tra an toàn",
                "Bàn học trong phòng ký túc xá bị lỏng chân và rung khi đặt máy tính. Em muốn báo sửa chữa, đồng thời biết có cần chuyển đồ khỏi khu vực trước khi kỹ thuật đến không.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU_SUA",
                "Phiếu sửa chữa đã chuyển kỹ thuật nhưng chưa có lịch kiểm tra",
                "Em báo cửa tủ bị hỏng và phiếu đã chuyển bộ phận kỹ thuật, nhưng chưa có lịch kiểm tra. Em muốn biết thời gian dự kiến để sắp xếp có mặt tại phòng.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.NORMAL,
                4,
                4,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đang xác nhận lịch của đội bảo trì theo khu nhà. Em cho biết khung giờ có thể có mặt và giữ lối tiếp cận thiết bị thông thoáng để kỹ thuật kiểm tra.",
                "Dạ em có mặt sau 17 giờ các ngày trong tuần.",
                "Lịch kiểm tra sẽ được cập nhật vào phiếu sửa chữa khi đội bảo trì xác nhận. Em theo dõi yêu cầu hiện tại thay vì gửi phiếu mới.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU_SUA",
                "Vòi nước đã được sửa nhưng yêu cầu chưa chuyển hoàn tất",
                "Đội bảo trì đã sửa vòi nước trong phòng và em xác nhận không còn rò rỉ, nhưng phiếu vẫn ở trạng thái đang xử lý. Em muốn cập nhật để hệ thống phản ánh đúng kết quả.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                8,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đã ghi nhận phản hồi của em và cập nhật kết quả sửa chữa vào phiếu. Nếu tình trạng rò rỉ quay lại, em có thể phản hồi mã yêu cầu hiện tại để đối chiếu lịch sửa trước.",
                "Dạ vòi nước đã hoạt động bình thường sau khi sửa.",
                "Yêu cầu sửa chữa đã được hoàn tất theo xác nhận của sinh viên. Em liên hệ lại bằng mã phiếu nếu phát sinh lại cùng lỗi.",
                5,
                "Theo dõi kết quả sửa chữa đến khi sinh viên xác nhận."
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU_DIENNUOC",
                "Nước sinh hoạt tầng của em yếu bất thường vào buổi tối",
                "Nước ở khu vệ sinh chung tầng em ở chảy rất yếu vào buổi tối, gây bất tiện khi sinh hoạt. Em muốn báo kiểm tra hệ thống cấp nước và biết liệu có lịch bảo trì đã thông báo không.",
                TicketStatus.NEW,
                TicketPriority.NORMAL,
                2,
                2,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                null,
                null,
                null,
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU_DIENNUOC",
                "Công tơ điện khu phòng hiển thị số liệu không ổn định",
                "Số điện hiển thị trên bảng theo dõi thay đổi bất thường so với lần ghi gần nhất. Em muốn ban quản lý kiểm tra chỉ số trước khi chốt phí tháng này.",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                5,
                1,
                false,
                null,
                false,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đã tiếp nhận phản ánh và cần mã phòng, thời điểm chụp số điện cùng chỉ số hiện tại để đối chiếu lần ghi trước. Em không tự tháo hoặc can thiệp vào thiết bị đo.",
                "Dạ em gửi mã phòng, ảnh chỉ số và thời điểm chụp.",
                "Đội kỹ thuật đang đối chiếu dữ liệu công tơ và sẽ cập nhật khi có kết quả. Em chờ kết luận trước khi phản ánh khoản phí điện cụ thể.",
                null,
                null
        ));
        list.add(new TicketTemplate(
                "KTX_DICHVU_DIENNUOC",
                "Phí điện nước trên bảng thông báo khác số liệu sinh hoạt",
                "Em thấy khoản phí điện nước trong thông báo tháng này cao hơn số liệu em ghi hàng ngày. Em muốn biết cách đề nghị đối soát theo chỉ số đầu kỳ và cuối kỳ.",
                TicketStatus.RESOLVED,
                TicketPriority.NORMAL,
                12,
                4,
                false,
                null,
                true,
                SlaStatus.ON_TRACK,
                false,
                "Ban quản lý KTX đã đối chiếu chỉ số ghi đầu kỳ, cuối kỳ và khoản phân bổ theo phòng. Bảng giải thích đã được cập nhật để em so sánh số liệu và cách tính.",
                "Dạ em đã xem bảng đối chiếu và hiểu số liệu tháng này.",
                "Kết quả đối soát đã được cung cấp theo kỳ thu. Em lưu lại chỉ số tự ghi và thông báo phí để đối chiếu những kỳ tiếp theo.",
                4,
                "Cung cấp căn cứ đối chiếu số liệu rõ ràng."
        ));

        return list;
    }
}
