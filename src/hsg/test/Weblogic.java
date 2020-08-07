package hsg.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import weblogic.health.HealthState;

/**
 * 类描述
 * 
 * @author HSG
 * @version 1.0 创建时间 2016-8-3
 */
public  class Weblogic {
	private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static ObjectName service;
    
	static {
		try {
			service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e.getMessage());
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
	 * @date 创建时间 2016-8-3
	 * @since V1.0
	 */

	@SuppressWarnings({ "unused", "deprecation" })
	public static void main(String[] args) throws Exception {
		Weblogic dg = new Weblogic();
		dg.connection("10.1.60.104", "7001", "weblogic", "weblogic");
		ObjectName[] serverRT = getServerRuntimes();
		int length = (int) serverRT.length;
	      for (int i = 0; i < length; i++) {
	    	  getServerRuntime(serverRT[i]);
	      }
	}
	
	@SuppressWarnings("unchecked")
	public void connection(String hostname, String portString, String username,
			String password) throws IOException, MalformedURLException {
		  String protocol = "t3";
	      Integer portInteger = Integer.valueOf(portString);
	      int port = portInteger.intValue();  
	      String jndiroot = "/jndi/";
	      String mserver = "weblogic.management.mbeanservers.domainruntime";
	      JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port,
	      jndiroot + mserver);
	      @SuppressWarnings("rawtypes")
		  Hashtable h = new Hashtable();
	      h.put(Context.SECURITY_PRINCIPAL, username);
	      h.put(Context.SECURITY_CREDENTIALS, password);  
	      h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
	         "weblogic.management.remote");
	      connector = JMXConnectorFactory.connect(serviceURL, h);
	      connection = connector.getMBeanServerConnection();
	}
	
	 public static ObjectName[] getServerRuntimes() throws Exception {
	      return (ObjectName[]) connection.getAttribute(service,
	         "ServerRuntimes");
	 }
	 
	 public void printNameAndState() throws Exception {
	      ObjectName[] serverRT = getServerRuntimes();
	      System.out.println("got server runtimes");
	      int length = (int) serverRT.length;
	      for (int i = 0; i < length; i++) {
	         String name = (String) connection.getAttribute(serverRT[i],
	            "Name");
	         String state = (String) connection.getAttribute(serverRT[i],
	            "State");
	         System.out.println("Server name: " + name + ".   Server state: "
	            + state);
	      }
	   }
	
	/**
	 * 连接池管理
	 * 
	 * @param serverRuntime
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	public void getConnectorServiceRuntime(ObjectName serverRuntime)
	        throws AttributeNotFoundException, InstanceNotFoundException,
	        MBeanException, ReflectionException, IOException {
	    // 应用服务运行时 ApplicationRuntimes
	    ObjectName[] applicationRuntimes = getAttribute(serverRuntime,
	            "ApplicationRuntimes");
	    for (int i = 0; i < applicationRuntimes.length; i++) {
	        ObjectName applicationRuntime = applicationRuntimes[i];
	        ObjectName[] componentRuntimes = getAttribute(applicationRuntime,
	                "ComponentRuntimes");
	        for (int j = 0; j < componentRuntimes.length; j++) {
	            ObjectName componentRuntime = componentRuntimes[j];
	            String type = getAttribute(componentRuntime, "Type");
	            System.out.println(type);
	            if (!type.equals("ConnectorComponentRuntime")) {
	                continue;
	            }

	            ObjectName[] connectionPools = getAttribute(componentRuntime,
	                    "ConnectionPools");
	            for (int k = 0; k < connectionPools.length; k++) {
	                ObjectName connectionPool = connectionPools[k];
	                // 连接池的状态 State
	                String state = getAttribute(connectionPool, "State");
	                // 服务名称 Name
	                String name = getAttribute(connectionPool, "Name");
	                // 连接池名称 PoolName
	                String poolName = getAttribute(connectionPool, "PoolName");
	                // 连接池中的当前使用的连接数量 ActiveConnectionsCurrentCount
	                Integer activeConnectionsCurrentCount = getAttribute(
	                        connectionPool, "ActiveConnectionsCurrentCount");
	                // 等待池中连接的最大客户数 HighestNumWaiters
	                Long highestNumWaiters = getAttribute(connectionPool,
	                        "HighestNumWaiters");
	                // 丢失的连接数 ConnectionLeakProfileCount
	                Integer connectionLeakProfileCount = getAttribute(
	                        connectionPool, "ConnectionLeakProfileCount");
	                // 连接池最大连接数 MaxCapacity
	                Integer maxCapacity = getAttribute(connectionPool,
	                        "StMaxCapacityate");

	                System.out.println(name);
	                System.out.println(poolName);
	                System.out.println(state);
	                System.out.println(activeConnectionsCurrentCount);
	                System.out.println(highestNumWaiters);
	                System.out.println(connectionLeakProfileCount);
	                System.out.println(maxCapacity);

	            }
	        }

	    }
	}
	
	/**
	 * 服务器运行时
	 * 
	 * @param serverRuntime
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	public static void getServerRuntime(ObjectName serverRuntime)
	        throws AttributeNotFoundException, InstanceNotFoundException,
	        MBeanException, ReflectionException, IOException {

	    // 服务名称 Name
	    //String name = getAttribute(serverRuntime, "AdminServer2");
	    // 返回服务器激活时间ActivationTime
	   // Long activationTime = getAttribute(serverRuntime, "ActivationTime");

	    //Date date = new Date(activationTime);
	   /// String time = formatDate(date, "yyyy/MM/dd HH:mm:ss");
	    // 返回当前服务器监听连接的端口 ListenPort
	    Integer listenPort = getAttribute(serverRuntime, "ListenPort");
	    // 返回当前服务器监听连接的IP地址 ListenAddress
	    String listenAddress = getAttribute(serverRuntime, "ListenAddress");
	    // 状态 State
	    String state = getAttribute(serverRuntime, "State");
	    // 应用服务器的健康状态 HealthState
	    HealthState healthState = (HealthState) connection.getAttribute(
	            serverRuntime, "HealthState");
	    // 当前打开的Socket数量 OpenSocketsCurrentCount
	    Integer openSocketsCurrentCount = getAttribute(serverRuntime,
	            "OpenSocketsCurrentCount");
	    // 打开的Socket的总数 SocketsOpenedTotalCount
	    Long socketsOpenedTotalCount = getAttribute(serverRuntime,
	            "SocketsOpenedTotalCount");
	    // 当前连接数 TODO


	    System.out.println(healthState.getState());
	    System.out.println(healthState.getMBeanName());
	    System.out.println(healthState.getMBeanType());
	    System.out.println(healthState.getSubsystemName());
	    // TODO
	// System.out.println(name);
	// System.out.println(time);
	// System.out.println(listenPort);
	// System.out.println(listenAddress);
	// System.out.println(state);
	// System.out.println(healthState.getState());
	// System.out.println(openSocketsCurrentCount);
	// System.out.println(socketsOpenedTotalCount);

	}
	
	/**
	 * 日期格式转换
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format) {
	    DateFormat df = new SimpleDateFormat(format);
	    return df.format(date);
	}
	
	/**
	 * 获取weblogic属性参数
	 * 
	 * @param objectName
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(ObjectName objectName, String name) {
	    Object obj = null;
	    try {
	        obj = connection.getAttribute(objectName, name);
	    } catch (Exception e) {
	        // TODO
	        e.printStackTrace();
	    }
	    return (T) obj;
	}
	public static String getString(){
		return "0";
		
	}
	public static void copyFolderWithSelf(String oldPath,String newPath){
		  try {  
	            new File(newPath).mkdirs(); //如果文件夹不存在 则建立新文件夹  
	            File dir = new File(oldPath);  
	            // 目标  
	            //newPath +=  File.separator;  
	            File moveDir = new File(newPath);  
	            if(dir.isDirectory()){  
	                if (!moveDir.exists()) {  
	                    moveDir.mkdirs();  
	                }  
	            }
	            System.out.println("1234567");
	            String[] file = dir.list();  
	            File temp = null;  
	            for (int i = 0; i < file.length; i++) {  
	                if (oldPath.endsWith(File.separator)) {  
	                    temp = new File(oldPath + file[i]);  
	                } else {  
	                    temp = new File(oldPath + File.separator + file[i]);  
	                }  
	                if (temp.isFile()) {  
	                    FileInputStream input = new FileInputStream(temp);  
	                    FileOutputStream output = new FileOutputStream(newPath +  
	                            "/" +  
	                            (temp.getName()).toString());  
	                    byte[] b = new byte[1024 * 5];  
	                    int len;  
	                    while ((len = input.read(b)) != -1) {  
	                        output.write(b, 0, len);  
	                    }  
	                    output.flush();  
	                    output.close();  
	                    input.close();  
	                }  
	                if (temp.isDirectory()) { //如果是子文件夹  
	                	String ss =  file[i];
	                    copyFolderWithSelf(oldPath + "/" + file[i], newPath + "/" + file[i]);  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		    
	}
	private class CountBean {
		private int jsp;
		private int clazz;

		public int getJsp() {
			return jsp;
		}

		public int getClazz() {
			return clazz;
		}

		public void setJsp(int jsp) {
			this.jsp = jsp;
		}

		public void setClazz(int clazz) {
			this.clazz = clazz;
		}

	}

	public CountBean count(File file) {
		StringBuffer sqlBF = new StringBuffer();
		CountBean countBean = new CountBean();
		int jsp = 0;
		int clazz = 0;
		if (file.isDirectory()) {
			File[] subfiles = file.listFiles();
			for (File subfile : subfiles) {
				if (subfile.isDirectory()) {
					CountBean subCountBean = count(subfile);
					jsp += subCountBean.getJsp();
					clazz += subCountBean.getClazz();
				} else if (subfile.isFile()) {
					String filename = subfile.getName();
					String filepath = subfile.getPath();
					if (filename.endsWith(".jsp")) {
						filename = filename.substring(0, filename.length() - 4);

						// sqlBF.append(" insert into bm (jspname,jsppath) values(?,?) ");
						jsp++;
					} else if (filename.endsWith(".class")) {
						filename = filename.substring(2, filename.length() - 6);
						clazz++;
					}
				}
			}
		} else if (file.isFile()) {
			String filename = file.getName();
			String filepath = file.getPath();
			if (filename.endsWith(".jsp")) {
				filename = filename.substring(0, filename.length() - 4);
				jsp++;
			} else if (filename.endsWith(".class")) {
				filename = filename.substring(2, filename.length() - 6);
				clazz++;
			}
		}
		countBean.setJsp(jsp);
		countBean.setClazz(clazz);
		return countBean;
	}
	public static void testCompiler(String workspace) throws Exception {
		// String currentDir = System.getProperty("user.dir");
		// 將源码写入文件中
		// String src = "package com.test.cp;"
		// + "public class TestCompiler {"
		// + " public void disply() {"
		// + " System.out.println(\"Hello\");"
		// + "}}";
		String filename = "G:\\myeclipse2017workspace\\si10_facade\\src\\com\\dareway\\si\\jn\\bpo\\FysbJNBPO.java";
		// File file = new File(filename);
		// FileWriter fw = new FileWriter(file);
		// fw.write(src);
		// fw.flush();
		// fw.close();
		// 使用JavaCompiler 编译java文件
		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = jc
			.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> fileObjects = fileManager
			.getJavaFileObjects(filename);
		CompilationTask cTask = jc
			.getTask(null, fileManager, null, null, null, fileObjects);
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
}
