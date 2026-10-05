package vn.edu.hcmute.qaute.common.constant;

public final class SettingKeys {

    public static final String OTP_TTL_MINUTES = "otp.ttl.minutes";
    public static final String OTP_MAX_ATTEMPTS = "otp.max.attempts";
    public static final String OTP_RESEND_COOLDOWN_SECONDS = "otp.resend.cooldown.seconds";
    public static final String OTP_MAX_SEND_PER_HOUR = "otp.max.send.per.hour";
    public static final String AUTH_ACCESS_TTL_MINUTES = "auth.access.ttl.minutes";
    public static final String AUTH_REFRESH_TTL_DAYS = "auth.refresh.ttl.days";
    public static final String AUTH_REFRESH_REMEMBER_TTL_DAYS = "auth.refresh.remember.ttl.days";
    public static final String AUTH_MAX_FAILED_LOGINS = "auth.max.failed.logins";
    public static final String AUTH_LOCK_MINUTES = "auth.lock.minutes";
    public static final String AUTH_UNVERIFIED_CLEANUP_HOURS = "auth.unverified.cleanup.hours";
    public static final String TICKET_URGENT_REQUEST_LIMIT = "ticket.urgent.request.limit";
    public static final String TICKET_AUTO_CLOSE_RESOLVED_DAYS = "ticket.auto.close.resolved.days";
    public static final String TICKET_AUTO_CLOSE_WAITING_DAYS = "ticket.auto.close.waiting.days";
    public static final String TICKET_REOPEN_WINDOW_DAYS = "ticket.reopen.window.days";
    public static final String TICKET_REOPEN_MAX = "ticket.reopen.max";
    public static final String TICKET_RATING_EDIT_DAYS = "ticket.rating.edit.days";
    public static final String TICKET_AUTO_ASSIGN_ENABLED = "ticket.auto.assign.enabled";
    public static final String SLA_AT_RISK_PERCENT = "sla.at.risk.percent";
    public static final String FILE_MAX_COUNT = "file.max.count";
    public static final String FILE_MAX_SIZE_MB = "file.max.size.mb";
    public static final String FILE_MAX_TOTAL_MB = "file.max.total.mb";
    public static final String CHAT_MAX_CONCURRENT = "chat.max.concurrent";
    public static final String CHAT_WAITING_TIMEOUT_MINUTES = "chat.waiting.timeout.minutes";
    public static final String CHAT_IDLE_TIMEOUT_MINUTES = "chat.idle.timeout.minutes";
    public static final String CHAT_MESSAGE_MAX_LENGTH = "chat.message.max.length";
    public static final String CHAT_USE_BUSINESS_HOURS = "chat.use.business.hours";
    public static final String APPOINTMENT_SLOT_MINUTES = "appointment.slot.minutes";
    public static final String APPOINTMENT_LEAD_HOURS = "appointment.lead.hours";
    public static final String APPOINTMENT_CANCEL_BEFORE_HOURS = "appointment.cancel.before.hours";
    public static final String APPOINTMENT_MAX_UPCOMING = "appointment.max.upcoming";
    public static final String APPOINTMENT_GENERATE_DAYS = "appointment.generate.days";
    public static final String FAQ_VIEW_DEDUPE_MINUTES = "faq.view.dedupe.minutes";
    public static final String PAGE_SIZE_DEFAULT = "page.size.default";
    public static final String PAGE_SIZE_MAX = "page.size.max";
    public static final String RETENTION_NOTIFICATION_DAYS = "retention.notification.days";
    public static final String RETENTION_SEARCH_LOGS_DAYS = "retention.search.logs.days";
    public static final String RETENTION_LOGIN_HISTORY_DAYS = "retention.login.history.days";
    public static final String RETENTION_AUDIT_DAYS = "retention.audit.days";
    public static final String CLOUDINARY_SIGNED_URL_TTL_MINUTES = "cloudinary.signed.url.ttl.minutes";
    public static final String SITE_CONTACT_EMAIL = "site.contact.email";
    public static final String SITE_CONTACT_PHONE = "site.contact.phone";
    public static final String SITE_CONTACT_ADDRESS = "site.contact.address";

    private SettingKeys() {
    }
}
