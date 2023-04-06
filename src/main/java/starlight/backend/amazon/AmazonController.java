package starlight.backend.amazon;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-profile")
@Tag(name = "Amazon", description = "Amazon API")
@Slf4j
public class AmazonController {
    AmazonService amazonService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        log.info("Uploading file {}", file.getOriginalFilename());
        return amazonService.saveFile(file);
    }

    @GetMapping("/download/{filename}")
    public byte[] download(@PathVariable String filename) {
        log.info("Downloading file {}", filename);
        return amazonService.downloadFile(filename);
    }

    @DeleteMapping("/delete/{filename}")
    public String delete(@PathVariable String filename) {
        log.info("Deleting file {}", filename);
        return amazonService.deleteFile(filename);
    }

    @GetMapping
    public List<String> getAllFiles() {
        log.info("Listing all files");
        return amazonService.listAllFiles();
    }
}
