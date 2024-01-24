package tm.railway.parser.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class Parser217Service {

    private final FilesHandler filesHandler;
    private final ExcelFileCreator excelFileCreator;

    public byte[] filesToExcel(MultipartFile[] files) throws IOException, ParseException {

        List<String[]> readFiles = filesHandler.readFiles(files);
        List<String[]> prepared = filesHandler.prepareForDocument(readFiles);

        return excelFileCreator.makeExcelFile(prepared);
    }
}
