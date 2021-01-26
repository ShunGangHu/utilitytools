package svnkittest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class GetSvnLogAuthor {
	/**
	 * 
	 * @param args
	 * @param args[0]:svnurl,args[1]:username,args[2]:password
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String svnRoot = "svn://10.1.10.14:9017/dwat";//args[0];
		String username = "hushungang";//args[1];
		String password = "hudl0712";//args[2];
		long startRevision = 0;
		long endRevision = -1;// 表示最后一个版本
		final List<String> history = new ArrayList<String>();
		int count = 0;
		// 初始化版本库
		SVNRepositoryFactoryImpl.setup();
		// 创建库连接
		SVNRepository repository = null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));
		} catch (SVNException e) {
			// logger.error(e.getErrorMessage(), e);
			System.out.println(e.getLocalizedMessage());
			throw new Exception(e);
		}
		String author = "";
		// 身份验证
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
		// 创建身份验证管理器
		repository.setAuthenticationManager(authManager);
		// String[] 为过滤的文件路径前缀，为空表示不进行过滤
		Collection<?> logEntries = null;
		logEntries = repository.log(new String[] { "" }, null, startRevision, endRevision, true, true);
		for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());
			String authort = logEntry.getAuthor();
			count++;
			if (!"".equals(author) || null != author) {
				if (author.indexOf(authort) > -1) {
					continue;
				} else {
					author = author + authort + "\n";
				}
			} else {
				author = author + authort + "\n";
			}
		}
        File file=new File("D://authors.txt");
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(author);// 往文件里写入字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		System.out.println(count + "log查询完成！"+author);
	}
}
