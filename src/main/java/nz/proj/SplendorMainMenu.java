package nz.proj;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.DialogService;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.net.SocketClient;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SplendorMainMenu extends FXGLMenu {
    int temp = 0;

    public SplendorMainMenu() {
        super(MenuType.MAIN_MENU);
        getPrimaryStage().setOnCloseRequest(event -> {
            if (SocketClient.getInstance().login) {
                Bundle b = new Bundle("close");
                b.put("name", SocketClient.getInstance().name);
                SocketClient.getInstance().send(b);
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
                new MenuButton("New Game", () -> {
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
        SocketClient SOCKET_CLIENT = SocketClient.getInstance();
        SOCKET_CLIENT.connect();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        DialogService dialogService = getDialogService();
        pane.setHgap(20);
        pane.setVgap(15);

        TextField name_inp = new TextField();
        PasswordField password_inp = new PasswordField();
        pane.addRow(0, getUIFactoryService().newText("Account"), name_inp);
        pane.addRow(1, getUIFactoryService().newText("Password"), password_inp);

        Button ack = getUIFactoryService().newButton("SignIn");
        Button signup = getUIFactoryService().newButton("SignUp");
        Button retrieve = getUIFactoryService().newButton("Retrieve pwd");

        ack.setOnAction(actionEvent -> {
            String name = name_inp.getText();
            String pwd = password_inp.getText();
            Bundle loginBundle = new Bundle("login");
            loginBundle.put("name", name);
            loginBundle.put("pwd", pwd);
            SocketClient.getInstance().send(loginBundle);
            SocketClient.getInstance().name = name;
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

    private void onlineGame() {
        if (!SocketClient.getInstance().login) {
            loginPane();
        }
//        if (!Config.CONN.isConnected()) {
//            if (Config.CONN.Connect()) {
//                loginPane();
//            } else {
//                getDialogService().showMessageBox("Network Error");
//            }
//        } else {
//            FXGL.getNotificationService().pushNotification("You have logged in");
//        }
    }

    private void instructions() {
        getDialogService().showMessageBox("目前:右键拿牌左键保留牌");
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
        if (SocketClient.getInstance().login) {
            if (temp == 0) {
                getSceneService().pushSubScene(Config.MODE_SCENE);
                FXGL.getNotificationService().pushNotification("Login Successfully");
                temp += 1;
            }
            if (Config.MODE_SCENE.mode != 0 && SocketClient.getInstance().match) {
                fireNewGame();
            }
        } else {
            if (Config.MODE_SCENE.mode != 0) {
                fireNewGame();
            }
        }
    }
}
