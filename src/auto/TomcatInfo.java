package auto;

/**
 * 描述：
 *
 * @author HSG
 * @version 1.0
 * @date 2020/8/21 17:15
 */
public class TomcatInfo {

    //tomcat文件夹
    public static String tomcatName = "tomcat_dwat";
    //远程Linux服务器IP
    public static String host = "10.1.10.21";
    //远程Linux服务器用户
    public static String username = "root";
    //远程Linux服务器密码
    public static String password = "oracledba";
    //远程Linux服务器重启脚本
    public static String shellPath = "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ";
}