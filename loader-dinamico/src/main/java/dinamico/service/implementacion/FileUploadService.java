package dinamico.service.implementacion;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dinamico.service.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class FileUploadService implements IFileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    @Override
    public String upload(MultipartFile file) {

        String extensions = null;

        if (file.getOriginalFilename() != null) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            extensions = splitName[splitName.length - 1];
        }

        try {
            Map<String, Object> resultUpload = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "library")
            );

            return resultUpload.get("secure_url").toString();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "";
        }
    }
}
