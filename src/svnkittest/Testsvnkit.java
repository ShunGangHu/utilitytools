package svnkittest;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNAuthentication;
import org.tmatesoft.svn.core.auth.SVNPasswordAuthentication;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author: HSG
 * @创建时间: 2017年4月26日 下午4:22:28
 * @version: 1.0
 */
public class Testsvnkit {
	// 声明SVN客户端管理类
	// private static SVNClientManager ClientManager;
	static String svnurl = "svn://10.1.10.14:9043/开发程序";
	static String path = "hsu/src/com/dw/ws/lm3/sy/ksbm";
	static String localpath = "G:\\eclipdeworkspase\\Selenium-hsu\\src\\bean\\server\\getA\\getinfo.java";
	static String username = "admin-mihsu";
	static String password = "dwsa2410";
	static String workspace = "D:\\newbuild_tool\\workspace\\hsu1.2.80.1_180524源";
	private static SVNRepository repository = null;
	private static int svnfilecount = 0;
	private static int localfilecount = 0;

	public static void main(String[] args) throws Exception {
		//setupLibrary();
		//SVNNodeKind kind=filecheck(svnurl, username, password);
		SVNClientManager clientManager = authSvn(svnurl, username,password);
		// if (null == clientManager) {
		// logger.error("SVN login error! >>> url:" +svnURL + path
		// + " username:" + name + " password:" + password);
		// }

		// String workspasepath = workspace+path ;
		// Boolean flag = isURLExist(svnURL + path, name, password);
		//SVNStatus svnstate = showStatus(clientManager, new File(workspace+path), true);
		//if(svnstate==null){
		//	System.out.println("未关联");
		//}
		//updateProjectFromSvn(clientManager, workspace+path,kind);
		// System.out.println(clientManager);
		//getcommithis();
		SVNURL repositoryURL = null;
		repositoryURL = SVNURL.parseURIEncoded(svnurl);
		checkout(clientManager, repositoryURL, SVNRevision.HEAD, new File("G:\\eclipdeworkspasea\\mihsu"),SVNDepth.INFINITY);
		//getFileRevisions();
		//CountFiles countsvnFiles = listEntries(path);
		//CountFiles countlocalFiles = listLocalEntries("D:\\newbuild_tool\\workspace\\hsu\\src\\com\\dw\\ws\\lm3\\sy\\ksbm");
		//int i = countsvnFiles.getSvnFiles();
		//int k =  countsvnFiles.getLocalFiles();
		//System.out.println(countsvnFiles.getSvnFiles() + countsvnFiles.getLocalFiles() );
		//docommit();
	}

	private static class CountFiles {
		int svnfiles = 0;
		int localfiles = 0;
		public int getSvnFiles() {
			return svnfiles;
		}

		public int getLocalFiles() {
			return localfiles;
		}

		public void setSvnFiles(int svnfiles) {
			this.svnfiles = svnfiles;
		}

		public void setLocalFiles(int localfiles) {
			this.localfiles = localfiles;
		}

	}
	
	/**
	 * 确定一个URL在SVN上是否存在
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isURLExist(SVNURL url, String username, String password) {
		try {
			SVNRepository svnRepository = SVNRepositoryFactory.create(url);
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
			svnRepository.setAuthenticationManager(authManager);
			SVNNodeKind nodeKind = svnRepository.checkPath("", -1);
			return nodeKind == SVNNodeKind.NONE ? false : true;
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 通过不同的协议初始化版本库
	 */
	public static void setupLibrary() {
		// DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		// FSRepositoryFactory.setup();
	}

