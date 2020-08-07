package hsg.test;

public class DateTest {
	public static void main(String[] args) {

		/*Calendar calendar = Calendar.getInstance();
		calendar.set(2018, Calendar.DECEMBER, 31);
		Date strDate = calendar.getTime();
		DateFormat formatUpperCase = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("2018-12-31 to yyyy-MM-dd:" + formatUpperCase.format(strDate));
		formatUpperCase = new SimpleDateFormat("YYYY-MM-dd");
		System.out.println("2018-12-31 to YYYY-MM-dd:" + formatUpperCase.format(strDate));*/
		
		
		for (int i = 0; i <= 20; i++) {
			if (i % 2 == 0) {
				System.out.println(i);
			}
		}
	}
}
