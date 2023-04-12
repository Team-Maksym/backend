package starlight.backend.amazon;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user-profile")
public class AmazonController {
    AmazonService amazonService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return amazonService.saveFile(file);
    }

    @GetMapping("/download/{filename}")
    public byte[] download(@PathVariable String filename) {
        return amazonService.downloadFile(filename);
    }

    @DeleteMapping("/delete/{filename}")
    public String delete(@PathVariable String filename) {
        return amazonService.deleteFile(filename);
    }

    @GetMapping("/")
    public List<String> getAllFiles() {
        return amazonService.listAllFiles();
    }

}
