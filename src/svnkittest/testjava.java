package svnkittest;

import java.io.File;

/**
* @author: HSG 
* @����ʱ��: 2017��5��5�� ����11:05:51 
* @version: 1.0  
*/
public class testjava{
	
	
	static String svnURL = "svn://10.1.10.14:9017/";
	static String path = "/Selenium-hsu/src/eeeee/";
	static String name = "hushungang";
	static String password = "hudl0712";
	static String workspace = "G:/eclipdeworkspase";
	public static void main(String[] args) {
		// TODO Auto-generated method st
		/*String str=workspace+path;
		String ss= "D:\\newbuild_tool\\workspace/Selenium-hsu/src/eeeee";
		File sw =  new File(ss);
		String name=sw.getName();
		String sd = ss.replace("D:\\newbuild_tool\\workspace", "");
		String [] arr = str.split("/");
		for (int i=0;i<arr.length;i++){
			if(i==arr.length-1){
				System.out.println("123");
				break;
			}
			if(arr[i].equals(name)){
				ss=ss+arr[i+1];
				break;
			}
		}
		//str.substring(name, "/");
		System.out.println("1234");*/
		String ss = "/��������/hsu/src/com/dw/service/sicp3/bh/SavePerBirthZttcTIO.java";
		String [] arr = ss.split("/");
		System.out.println(ss);

		String dd = ss.substring(5, ss.length());
		int ll = arr.length;
		for (int i =0 ;i<arr.length;i++){
			String sa = arr[i];
			sa = sa.substring(1, sa.length());
			System.out.println(sa);
		}
	}
	
}

