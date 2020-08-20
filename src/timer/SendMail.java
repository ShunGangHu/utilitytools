package timer;

import java.security.GeneralSecurityException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

public class SendMail {
	// �����˵� ���� �� ���루�滻Ϊ�Լ�����������룩
    public static String myEmailAccount = "1096461774@qq.com";
    public static String myEmailPassword = "siekufplpouagfib";//������벻��QQ��������������룬Ҫ��֤�ġ�

	// ����������� SMTP ��������ַ, ����׼ȷ, ��ͬ�ʼ���������ַ��ͬ, һ���ʽΪ: smtp.xxx.com
	// qq����� SMTP ��������ַΪ: smtp.qq.com
	public static String myEmailSMTPHost = "smtp.qq.com";

	// �ռ������䣨�滻Ϊ�Լ�֪������Ч���䣩
	public static String receiveMailAccount = "1156147537@qq.com";
	
	private static int count = 0;
	
	public static void main(String[] args) throws Exception {
		while (true) {
			Calendar now = Calendar.getInstance();
			int hour = now.get(Calendar.HOUR_OF_DAY);
			System.out.println("ʱ: " + hour);
			if (19 == hour && count == 0) {
				count = 1;
				doSendMail();
			}
			if (19 != hour) {
				count = 0;
			}
			Thread.sleep(2000);
		}
	}

