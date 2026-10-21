package vn.edu.hcmute.qaute.service.ticket;

public interface PublicStatsService {

    Double getMedianFirstResponseMinutes(int days);

    default Double getMedianFirstResponseMinutes() {
        return getMedianFirstResponseMinutes(30);
    }

    default Double medianFirstResponseMinutes() {
        return getMedianFirstResponseMinutes(30);
    }
}

