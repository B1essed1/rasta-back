package uz.rasta.media;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.rasta.config.ApiException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String upload(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf('.'));
        }
        String objectName = UUID.randomUUID() + ext;

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw ApiException.badRequest("media.upload.failed");
        }

        return getPublicUrl(objectName);
    }

    public void delete(String url) {
        String objectName = extractObjectName(url);
        if (objectName == null) return;
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw ApiException.badRequest("media.upload.failed");
        }
    }

    private String getPublicUrl(String objectName) {
        return minioConfig.getEndpoint() + "/" + minioConfig.getBucket() + "/" + objectName;
    }

    private String extractObjectName(String url) {
        String prefix = minioConfig.getEndpoint() + "/" + minioConfig.getBucket() + "/";
        if (url != null && url.startsWith(prefix)) {
            return url.substring(prefix.length());
        }
        return null;
    }
}
