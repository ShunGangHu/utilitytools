package auto;

import java.util.Scanner;

/**
 * ������
 *
 * @author HSG
 * @version 1.0
 * @date 2020/8/21 17:11
 */
public class reStartTomcat {

    public static void main(String[] args) throws Exception {

        System.out.println("��ȷ��Tomcat��Ϣ�Ƿ���ȷ��0������1����ȷ");
        System.out.println("HOST��" + TomcatInfo.host);
        System.out.println("PORT��" + 22);
        System.out.println("�û�����" + TomcatInfo.username);
        System.out.println("���룺" + TomcatInfo.password);
        System.out.println("Shell���" + TomcatInfo.shellPath);
        Scanner scanner = new Scanner(System.in);
        String fileedit = scanner.next();
        if (fileedit.equals("0")) {//0������1����ȷ
            System.out.println("=========�������ж�========");
            System.exit(0);
        }

        scanner.close();
        System.out.println("==========������ʼ==========");
        SystemUtil.remoteInvokeShell(TomcatInfo.host, 22, TomcatInfo.username, TomcatInfo.password, TomcatInfo.shellPath);
        System.out.println("==========�����ɹ�==========");
    }
}
