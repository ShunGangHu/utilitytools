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
	 * �������.
	 * <p>
	 * ��������
	 * </p>
	 *
	 * @author HSG
	 * @date ����ʱ�� 2020��8��5��
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
			session.setConfig("StrictHostKeyChecking", "no");// �Ƿ���֤������Կ
			Properties sshConfig = new Properties();
			sshConfig.put("kex", "diffie-hellman-group1-sha1");
			session.connect();
		} catch (Exception e) {
			throw new Exception("����linux������ʱ����" + e.getMessage());
		}
		return session;
	}

	/**
	 * �������.
	 * <p>
	 * ��������
	 * </p>
	 *
	 * @throws Exception
	 * @author HSG
	 * @date ����ʱ�� 2020��8��5��
	 * @since V1.0
	 */
	public static void disconnectLinux(Session session) throws Exception {
		try {
			session.disconnect();
		} catch (Exception e) {
			throw new Exception("�Ͽ�linux����������ʱ����" + e.getMessage());
		}

	}

	/**
	 * �������.
	 * <p>
	 * ��������
	 * </p>
	 *
	 * @author HSG
	 * @throws Exception
	 * @date ����ʱ�� 2020��8��5��
	 * @since V1.0
	 */
	public static String executeLinuxCommand(Session session, String command,
			Boolean flag) throws Exception {
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		if (flag) {
			try {
				InputStream in = channelExec.getInputStream();
				channelExec.setCommand(command);
				channelExec.setErrStream(System.err);// ���ô�����
				channelExec.connect();
				String out = IOUtils.toString(in, "UTF-8");
				channelExec.disconnect();
				return out;
			} catch (Exception e) {
				throw new Exception("ִ��Linux����ʱ����" + e.getMessage());
			}
		} else {
			try {
				channelExec.setCommand(command);
				channelExec.setErrStream(System.err);
				channelExec.connect();
				channelExec.disconnect();
				return "������ִ��";
			} catch (Exception e) {
				throw new Exception("ִ��Linux����ʱ����" + e.getMessage());
			}
		}
	}

	/**
	 * �������.
	 * <p>
	 * ��������
	 * </p>
	 *
	 * @author HSG
	 * @date ����ʱ�� 2020��8��5��
	 * @since V1.0
	 */
	public static void uploadFileToLinux(Session session, String src,
			String dst) throws Exception {

		ChannelSftp channelSftp = null;
		try {
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
		} catch (Exception e) {
			throw new Exception("�ϴ��ļ���Linuxʱ����" + e.getMessage());
		}
		try {
			channelSftp.put(src, dst);
		} catch (Exception e) {
			throw new Exception("�ϴ��ļ���Linux������ʧ�ܣ�" + e.getMessage());
		}
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}
	}

	/**
	 * �������.
	 * <p>
	 * ��������
	 * </p>
	 *
	 * @author HSG
	 * @date ����ʱ�� 2020��8��5��
	 * @since V1.0
	 */
	public static void downloadFileFromLinux(Session session, String src,
			String dst) throws Exception {

		ChannelSftp channelSftp = null;
		try {
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
		} catch (Exception e) {
			throw new Exception("��Linux�������������ļ�ʱ����" + e.getMessage());
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
	 * �������.ssh ��ʽ����linux ��ִ������
	 *
	 * @author HSG
	 * @date ����ʱ�� 2020��8��6��
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
				logger.info("��¼Զ�̻���ʧ��" + ip);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
				System.out.println("==========�����Ự�ѹر�==========");
			}
		}
	}

}
