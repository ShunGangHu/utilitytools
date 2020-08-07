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
	 // 定义连接所需的字符串
    // 192.168.0.X是本机地址(要改成自己的IP地址)，1521端口号，XE是精简版Oracle的默认数据库名
    private static String USERNAMR = "system";
    private static String PASSWORD = "oracledba";
    private static String DRVIER = "oracle.jdbc.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@localhost:1521:orcl";

    // 创建一个数据库连接
    Connection connection = null;
    // 创建预编译语句对象，一般都是用这个而不用Statement
    PreparedStatement pstm = null;
    // 创建一个结果集对象
    ResultSet rs = null;

    
    /**
     * 连接数据库
     * @return
     */
    public Connection getConnection() {
        try {
            Class.forName(DRVIER);
            connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
            System.out.println("成功连接数据库");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        }

        return connection;
    }

    
    /**
     * 释放资源
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
     * 向数据库中插入数据
     * @throws IOException 
     */
	public void insertData(String stuName, String gender, int age, String address, String comments) throws IOException {
		//connection = getConnection();
		// String sql =
		// "insert into student values('1','王小军','1','17','北京市和平里七区30号楼7门102')";
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
			// 计算数据库student表中数据总数
			/*pstm = connection.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1) + 1;
				System.out.println(rs.getInt(1));
			}*/
			// 执行插入数据操作
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
		om.insertData("小明", "1", 22, "中国台湾", "这个就是大家常说的小明。");
		HelloWord hw = new HelloWord();
		//String [] arr = null;
		//hw.main(arr);
	}
}
