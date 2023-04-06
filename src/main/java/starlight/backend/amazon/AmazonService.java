package starlight.backend.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class AmazonService {
    private String bucketName = "maksym-team-backend";

    @Autowired
    AmazonS3 s3;


    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {
            File file1 = convertMultiPartToFile(file);
            PutObjectResult objectResult = s3.putObject(bucketName, originalFilename, file1);
            return objectResult.getContentMd5();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] downloadFile(String filename) {
        S3Object object = s3.getObject(bucketName, filename);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String deleteFile(String filename) {
        s3.deleteObject(bucketName, filename);

        return "Deleting file completed";
    }


    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
