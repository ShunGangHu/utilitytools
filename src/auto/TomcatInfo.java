package auto;

/**
 * ������
 *
 * @author HSG
 * @version 1.0
 * @date 2020/8/21 17:15
 */
public class TomcatInfo {

    //tomcat�ļ���
    public static String tomcatName = "tomcat_dwat";
    //Զ��Linux������IP
    public static String host = "10.1.10.21";
    //Զ��Linux�������û�
    public static String username = "root";
    //Զ��Linux����������
    public static String password = "oracledba";
    //Զ��Linux�����������ű�
    public static String shellPath = "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ";
}