package interaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Register {
	
	public static Label lblUserId2 = null;
	public static Label lblPassWord2 = null;
	public static TextField tfUserId2 = null;
	public static TextField tfPassWord2 = null;
	public static Stage stgSuccessCreate = null;
	public static Stage stgFailCreate = null;
    public static Stage primaryStage2 = null;

	
	public static void RegisterInterface() {
		System.out.println("OKOK");
		primaryStage2 = new Stage();
		
		//注册成功的窗口
        stgSuccessCreate = new Stage();
        stgSuccessCreate.setResizable(false);//设置无法调节窗口大小
        stgSuccessCreate.initStyle(StageStyle.UTILITY);//窗口只有退出
        stgSuccessCreate.initModality(Modality.APPLICATION_MODAL);//只能点改窗口
        
        Text textSuccess = new Text("Register Successfully!");
        Button btSuccess = new Button("OK");
        
        HBox hBoxForSuccessButton = new HBox();
        hBoxForSuccessButton.getChildren().add(btSuccess);
        hBoxForSuccessButton.setAlignment(Pos.CENTER);
        hBoxForSuccessButton.setPadding(new Insets(5));
        BorderPane paneForSuccess = new BorderPane();
        StackPane paneForSuccessText = new StackPane();
        paneForSuccessText.getChildren().add(textSuccess);
        paneForSuccess.setCenter(paneForSuccessText);
        paneForSuccess.setBottom(hBoxForSuccessButton);

        Scene sceneForSuccess = new Scene(paneForSuccess,150,80);
        stgSuccessCreate.setScene(sceneForSuccess);

        btSuccess.setOnAction(event -> {
            //注册成功后点击确认的操作，之后可以加入更多的后续操作
            stgSuccessCreate.close();
        });
        btSuccess.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) stgSuccessCreate.close();});
		
        
        //注册失败的窗口
        stgFailCreate = new Stage();
        stgFailCreate.setResizable(false);//设置无法调节窗口大小
        stgFailCreate.initStyle(StageStyle.UTILITY);//窗口只有退出
        stgFailCreate.initModality(Modality.APPLICATION_MODAL);//只能点改窗口

        Text textFail = new Text("Fail to Register\nplease change a name :)");
        Button btFail = new Button("OK");
        
        HBox hBoxForFailButton = new HBox();
        hBoxForFailButton.getChildren().add(btFail);
        hBoxForFailButton.setAlignment(Pos.CENTER);
        hBoxForFailButton.setPadding(new Insets(5));
        BorderPane paneForFail = new BorderPane();
        StackPane paneForFailText = new StackPane();
        paneForFailText.getChildren().add(textFail);
        paneForFail.setCenter(paneForFailText);
        paneForFail.setBottom(hBoxForFailButton);

        Scene sceneForFail = new Scene(paneForFail,150,80);
        stgFailCreate.setScene(sceneForFail);

        btFail.setOnAction(event -> {
            //注册失败后点击确认的操作，之后可以加入更多的后续操作
            stgFailCreate.close();
        });
        btFail.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) stgFailCreate.close();});
        
        //注册的UI界面
        tfUserId2 = new TextField();
        lblUserId2 = new Label("name:",tfUserId2);
        lblUserId2.setContentDisplay(ContentDisplay.RIGHT);
        tfPassWord2 = new TextField();
        lblPassWord2 = new Label("password:",tfPassWord2);
        lblPassWord2.setContentDisplay(ContentDisplay.RIGHT);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(15);
        gridPane.add(lblUserId2,0,0);
        gridPane.add(lblPassWord2,0,1);

        Button btSubmit = new Button("Submit");
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(5));
        hBox.getChildren().addAll(btSubmit);
        hBox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setBottom(hBox);
        pane.setCenter(gridPane);
        
        Register j = new Register();

        btSubmit.setOnAction(event -> j.judge());
        btSubmit.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) j.judge();});
        tfPassWord2.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) j.judge();});
        tfUserId2.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) j.judge();});

        Scene scene = new Scene(pane,400,300);
        
        primaryStage2.setScene(scene);
        primaryStage2.setTitle("Player Register System");
        primaryStage2.show();
	}
	
	public void judge(){
        if (submit() == true){
            //账号密码正确
            stgSuccessCreate.show();
            primaryStage2.close();
        }else {
            //用户名已存在
            stgFailCreate.show();
        }
    }
	
	private boolean submit() {
        Connection conn = null;
        //用PreparedStatement代替Statement
        PreparedStatement ps = null;
        ResultSet rs= null;

        try {
            //取出Map集合中用户输入的账户名和密码
            String inputName = tfUserId2.getText();
            String inputPwd = tfPassWord2.getText();
            // 1、注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2、获取连接
            conn = DriverManager.getConnection("jdbc:mysql://20.239.88.211/players?useSSL=false","root","158356Proj.");
            // 3、获取数据库操作对象
            //先建立起一个sql框架，用？占位符占位
            String sql1 = "select * from playersInfo where name = ? "; //and password = ?
            ps = conn.prepareStatement(sql1);
            //给？占位符传值
            ps.setString(1,inputName);
            // 4、执行sql语句
            rs = ps.executeQuery();
            // 5、获取查询结果集
            if (rs.next() == true){
            	// 用户名已存在
                return false;
            }else {
            	Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            	ResultSet rs2 = stmt.executeQuery("select * from playersInfo");
            	rs2.last();
            	int id = rs.getRow();
            	int account = id*111;
            	String sql2 = "INSERT INTO playersInfo (id, name, password, account) values (id, ?, ?, account)";
            	ps = conn.prepareStatement(sql2);
            	ps.setString(1, inputName);
            	ps.setString(2, inputPwd);
            	ps.executeUpdate();
            	
            	return true;
            	
            	// 这里需要做的是在数据库中创建新的用户
            }
            // 6、释放资源
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        //账户密码不正确
        return false;
    }
	
	
	
	
	
	
}
