package nz.proj;

import com.almasb.fxgl.app.scene.FXGLMenu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.DialogService;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.net.SocketClient;
import nz.ui.Story;
import nz.ui.UIFactory;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SplendorMainMenu extends FXGLMenu {
	
	private LazyValue<Story> storyLazyValue = new LazyValue<>(() -> {
		return new Story();
	});
	
    int temp = 0;
    public SplendorMainMenu() {
    	 super(MenuType.MAIN_MENU);
    	 
        getPrimaryStage().setOnCloseRequest(event -> {
            if (SocketClient.getInstance().login) {
                Bundle b = new Bundle("close");
                b.put("name", SocketClient.getInstance().name);
                SocketClient.getInstance().send(b);
                SocketClient.getInstance().close();
            } else if (SocketClient.getInstance().isOpen()) {
                SocketClient.getInstance().close();
            }
        });
//        loopBGM(Config.BackMusic);
        
        getContentRoot().getChildren().setAll(texture("backg (2).png",Config.APP_WIDTH,Config.APP_HEIGHT));
        var blocks = new ArrayList<ColorBlock>();

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, Config.BlockColor);
            block.setTranslateX(getAppWidth() / 2.0 - 380 + i*50);
            block.setTranslateY(1);
            blocks.add(block);
            getContentRoot().getChildren().add(block);
            var block1 = new ColorBlock(40, Config.BlockColor);
            block1.setTranslateX(getAppWidth() / 2.0 - 380 + i*50);
            block1.setTranslateY(190);
            blocks.add(block1);
            getContentRoot().getChildren().add(block1);
        }
        for (int i = 0; i < 3; i++) {
            var block = new ColorBlock(40, Config.BlockColor);
            block.setTranslateX(getAppWidth() / 2.0 - 380 );
            block.setTranslateY(45+ i*50);
            blocks.add(block);
            getContentRoot().getChildren().add(block);
            var block1 = new ColorBlock(40, Config.BlockColor);
            block1.setTranslateX(getAppWidth() / 2.0 + 320 );
            block1.setTranslateY(45+ i*50);
            blocks.add(block1);
            getContentRoot().getChildren().add(block1);
        }
        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.get(i);
            animationBuilder()
                    .delay(Duration.seconds(i * 0.05))
                    .duration(Duration.seconds(0.5))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .animate(block.fillProperty())
                    .from(Config.BlockColor)
                    .to(Config.BlockColor.brighter().brighter())
                    .buildAndPlay(this);
        }
        
        
        var menuBox = new VBox(
                5,
                new MenuButton("Play with AI", () -> {
                	
                	getContentRoot().getChildren().setAll(texture("S-castle.png",Config.APP_WIDTH,Config.APP_HEIGHT));
                	getSceneService().pushSubScene(storyLazyValue.get());
                	getSceneService().popSubScene();
                    Config.MODE_SCENE.online = false;
                    getSceneService().pushSubScene(Config.MODE_SCENE);
                    
                }),
                new MenuButton("Online Game", this::onlineGame),
                new MenuButton("How to Play", this::instructions),
                
                new MenuButton("Exit", () -> {
                    if (SocketClient.getInstance().login) {
                        Bundle b = new Bundle("close");
                        b.put("name", SocketClient.getInstance().name);
                        SocketClient.getInstance().send(b);
                        SocketClient.getInstance().close();
                    } else if (SocketClient.getInstance().isOpen()) {
                        SocketClient.getInstance().close();
                    }
                    fireExit();
                })
        );
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(getAppWidth() / 2.0 - 140);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 200);
        getContentRoot().getChildren().addAll(menuBox);
       
    }

    private void loginPane() {
        if (!SocketClient.getInstance().isOpen()) {
            SocketClient.getInstance().connect();

            GridPane pane = new GridPane();
            pane.setAlignment(Pos.CENTER);
            DialogService dialogService = getDialogService();
            pane.setHgap(20);
            pane.setVgap(15);

            TextField name_inp = new TextField();
            PasswordField password_inp = new PasswordField();
            pane.addRow(0, getUIFactoryService().newText("Name"), name_inp);
            pane.addRow(1, getUIFactoryService().newText("Password"), password_inp);

            Button ack = getUIFactoryService().newButton("SignIn");
            Button signup = getUIFactoryService().newButton("SignUp");
            Button retrieve = getUIFactoryService().newButton("Retrieve pwd");

            ack.setOnAction(actionEvent -> {
                if (! SocketClient.getInstance().login) {
                    String name = name_inp.getText();
                    String pwd = password_inp.getText();
                    Bundle loginBundle = new Bundle("login");
                    loginBundle.put("name", name);
                    loginBundle.put("pwd", pwd);
                    SocketClient.getInstance().send(loginBundle);
                    SocketClient.getInstance().name = name;
                }

            });

            signup.setOnAction(actionEvent -> {
                GridPane signup_pane = new GridPane();
                signup_pane.setAlignment(Pos.CENTER);
                signup_pane.setHgap(20);
                signup_pane.setVgap(15);
                TextField newName = new TextField();
                TextField newAcc = new TextField();
                PasswordField newPwd = new PasswordField();

                Button ok = getUIFactoryService().newButton("SignUp");
                ok.setOnAction(event -> {
                    Bundle sign_up = new Bundle("signup");
                    sign_up.put("name", newName.getText());
                    sign_up.put("pwd", newPwd.getText());
                    sign_up.put("acc", newAcc.getText());
                    SocketClient.getInstance().send(sign_up);
                });

                signup_pane.addRow(0, getUIFactoryService().newText("Set Username"), newName);
                signup_pane.addRow(1, getUIFactoryService().newText("Set Account"), newAcc);
                signup_pane.addRow(2, getUIFactoryService().newText("Set Password"), newPwd);
                dialogService.showBox("Login", signup_pane, ok, getUIFactoryService().newButton("Cancel"));
            });

            retrieve.setOnAction(actionEvent -> {
                GridPane reset_pane = new GridPane();
                reset_pane.setAlignment(Pos.CENTER);
                reset_pane.setHgap(20);
                reset_pane.setVgap(15);
                TextField re_name = new TextField();
                PasswordField re_pwd = new PasswordField();

                Button ok = getUIFactoryService().newButton("Reset");
                ok.setOnAction(event -> {
                    Bundle re = new Bundle("reset");
                    re.put("name", re_name.getText());
                    re.put("pwd", re_pwd.getText());
                    SocketClient.getInstance().send(re);
                });

                reset_pane.addRow(0, getUIFactoryService().newText("Your Username"), re_name);
                reset_pane.addRow(1, getUIFactoryService().newText("Set Password"), re_pwd);
                dialogService.showBox("Login", reset_pane, ok, getUIFactoryService().newButton("Cancel"));
            });
            pane.addRow(2, signup, retrieve);
            dialogService.showBox("Login", pane, ack, getUIFactoryService().newButton("Cancel"));
        }
    }

    private void onlineGame() {
        if (!isReachable()) {
            getDialogService().showMessageBox("Server Error");
        } else {
            if (!SocketClient.getInstance().login) {
                loginPane();
            }
            if (temp == 1) {
                temp = 0;
            }
        }
    }
    
    //展示游戏规则，还需要改进
    private void instructions() {
    	//做成一个面板上面有左右两侧按钮，点击后更换带有游戏解释的图片。
        //getDialogService().showMessageBox("Right now: Use your mouse to choose cards and gems ~");
        
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 50.0);
            text.strokeProperty().bind(text.fillProperty());
            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.BLUE)
                            .otherwise(Color.WHITE)
            );
            setOnMouseClicked(e -> action.run());
            setPickOnBounds(true);
            getChildren().add(text);
        }
    }
    public static class ColorBlock extends Rectangle {
        public ColorBlock(int size, Color color) {
            super(size, size, color);
            if (!FXGL.isMobile()) {
                var shadow = new InnerShadow(25, Color.BLACK);
                shadow.setOffsetX(-3);
                shadow.setOffsetY(-3);
                setEffect(shadow);
            }
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        if (Config.MODE_SCENE.mode != 0 && !Config.MODE_SCENE.online) {
            fireNewGame();
        } else if (SocketClient.getInstance().login) {
            if (temp == 0) {
                Config.MODE_SCENE.online = true;
                getSceneService().pushSubScene(Config.MODE_SCENE);
                FXGL.getNotificationService().pushNotification("Login Successfully");
                temp = 1;
            }
            if (SocketClient.getInstance().match) {
                fireNewGame();
            }
        }
        if (! SocketClient.getInstance().info_corr) {
            FXGL.getNotificationService().pushNotification("Information Error");
            SocketClient.getInstance().info_corr = true;
        }
    }

    private boolean isReachable() {
        boolean reachable;
        // 如果端口为空，使用 isReachable 检测，非空使用 socket 检测
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(Config.IP, Integer.parseInt(Config.PORT)));
            reachable = true;
        } catch (Exception e) {
            reachable = false;
        }
        return reachable;
    }
}
