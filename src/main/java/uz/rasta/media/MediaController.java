package uz.rasta.media;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.rasta.config.ApiException;
import uz.rasta.config.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw ApiException.badRequest("media.file.empty");
        }

        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw ApiException.badRequest("media.file.too.large");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw ApiException.badRequest("media.file.not.image");
        }

        String url = mediaService.upload(file);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("url", url)));
    }
}
