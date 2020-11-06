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
     * �������. �ӹ���Ŀ¼�����ļ�������
     *
     * @param remoteUrl
     *          ����Ŀ¼�ϵ��ļ�·��
     * @param localDir
     *          ����Ŀ¼
     * @author HSG
     * @date ����ʱ�� 2020/10/27
     * @since V1.0
    */
    public static byte[] smbGet(String remoteUrl, NtlmPasswordAuthentication auth) {
        byte[] bytes = {};
        try {
            SmbFile remoteFile = new SmbFile(remoteUrl, auth);
            if (remoteFile == null) {
                System.out.println("�����ļ�������");
                return null;
            }
            bytes = IOUtils.toByteArray(remoteFile.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * Description: �ӱ����ϴ��ļ�������Ŀ¼
     *
     * @Version1.0 Sep 25, 2009 3:49:00 PM
     * @param remoteUrl
     *          �����ļ�Ŀ¼
     * @param localFilePath
     *          �����ļ�����·��
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
     * Description: �ӹ���Ŀ¼�����ļ�
     *
     * @Version1.0 Sep 25, 2009 3:48:38 PM
     * @param remoteUrl
     *          ����Ŀ¼�ϵ��ļ�·��
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
        // smb:����;�û���:����@Ŀ��IP/�ļ���/�ļ���.xxx
        // test.smbGet("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake/test.txt",
        // "c://") ;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("10.1.60.75", "selenium", "selenium"); // �ȵ�¼��֤
        UniAddress address = UniAddress.getByName("10.1.60.75");//
        SmbSession.logon(address, auth);
        // SmbFile fp = new SmbFile(remoteurl+"//"+dir,auth);
        //test.smbPut("smb://10.1.60.75/test-hu/", "D:\\build.xml",auth);
        //test.smbGet("smb://10.12.91.156/smp_shared/contractvayiVyjK.pdf", auth);
        //String url = "smb://10.1.60.75/test-hu/12.txt";
        //test.smbDel(url, auth);
    }
 }

