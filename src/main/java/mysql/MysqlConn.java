package mysql;

import java.sql.*;

public class MysqlConn {
    Connection conn;
    private boolean isConnect = false;

    Statement statement = null;

    public MysqlConn() {
    }

    public boolean Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("success1");
        }catch (Exception e) {
            System.out.println("failure1");
            e.printStackTrace();
        }

        try {
            String user = "root";
            String password = "158356Proj.";
            String url = "jdbc:mysql://20.239.88.211/players?useSSL=false";
            this.conn = DriverManager.getConnection(url, user, password);
            if(!this.conn.isClosed()) {
                System.out.println("success2");
            }
            this.isConnect = true;
            statement = this.conn.createStatement();
            return true;
        }catch (Exception e) {
            System.out.println("failure2");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        return this.isConnect;
    }

    public boolean login(String user, String pwd) {
        try {
            String sql = "select password from players.playersInfo where name = '" + user + "'";
            ResultSet results = statement.executeQuery(sql);
            results.next();
            return results.getString("password").equals(pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean signUp(String acc, String pwd, String name) {
        String sql2 = "select max(id) from players.playersInfo";
        try {
            ResultSet results = statement.executeQuery(sql2);
            results.next();
            int id = results.getInt("max(id)") + 1;
            String sql1 = "insert into players.playersInfo values (" + id + ", " + name + ", " + pwd + ", " + acc + ")";
            statement.executeUpdate(sql1);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
