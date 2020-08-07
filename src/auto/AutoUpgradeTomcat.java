package auto;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.Session;

/**
* @author: HSG 
* @创建时间: 2020年8月5日 下午1:16:36 
* @version: 1.0  
*/
public class AutoUpgradeTomcat {

	public static void main(String[] args) throws Exception {
		
		String filePath = AutoUpgradeTomcat.class.getClassLoader().getResource("filePathToUpgrade.txt").getPath();;
		
		List<String> filePathList = FileUtils.readLines(new File(filePath), "utf-8");
		
		//String fileStr = SystemUtil.readFileByLine(filePath);
		//String[] cls = new String[] {"cmd.exe","/c","cls"};
		//Runtime.getRuntime().exec(cls);
		
		System.out.println("请确定是否已修改待升级文件：0：未修改，1：已修改");
		Scanner scanner = new Scanner(System.in);
		String fileedit = scanner.next();
		if (fileedit.equals("0")) {//0：未修改，1：已修改
			System.out.println("=========未修改已中断========");
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
		//以后升级war包的次数会很少，甚至没有
		String warPath  = "C:/Users/HSG/Desktop/dwat.war";
		boolean reStartFlag = false;
		try {
			session = SystemUtil.connectLinux(host, 22, username, password);
			for (int i = 0; i < filePathList.size(); i++) {
				pathi = filePathList.get(i);
				if (!pathi.trim().endsWith(".jsp")
						&& !pathi.trim().endsWith(".java")
						&& !pathi.trim().endsWith(".war")) {
					throw new Exception("第" + (i + 1)
							+ "行文件路径不是以.jsp或.java结尾！");
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
				System.out.println("==========" + src + "：上传成功==========");
			}
			SystemUtil.disconnectLinux(session);
			System.out.println("==========上传会话已关闭==========");
			if (reStartFlag) {
				SystemUtil.remoteInvokeShell(host, 22, username, password, "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ");
				//SystemUtil.executeLinuxCommand(session, "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ", false);
				System.out.println("==========重启成功==========");
			}
		} catch (Exception e) {
			if (session != null && session.isConnected()) {
				SystemUtil.disconnectLinux(session);
			}
			//System.out.println(e.getMessage());
			throw e;
		}
		System.out.println("==========升级完成==========");

	}

}

