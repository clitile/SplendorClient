package nz.sample;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Map;
import static com.almasb.fxgl.dsl.FXGL.*;
public class MyDropApp extends GameApplication {
    public enum Type{
        DROPLET,BUCKET
    }
    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Drop");
        settings.setVersion("1.0");
        settings.setWidth(480);
        settings.setHeight(800);
        settings.setFontUI("VarelaRound-Regular.ttf");
//        settings.setApplicationMode(ApplicationMode.DEVELOPER);

    }

    @Override
    protected void initInput() {

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {

    }

    @Override
    protected void initGame() {
        Entity bucket = entityBuilder()
                .type(Type.BUCKET)
                .at(getAppWidth() / 2, getAppHeight() - 200)
                .viewWithBBox("bucket.png")
                .collidable()
                .buildAndAttach();
        bucket.xProperty().bind(getInput().mouseXWorldProperty());
        run(()->spawnDroplet(),Duration.seconds(1));
        loopBGM("bgm.mp3");
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Type.BUCKET,Type.DROPLET,(bucket,droplet)->{
            droplet.removeFromWorld();
            play("drop.wav");
        });
    }

    @Override
    protected void initUI() {

    }
    @Override
    protected void onUpdate(double tpf){
        getGameWorld().getEntitiesByType(Type.DROPLET).forEach(droplet->droplet.translateY(150*tpf));
    }
    private void spawnDroplet() {
        entityBuilder()
                .type(Type.DROPLET)
                .at(FXGLMath.random(0, getAppWidth() - 64), 0)
                .viewWithBBox("droplet.png")
                .collidable()
                .with(new OffscreenCleanComponent())
                .buildAndAttach();
    }
}