	public static void doSendMail() throws GeneralSecurityException {

		// 1. ������������, ���������ʼ��������Ĳ�������
		Properties props = new Properties(); // ��������
		props.setProperty("mail.transport.protocol", "smtp"); // ʹ�õ�Э�飨JavaMail�淶Ҫ��
		props.setProperty("mail.smtp.host", myEmailSMTPHost); // �����˵������
																// SMTP
																// ��������ַ
		props.setProperty("mail.smtp.auth", "true"); // ��Ҫ������֤

		// ���� SSL ����, �Լ�����ϸ�ķ��Ͳ����뿴��һƪ: ���� JavaMail �� Java �ʼ����ͣ����ʼ�����
		// QQ����˿������������԰ٶȡ�
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		
		 MailSSLSocketFactory sf = new MailSSLSocketFactory();
	        sf.setTrustAllHosts(true);
	        

	        props.put("mail.smtp.ssl.enable", "true");
	        props.put("mail.smtp.ssl.socketFactory", sf);
		
		// 2. �������ô����Ự����, ���ں��ʼ�����������
		Session session = Session.getInstance(props);
		// ����Ϊdebugģʽ, ���Բ鿴��ϸ�ķ��� log
		session.setDebug(true);

		// 3. ����һ���ʼ�
		MimeMessage message = null;
		try {
			message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		// Ҳ���Ա��ֵ����ز鿴
		// message.writeTo(file_out_put_stream);

		// 4. ���� Session ��ȡ�ʼ��������
		Transport transport = null;
		try {
			transport = session.getTransport();
		} catch (NoSuchProviderException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// 5. ʹ�� �����˺� �� ���� �����ʼ�������
		// ������֤����������� message �еķ���������һ�£����򱨴�
		try {
			transport.connect(myEmailAccount, myEmailPassword);
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 6. �����ʼ�, �������е��ռ���ַ, message.getAllRecipients()
		// ��ȡ�������ڴ����ʼ�����ʱ��ӵ������ռ���, ������, ������
		try {
			transport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 7. �ر�����
		try {
			transport.close();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** ����һ���ʼ��ʼ����ı�+ͼƬ+������ */

	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. �����ʼ�����
		MimeMessage message = new MimeMessage(session);

		// 2. From: ������
		message.setFrom(new InternetAddress(sendMail, "������", "UTF-8"));

		// 3. To: �ռ��ˣ��������Ӷ���ռ��ˡ����͡����ͣ�
		message.addRecipient(RecipientType.TO, new InternetAddress(receiveMail, "С�ɰ�", "UTF-8"));
		Date date = new Date();
		SimpleDateFormat sdm = new SimpleDateFormat();
		sdm.format(date);
		Calendar ca = Calendar.getInstance();

		ca.setFirstDayOfWeek(Calendar.MONDAY);
		ca.setTime(date);
	
		String displayName = ca.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
		System.out.println("����" + displayName + "," + dateToString(getCurrentDate(), "yyyymmdd"));
		String ss = "20190202";
		Date sss = stringToDate(ss);
		long days = getDayDifferenceBetweenTwoDate(sss, getCurrentDate());
		System.out.println("������һ���Ѿ�" + days + "���ˣ�");
		
		// 4. Subject: �ʼ�����
		message.setSubject("����Ůʿ��һ�������", "UTF-8");

		// �������ʼ����ݵĴ���:

		// 5. ����ͼƬ���ڵ㡱
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new
		FileDataSource("C:\\Users\\HSG\\Desktop\\mg.jpg")); // ��ȡ�����ļ�
		image.setDataHandler(dh); // ��ͼƬ������ӵ����ڵ㡱
		image.setContentID("image_fairy_tail"); //
		// Ϊ���ڵ㡱����һ��Ψһ��ţ����ı����ڵ㡱�����ø�ID��

		// 6. �����ı����ڵ㡱
		String brithday = dateToString(getCurrentDate(), "yyyymmdd").substring(4, 8);
		String nr = "����" + displayName +"��������һ��"+ days + "���ˣ�ÿ�춼ֵ�ü���Ŷ��";
		if(brithday.equals("0706")){
			nr = "����" + displayName +"��\n������һ��"+ days + "���ˣ�\n���������Ҫ����Ϊ������վ��ڽ��죬���տ���Ŷ��\n�������յ����죬\n�Ҹ�л�������������㣻\n��л��������ҵ������У�\n��л������õİ��š�\n��Ҫ�����ĺ�����\n����ͬѧ��Ը��޸��ң�\n�������װ������ţ�\n���Ұ��㡢���㡢�ǻ���Ļ�����";
		}
		MimeBodyPart text = new MimeBodyPart();
		// �������ͼƬ�ķ�ʽ�ǽ�����ͼƬ�������ʼ�������, ʵ����Ҳ������ http ���ӵ���ʽ�������ͼƬ
		text.setContent(nr + "<br/><img src='cid:image_fairy_tail'/>",
		"text/html;charset=UTF-8");

		// 7. ���ı�+ͼƬ������ �ı� �� ͼƬ ���ڵ㡱�Ĺ�ϵ���� �ı� �� ͼƬ ���ڵ㡱�ϳ�һ����ϡ��ڵ㡱��
		MimeMultipart mm_text_image = new MimeMultipart();
		mm_text_image.addBodyPart(text);
		mm_text_image.addBodyPart(image);
		mm_text_image.setSubType("related"); // ������ϵ

		// 8. �� �ı�+ͼƬ �Ļ�ϡ��ڵ㡱��װ��һ����ͨ���ڵ㡱
		// ������ӵ��ʼ��� Content ���ɶ�� BodyPart ��ɵ� Multipart, ����������Ҫ���� BodyPart,
		// ����� mm_text_image ���� BodyPart, ����Ҫ�� mm_text_image ��װ��һ�� BodyPart
		MimeBodyPart text_image = new MimeBodyPart();
		text_image.setContent(mm_text_image);

		// 9. �����������ڵ㡱
		// MimeBodyPart attachment = new MimeBodyPart();
		// DataHandler dh2 = new DataHandler(new
		// FileDataSource("C:\\Users\\HSG\\Desktop\\����.txt")); // ��ȡ�����ļ�
		// attachment.setDataHandler(dh2); // ������������ӵ����ڵ㡱
		// attachment.setFileName(MimeUtility.encodeText(dh2.getName())); //
		// ���ø������ļ�������Ҫ���룩

		// 10. ���ã��ı�+ͼƬ���� ���� �Ĺ�ϵ���ϳ�һ����Ļ�ϡ��ڵ㡱 / Multipart ��
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text_image);
		//mm.addBodyPart(null); // ����ж�����������Դ������������
		mm.setSubType("mixed"); // ��Ϲ�ϵ

		// 11. ���������ʼ��Ĺ�ϵ�������յĻ�ϡ��ڵ㡱��Ϊ�ʼ���������ӵ��ʼ�����
		// message.setContent();
		message.setContent(mm);

		// 12. ���÷���ʱ��
		message.setSentDate(new Date());

		// 13. �����������������
		message.saveChanges();

		return message;
	}

	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	public static String dateToString(Date date, String format) throws Exception {
		if (date == null)
			return null;

		Hashtable<Integer, String> h = new Hashtable<Integer, String>();
		String javaFormat = new String();
		if (format.indexOf("yyyy") != -1)
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		else if (format.indexOf("yy") != -1)
			h.put(new Integer(format.indexOf("yy")), "yy");
		if (format.indexOf("MM") != -1)
			h.put(new Integer(format.indexOf("MM")), "MM");
		else if (format.indexOf("mm") != -1)
			h.put(new Integer(format.indexOf("mm")), "MM");
		if (format.indexOf("dd") != -1)
			h.put(new Integer(format.indexOf("dd")), "dd");
		if (format.indexOf("hh24") != -1)
			h.put(new Integer(format.indexOf("hh24")), "HH");
		else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1)
			h.put(new Integer(format.indexOf("mi")), "mm");
		else if (format.indexOf("mm") != -1 && h.containsValue("HH"))
			h.put(new Integer(format.lastIndexOf("mm")), "mm");
		if (format.indexOf("ss") != -1)
			h.put(new Integer(format.indexOf("ss")), "ss");
		if (format.indexOf("SSS") != -1)
			h.put(new Integer(format.indexOf("SSS")), "SSS");

		for (int intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}
		for (int intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (int intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (int intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("ʱ") != -1)
			h.put(new Integer(format.indexOf("ʱ")), "ʱ");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		int i = 0;
		while (h.size() != 0) {
			Enumeration<Integer> e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n)
					n = i;
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat, new DateFormatSymbols());
		return df.format(date);
	}

	public static Date stringToDate(String dateString) throws Exception {
		Date vdate = null;
		String vformat = null;
		if (dateString == null)
			return null;
		if (dateString.length() != 4 && dateString.length() != 6 && dateString.length() != 7 && dateString.length() != 8
				&& dateString.length() != 10 && dateString.length() != 14 && dateString.length() != 19) {
			// Alert.FormatError("ʱ�䴮��" + dateString + "�������ʽ����,������Ϸ������ڸ�ʽ!");
		}
		if (dateString.length() == 4) {
			vformat = "yyyy";
		} else if (dateString.length() == 6) {
			vformat = "yyyyMM";
		} else if (dateString.length() == 7) {
			dateString = dateString.substring(0, 4) + dateString.substring(5, 7);
			vformat = "yyyyMM";
		} else if (dateString.length() == 8) {
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 10) {
			dateString = dateString.substring(0, 4) + dateString.substring(5, 7) + dateString.substring(8, 10);
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 14) {
			vformat = "yyyyMMddHHmmss";
		} else if (dateString.length() == 19) {
			vformat = "yyyy-MM-dd HH:mm:ss";
		}
		vdate = stringToDate(dateString, vformat);
		return vdate;
	}

	public static Date stringToDate(String dateString, String format) throws Exception {
		if (dateString == null) {
			return null;
		}
		if (dateString.equalsIgnoreCase("")) {
			// Alert.isNull("��������е�[ʱ�䴮]Ϊ��");
		}
		if (format == null || format.equalsIgnoreCase("")) {
			/// Alert.isNull("��������е�[ʱ���ʽ]Ϊ��");
		}

		Hashtable<Integer, String> h = new Hashtable<Integer, String>();
		if (format.indexOf("yyyy") != -1)
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		else if (format.indexOf("yy") != -1)
			h.put(new Integer(format.indexOf("yy")), "yy");
		if (format.indexOf("MM") != -1)
			h.put(new Integer(format.indexOf("MM")), "MM");
		else if (format.indexOf("mm") != -1)
			h.put(new Integer(format.indexOf("mm")), "MM");
		if (format.indexOf("dd") != -1)
			h.put(new Integer(format.indexOf("dd")), "dd");
		if (format.indexOf("hh24") != -1)
			h.put(new Integer(format.indexOf("hh24")), "HH");
		else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1)
			h.put(new Integer(format.indexOf("mi")), "mm");
		else if (format.indexOf("mm") != -1 && h.containsValue("HH"))
			h.put(new Integer(format.lastIndexOf("mm")), "mm");
		if (format.indexOf("ss") != -1)
			h.put(new Integer(format.indexOf("ss")), "ss");
		if (format.indexOf("SSS") != -1)
			h.put(new Integer(format.indexOf("SSS")), "SSS");

		for (int intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}
		for (int intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (int intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (int intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("ʱ") != -1)
			h.put(new Integer(format.indexOf("ʱ")), "ʱ");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");
		if (format.indexOf("��") != -1)
			h.put(new Integer(format.indexOf("��")), "��");

		String javaFormat = new String();
		int i = 0;
		while (h.size() != 0) {
			Enumeration<Integer> e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n)
					n = i;
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat);
		df.setLenient(false);// ����Ĺ����ǲ���1996-13-3 ת��Ϊ1997-1-3
		Date myDate = new Date();
		try {
			myDate = df.parse(dateString);
		} catch (ParseException e) {
			/**
			 * �����׳��쳣�� ����ж����쳣Ϊ"��ʱ��"��ʼ���ڵ��£� ��ʹ��"��ɢģʽ"�ٴν������ڣ������ؽ����
			 * 
			 * 1940-06-03 01:00:00 ~ 1940-09-30 23:00:00 1941-03-16 01:00:00 ~
			 * 1941-09-30 23:00:00 1986-05-04 01:00:00 ~ 1986-09-13 23:00:00
			 * 1987-04-12 01:00:00 ~ 1987-09-12 23:00:00 1988-04-10 01:00:00 ~
			 * 1988-09-10 23:00:00 1989-04-16 01:00:00 ~ 1989-09-16 23:00:00
			 * 1990-04-15 01:00:00 ~ 1990-09-15 23:00:00 1991-04-14 01:00:00 ~
			 * 1991-09-14 23:00:00 20131015 lzx
			 */
			try {
				df.setLenient(true);

				Calendar c = Calendar.getInstance();
				c.setTime(df.parse(dateString));

				if ((c.get(Calendar.YEAR) == 1991 && c.get(Calendar.MONTH) == 3 && c.get(Calendar.DAY_OF_MONTH) == 14
						&& c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1990 && c.get(Calendar.MONTH) == 3
								&& c.get(Calendar.DAY_OF_MONTH) == 15 && c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1989 && c.get(Calendar.MONTH) == 3
								&& c.get(Calendar.DAY_OF_MONTH) == 16 && c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1988 && c.get(Calendar.MONTH) == 3
								&& c.get(Calendar.DAY_OF_MONTH) == 10 && c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1987 && c.get(Calendar.MONTH) == 3
								&& c.get(Calendar.DAY_OF_MONTH) == 12 && c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1986 && c.get(Calendar.MONTH) == 4
								&& c.get(Calendar.DAY_OF_MONTH) == 4 && c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1941 && c.get(Calendar.MONTH) == 2
								&& c.get(Calendar.DAY_OF_MONTH) == 16 && c.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1940 && c.get(Calendar.MONTH) == 5
								&& c.get(Calendar.DAY_OF_MONTH) == 3 && c.get(Calendar.HOUR_OF_DAY) == 1)) {
					myDate = c.getTime();
				} else {
					throw new Exception("���ڸ�ʽת������!����" + dateString + "�����ո�ʽ��" + javaFormat + "��ת����ʱ��ʱ����", e);
				}
			} catch (ParseException e1) {
				throw new Exception("���ڸ�ʽת������!����" + dateString + "�����ո�ʽ��" + javaFormat + "��ת����ʱ��ʱ����", e1);
			}
		}
		return myDate;
	}

	public static long getDayDifferenceBetweenTwoDate(Date beginDate, Date endDate) throws Exception {
		long ld1 = beginDate.getTime();
		long ld2 = endDate.getTime();
		long days = (long) ((ld2 - ld1) / 86400000);
		return days;
	}
}
