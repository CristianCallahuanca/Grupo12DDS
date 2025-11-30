package dinamico.service;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {

    public String upload(MultipartFile file);

}
