package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.util.CsvUtil;
import vn.edu.hcmute.qaute.dto.request.admin.UserCreateForm;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@ExtendWith(MockitoExtension.class)
class UserImportServiceTest {
    private static ValidatorFactory validatorFactory;
    private static final String HEADER = "mssv,fullName,email,facultyCode,classCode,cohort\r\n";

    @Mock AdminUserService adminUsers;
    @Mock UserRepository users;
    @Mock FacultyRepository faculties;
    @Mock AuditService audit;
    @Mock PlatformTransactionManager transactionManager;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void dryRunReportsDuplicateAndDoesNotCreateAccounts() {
        var service = service();
        var file = csv(HEADER
                + "12345678,Nguyễn Văn A,ALICE@example.edu.vn,,20DTH1,2024\r\n"
                + "12345678,Nguyễn Văn B,alice@example.edu.vn,,20DTH2,2024\r\n");

        var report = service.process(file, true, 1L);

        assertEquals(2, report.total());
        assertEquals(1, report.ok());
        assertEquals(1, report.failed());
        assertTrue(report.rows().get(1).message().contains("trùng trong tệp"));
        assertEquals("alice@example.edu.vn", report.rows().get(0).email());
        verify(adminUsers, never()).create(any(), any());
        verify(audit, never()).log(any(), any(), any(), any(), any());
    }

    @Test
    void realImportContinuesAfterOneRowFails() {
        var service = service();
        when(transactionManager.getTransaction(any())).thenReturn(new SimpleTransactionStatus());
        when(adminUsers.create(any(UserCreateForm.class), eq(1L)))
                .thenThrow(new DuplicateException("email", "Email đã tồn tại"))
                .thenReturn(new User());
        var file = csv(HEADER
                + "12345678,Nguyễn Văn A,a@example.edu.vn,,,\r\n"
                + "87654321,Nguyễn Văn B,b@example.edu.vn,,,\r\n");

        var report = service.process(file, false, 1L);

        assertEquals(1, report.ok());
        assertEquals(1, report.failed());
        assertFalse(report.rows().get(0).ok());
        assertTrue(report.rows().get(1).ok());
        verify(audit).log(eq("USER_IMPORTED"), eq("User"), eq(null), eq(null),
                eq("total=2,ok=1,failed=1"));
    }

    @Test
    void rejectsWrongHeader() {
        var service = service();
        assertThrows(BadRequestException.class,
                () -> service.process(csv("email,mssv\r\na@example.edu.vn,12345678\r\n"), true, 1L));
    }

    private UserImportService service() {
        return new UserImportService(adminUsers, users, faculties,
                validatorFactory.getValidator(), audit, transactionManager);
    }

    private static MockMultipartFile csv(String text) {
        return new MockMultipartFile("file", "students.csv", "text/csv",
                ("\uFEFF" + text).getBytes(StandardCharsets.UTF_8));
    }
}
