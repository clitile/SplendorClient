package nz.sample;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.CursorInfo;
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
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.time.LocalTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static com.almasb.fxgl.dsl.FXGL.*;

public class MyTankApp extends GameApplication {
    public enum GameType{
        BULLET,ENEMY
    }
    private Entity tankEntity;
    private LocalTimer shootTimer;
    private boolean isMoving;
    private Dir dir;
    enum Dir{
        UP,DOWN,LEFT,RIGHT
    }

    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Tank");
        gameSettings.setVersion("0.1");
        gameSettings.setWidth(700);
        gameSettings.setHeight(500);
        gameSettings.setAppIcon("ba.png");

        gameSettings.setDefaultCursor(new CursorInfo("ba.png",0,0));
//        gameSettings.setMainMenuEnabled(true);
    }
    @Override
    protected void initGame() {
        FXGL.getip("score").addListener((ob,ov,nv)->{
            if (nv.intValue()>=5){
                getNotificationService().pushNotification("good");
            }
        });

        shootTimer=newLocalTimer();
        Canvas canvas=new Canvas(100,100);
        GraphicsContext g2d=canvas.getGraphicsContext2D();
        g2d.setFill(Color.web("#ffec03"));
        g2d.fillRect(0,0,80,30);
        g2d.setFill(Color.web("#cebc17"));
        g2d.fillRect(15,30,50,40);
        g2d.setFill(Color.web("#ffec03"));
        g2d.fillRect(0,70,80,30);
        g2d.setFill(Color.web("#f9ee8a"));
        g2d.fillRect(40,40,60,20);
        tankEntity=FXGL.entityBuilder()
                .viewWithBBox(canvas)
                .build();
        tankEntity.setRotationOrigin(new Point2D(50,50));
        FXGL.getGameWorld().addEntity(tankEntity);

        createEnemy();

        //////////
        getGameWorld().addEntityFactory(new TestFactory());
        getGameWorld().spawn("rect",new SpawnData(100,100).put("w",60).put("h",60).put("c",Color.YELLOW));

        ////////


    }
    private void createEnemy(){
        entityBuilder()
                .at(FXGLMath.random(60,700-60),FXGLMath.random(60,500-60))
                .viewWithBBox(new Rectangle(60,60,Color.BLUE))
                .collidable()
                .type(GameType.ENEMY)
                .buildAndAttach();
    }
    @Override
    protected void initInput() {


        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (isMoving){
                    return;
                }
                dir=Dir.UP;
                isMoving=true;
                tankEntity.translateY(-5);
                tankEntity.setRotation(270);
            }
        }, KeyCode.UP);
        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (isMoving){
                    return;
                }isMoving=true;
                dir=Dir.DOWN;
                tankEntity.translateY(5);
                tankEntity.setRotation(90);
            }
        }, KeyCode.DOWN);
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (isMoving){
                    return;
                }isMoving=true;
                dir=Dir.LEFT;
                tankEntity.translateX(-5);
                tankEntity.setRotation(180);
            }
        }, KeyCode.LEFT);
        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (isMoving){
                    return;
                }isMoving=true;
                dir=Dir.RIGHT;
                tankEntity.translateX(5);
                tankEntity.setRotation(0);
            }
        }, KeyCode.RIGHT);
        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onAction() {
                if (!shootTimer.elapsed(Duration.seconds(0.25))){
                    return;
                }
                play("drop.wav");

                shootTimer.capture();
                Point2D p;
                if (dir==Dir.UP){
                    p=new Point2D(0,-1);
                }else if (dir==Dir.DOWN){
                    p=new Point2D(0,1);
                }else if (dir==Dir.RIGHT){
                    p=new Point2D(1,0);
                }else{
                    p=new Point2D(-1,0);
                }
                Entity bullet=entityBuilder()
                        .type(GameType.BULLET)
                        .at(tankEntity.getCenter().getX()-10,tankEntity.getCenter().getY()-10)
                        .viewWithBBox(new Rectangle(20,20))
                        .with(new ProjectileComponent(p,600))
                        .with(new OffscreenCleanComponent())
                        .collidable()
                        .buildAndAttach();
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score",0);
    }
    @Override
    protected void onUpdate(double tpf){
        isMoving=false;
    }


    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameType.BULLET,GameType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
//                int score=geti("score")+10;
//                set("score",score);
                play("beam.wav");
                inc("score",1);

                a.removeFromWorld();
                Point2D center=b.getCenter();
                b.removeFromWorld();

                Circle circle=new Circle(10,Color.RED);
                Entity boom=entityBuilder()
                        .at(center)
                        .view(circle)
                        .with(new ExpireCleanComponent(Duration.seconds(.35)))
                        .buildAndAttach();
                ScaleTransition st=new ScaleTransition(Duration.seconds(.35),circle);
                st.setToX(10);
                st.setToY(10);
                FadeTransition ft=new FadeTransition(Duration.seconds(.35),circle);
                ft.setToValue(0);
                ParallelTransition pt=new ParallelTransition(st,ft);
//                pt.setOnFinished(event->boom.removeFromWorld());
                pt.play();
                createEnemy();

            }
        });
    }

    @Override
    protected void onPreInit() {

        getSettings().setGlobalMusicVolume(0.5);
        getSettings().setGlobalSoundVolume(0.8);
        loopBGM("rain.mp3");
    }

    @Override
    protected void initUI() {
//        Text text=addVarText("score",20,20);
//        text.setFill(Color.RED);

        Text text=FXGL.getUIFactoryService()
                .newText(FXGL.getip("score").asString("score:%d"));
        text.setLayoutX(30);
        text.setLayoutY(30);
        text.setFill(Color.RED);
        FXGL.addUINode(text);
    }
    public class TestFactory implements EntityFactory{
        @Spawns("rect")
        public Entity newRect(SpawnData data){
            return entityBuilder(data)
                    .view(new Rectangle(data.<Integer>get("w"),data.<Integer>get("h"),data.get("c")))
                    .build();
        }
    }
}
