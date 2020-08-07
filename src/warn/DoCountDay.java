package warn;

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
	public static void main(String[] args) throws InterruptedException {
		int initNum = 0;
		while (true) {
			Calendar now = Calendar.getInstance();
			int hour = now.get(Calendar.HOUR_OF_DAY);
			
			 Date date = new Date();
             SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
             String time = format2.format(date);
			if (13 == hour && count == 0) {
				count = 1;
				if (initNum % 3 == 0) {
					System.out.println(time + "：YES");
				} else {
					System.out.println(time + "：NO");
				}
				initNum +=1;
				//
			}
			if (13 != hour) {
				count = 0;
			}
			if (initNum == 9) {
				initNum = 0;
			}
			Thread.sleep(2000);
		}
	}

}

