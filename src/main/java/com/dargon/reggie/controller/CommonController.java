package com.dargon.reggie.controller;

import com.dargon.reggie.common.R;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;


@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String path;
    /*
    * 文件上传
    *
    */
    @SneakyThrows
    @PostMapping("/upload")
    public R<String> upload(@RequestBody MultipartFile file){

        //原始文件名
        String originalFilename = file.getOriginalFilename();

        originalFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
        //uuid生成文件名
        String fileName = UUID.randomUUID().toString()+originalFilename;

        File f = new File(path);

        if (!f.exists()){
            f.mkdirs();
        }

        file.transferTo(new File(path+fileName));

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
            response.setContentType("image/jpeg");

        try {
            InputStream inputStream = new FileInputStream(new File(path+name));

            ServletOutputStream outputStream = response.getOutputStream();

            int length = 0;
            byte b1[] = new byte[1024];

            while ((length=inputStream.read(b1))!=-1){
                 outputStream.write(b1,0,length);
                 outputStream.flush();
            }
           inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
