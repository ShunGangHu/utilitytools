package hsg.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.caiyi.SqlHandler.core.Sql;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import oracle.net.aso.e;


public class HelloWord {

	public static void main(String[] args) {
		//String skillKeyWord = "Borland";
		
		
		
		
		//Regex reg2 = new Regex("\\." + skillKeyWord + "\\.");
		
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL resUrl = new URL("http://localhost:8089/actuator/bus-refresh/");
            URLConnection urlConnec = resUrl.openConnection();
            urlConnec.setRequestProperty("accept", "*/*");
            urlConnec.setRequestProperty("connection", "Keep-Alive");
            urlConnec.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            urlConnec.setDoInput(true);
            urlConnec.setDoOutput(true);

            out = new PrintWriter(urlConnec.getOutputStream());
            out.print("");// ����post����
            out.flush();
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(urlConnec.getInputStream()));
        } catch (Exception e) {
            System.out.println("post������ʧ��" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
 
	// ƥ��������ʽ����
	public static boolean matcherRegularExpression(String regEx, String str) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		boolean found = false;
		while (matcher.find()) {
			//System.out.println("���� \"" + matcher.group() + "\" ��ʼ�� "
			//+ matcher.start() + " ������ " + matcher.end());
			found = true;
		}
		return found;
	}

	public String getUserName(){
		return "Test";
	}
	
	public String getUserName(int count){
		for(int i = 0; i<count;i++ ){
			System.out.println("User"+i);
		}
		return "Test";
	}
	public String getUserName(String [] arr){
		for(int i = 0; i<2;i++ ){
			System.out.println("User"+i);
		}
		return "Test";
	}
	
}
