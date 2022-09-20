package nz.proj;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import nz.comp.*;

import java.io.FileNotFoundException;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class SplendorFactory implements EntityFactory {
    @Spawns("level1,level2,level3")
    public Entity newLevel(SpawnData data) throws FileNotFoundException {
        return entityBuilder(data)
                .at(data.getX(),data.getY())
                .with(new CardComponent(String.valueOf(data.getData().get("spawnName"))))
                .build();
    }
    @Spawns("f_level1,f_level2,f_level3")
    public Entity newF_level(SpawnData data){
        return entityBuilder(data)
                .at(data.getX(),data.getY())
                .with(new FirstCardComponent(String.valueOf(data.getData().get("spawnName"))))
                .build();
    }
    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        return entityBuilder(data)
                .with(new PlayerComponent())
                .at(data.getX(),data.getY())
                .build();
    }
    
    @Spawns("coin")
    public Entity newCoin(SpawnData data){
        return entityBuilder(data)
                .with(new CoinComponent(data.get("style")))
                .at(data.getX(),data.getY())
                .build();
    }
    @Spawns("noble")
    public Entity newNoble(SpawnData data){
        return entityBuilder(data)
                .with(new NobleComponent())
                .at(data.getX(),data.getY())
                .build();
    }
    
    @Spawns("white_gem")
    public Entity newWhiteGem(SpawnData data) {
    	return entityBuilder(data)
    			.view("gem/white.png")
    			.build();
    }

    @Spawns("red_gem")
    public Entity newRedGem(SpawnData data) {
    	return entityBuilder(data)
    			.view("gem/red.png")
    			.build();
    }
    
    @Spawns("green_gem")
    public Entity newGreenGem(SpawnData data) {
    	return entityBuilder(data)
    			.view("gem/green.png")
    			.build();
    }
    
    @Spawns("blue_gem")
    public Entity newBlueGem(SpawnData data) {
    	return entityBuilder(data)
    			.view("gem/blue.png")
    			.build();
    }
    
    @Spawns("black_gem")
    public Entity newBlackGem(SpawnData data) {
    	return entityBuilder(data)
    			.view("gem/black.png")
    			.build();
    }
}
