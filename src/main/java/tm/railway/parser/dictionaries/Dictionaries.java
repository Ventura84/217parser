package tm.railway.parser.dictionaries;


import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class Dictionaries {

    private List<String> stanCodesNames;
    private List<String> operNames;
    private List<String> arenda;

    public static List<String> readSpr(Resource resource) throws IOException {

        Charset cp866 = Charset.forName("cp866");

        InputStream stanCodesNames = resource.getInputStream();
        byte[] fileContent = new byte[stanCodesNames.available()];
        stanCodesNames.read(fileContent);
        String content = new String (fileContent, cp866);

        return Arrays.stream(content.split("\n")).collect(Collectors.toList());
    }
}
