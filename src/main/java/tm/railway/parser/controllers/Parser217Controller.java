package tm.railway.parser.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.railway.parser.services.Parser217Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;


@Controller
@RequestMapping("service")
@RequiredArgsConstructor
public class Parser217Controller {

    private final Parser217Service parser217Service;

    @PostMapping("/parser-217")
    public @ResponseBody byte[] start(@RequestParam("files") MultipartFile[] files, HttpServletResponse response) throws IOException, ParseException {

        response.setHeader("Content-disposition", "attachment;filename=result.xls");
        response.setContentType("application/vnd.ms-excel");

        return parser217Service.filesToExcel(files);
    }

    @GetMapping("/parser-217")
    public String uploadFiles(Model model) {
        return "upload_files";
    }
}
