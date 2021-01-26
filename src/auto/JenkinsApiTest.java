package auto;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * ������
 *
 * @author HSG
 * @version 1.0
 * @date 2020/10/19 17:15
 */
public class JenkinsApiTest {
    public static void main(String[] args) throws Exception {
        String jenkinsUrl = "http://10.1.60.96:9999/jenkins";
        String jenlinsName = "admin";
        String jenkinsPwd = "sa";
        JenkinsServer jenkinsServer = JenkinsUtil.connectionJenkinsServer(jenkinsUrl,jenlinsName,jenkinsPwd);
        HashMap map = new HashMap();
        map.put("version","1.0.0");
        //jenkinsServer.getJob("gittest").build(map);
        JobWithDetails jobWithDetails = jenkinsServer.getJob("gittest").details();
        int nextNum = jobWithDetails.getNextBuildNumber();
        Build buildByNumber = jobWithDetails.getBuildByNumber(140);
        String url = buildByNumber.details().getUrl() + "console";
        JenkinsApiTest.openURLOnBrowser(url);
        Boolean isBuilding = true;
        while (isBuilding) {
            if (null == buildByNumber) {
                jobWithDetails = jenkinsServer.getJob("gittest").details();
                buildByNumber = jobWithDetails.getBuildByNumber(nextNum);
            }
            if (null != buildByNumber) {
                BuildWithDetails details = buildByNumber.details();
                if (null != details) {
                    isBuilding = details.isBuilding();

                } else {
                    isBuilding = true;
                }
            } else {
                isBuilding = true;
            }
        }
        BuildResult buildResult = jobWithDetails.getLastBuild().details().getResult();
        ConsoleLog consoleLog = jobWithDetails.getLastBuild().details().getConsoleOutputText(0);
        String consoleTxt = consoleLog.getConsoleLog();
        System.out.println(consoleTxt) ;
        System.out.println("������ɣ�" + buildResult) ;
        jenkinsServer.close();
        System.out.println(111);
    }

    private static void openURLOnBrowser(String url) throws Exception {
        //��ȡ����ϵͳ������
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            //ƻ���Ĵ򿪷�ʽ
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
            openURL.invoke(null, new Object[] { url });
        } else if (osName.startsWith("Windows")) {
            //windows�Ĵ򿪷�ʽ��
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux�Ĵ򿪷�ʽ
            String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                //ִ�д��룬��brower��ֵ��������
                //������������̴����ɹ��ˣ�==0�Ǳ�ʾ����������
                if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                //���ֵ�������Ѿ��ɹ��ĵõ���һ�����̡�
                Runtime.getRuntime().exec(new String[] { browser, url });
        }
    }
}