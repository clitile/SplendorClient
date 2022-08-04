package nz.proj;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.view.MouseButtonView;
import com.almasb.fxgl.input.view.TriggerView;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.X;
public class SplendorMainMenu extends FXGLMenu {
    public SplendorMainMenu() {
        super(MenuType.MAIN_MENU);
        loopBGM(Config.BackMusic);
        getContentRoot().getChildren().setAll(texture("backg (2).png",Config.WIDTH,Config.HEIGHT));
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
                new MenuButton("New Game", () -> fireNewGame()),
                new MenuButton("Online Game", () -> onlineGame()),
                new MenuButton("How to Play", () -> instructions()),
                new MenuButton("Exit", () -> fireExit())
        );
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(getAppWidth() / 2.0 - 140);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 200);
        getContentRoot().getChildren().addAll(menuBox);

    }

    private void onlineGame() {
        getDialogService().showMessageBox("联网");
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



}
