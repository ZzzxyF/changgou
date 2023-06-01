import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import java.io.IOException;

public class FastDFSTest {

    //上传
    @Test
    public void upload() throws IOException, MyException {
        ClientGlobal.init("D:\\ideal_soft\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建trackerClient
        TrackerClient tracker=new TrackerClient();
        //获取trackerServer
        TrackerServer trackerServer= tracker.getConnection();
        //获取storageClient
        StorageClient storageClient=new StorageClient(trackerServer,null);
       String[] jpgs= storageClient.upload_file("D:\\图片\\01.jpg","jpg",null);
        for(String jpg:jpgs){
            System.out.println(jpg);
        }
    }
}
