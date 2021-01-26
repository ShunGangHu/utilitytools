package auto;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;

import java.net.URI;

import java.net.URISyntaxException;
/**
 * 描述：
 *
 * @author HSG
 * @version 1.0
 * @date 2020/10/19 17:16
 */
public class JenkinsUtil {

    /*
     * 方法简介.链接jenkins客户端  可以执行命令
     *
     * @author HSG
     * @date 创建时间 2020/10/19
     * @since V1.0
    */
    public static JenkinsHttpClient getClient(String jenkinsUrl, String jenlinsName, String jenkinsPwd) {
        JenkinsHttpClient jenkinsHttpClient = null;
        try {
            jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsUrl), jenlinsName, jenkinsPwd);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsHttpClient;
    }

    /*
     * 方法简介.连接Jenkins
     *
     * @author HSG
     * @date 创建时间 2020/10/19
     * @since V1.0
    */
    public static JenkinsServer connectionJenkinsServer(String jenkinsUrl, String jenlinsName, String jenkinsPwd) {
        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenlinsName, jenkinsPwd);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsServer;
    }

    /*
     * 方法简介.关闭jenkins
     *
     * @author HSG
     * @date 创建时间 2020/10/19
     * @since V1.0
    */
    public static void closejenkinsServer(JenkinsServer jenkinsServer){
        jenkinsServer.close();
    }
}