	/**
	 * 验证登录svn
	 * 
	 * @throws SVNException
	 */
	public static SVNClientManager authSvn(String svnRoot, String username, String password) throws SVNException {
		// 初始化版本库
		setupLibrary();

		// 创建库连接

		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));
		} catch (SVNException e) {
			// logger.error(e.getErrorMessage(), e);
			System.out.println(e.getLocalizedMessage());
			return null;
		}

		// 身份验证
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
		// 创建身份验证管理器
		repository.setAuthenticationManager(authManager);
		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
		return clientManager;
	}

	/**
	 * 验证登录svn
	 * 
	 * @throws SVNException
	 */
	public static SVNNodeKind filecheck(String svnRoot, String username, String password) throws SVNException {
		/*
		 * // 初始化版本库 setupLibrary();
		 * 
		 * // 创建库连接 SVNRepository repository = null; try { repository =
		 * SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot)); } catch
		 * (SVNException e) { // logger.error(e.getErrorMessage(), e);
		 * System.out.println(e.getLocalizedMessage()); return null; }
		 * 
		 * // 身份验证 ISVNAuthenticationManager authManager =
		 * SVNWCUtil.createDefaultAuthenticationManager(username, password); //
		 * 创建身份验证管理器 repository.setAuthenticationManager(authManager);
		 * DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		 * SVNClientManager clientManager =
		 * SVNClientManager.newInstance(options, authManager); long version =
		 * SVNRevision.HEAD.getNumber(); SVNNodeKind kind =
		 * repository.checkPath(checkpath,version );
		 * if(kind==SVNNodeKind.NONE){//STPortal/stportal/stjsp/win10weihai/
		 * homePage_3705SBK.jsp
		 * 
		 * } return kind;
		 */

		// DataObject result = new DataObject();

		String classpath = path;
		// String xgxzbz = vdo.getString("xgxzbz");
		int errcount = 0;
		String errtext = "";
		String pjnames = "hsu";
		SVNNodeKind nodekind = null;
		if (!"2".equals("1")) {
			String pjname = "hsu";
			if (!pjname.toLowerCase().equals(pjnames.toLowerCase())) {
				errcount++;
				errtext = errtext + "、【" + classpath + "】：本地工程名与SVN的工程名【" + pjnames
						+ "】不一致或该文件路径不属于本工程,请输入文件的正确路径,比如：/hsu/src/com/dw/lov/sicp3/bh/BhBtbhLOVBPO.java！";
			}
			SVNRepository repository = null;
			try {
				repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnurl));
			} catch (SVNException e) {
				// logger.error(e.getErrorMessage(), e);
			}

			// 身份验证
			/*
			 * ISVNAuthenticationManager authManager = SVNWCUtil
			 * .createDefaultAuthenticationManager(username, password);
			 */
			ISVNAuthenticationManager authManager = new BasicAuthenticationManager(new SVNAuthentication[] {
					new SVNPasswordAuthentication(username, password, false, SVNURL.parseURIEncoded(svnurl), false), });
			// 创建身份验证管理器
			repository.setAuthenticationManager(authManager);
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
			SVNClientManager.newInstance(options, authManager);
			Boolean flag = svnurlcheck(svnurl);
			String classpatha = "";
			if (flag == null) {
				errcount++;
				errtext = errtext + "、【" + classpath + "】请输入文件的正确路径,比如：/hsu/src/com/dw/lov/sicp3/bh/BhBtbhLOVBPO.java！";
			}
			if (flag) {
				classpatha = classpath.substring(1, classpath.length());
				int num = classpatha.indexOf("/");
				// int ssss = aa.indexOf("/", 1);
				classpatha = classpatha.substring(num, classpatha.length());
				classpatha = pjnames + classpatha;
			}
			if (!flag) {
				classpatha = classpath.substring(1, classpath.length());
				int num = classpatha.indexOf("/");
				classpatha = classpatha.substring(num, classpatha.length());
				classpatha = pjnames + classpatha;
			}
			nodekind = repository.checkPath(classpatha, SVNRevision.HEAD.getNumber());
			if (nodekind.equals(SVNNodeKind.NONE)) {
				errcount++;
				errtext = errtext + "、不存在路径为【" + classpath + "】的文件！";

			} else if (nodekind == SVNNodeKind.DIR) {

			} else {

			}

		} else {

		}

		return nodekind;
	}

	/**
	 * 从SVN更新项目到work copy
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static boolean updateProjectFromSvn(SVNClientManager clientManager, String workspase, SVNNodeKind kind)
			throws Exception {
		File ws = new File(workspase);
		String dqlj = ws.getAbsolutePath();
		long version = 0;
		Boolean flag;
		flag = ws.exists();
		flag = ws.isDirectory();
		flag = ws.isFile();
		String p = "";
		if (kind.equals(SVNNodeKind.DIR)) {
			p = update(clientManager, ws, SVNRevision.HEAD, SVNDepth.INFINITY);
			// version = update(clientManager, ws, SVNRevision.HEAD,
			// SVNDepth.INFINITY, nodekind);
		} else {
			p = update(clientManager, ws, SVNRevision.HEAD, SVNDepth.EMPTY);
		}
		if (p.equals("parentbz")) {
			if (!ws.exists()) {
				String parentpath = ws.getParent();
				// nodekind = "DIR";
				// vdokind.clear();
				// vdokind.put("nodekind", nodekind);
				updateProjectFromSvnEc(clientManager, parentpath, "DIR");
			}
		}
		// if(ws.isDirectory()){

		// version = update(clientManager, ws, SVNRevision.HEAD,
		// SVNDepth.INFINITY);
		// }
		// version = update(clientManager, ws, SVNRevision.HEAD,
		// SVNDepth.EMPTY);
		// if (version > 0) {
		/*
		 * if (ws.isDirectory()) { version = update(clientManager, ws,
		 * SVNRevision.HEAD, SVNDepth.INFINITY); // String patha = workspace +
		 * path; // File filedq = new File(dqlj); // String name =
		 * filedq.getName(); // String[] ysljarr = patha.split("/"); // for (int
		 * i = 0; i < ysljarr.length; i++) { // if (i == ysljarr.length - 1) {
		 * // break; // } // if (ysljarr[i].equals(name)) { // String ss =
		 * ysljarr[i + 1]; // dqlj = dqlj + "/" + ysljarr[i + 1]; //
		 * updateProjectFromSvn(clientManager, dqlj); // } // } }
		 */
		// }

		// if (version <= -1) {
		// if(!ws.exists()){
		// String parentpath = ws.getParent();
		// updateProjectFromSvnEc(clientManager, parentpath);
		// }
		// }
		// testCompiler(workspace);
		return true;
	}

	public static void updateProjectFromSvnEc(SVNClientManager clientManager, String workspase, String kind)
			throws Exception {
		File ws = new File(workspase);
		String dqlj = ws.getAbsolutePath();
		// String nodekind = vdokind.getString("nodekind");
		// DataObject ret = new DataObject();
		// long version = 0;
		String p = "";
		if (kind.toUpperCase().equals("DIR") || kind.toUpperCase().equals("FILE")) {
			p = update(clientManager, ws, SVNRevision.HEAD, SVNDepth.EMPTY);
			if (p.equals("parentbz")) {
				if (!ws.exists()) {
					String parentpath = ws.getParent();
					ws = new File(parentpath);
					dqlj = ws.getAbsolutePath();
					p = update(clientManager, ws, SVNRevision.HEAD, SVNDepth.EMPTY);
				}
			}
		}
		boolean flag = true;
		SVNNodeKind nodekind = null;
		if (p.equals("true")) {
			if (ws.isDirectory()) {
				while (flag) {
					String patha = workspace + path;
					String name = ws.getName();
					if (patha.contains("/")) {
						String[] ysljarr = patha.split("/");
						int kk = ysljarr.length;
						for (int i = 0; i < ysljarr.length; i++) {
							if (i == ysljarr.length - 1) {
								break;
							}
							if (ysljarr[i].equals(name)) {
								dqlj = dqlj + "/" + ysljarr[i + 1];
								// DataObject vdoflx = filelxjudge(dqlj, appid,
								// dblx);
								nodekind = SVNNodeKind.DIR;
								if (i == ysljarr.length - 2) {
									nodekind = SVNNodeKind.FILE;
								}
								// vdokind.put("nodekind",nodekind);
								updateProjectFromSvnEc(clientManager, dqlj, nodekind.toString());
							}
						}
					}
					String pathnew = patha.replace('/', '\\');
					dqlj = dqlj.replace('/', '\\');
					if (pathnew.equals(dqlj)) {
						break;
					}
				}
			}
		}
	}

	public static String Plzml(File ws, String path) {
		String pathaa = ws.getAbsolutePath();
		String patha = workspace + path;
		File zml = new File(new File(patha), "");
		if (zml.getPath().equals(pathaa)) {

		}
		return patha;
	}

	////////////////////////
	/**
	 * 方法简介.
	 * <p>
	 * 方法详述
	 * </p>
	 * 
	 * @throws
	 * @author HSG
	 * @date 创建时间 2017年4月27日
	 * @since V1.0
	 */
	public static void testCompiler(String workspace) throws Exception {
		// String currentDir = System.getProperty("user.dir");
		// 將源码写入文件中
		// String src = "package com.test.cp;"
		// + "public class TestCompiler {"
		// + " public void disply() {"
		// + " System.out.println(\"Hello\");"
		// + "}}";
		String filename = workspace + path;
		// File file = new File(filename);
		// FileWriter fw = new FileWriter(file);
		// fw.write(src);
		// fw.flush();
		// fw.close();
		// 使用JavaCompiler 编译java文件
		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
		int res = jc.run(null, null, null, "G:/eclipdeworkspase/Selenium-hsu/src/iiiiii/iii.java");

		StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(filename);
		CompilationTask cTask = jc.getTask(null, fileManager, null, null, null, fileObjects);
		cTask.call();
		fileManager.close();
		// 使用URLClassLoader加载class到内存
		// URL[] urls = new URL[] { new URL("file:/" + currentDir + "/src/") };
		// URLClassLoader cLoader = new URLClassLoader(urls);
		// Class<?> c = cLoader.loadClass("com.test.cp.TestCompiler");
		// cLoader.close();
		// 利用class创建实例，反射执行方法
		// Object obj = c.newInstance();
		// Method method = c.getMethod("disply");
		// method.invoke(obj);
	}

	////////////////////////
	/**
	 * Updates a working copy (brings changes from the repository into the
	 * working copy).
	 * 
	 * @param clientManager
	 * @param wcPath
	 *            working copy path
	 * @param updateToRevision
	 *            revision to update to
	 * @param depth
	 *            update的深度：目录、子目录、文件
	 * @return
	 * @throws SVNException
	 */
	public static String update(SVNClientManager clientManager, File wcPath, SVNRevision updateToRevision,
			SVNDepth depth) {
		SVNUpdateClient updateClient = clientManager.getUpdateClient();

		/*
		 * sets externals not to be ignored during the update
		 */
		updateClient.setIgnoreExternals(false);

		/*
		 * returns the number of the revision wcPath was updated to
		 */
		long headversion = 0;
		try {
			headversion = updateClient.doUpdate(wcPath, updateToRevision, depth, false, false);
		} catch (SVNException e) {
			// logger.error(e.getErrorMessage(), e);
		}
		if (headversion < 0) {
			return "parentbz";
			// return headversion;
		}
		return "true";
	}

	/**
	 * recursively checks out a working copy from url into wcDir
	 * 
	 * @param clientManager
	 * @param url
	 *            a repository location from where a Working Copy will be
	 *            checked out
	 * @param revision
	 *            the desired revision of the Working Copy to be checked out
	 * @param destPath
	 *            the local path where the Working Copy will be placed
	 * @param depth
	 *            checkout的深度，目录、子目录、文件
	 * @return
	 * @throws SVNException
	 */
	public static long checkout(SVNClientManager clientManager, SVNURL url, SVNRevision revision, File destPath,
			SVNDepth depth) {

		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		/*
		 * sets externals not to be ignored during the checkout
		 */
		updateClient.setIgnoreExternals(false);
		/*
		 * returns the number of the revision at which the working copy is
		 */
		long lg = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date kssj = new Date();
			lg = updateClient.doCheckout(url, destPath, revision, revision, depth, true);
			//String zzsj = sdf.format(new Date());
			Date zzsj = new Date();
			System.out.println(zzsj.getTime()-kssj.getTime());
		} catch (SVNException e) {
			// logger.error(e.getErrorMessage(), e);
		}

		return lg;
	}

	public static void getFileRevisions() throws SVNException, ParseException {
		Collection<?> logEntries = null;
		
		logEntries = repository.getFileRevisions("lgdsu/src/main/java/com/dw/acc/zhyw/djgl/ModiDyxsztLosCreateBillAPO.java",
				(Collection<?>) null, repository.getLatestRevision(),  repository.getLatestRevision());
		 for (Iterator<?> revs = logEntries.iterator(); revs.hasNext();) { 
             SVNFileRevision fileRevision = (SVNFileRevision) revs.next();
             SVNProperties ss = fileRevision.getRevisionProperties();
             String author = ss.getStringValue("svn:author");
              /* Some private classes... 
              DataRevision revision = new DataRevision(); 
              revision.setRevision(fileRevision.getRevision()); 
              ret.add(revision);*/ 
             System.out.println("log查询完成！");
          } 
	}
	public static void getcommithis() throws SVNException, ParseException {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		final Date begin = format.parse("2018-7-01");
		final Date end = format.parse("2019-10-04");
		// final String author = "";
		long startRevision = 0;
		long endRevision = -1;// 表示最后一个版本
		final List<String> history = new ArrayList<String>();
		int count = 0;
		
		// String[] 为过滤的文件路径前缀，为空表示不进行过滤
		Collection<?> logEntries = null;
		logEntries = repository.log(new String[] { "src/main/java/com/dw/acc/zhyw/djgl/ModiDyxsztLosCreateBillAPO.java" }, null, startRevision, endRevision, true, true);
		for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			// System.out.println(
			// "---------------------------------------------" );
			/*
			 * System.out.println ("revision: " + logEntry.getRevision( ) );
			 * System.out.println( "author: " + logEntry.getAuthor( ) );
			 * System.out.println( "date: " + logEntry.getDate( ) );
			 * System.out.println( "log message: " + logEntry.getMessage( ) );
			 */
			Date cimmdate = logEntry.getDate();
			//if (cimmdate.after(begin) && cimmdate.before(end)) {
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());
			count++;
			if (logEntry.getChangedPaths().size() > 0) {
				// System.out.println( );
				System.out.println("changed paths:");
				Set<?> changedPathsSet = logEntry.getChangedPaths().keySet();
				for (Iterator<?> changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths()
							.get(changedPaths.next());
					
					try {
						System.out.println(entryPath.getType());
						String path = entryPath.getPath();
						if(path.endsWith(".jsp")){
							System.out.println(entryPath.getCopyPath());
						}
						int index = path.indexOf("lgdsu");
						path = path.substring(index-1, path.length());
						System.out.println(entryPath.getPath());
						//System.out.println(entryPath.getCopyRevision());
						count ++;
						
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		//}
		System.out.println(count + "log查询完成！");
	}

	public static Boolean svnurlcheck(String svnurl) {
		String[] urlarr = svnurl.split("/");
		boolean flag;
		if (urlarr.length <= 3) {
			flag = false;
		} else if (urlarr.length >= 4) {
			flag = true;
		} else {
			return null;
		}
		return flag;
	}

	/*
	 * 此函数递归的获取版本库中某一目录下的所有条目。
	 */
	public static CountFiles listEntries(String path) throws SVNException {
		// 获取版本库的path目录下的所有条目。参数－1表示是最新版本
		Collection<?> entries;
		Iterator<?> iterator;
		CountFiles cf = new CountFiles();
		int svnfilecount = 0;
		SVNNodeKind svnkind= repository.checkPath(path, -1);
		if (svnkind!=SVNNodeKind.NONE ){
			if ( svnkind == SVNNodeKind.DIR ) {
				entries = repository.getDir(path, -1, null, (Collection<?>) null);
				iterator = entries.iterator();
				while (iterator.hasNext()) {
					SVNDirEntry entry = (SVNDirEntry) iterator.next();
					svnfilecount++;
					System.out.println(svnfilecount+"\t/" + (path.equals("") ? "" :
					 path + "/") + entry.getName());
					if (entry.getKind() == SVNNodeKind.DIR) {
						listEntries((path.equals("")) ? entry.getName() : path + "/" + entry.getName());
					}
				}
			}else{
				svnfilecount++;
			}
		}else{
			System.out.println(svnkind.toString());
		}
		
		cf.setSvnFiles(svnfilecount);
		System.out.println(svnfilecount);
		return cf;
	}
	/**
	 * 
	 * @param path
	 * @throws SVNException
	 */
	public static CountFiles listLocalEntries(String path) throws SVNException {
		File file = new File(path);
		CountFiles cf = new CountFiles();
		int localfilecount = 0;
		if ( file.exists()) {
			if (file.isDirectory()) {
				File flist[] = file.listFiles();
				if (flist == null || flist.length == 0) {
					System.out.println("为空！");
				}
				for (File f : flist) {
					localfilecount++;
					if (f.isDirectory()) {
						// 这里将列出所有的文件夹
						// System.out.println("Dir==>" + f.getAbsolutePath());
						CountFiles cfs = listLocalEntries(f.getAbsolutePath());
					} else {
						// 这里将列出所有的文件
						// System.out.println("file==>" + f.getAbsolutePath());
					}
				}
			} else {
				localfilecount++;
			}
		} else {
			System.out.println("none");
		}
		cf.setLocalFiles(localfilecount);
		return cf;
	}
	/**
	 * 
	 * @param clientManager
	 * @param wcPath
	 * @param remote
	 * @return
	 */
	public static SVNStatus showStatus(SVNClientManager clientManager,
            File wcPath, boolean remote) {
        SVNStatus status = null;
        try {
            status = clientManager.getStatusClient().doStatus(wcPath, remote);
            SVNNodeKind sk = status.getKind();
            System.out.println(sk);
        } catch (SVNException e) {
            System.out.println(e.getMessage());
        }
        return status;
    }
	public static void docommit() throws SVNException{
		try {
			SVNClientManager clientManager = authSvn(svnurl, username, password);
			if (null == clientManager) {

			}
			File wcPath = new File(localpath);
			update(clientManager, wcPath, SVNRevision.HEAD, SVNDepth.EMPTY);
			String ss = wcPath.getAbsolutePath();
			clientManager.getCommitClient().doCommit(new File[] { wcPath }, false, 
					"修改系统版本号", null, null, false, false,
					SVNDepth.INFINITY);
		} catch (SVNException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("cg");
	}
}
