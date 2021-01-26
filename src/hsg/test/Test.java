package hsg.test;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * ������
 *
 * @author HSG
 * @version 1.0
 * @date 2021/1/26 9:26
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("��ȡ��������ĸ��"+getFirstSpell("��������"));
        System.out.println("����ת��Ϊƴ����"+getPingYin("��������"));
        System.out.println("����ת��Ϊƴ����"+getFullSpell("��������"));
        //System.out.println("���ַ���ת����ASCII��:"+getCnASCII("��������"));
        System.out.println("�ж��Ƿ���һ���ַ���"+"��".matches("[\\u4E00-\\u9FA5]+"));
    }


    /**
     * ��ȡ���ִ�ƴ������ĸ��Ӣ���ַ�����
     * @param chinese ���ִ�
     * @return ����ƴ������ĸ
     */
    public static String getFirstSpell(String chinese) {
        // ��StringBuffer���ַ������壩�����մ��������
        StringBuffer sb = new StringBuffer();
        //�ַ���ת��Ϊ�ֽ�����
        char[] arr = chinese.toCharArray();
        //����ת������
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        //ת�����ͣ���дorСд��
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //�������������������ʽ
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            //�ж��Ƿ��Ǻ����ַ�
            if (arr[i] > 128) {
                try {
                    // ��ȡ���ֵ�����ĸ
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        sb.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                // ������Ǻ����ַ���ֱ��ƴ��
                sb.append(arr[i]);
            }
        }
        return sb.toString().replaceAll("\\W", "").trim();
    }

    /**
     * ���ַ����е�����ת��Ϊƴ��,�����ַ�����
     * @param inputString
     * @return ����ƴ��
     */
    public static String getPingYin(String inputString) {

        //����ת������
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //ת�����ͣ���дorСд��
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //�������������������ʽ
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //�����ַ��������ʽ
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);

        //ת��Ϊ�ֽ�����
        char[] input = inputString.trim().toCharArray();
        // ��StringBuffer���ַ������壩�����մ��������
        StringBuffer output = new StringBuffer();

        try {
            for (int i = 0; i < input.length; i++) {
                //�ж��Ƿ���һ�������ַ�
                if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output.append(temp[0]);
                } else {
                    // ������Ǻ����ַ���ֱ��ƴ��
                    output.append(java.lang.Character.toString(input[i]));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * ��ȡ���ִ�ƴ����Ӣ���ַ����� ������ĸ��д��
     * @param chinese ���ִ�
     * @return ����ƴ��
     */
    public static String getFullSpell(String chinese) {
        // ��StringBuffer���ַ������壩�����մ��������
        StringBuffer sb = new StringBuffer();
        //�ַ���ת���ֽ�����
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        //ת�����ͣ���дorСд��
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //�������������������ʽ
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //�����ַ��������ʽ
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        for (int i = 0; i < arr.length; i++) {
            //�ж��Ƿ��Ǻ����ַ�
            if (arr[i] > 128) {
                try {
                    sb.append(capitalize(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                // ������Ǻ����ַ���ֱ��ƴ��
                sb.append(arr[i]);
            }
        }
        return sb.toString();
    }

    /**
     * ���ַ���ת����ASCII��
     * @param cnStr
     * @return String
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        // ���ַ���ת�����ֽ�����
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // ��ÿ���ַ�ת����ASCII��
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
    /**
     * ����ĸ��д
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        char ch[];
        ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        String newString = new String(ch);
        return newString;
    }

}