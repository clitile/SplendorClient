
package interaction;

import javafx.application.Application;
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

import java.sql.*;

public class Login{
	public static Label lblUserId = null;
	public static Label lblPassWord = null;
	public static TextField tfUserId = null;
	public static TextField tfPassWord = null;
	public static Stage stgSuccess = null;
	public static Stage stgFail = null;
    public static Stage primaryStage = null;
    
    public static void LoginInterface() {
    	primaryStage = new Stage();
    	
        //登录成功的窗口
        stgSuccess = new Stage();
        stgSuccess.setResizable(false);//设置无法调节窗口大小
        stgSuccess.initStyle(StageStyle.UTILITY);//窗口只有退出
        stgSuccess.initModality(Modality.APPLICATION_MODAL);//只能点改窗口

        Text textSuccess = new Text("Log in Successfully!");
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
        stgSuccess.setScene(sceneForSuccess);

        btSuccess.setOnAction(event -> {
            //登录成功后点击确认的操作，之后可以加入更多的后续操作
            stgSuccess.close();
        });
        btSuccess.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) stgSuccess.close();});


        //登陆失败的窗口
        stgFail = new Stage();
        stgFail.setResizable(false);//设置无法调节窗口大小
        stgFail.initStyle(StageStyle.UTILITY);//窗口只有退出
        stgFail.initModality(Modality.APPLICATION_MODAL);//只能点改窗口

        Text textFail = new Text("Fail to log in");
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
        stgFail.setScene(sceneForFail);

        btFail.setOnAction(event -> {
            //登录失败后点击确认的操作，之后可以加入更多的后续操作
            stgFail.close();
        });
        btFail.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) stgFail.close();});

        //登录的UI界面
        tfUserId = new TextField();
        lblUserId = new Label("name:",tfUserId);
        lblUserId.setContentDisplay(ContentDisplay.RIGHT);
        tfPassWord = new TextField();
        lblPassWord = new Label("password:",tfPassWord);
        lblPassWord.setContentDisplay(ContentDisplay.RIGHT);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(15);
        gridPane.add(lblUserId,0,0);
        gridPane.add(lblPassWord,0,1);

        Button btSubmit = new Button("Submit");
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(5));
        hBox.getChildren().addAll(btSubmit);
        hBox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setBottom(hBox);
        pane.setCenter(gridPane);
        
   
        Login j = new Login();

        btSubmit.setOnAction(event -> j.judge());
        btSubmit.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) j.judge();});
        tfPassWord.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) j.judge();});
        tfUserId.setOnKeyPressed(event -> {if(event.getCode() == KeyCode.ENTER) j.judge();});

        Scene scene = new Scene(pane,400,300);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Player Login System");
        primaryStage.show();

    }

    public void judge(){
        if (submit()){
            //账号密码正确
            stgSuccess.show();
            primaryStage.close();
        }else {
            //账号密码错误
            stgFail.show();
        }
    }

    private boolean submit() {
        Connection conn = null;
        //用PreparedStatement代替Statement
        PreparedStatement ps= null;
        ResultSet rs= null;

        try {
            //取出Map集合中用户输入的账户名和密码
            String inputName = tfUserId.getText();
            String inputPwd = tfPassWord.getText();
            // 1、注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2、获取连接
            conn = DriverManager.getConnection("jdbc:mysql://20.239.88.211/players?useSSL=false","root","158356Proj.");
            // 3、获取数据库操作对象
            //先建立起一个sql框架，用？占位符占位
            String sql = "select * from players.playersInfo where name = ? and password = ?";
            ps = conn.prepareStatement(sql);
            //给？占位符传值
            ps.setString(1,inputName);
            ps.setString(2,inputPwd);
            // 4、执行sql语句
            rs = ps.executeQuery();
            // 5、获取查询结果集
            if (rs.next() == true){
                return true;
            }else {
            	System.out.println("Cannnot find corresponding results");
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

