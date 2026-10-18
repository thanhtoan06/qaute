package vn.edu.hcmute.qaute.service.media;

import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.MediaDeliveryType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.MediaStream;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;

@Service
public class MediaDownloadService {
    private final MediaAssetRepository assets;
    private final Cloudinary cloudinary;
    private final Map<MediaUsageType, MediaAccessPolicy> policies;
    private final HttpClient httpClient;

    public MediaDownloadService(MediaAssetRepository assets, Cloudinary cloudinary,
                                List<MediaAccessPolicy> policies) {
        this.assets = assets;
        this.cloudinary = cloudinary;
        this.policies = new EnumMap<>(MediaUsageType.class);
        policies.forEach(policy -> this.policies.put(policy.usage(), policy));
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    @Transactional(readOnly = true)
    public MediaStream open(Long assetId, Long userId, RoleCode role) {
        MediaAsset asset = assets.findById(assetId).filter(item -> item.getStatus() == MediaStatus.ACTIVE)
                .orElseThrow(this::notFound);
        if (asset.getDeliveryType() != MediaDeliveryType.UPLOAD) {
            MediaAccessPolicy policy = policies.get(asset.getUsageType());
            if (policy == null || !policy.canAccess(asset, userId, role)) throw notFound();
        }
        String url = cloudinary.url().resourceType(asset.getResourceType())
                .type(asset.getDeliveryType() == MediaDeliveryType.AUTHENTICATED ? "authenticated" : "upload")
                .signed(true).secure(true).generate(asset.getPublicId());
        try {
            HttpResponse<java.io.InputStream> response = httpClient.send(HttpRequest.newBuilder(URI.create(url))
                    .GET().timeout(Duration.ofSeconds(30)).build(), HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                response.body().close();
                throw notFound();
            }
            return new MediaStream(response.body(), asset.getMimeType(), asset.getSizeBytes(),
                    asset.getOriginalFilename(), inline(asset.getMimeType()));
        } catch (IOException | InterruptedException exception) {
            if (exception instanceof InterruptedException) Thread.currentThread().interrupt();
            throw notFound();
        }
    }

    private static boolean inline(String mimeType) {
        return mimeType != null && (mimeType.startsWith("image/") || "application/pdf".equals(mimeType));
    }

    private NotFoundException notFound() {
        return new NotFoundException("Không tìm thấy tệp");
    }
}
