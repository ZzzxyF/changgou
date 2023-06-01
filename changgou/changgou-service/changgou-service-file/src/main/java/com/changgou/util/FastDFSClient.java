package com.changgou.util;

import com.changgou.entity.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import javax.sound.midi.Track;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSClient {

    //初始化读取配置信息
    static{
        try {
            String filePath=new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }
    //上传文件
    public static  String[] upload(FastDFSFile fastDFSFile) throws IOException, MyException {
        NameValuePair[] valuePair=new NameValuePair[1];
        valuePair[0]=new NameValuePair(fastDFSFile.getAuthor());
        /***
         * 文件上传后的返回值
         * uploadResults[0]:文件上传所存储的组名，例如:group1
         * uploadResults[1]:文件存储路径,例如：M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
         */
        String[] unploadResult=null;
        TrackerClient tracker=new TrackerClient();
        //获取trackerServer
        TrackerServer trackerServer= tracker.getConnection();
        //获取storageClient
        StorageClient storageClient=new StorageClient(trackerServer,null);
        //进行文件上传--->上传文件的内容，文件后缀，文件作者
         unploadResult= storageClient.upload_file(fastDFSFile.getContent(),fastDFSFile.getExt(),valuePair);
         return unploadResult;
    }

    //获取文件信息
     // @param groupName:组名
     //@param remoteFileName：文件存储完整名
    public static FileInfo getFileInfo(String groupName,String remoteFileName) throws IOException, MyException {
    TrackerClient trackerClient=new TrackerClient();
    //获取trackerServer
    TrackerServer trackerServer=trackerClient.getConnection();
    StorageClient storageClient=new StorageClient(trackerServer,null);
    return   storageClient.get_file_info(groupName,remoteFileName);
    }

    //文件下载
    public static InputStream downLoadFile(String groupName,String remoteFileName) throws IOException, MyException {
    StorageClient storageClient=getStorageClient();
    byte[]  fileBytes=storageClient.download_file(groupName,remoteFileName);
    return new ByteArrayInputStream(fileBytes);
    }

    //文件删除
    public static void deleteFile(String groupName,String remoteFileName) throws IOException, MyException {
        StorageClient storageClient=getStorageClient();
        int flag=storageClient.delete_file(groupName,remoteFileName);
    }

    //获取组信息
    public static StorageServer getStorages(String groupName) throws IOException {
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer=trackerClient.getConnection();
        return trackerClient.getStoreStorage(trackerServer,groupName);
    }

    //根据文件组名和文件存储路径获取storage的存储ip,端口信息
    public static ServerInfo[] getServerInfo(String groupName,String remoteFileName) throws IOException {
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer=trackerClient.getConnection();
       return   trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);
    }

    //获取tracker服务地址
    public static String getTrackerUrl(){
        try {
            //创建TrackerClient对象
         //   TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient获取TrackerServer对象
            TrackerServer trackerServer = getTrackerServer();
            //获取Tracker地址
            return "http://"+trackerServer.getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取trackerServer
    public static TrackerServer getTrackerServer() throws IOException {
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获取TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    //获取stroageClient
    public static StorageClient getStorageClient() throws IOException {
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer=trackerClient.getConnection();
        StorageClient storageClient=new StorageClient(trackerServer,null);
        return storageClient;
    }
}
