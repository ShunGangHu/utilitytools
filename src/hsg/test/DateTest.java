package hsg.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import jcifs.smb.SmbSession;
import org.apache.commons.io.IOUtils;

public class DateTest {

    /*
     * 方法简介. 从共享目录拷贝文件到本地
     *
     * @param remoteUrl
     *          共享目录上的文件路径
     * @param localDir
     *          本地目录
     * @author HSG
     * @date 创建时间 2020/10/27
     * @since V1.0
    */
    public static byte[] smbGet(String remoteUrl, NtlmPasswordAuthentication auth) {
        byte[] bytes = {};
        try {
            SmbFile remoteFile = new SmbFile(remoteUrl, auth);
            if (remoteFile == null) {
                System.out.println("共享文件不存在");
                return null;
            }
            bytes = IOUtils.toByteArray(remoteFile.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * Description: 从本地上传文件到共享目录
     *
     * @Version1.0 Sep 25, 2009 3:49:00 PM
     * @param remoteUrl
     *          共享文件目录
     * @param localFilePath
     *          本地文件绝对路径
     */
    public static String smbPut(String remoteUrl, String localFilePath,NtlmPasswordAuthentication auth) {
        String result = null;
        FileInputStream fis = null;
        try {
            File localFile = new File(localFilePath);
            localFile.setReadOnly();
            String fileName = localFile.getName();
            SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName,auth);
            fis = new FileInputStream(localFile);
            IOUtils.copyLarge(fis, remoteFile.getOutputStream());
            result = "success";
        } catch (Exception e) {
            result = "failed";
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Description: 从共享目录下载文件
     *
     * @Version1.0 Sep 25, 2009 3:48:38 PM
     * @param remoteUrl
     *          共享目录上的文件路径
     */
    public static void smbDel(String remoteUrl, NtlmPasswordAuthentication auth) {
        try {
            SmbFile remoteFile = new SmbFile(remoteUrl, auth);
            if (remoteFile.exists()) {
                remoteFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception{
        DateTest test = new DateTest();
        // smb:域名;用户名:密码@目的IP/文件夹/文件名.xxx
        // test.smbGet("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake/test.txt",
        // "c://") ;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("10.1.60.75", "selenium", "selenium"); // 先登录验证
        UniAddress address = UniAddress.getByName("10.1.60.75");//
        SmbSession.logon(address, auth);
        // SmbFile fp = new SmbFile(remoteurl+"//"+dir,auth);
        //test.smbPut("smb://10.1.60.75/test-hu/", "D:\\build.xml",auth);
        //test.smbGet("smb://10.12.91.156/smp_shared/contractvayiVyjK.pdf", auth);
        //String url = "smb://10.1.60.75/test-hu/12.txt";
        //test.smbDel(url, auth);
    }
 }

