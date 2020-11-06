package auto;

import java.util.Scanner;

/**
 * 描述：
 *
 * @author HSG
 * @version 1.0
 * @date 2020/8/21 17:11
 */
public class reStartTomcat {

    public static void main(String[] args) throws Exception {

        System.out.println("请确认Tomcat信息是否正确：0：错误，1：正确");
        System.out.println("HOST：" + TomcatInfo.host);
        System.out.println("PORT：" + 22);
        System.out.println("用户名：" + TomcatInfo.username);
        System.out.println("密码：" + TomcatInfo.password);
        System.out.println("Shell命令：" + TomcatInfo.shellPath);
        Scanner scanner = new Scanner(System.in);
        String fileedit = scanner.next();
        if (fileedit.equals("0")) {//0：错误，1：正确
            System.out.println("=========错误并已中断========");
            System.exit(0);
        }

        scanner.close();
        System.out.println("==========重启开始==========");
        SystemUtil.remoteInvokeShell(TomcatInfo.host, 22, TomcatInfo.username, TomcatInfo.password, TomcatInfo.shellPath);
        System.out.println("==========重启成功==========");
    }
}
