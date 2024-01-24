package tm.railway.parser.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Data
public class FileVO {

    private final InputStream inputStream;
    private final String originalFilename;
    private final String extension;
    private final long size;
    private final String contentType;

    public FileVO(MultipartFile file) throws IOException {
        inputStream = file.getInputStream();
        originalFilename = file.getOriginalFilename();
        extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        size = file.getSize();
        contentType = file.getContentType();
    }

}
