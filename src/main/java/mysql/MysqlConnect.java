package mysql;
import java.sql.*;

public class MysqlConnect {
	public static void main(String args[]) {
		
		System.out.println("begin!");
		
		//加载MYSQL JDBC驱动程序
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");  
			System.out.println("success1");
		}catch (Exception e) {
			System.out.println("failure1");
			e.printStackTrace();
		}
		
		Connection con;
		//182.92.238.179:3306/test 域：端口号/数据库
		String url = "jdbc:mysql://20.239.88.211/players?useSSL=false";
		//用户名
		String user = "root";  
		//密码
		String password = "158356Proj.";
		
		try {
			con = DriverManager.getConnection(url, user, password);
			if(!con.isClosed()) {
				System.out.println("success2");
			}
			
			//创建statement类对象，用来执行SQL语句
			Statement statement = con.createStatement();
			//student变为players
			String sql = "Select * from players.playersInfo";
			ResultSet results = statement.executeQuery(sql);
			
			while(results.next()) {
				System.out.print(results.getInt("id"));
				System.out.print(" ");
				System.out.print(results.getString("name"));
				System.out.print(" ");
				System.out.println(results.getInt("password"));
			}
			
		}catch (Exception e) {
			System.out.println("failure2");
			e.printStackTrace();
		}
	}
}
