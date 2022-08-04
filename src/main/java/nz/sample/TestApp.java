package nz.sample;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.LocalTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Map;
import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.app.GameApplication;

public class TestApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {

    }
    @Override
    protected void initInput() {
    }
    @Override
    protected void onPreInit() {
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
    }

    @Override
    protected void initGame() {
        AnimatedTexture at;
        int RandomFrame= FXGLMath.random(0,24);
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("oleum-small.png")
                ,5,620/5,860/5, Duration.seconds(1),0,0));
        entityBuilder()
                .at(100,100)
                .viewWithBBox(at)
                .buildAndAttach();

    }
    @Override
    protected void initPhysics() {
    }

    @Override
    protected void initUI() {
    }

    @Override
    protected void onUpdate(double tpf) {
    }


    public static void main(String[] args) {
        launch(args);
    }





}
