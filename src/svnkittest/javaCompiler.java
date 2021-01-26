package svnkittest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class javaCompiler {

	/*
	 * public static void main(String[] args) throws Exception { // TODO
	 * Auto-generated method stub compilejava(); }
	 */
	/**
	 * <b>function:</b> 导出增量文件
	 * 
	 * @author hoojo
	 * @createDate 2013-11-2 下午10:15:43
	 * @param configPath
	 *            增量文件路径配置目录
	 * @param baseDir
	 *            基本路径 目标位置
	 * @param destDir
	 *            增量文件保存位置
	 * @throws Exception
	 */
	private static void export(String configPath, String baseDir, String destDir) throws Exception {
		String srcFile = baseDir + configPath;
		String desFile = destDir + configPath;
		System.out.println(123);
		int lastIndex = desFile.lastIndexOf("/");
		String desPath = desFile.substring(0, lastIndex);

		File srcF = new File(srcFile);
		if (srcF.exists()) {// 如果不存在这样的源文件，就不再拷贝，这个用来解决版本之间有删除文件的情况。
			File desF = new File(desFile);
			File desP = new File(desPath);
			if (!desP.exists()) {
				desP.mkdirs();
			}
			System.out.println(srcFile);
			FileInputStream fis = new FileInputStream(srcF);
			FileOutputStream fos = new FileOutputStream(desF);

			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = fis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			fos.close();
			fis.close();
		}
	}

	/**
	 * <b>function:</b> 主函数 执行导出增量包任务
	 * 
	 * @author hoojo
	 * @createDate 2013-11-2 下午10:00:01
	 * @param args
	 *            参数1 增量包导出文件路径，参数2 要导出的文件的所在目标位置，参数3 增量包导出保存的位置路径
	 */
	public static void main(String[] args) {
		
		String configPath = "D:\\increment_test\\patch.txt";//args[0];
		String baseDir = "D:/workspace";//args[1]
		String destDir = "D:/increment_build";//args[2];
		try {
			BufferedReader br = new BufferedReader(new FileReader(configPath));
			String s = null;
			while ((s = br.readLine()) != null) {
				s = s.trim();// 去掉路径前面的空格
				//String str = destDir + s;
				/*if (!destDir.equals(str)) {// 过滤空行
					export(s, baseDir, destDir);
				}*/
				String [] arr = s.split("@@@@");
				for (int i = 0; i < arr.length; i++) {
					System.out.println(arr[i]);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String word1 = "A" + "@@@@" + "D:\\increment_test\\patch.txt"+"\n";
		String word2 = "M" + "@@@@" + "D:\\increment_test\\patch.txt"+"\n";
		String word3 = "D" + "@@@@" + "D:\\increment_test\\patch.txt"+"\n";
		String word4 = "M" + "@@@@" + "D:\\increment_test\\patch.txt"+"\n";
		String word5 = "M" + "@@@@" + "hahahahhahahahhah"+"\n";
		String all = word1 + word2 +word3+word4+word5; 
        FileOutputStream fileOutputStream = null;
        File file = new File("D:\\increment_test\\patch.txt");
        File filep = file.getParentFile();
        try {
        	if(!filep.exists()){
        		filep.mkdirs();
        	}
        	 if(!file.exists()){
 	            file.createNewFile();
 	        }
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(all.getBytes("gbk"));
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*if (args.length > 0) {
			if (args.length == 1 && "help".equals(args[0])) {
				System.out.println("args[0] is Export Increment Files content path");
				System.out.println("args[1] is Export Increment Files target path");
				System.out.println("args[2] is Increment Files Export loaction");
			} else {
				
			}
		}*/
	}

/*	private static void compilejava() throws Exception {
		String path = "G:/jspls/src/LOVController.java";// adBillFundViewLOV.jsp
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			synchronized (javaCompiler.class) {
				if (compiler == null) {
					// 讲道理，根据JavaCompiler
					// 的获取方式来看，应该是采用了单例模式的，但是这里为了顺便复习一下单例模式，以及确保一下单例吧
					compiler = ToolProvider.getSystemJavaCompiler();
				}
			}
		}
		// 建立DiagnosticCollector对象
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		// 建立用于保存被编译文件名的对象
		// 每个文件被保存在一个从JavaFileObject继承的类中
		Iterable<? extends JavaFileObject> compilationUnits = fileManager
				.getJavaFileObjectsFromStrings(Arrays.asList(path));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
				compilationUnits);
		// 编译源程式
		boolean success = task.call();
		fileManager.close();
		System.out.println((success) ? "编译成功" : "编译失败");
	}
*/}
