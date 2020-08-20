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
		
		//tomcat文件夹
		String tomcatName = "tomcat_dwat";
		//远程Linux服务器IP
		String host = "10.1.10.21";
		//远程Linux服务器用户
		String username = "root";
		//远程Linux服务器密码
		String password = "oracledba";
		//远程Linux服务器重启脚本
		String shellPath = "sh /new/" + tomcatName + "/bin/stopAllAndStartAll.sh ";
		//远程Tomcat目录
		String jspDstRoot = "/new/" + tomcatName + "/webapps/";
		String javaDstRoot = "/new/" + tomcatName + "/webapps/dwat/WEB-INF/classes/";
		//本地tomcat路径
		String jspSrcRoot = "F:/apache-tomcat-6.0.41-windows-x64/apache-tomcat-6.0.41/webapps/";
		String javaSrcRoot = "F:/apache-tomcat-6.0.41-windows-x64/apache-tomcat-6.0.41/webapps/dwat/WEB-INF/classes/";
		
		//待升级文件路径集合文件
		String filePath = AutoUpgradeTomcat.class.getClassLoader().getResource("filePathToUpgrade.txt").getPath();;
		
		List<String> filePathList = FileUtils.readLines(new File(filePath), "utf-8");//解析文件内容
		
		//String fileStr = SystemUtil.readFileByLine(filePath);
		//String[] cls = new String[] {"cmd.exe","/c","cls"};
		//Runtime.getRuntime().exec(cls);
		
		System.out.println("请确定待升级文件、远程服务器信息等是否正确：0：错误，1：正确");
		Scanner scanner = new Scanner(System.in);
		String fileedit = scanner.next();
		if (fileedit.equals("0")) {//0：错误，1：正确
			System.out.println("=========错误并已中断========");
			System.exit(0);
		}
		
		scanner.close();
		
		String src = "",dst ="",pathi="";
		Session session = null;
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
						//本地war包路径   以后升级war包的次数会很少，甚至没有
						String warPath  = "C:/Users/HSG/Desktop/dwat.war";
						///System.out.println("请输入war包路径：");
						//Scanner scannerWar = new Scanner(System.in);
						//warPath = scannerWar.next();
						src = warPath;
						dst = jspDstRoot;
						//scannerWar.close();
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
				SystemUtil.remoteInvokeShell(host, 22, username, password, shellPath);
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

