package vn.uef.g2.foodlocation.utility;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UploadFile {
    public static String uploadFile(MultipartFile file) throws FileUploadException {
        try {
            if(file.isEmpty()) {
                throw new Exception("Failed to store empty file");
            }

            // Tạo thư mục nếu nó không tồn tại
            String uploadDir = "uploads/"+ LocalDateTime.now().getYear()+"/"+LocalDateTime.now().getMonthValue()+"/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Tạo tên file : ngày giờ + tên file
            String filename = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)+"_"+file.getOriginalFilename();

            // Tạo đường dẫn file
            Path filePath = uploadPath.resolve(filename);

            // Sao chép file vào thư mục
            Files.copy(
                    file.getInputStream(),
                    filePath);
            // Trả về đường dẫn file
            return uploadDir + filename;
        }
        catch (Exception e) {
            throw new FileUploadException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!", e);
        }
    }

    public static void deleteFile(String avatar) {
        try {
            // Lấy đường dẫn file
            Path path = Paths.get(avatar);
            // Xóa file
            Files.delete(path);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete file " + avatar
                    + ". Please try again!", e);
        }
    }
}
