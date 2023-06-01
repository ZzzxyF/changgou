package com.changgou.entity;


import lombok.Data;
import lombok.ToString;

//文件上传实体类（封装）
@Data
@ToString
public class FastDFSFile {
    //文件名称
    private String name;
    //文件内容
    private byte[] content;
    //文件扩展名---》文件后缀
    private String ext;
    //文件md5摘要值
    private String md5;
    //文件创建作者
    private String author;

    public FastDFSFile(String name, byte[] content, String ext, String md5, String author) {
        this.name = name;
        this.content = content;
        this.ext = ext;
        this.md5 = md5;
        this.author = author;
    }

    public FastDFSFile(String name, String ext) {
        this.name = name;
        this.ext = ext;
    }

    public FastDFSFile() {
    }
}
