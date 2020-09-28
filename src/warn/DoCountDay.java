package warn;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
* @author: HSG 
* @创建时间: 2020年7月21日 下午3:47:37 
* @version: 1.0  
*/
public class DoCountDay{
	
	private static int count = 0;
	public static void main(String[] args) throws Exception {
		int initNum = 2;
		System.out.println("====================功能已启动================");
		while (true) {
			Calendar now = Calendar.getInstance();
			int hour = now.get(Calendar.HOUR_OF_DAY);
			
			Date date = new Date();
            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            String time = format2.format(date);
			if (13 == hour && count == 0) {
				count = 1;
				String log = "";
				if (initNum % 2 == 0) {
					log = time + "：YES";
					System.out.println(log);
				} else {
					log = time + "：NO";
					System.out.println(log);
				}
				File filelog = new File("D:\\log\\countday.log");
				String fileContent = FileUtils.readFileToString(filelog,"utf-8");
				fileContent = fileContent + "\n" + log;
				FileUtils.writeStringToFile(filelog,fileContent,"utf-8");
				initNum +=1;
				//
			}
			if (13 != hour) {
				count = 0;
			}
			if (initNum == 9) {
				initNum = 1;
			}
			Thread.sleep(2000);
		}
	}

}

