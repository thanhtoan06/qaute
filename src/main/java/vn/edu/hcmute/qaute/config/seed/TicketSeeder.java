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

// Khởi tạo dữ liệu mẫu cho TicketSeeder trong môi trường demo.
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

    // Khởi tạo seeder phiếu hỗ trợ với các kho dữ liệu cần thiết.
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

    // Xác định thứ tự chạy của seeder phiếu hỗ trợ.
    @Override
    public int order() {
        return 40;
    }

    // Tạo phiếu hỗ trợ demo cùng lịch sử và phản hồi liên quan.
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


    // Chọn quản lý phụ trách chuyên mục phiếu hỗ trợ.
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



    // Khai báo các mẫu phiếu hỗ trợ dùng cho dữ liệu demo.
    private List<TicketTemplate> buildTemplates() {
        List<TicketTemplate> list = new ArrayList<>();

        // 45 kịch bản gốc còn phù hợp, giữ nguyên nội dung từng tình huống.
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

        // Các kịch bản mở rộng được khai báo riêng, đủ các chuyên mục của hệ thống mới.
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

        return list;
    }
}
