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
	// 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    public static String myEmailAccount = "1096461774@qq.com";
    public static String myEmailPassword = "siekufplpouagfib";//这个密码不是QQ密码或者邮箱密码，要验证的。

	// 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
	// qq邮箱的 SMTP 服务器地址为: smtp.qq.com
	public static String myEmailSMTPHost = "smtp.qq.com";

	// 收件人邮箱（替换为自己知道的有效邮箱）
	public static String receiveMailAccount = "1156147537@qq.com";
	
	private static int count = 0;
	
	public static void main(String[] args) throws Exception {
		while (true) {
			Calendar now = Calendar.getInstance();
			int hour = now.get(Calendar.HOUR_OF_DAY);
			System.out.println("时: " + hour);
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

		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", myEmailSMTPHost); // 发件人的邮箱的
																// SMTP
																// 服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

		// 开启 SSL 连接, 以及更详细的发送步骤请看上一篇: 基于 JavaMail 的 Java 邮件发送：简单邮件发送
		// QQ邮箱端口有两个，可以百度。
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		
		 MailSSLSocketFactory sf = new MailSSLSocketFactory();
	        sf.setTrustAllHosts(true);
	        

	        props.put("mail.smtp.ssl.enable", "true");
	        props.put("mail.smtp.ssl.socketFactory", sf);
		
		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getInstance(props);
		// 设置为debug模式, 可以查看详细的发送 log
		session.setDebug(true);

		// 3. 创建一封邮件
		MimeMessage message = null;
		try {
			message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		// 也可以保持到本地查看
		// message.writeTo(file_out_put_stream);

		// 4. 根据 Session 获取邮件传输对象
		Transport transport = null;
		try {
			transport = session.getTransport();
		} catch (NoSuchProviderException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// 5. 使用 邮箱账号 和 密码 连接邮件服务器
		// 这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
		try {
			transport.connect(myEmailAccount, myEmailPassword);
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients()
		// 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
		try {
			transport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 7. 关闭连接
		try {
			transport.close();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 创建一封邮件邮件（文本+图片+附件） */

	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. 创建邮件对象
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人
		message.setFrom(new InternetAddress(sendMail, "胡先生", "UTF-8"));

		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		message.addRecipient(RecipientType.TO, new InternetAddress(receiveMail, "小可爱", "UTF-8"));
		Date date = new Date();
		SimpleDateFormat sdm = new SimpleDateFormat();
		sdm.format(date);
		Calendar ca = Calendar.getInstance();

		ca.setFirstDayOfWeek(Calendar.MONDAY);
		ca.setTime(date);
	
		String displayName = ca.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
		System.out.println("今天" + displayName + "," + dateToString(getCurrentDate(), "yyyymmdd"));
		String ss = "20190202";
		Date sss = stringToDate(ss);
		long days = getDayDifferenceBetweenTwoDate(sss, getCurrentDate());
		System.out.println("你们在一起已经" + days + "天了！");
		
		// 4. Subject: 邮件主题
		message.setSubject("与王女士在一起的日子", "UTF-8");

		// 下面是邮件内容的创建:

		// 5. 创建图片“节点”
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new
		FileDataSource("C:\\Users\\HSG\\Desktop\\mg.jpg")); // 读取本地文件
		image.setDataHandler(dh); // 将图片数据添加到“节点”
		image.setContentID("image_fairy_tail"); //
		// 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）

		// 6. 创建文本“节点”
		String brithday = dateToString(getCurrentDate(), "yyyymmdd").substring(4, 8);
		String nr = "今天" + displayName +"，我们在一起"+ days + "天了，每天都值得纪念哦！";
		if(brithday.equals("0706")){
			nr = "今天" + displayName +"，\n我们在一起"+ days + "天了，\n今天格外重要，因为你的生日就在今天，生日快乐哦！\n在你生日的这天，\n我感谢命运让我遇见你；\n感谢你出现在我的生命中；\n感谢所有最好的安排。\n我要大声的喊出：\n王婷同学你愿意嫁给我；\n做我最亲爱的老婆；\n给我爱你、宠你、呵护你的机会吗？";
		}
		MimeBodyPart text = new MimeBodyPart();
		// 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
		text.setContent(nr + "<br/><img src='cid:image_fairy_tail'/>",
		"text/html;charset=UTF-8");

		// 7. （文本+图片）设置 文本 和 图片 “节点”的关系（将 文本 和 图片 “节点”合成一个混合“节点”）
		MimeMultipart mm_text_image = new MimeMultipart();
		mm_text_image.addBodyPart(text);
		mm_text_image.addBodyPart(image);
		mm_text_image.setSubType("related"); // 关联关系

		// 8. 将 文本+图片 的混合“节点”封装成一个普通“节点”
		// 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
		// 上面的 mm_text_image 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
		MimeBodyPart text_image = new MimeBodyPart();
		text_image.setContent(mm_text_image);

		// 9. 创建附件“节点”
		// MimeBodyPart attachment = new MimeBodyPart();
		// DataHandler dh2 = new DataHandler(new
		// FileDataSource("C:\\Users\\HSG\\Desktop\\回退.txt")); // 读取本地文件
		// attachment.setDataHandler(dh2); // 将附件数据添加到“节点”
		// attachment.setFileName(MimeUtility.encodeText(dh2.getName())); //
		// 设置附件的文件名（需要编码）

		// 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text_image);
		//mm.addBodyPart(null); // 如果有多个附件，可以创建多个多次添加
		mm.setSubType("mixed"); // 混合关系

		// 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
		// message.setContent();
		message.setContent(mm);

		// 12. 设置发件时间
		message.setSentDate(new Date());

		// 13. 保存上面的所有设置
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

		if (format.indexOf("年") != -1)
			h.put(new Integer(format.indexOf("年")), "年");
		if (format.indexOf("月") != -1)
			h.put(new Integer(format.indexOf("月")), "月");
		if (format.indexOf("日") != -1)
			h.put(new Integer(format.indexOf("日")), "日");
		if (format.indexOf("时") != -1)
			h.put(new Integer(format.indexOf("时")), "时");
		if (format.indexOf("分") != -1)
			h.put(new Integer(format.indexOf("分")), "分");
		if (format.indexOf("秒") != -1)
			h.put(new Integer(format.indexOf("秒")), "秒");
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
			// Alert.FormatError("时间串【" + dateString + "】输入格式错误,请输入合法的日期格式!");
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
			// Alert.isNull("传入参数中的[时间串]为空");
		}
		if (format == null || format.equalsIgnoreCase("")) {
			/// Alert.isNull("传入参数中的[时间格式]为空");
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

		if (format.indexOf("年") != -1)
			h.put(new Integer(format.indexOf("年")), "年");
		if (format.indexOf("月") != -1)
			h.put(new Integer(format.indexOf("月")), "月");
		if (format.indexOf("日") != -1)
			h.put(new Integer(format.indexOf("日")), "日");
		if (format.indexOf("时") != -1)
			h.put(new Integer(format.indexOf("时")), "时");
		if (format.indexOf("分") != -1)
			h.put(new Integer(format.indexOf("分")), "分");
		if (format.indexOf("秒") != -1)
			h.put(new Integer(format.indexOf("秒")), "秒");

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
		df.setLenient(false);// 这个的功能是不把1996-13-3 转换为1997-1-3
		Date myDate = new Date();
		try {
			myDate = df.parse(dateString);
		} catch (ParseException e) {
			/**
			 * 解析抛出异常后， 如果判定此异常为"夏时制"起始日期导致， 则使用"松散模式"再次解析日期，并返回结果。
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
					throw new Exception("日期格式转换错误!将【" + dateString + "】按照格式【" + javaFormat + "】转换成时间时出错", e);
				}
			} catch (ParseException e1) {
				throw new Exception("日期格式转换出错!将【" + dateString + "】按照格式【" + javaFormat + "】转换成时间时出错", e1);
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
