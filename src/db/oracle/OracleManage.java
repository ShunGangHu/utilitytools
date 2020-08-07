package db.oracle;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import hsg.test.HelloWord;

public class OracleManage {
	 // ��������������ַ���
    // 192.168.0.X�Ǳ�����ַ(Ҫ�ĳ��Լ���IP��ַ)��1521�˿ںţ�XE�Ǿ����Oracle��Ĭ�����ݿ���
    private static String USERNAMR = "system";
    private static String PASSWORD = "oracledba";
    private static String DRVIER = "oracle.jdbc.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@localhost:1521:orcl";

    // ����һ�����ݿ�����
    Connection connection = null;
    // ����Ԥ����������һ�㶼�������������Statement
    PreparedStatement pstm = null;
    // ����һ�����������
    ResultSet rs = null;

    
    /**
     * �������ݿ�
     * @return
     */
    public Connection getConnection() {
        try {
            Class.forName(DRVIER);
            connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
            System.out.println("�ɹ��������ݿ�");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        }

        return connection;
    }

    
    /**
     * �ͷ���Դ
     */
    public void ReleaseResource() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstm != null) {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * �����ݿ��в�������
     * @throws IOException 
     */
	public void insertData(String stuName, String gender, int age, String address, String comments) throws IOException {
		//connection = getConnection();
		// String sql =
		// "insert into student values('1','��С��','1','17','�����к�ƽ������30��¥7��102')";
		/*String sql = "select count(*) from be3u.student where 1 = 1";
		StringBuffer sqlBF = new StringBuffer();
		sqlBF.append(" insert into be3u.student ");
		sqlBF.append("  (stuid, stuname, gender, age, address, comments) ");
		sqlBF.append(" values ");
		sqlBF.append("  (?, ?, ?, ?, ?, ?) ");
		String sqlstr = sqlBF.toString();
		int count = 0;*/
		String sql = FileUtils.readFileToString(new File("C:/Users/HSG/Desktop/test.txt"), "utf-8");

		try {
			// �������ݿ�student������������
			/*pstm = connection.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1) + 1;
				System.out.println(rs.getInt(1));
			}*/
			// ִ�в������ݲ���
			sql = sql.replaceAll(";", "");
			Statement pstm = connection.createStatement();
			pstm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ReleaseResource();
		}
	}
	
	public static void main(String[] args) throws Exception {
		OracleManage om = new OracleManage();
		
		om.getConnection();
		om.insertData("С��", "1", 22, "�й�̨��", "������Ǵ�ҳ�˵��С����");
		HelloWord hw = new HelloWord();
		//String [] arr = null;
		//hw.main(arr);
	}
}
