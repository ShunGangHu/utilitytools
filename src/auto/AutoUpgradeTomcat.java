package auto;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.Session;

/**
* @author: HSG 
* @����ʱ��: 2020��8��5�� ����1:16:36 
* @version: 1.0  
*/
public class AutoUpgradeTomcat {

	public static void main(String[] args) throws Exception {
		
		String filePath = AutoUpgradeTomcat.class.getClassLoader().getResource("filePathToUpgrade.txt").getPath();;
		
		List<String> filePathList = FileUtils.readLines(new File(filePath), "utf-8");
		
		//String fileStr = SystemUtil.readFileByLine(filePath);
		//String[] cls = new String[] {"cmd.exe","/c","cls"};
		//Runtime.getRuntime().exec(cls);
		
		System.out.println("��ȷ���Ƿ����޸Ĵ������ļ���0��δ�޸ģ�1�����޸�");
		Scanner scanner = new Scanner(System.in);
		String fileedit = scanner.next();
		if (fileedit.equals("0")) {//0��δ�޸ģ�1�����޸�
			System.out.println("=========δ�޸����ж�========");
			System.exit(0);
		}
		
		scanner.close();
		String tomcatName = "tomcat_dwat_temp";
		//String [] fileArr = fileStr.split(",");
		String jspDstRoot = "/new/" + tomcatName + "/webapps/";
		String javaDstRoot = "/new/" + tomcatName + "/webapps/dwat/WEB-INF/classes/";
		
		String jspSrcRoot = "F:/apache-tomcat-6.0.41-windows-x64/apache-tomcat-6.0.41/webapps/";
		String javaSrcRoot = "F:/apache-tomcat-6.0.41-windows-x64/apache-tomcat-6.0.41/webapps/dwat/WEB-INF/classes/";
		
		String host = "";
		String username = "";
		String password = "";
		
		String src = "",dst ="",pathi="";
		Session session = null;
		//�Ժ�����war���Ĵ�������٣�����û��
		String warPath  = "C:/Users/HSG/Desktop/dwat.war";
		boolean reStartFlag = false;
		try {
			session = SystemUtil.connectLinux(host, 22, username, password);
			for (int i = 0; i < filePathList.size(); i++) {
				pathi = filePathList.get(i);
				if (!pathi.trim().endsWith(".jsp")
						&& !pathi.trim().endsWith(".java")
						&& !pathi.trim().endsWith(".war")) {
					throw new Exception("��" + (i + 1)
							+ "���ļ�·��������.jsp��.java��β��");
				}
				
				if (pathi.trim().endsWith(".jsp")) {
					src = jspSrcRoot + pathi.trim();
					dst = jspDstRoot + pathi.trim(); 
				} else {
					reStartFlag = true;
					if (pathi.trim().endsWith(".war")) {
						src = warPath;
						dst = jspDstRoot;
					} else {
						src = javaSrcRoot + pathi.trim().replaceAll("src/", "").replaceAll(".java", ".class");
						dst = javaDstRoot + pathi.trim().replaceAll("src/", "").replaceAll(".java", ".class");
					}
				}
				SystemUtil.uploadFileToLinux(session, src, dst);
				System.out.println("==========" + src + "���ϴ��ɹ�==========");
			}
			SystemUtil.disconnectLinux(session);
			System.out.println("==========�ϴ��Ự�ѹر�==========");
			if (reStartFlag) {
				SystemUtil.remoteInvokeShell(host, 22, username, password, "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ");
				//SystemUtil.executeLinuxCommand(session, "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ", false);
				System.out.println("==========�����ɹ�==========");
			}
		} catch (Exception e) {
			if (session != null && session.isConnected()) {
				SystemUtil.disconnectLinux(session);
			}
			//System.out.println(e.getMessage());
			throw e;
		}
		System.out.println("==========�������==========");

	}

}

