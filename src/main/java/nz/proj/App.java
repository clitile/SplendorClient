package nz.proj;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.*;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import nz.sample.MyTankApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {
    Entity player;//玩家
    List<Entity> f_card_3;//最右边三张卡
    List<Entity> s_card_12;//中间12张卡
    List<Entity> coinList;//硬币
    List<Entity> devCard;//保留卡
    List<Entity> nobleList;//贵族卡

    double mouse_x;//鼠标坐标x
    double mouse_y;//鼠标坐标y
    int clickThi=0;//判断点击事件
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(Config.HEIGHT);
        settings.setWidth(Config.WIDTH);
        settings.setTitle("Splendor");
        settings.setVersion("1.0.1");
        settings.setAppIcon("fp_token-1.png");

        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu() {
                return new SplendorMainMenu();
            }
        });

    }
    @Override
    protected void initInput() {
        onBtnDown(MouseButton.PRIMARY,()->{
            for (int i = 0; i <= 11; i++){
                if (initJudge(i,s_card_12,124,172)){
                    clickThi=1;break;//点右键对中间的卡操作
                }else if (initJudge(i,devCard,124,172)){
                    clickThi=1;break;//点右键对保留卡的卡操作
                }else if (initJudge(i,nobleList,150,151)){
                    clickThi=4;break;//点右键对贵族的卡操作
                }else if (initJudge(i,coinList,90,75)){
                    clickThi=2;break;//点右键对硬币操作
                }
            }});
        onBtnDown(MouseButton.SECONDARY,()->{
            for (int i = 0; i <= 11; i++) {
                if(mouseDis(i,124,172,s_card_12)){
                    clickThi=3;break;//鼠标右键对中间的卡操作
                }
            }});
    }
    @Override
    protected void onPreInit() {
        getAssetLoader().loadImage("cards_620_860.png");
        getAssetLoader().loadImage("nobles.png");
        getAssetLoader().loadImage("cards_372_172.png");


    }
    @Override//初始化列表，加载列表
    protected void initGame() {
        entityBuilder().viewWithBBox(texture("backg (1).jpg",Config.WIDTH,Config.HEIGHT)).buildAndAttach();
        getGameWorld().addEntityFactory(new SplendorFactory());
        f_card_3=new ArrayList<>();
        s_card_12=new ArrayList<>();
        coinList=new ArrayList<>();
        nobleList=new ArrayList<>();
        devCard=new ArrayList<>();
        player = getGameWorld().spawn("player",new SpawnData(800,750));
        for (int i = 0; i < 6; i++) {
            coinList.add(getGameWorld().spawn("coin",new SpawnData(1200,100*(1+i)).put("style",Config.list.get(i))));
            if (i<3){
                f_card_3.add(getGameWorld().spawn(Config.list_f.get(i),new SpawnData(100,500-200*i)));
                nobleList.add(getGameWorld().spawn("noble",new SpawnData(1400,100+(i*200))));
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 2; i <= 5; i++) {
                s_card_12.add(getGameWorld().spawn(Config.list_s.get(j),new SpawnData(200*i,500-200*j)));
            }
        }

    }
    @Override//根据鼠标点击操作不同的实体
    protected void onUpdate(double tpf) {
        mouse_x =getInput().mouseXWorldProperty().getValue();
        mouse_y =getInput().mouseYWorldProperty().getValue();
        if (clickThi==1){
            for (int i = 0; i <= 11; i++) {
                if (i<devCard.size()){
                    if(mouseDis(i,124,172,devCard)){
                        playerCall(i,devCard);
                        devCard.remove(i);
                        clickThi=0;break;
                    }
                }
                if(mouseDis(i,124,172,s_card_12)){
                    playerCall(i,s_card_12);
                    int l=(Integer)s_card_12.get(i).call("getLevel")==1?0:
                            (Integer)s_card_12.get(i).call("getLevel")==2?1:2;
                    f_card_3.get(l).call("setCardNumber");
                    f_card_3.get(l).call("showInfo");
                    int k=i<=3?0:i<=7?1:2;
                    s_card_12.set(i,getGameWorld().spawn(Config.list_s.get(k),new SpawnData(200*(i+2-k*4),500-200*k)));
                    clickThi=0;break;
                };
            }
        }else if (clickThi==2){
            for (int i = 0; i < 6; i++) {
                    if(mouseDis(i,90,75,coinList)){
                    player.call("addCoin",coinList.get(i).call("getStyle"),1);
                    coinList.get(i).call("setcoinNumber",1);
                    player.call("showInfo");
                    coinList.get(i).call("showInfo");
                    clickThi=0;break;
                }
            }
        }else if (clickThi==3) {
            for (int i = 0; i <= 11; i++) {
                if(mouseDis(i,124,172,s_card_12)){
                    if (devCard.size()<3){
                        s_card_12.get(i).setPosition(new Point2D(200*(devCard.size()+1),800));
                        int l=(Integer)s_card_12.get(i).call("getLevel")==1?0:
                                (Integer)s_card_12.get(i).call("getLevel")==2?1:2;
                        f_card_3.get(l).call("setCardNumber");
                        f_card_3.get(l).call("showInfo");
                        devCard.add(s_card_12.get(i));
                        int k=i<=3?0:i<=7?1:2;
                        s_card_12.set(i,getGameWorld().spawn(Config.list_s.get(k),new SpawnData(200*(i+2-k*4),500-200*k)));
                        clickThi=0;break;
                    }
                }
            }
        }else if (clickThi==4){
            for (int i = 0; i < nobleList.size(); i++) {
                    if(mouseDis(i,150,151,nobleList)){
                    player.call("addScore",(Integer)nobleList.get(i).call("getScore"));
                    nobleList.get(i).removeFromWorld();
                    nobleList.remove(i);
                    player.call("showInfo");
                    clickThi=0;break;
                };
            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean mouseDis(int a,int b, int c,List<Entity> entities){
        return mouse_x -entities.get(a).getPosition().getX()<b && mouse_x -entities.get(a).getPosition().getX()>0
                && mouse_y -entities.get(a).getPosition().getY()<c&& mouse_y -entities.get(a).getPosition().getY()>0;
    }
    public boolean initJudge(int i,List<Entity> entities,int a,int b){
        if (i<entities.size()){
            if (mouseDis(i,a,b,entities)){
                return true;
            }}return false;
    }
    public void playerCall(int i,List<Entity> entities){
        player.call("addScore",(Integer)entities.get(i).call("getScore"));
        player.call("addToken",(String)entities.get(i).call("getToken"));
        for (int j = 0; j < 5; j++) {
            player.call("cutCoin", Config.list.get(j),entities.get(i).call("numToken",Config.list.get(j)));
        }
        player.call("showInfo");
        entities.get(i).removeFromWorld();
    }
}
