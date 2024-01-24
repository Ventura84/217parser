package tm.railway.parser.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFilesDTO {

    private MultipartFile[] files;
}
