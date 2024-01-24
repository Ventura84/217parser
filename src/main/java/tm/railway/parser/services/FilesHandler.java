package tm.railway.parser.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tm.railway.parser.vo.FileVO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilesHandler {

    public List<String[]> readFiles(MultipartFile[] files) throws IOException {

        List<FileVO> filesVos = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                filesVos.add(new FileVO(file));
            } catch (IOException e) {
                log.error("Couldn't create FileVO", e);
            }
        }

        List<String> fileContents = new ArrayList<>();
        Charset cp866 = Charset.forName("cp866");

        for (FileVO fileVO : filesVos) {

            byte[] fileContent = new byte[fileVO.getInputStream().available()];
            fileVO.getInputStream().read(fileContent);

            fileContents.add(new String (fileContent, cp866));
        }

        return fileContents.stream().map(this::splitFile).collect(Collectors.toList());
    }

    private String[] splitFile(String content) {
        return content.split("\n");
    }

    public List<String[]> prepareForDocument(List<String[]> readFiles) {

        List<String[]> prepared = new ArrayList<>();

        Pattern patternNv = Pattern.compile("^\\d{8}");

        String nv = "";
        String sobstv = "";
        String lastOp = "";
        boolean nvFound = false;
        boolean lastOpFound = false;


        for (String[] file : readFiles) {

            List<String> strings = Arrays.asList(file);
            Collections.reverse(strings);


            for (String str : strings) {

                Matcher matcherNv = patternNv.matcher(str);

                if (str.length() >= 69
                        && !str.startsWith("CTAHЦ OПEP")
                        && !str.startsWith("CTAHЦ  OПEP")
                        && !str.startsWith("Ю2")
                        && !lastOpFound) {

                    lastOp = str;
                    lastOpFound = true;

                }

                if (matcherNv.find()) {
                    nvFound = true;
                    nv = str.substring(0, 8);
                    sobstv = str.substring(17).trim();
                }


                if (lastOpFound && nvFound) {

                    String[] strForPrepared = new String[10];

                    strForPrepared[0] = nv;
                    strForPrepared[1] = sobstv; //kod sobstv
                    strForPrepared[2] = lastOp.substring(0, 5).trim(); //stan
                    strForPrepared[3] = lastOp.substring(5, 11).trim(); //oper
                    strForPrepared[4] = lastOp.substring(11, 14).trim(); //god
                    strForPrepared[5] = lastOp.substring(14, 20).trim(); //data
                    strForPrepared[6] = lastOp.substring(20, 26).trim(); //vrem
                    strForPrepared[7] = lastOp.substring(26, 31).trim(); //sost
                    strForPrepared[8] = lastOp.substring(31, 37).trim(); //nazn

                    if (lastOp.substring(6, 7).equals(" ")) {
                        strForPrepared[9] = lastOp.substring(47, 63).trim();
                    } else {
                        strForPrepared[9] = lastOp.substring(46, 62).trim();
                    }
                    //strForPrepared[9] = lastOp.substring(46, 63).trim(); //indx

                    prepared.add(strForPrepared);

                    nvFound = false;
                    lastOpFound = false;
                }

            }


        }

        Collections.reverse(prepared);

        return prepared;

    }
}
