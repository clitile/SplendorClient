package nz.sample;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.LiftComponent;
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
public class MyCannonApp extends GameApplication {
    public enum EntityType{
        CANNON,BULLET,BASKET
    }
    private Entity cannon;
    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1000);
        gameSettings.setHeight(600);
        gameSettings.setTitle("cannon");
        gameSettings.setVersion("1");
    }

    @Override
    protected void initInput() {
        onBtnDown(MouseButton.PRIMARY,()->{
            spawn("bullet",cannon.getPosition().add(70,0));
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score",0);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CannonFactory());

        entityBuilder().buildScreenBoundsAndAttach(100);
        cannon=spawn("cannon",50,getAppHeight()-300);
        spawn("basketBarrier",400,getAppHeight()-300);
        spawn("basketBarrier",700,getAppHeight()-300);
        spawn("basketGround",500,getAppHeight()-100);
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.BULLET,EntityType.BASKET,(bullet,basket)->{
            bullet.removeFromWorld();
            inc("score", +1);
        });
    }

    @Override
    protected void initUI() {
        Text scoreText = getUIFactoryService().newText("", Color.BLACK, 24);
        scoreText.textProperty().bind(getip("score").asString("Score: [%d]"));

        addUINode(scoreText, 550, 100);
    }
    public class CannonFactory implements EntityFactory {

        @Spawns("cannon")
        public Entity newCannon(SpawnData data) {
            return entityBuilder(data)
                    .type(EntityType.CANNON)
                    .view(new Rectangle(70, 30, Color.BROWN))
                    .with(new LiftComponent().yAxisSpeedDuration(150, Duration.seconds(1)))
                    .build();
        }

        @Spawns("bullet")
        public Entity newBullet(SpawnData data) {
            PhysicsComponent physics = new PhysicsComponent();
            physics.setFixtureDef(new FixtureDef().density(0.05f));
            physics.setBodyType(BodyType.DYNAMIC);

            physics.setOnPhysicsInitialized(() -> {
                Point2D mousePosition = FXGL.getInput().getMousePositionWorld();
                physics.setLinearVelocity(mousePosition.subtract(data.getX(), data.getY()).normalize().multiply(800));
            });
            return entityBuilder(data)
                    .type(EntityType.BULLET)
                    .viewWithBBox(new Rectangle(25, 25, Color.BLUE))
                    .collidable()
                    .with(physics)
                    .with(new ExpireCleanComponent(Duration.seconds(4)))
                    .build();
        }

        @Spawns("basketBarrier")
        public Entity newBasketBarrier(SpawnData data) {
            return entityBuilder(data)
                    .type(EntityType.BASKET)
                    .viewWithBBox(new Rectangle(100, 300, Color.RED))
                    .with(new PhysicsComponent())
                    .build();
        }

        @Spawns("basketGround")
        public Entity newBasketGround(SpawnData data) {
            return entityBuilder(data)
                    .type(EntityType.BASKET)
                    .viewWithBBox(new Rectangle(300, 5, Color.BLACK))
                    .collidable()
                    .with(new PhysicsComponent())
                    .build();
        }
    }

}