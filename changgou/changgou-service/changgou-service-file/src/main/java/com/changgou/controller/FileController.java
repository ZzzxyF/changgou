package com.changgou.controller;
import com.changgou.entity.FastDFSFile;
import com.changgou.util.FastDFSClient;
import org.csource.common.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin//解决跨域
public class FileController {

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file) throws IOException, MyException {
        //写上传文件的配置
        FastDFSFile fastDFSFile=new FastDFSFile();
        fastDFSFile.setContent(file.getBytes());
        fastDFSFile.setName(file.getOriginalFilename());
        fastDFSFile.setExt(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        String[] uploadResults=FastDFSClient.upload(fastDFSFile);
        for (String result:uploadResults){
            System.out.println(result);
        }
        return "上传成功";
    }
}
