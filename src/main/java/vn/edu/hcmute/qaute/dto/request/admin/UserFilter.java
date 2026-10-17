package vn.edu.hcmute.qaute.dto.request.admin;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;

/** Query parameters accepted by the administrator user directory. */
@Getter
@Setter
public class UserFilter {

    private String q;
    private RoleCode role;
    private UserStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private String sort;
}
