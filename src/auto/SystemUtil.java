package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;

public class SystemUtil{

	private static final Logger logger = LoggerFactory
		.getLogger(SystemUtil.class);

	/**
	 * 方法简介.
	 * <p>
	 * 方法详述
	 * </p>
	 *
	 * @author HSG
	 * @date 创建时间 2020年8月5日
	 * @since V1.0
	 */
	public static Session connectLinux(String host, int port, String username,
			String password) throws Exception {
		Session session = null;
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			session.setTimeout(5 * 60 * 1000);
			session.setConfig("StrictHostKeyChecking", "no");// 是否验证主机秘钥
			Properties sshConfig = new Properties();
			sshConfig.put("kex", "diffie-hellman-group1-sha1");
			session.connect();
		} catch (Exception e) {
			throw new Exception("连接linux服务器时出错：" + e.getMessage());
		}
		return session;
	}

	/**
	 * 方法简介.
	 * <p>
	 * 方法详述
	 * </p>
	 *
	 * @throws Exception
	 * @author HSG
	 * @date 创建时间 2020年8月5日
	 * @since V1.0
	 */
	public static void disconnectLinux(Session session) throws Exception {
		try {
			session.disconnect();
		} catch (Exception e) {
			throw new Exception("断开linux服务器连接时出错：" + e.getMessage());
		}

	}

	/**
	 * 方法简介.
	 * <p>
	 * 方法详述
	 * </p>
	 *
	 * @author HSG
	 * @throws Exception
	 * @date 创建时间 2020年8月5日
	 * @since V1.0
	 */
	public static String executeLinuxCommand(Session session, String command,
			Boolean flag) throws Exception {
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		if (flag) {
			try {
				InputStream in = channelExec.getInputStream();
				channelExec.setCommand(command);
				channelExec.setErrStream(System.err);// 设置错误流
				channelExec.connect();
				String out = IOUtils.toString(in, "UTF-8");
				channelExec.disconnect();
				return out;
			} catch (Exception e) {
				throw new Exception("执行Linux命令时出错：" + e.getMessage());
			}
		} else {
			try {
				channelExec.setCommand(command);
				channelExec.setErrStream(System.err);
				channelExec.connect();
				channelExec.disconnect();
				return "命令已执行";
			} catch (Exception e) {
				throw new Exception("执行Linux命令时出错：" + e.getMessage());
			}
		}
	}

	/**
	 * 方法简介.
	 * <p>
	 * 方法详述
	 * </p>
	 *
	 * @author HSG
	 * @date 创建时间 2020年8月5日
	 * @since V1.0
	 */
	public static void uploadFileToLinux(Session session, String src,
			String dst) throws Exception {

		ChannelSftp channelSftp = null;
		try {
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
		} catch (Exception e) {
			throw new Exception("上传文件至Linux时出错：" + e.getMessage());
		}
		try {
			channelSftp.put(src, dst);
		} catch (Exception e) {
			throw new Exception("上传文件到Linux服务器失败：" + e.getMessage());
		}
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}
	}

	/**
	 * 方法简介.
	 * <p>
	 * 方法详述
	 * </p>
	 *
	 * @author HSG
	 * @date 创建时间 2020年8月5日
	 * @since V1.0
	 */
	public static void downloadFileFromLinux(Session session, String src,
			String dst) throws Exception {

		ChannelSftp channelSftp = null;
		try {
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
		} catch (Exception e) {
			throw new Exception("在Linux服务器中下载文件时出错：" + e.getMessage());
		}
		channelSftp.get(src, dst);
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}
	
	/**
	 * 
	 * 方法简介.ssh 方式连接linux 并执行命令
	 *
	 * @author HSG
	 * @date 创建时间 2020年8月6日
	 * @since V1.0
	 */
	public static void remoteInvokeShell(String ip, int port, String name,
			String pwd, String cmds) {

		Connection conn = null;
		try {
			conn = new Connection(ip, port);
			conn.connect();
			if (conn.authenticateWithPassword(name, pwd)) {
				// Open a new {@link Session} on this connection
				ch.ethz.ssh2.Session session = conn.openSession();
				// Execute a command on the remote machine.
				session.execCommand(cmds);
				Thread.sleep(1000*10);
				session.getStdin();
				BufferedReader br = new BufferedReader(new InputStreamReader(session
					.getStdout()));
				/*BufferedReader brErr = new BufferedReader(new InputStreamReader(session
					.getStderr()));*/

				String line;
				while ((line = br.readLine()) != null) {
					
					logger.info("br={}", line);
					if (line.indexOf("Server startup in") != -1||
							line.indexOf("Server started in RUNNING mode") != -1) {
						session.close();
					}
				}
				/*while ((line = brErr.readLine()) != null) {
					logger.info("brErr={}", line);
				}*/
				if (null != br) {
					br.close();
				}
				/*if (null != brErr) {
					brErr.close();
				}*/
				
			} else {
				logger.info("登录远程机器失败" + ip);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
				System.out.println("==========重启会话已关闭==========");
			}
		}
	}

}
