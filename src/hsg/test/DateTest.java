package hsg.test;

import java.util.Scanner;

/**
 * 
 * ������
 * @author HSG
 * @version 1.0
 * ����ʱ�� 2020��8��12��
 */
public class DateTest {
	public static void main(String[] args) {

		/*Calendar calendar = Calendar.getInstance();
		calendar.set(2018, Calendar.DECEMBER, 31);
		Date strDate = calendar.getTime();
		DateFormat formatUpperCase = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("2018-12-31 to yyyy-MM-dd:" + formatUpperCase.format(strDate));
		formatUpperCase = new SimpleDateFormat("YYYY-MM-dd");
		System.out.println("2018-12-31 to YYYY-MM-dd:" + formatUpperCase.format(strDate));*/
		Scanner scanner = new Scanner(System.in);
		String str = scanner.next();
		if ("123456".equals(str)) {
			System.out.println("¼��ɹ���");
		}
		scanner.close();
	}
}
