package nz.sample;




import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import nz.proj.Config;
import nz.proj.SplendorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {
    Entity player;
    List<Entity> f_card_3;
    List<Entity> s_card_12;
    List<Entity> coinList;
    List<Entity> devCard;
    List<Entity> nobleList;

    double mouse_x;
    double mouse_y;
    int replaceCard=0;
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(Config.HEIGHT);
        settings.setWidth(Config.WIDTH);
        settings.setTitle("Splendor");
        settings.setVersion("1.0.1");
        settings.setAppIcon("fp_token-1.png");

//        settings.setMainMenuEnabled(true);
//        settings.setSceneFactory(new SceneFactory(){
//            @Override
//            public FXGLMenu newMainMenu() {
//                return new SplendorMainMenu();
//            }
//        });

    }

    @Override
    protected void initInput() {
        onBtnDown(MouseButton.PRIMARY,()->{
            for (int i = 0; i <= 11; i++){
                if(mouseDis(i,124,172,s_card_12)){
                    replaceCard=1;break;
                }
                if (i<devCard.size()){
                    if(mouseDis(i,124,172,devCard)){
                        replaceCard=1;break;
                    }}
                if (i<nobleList.size()){
                    if(mouseDis(i,150,151,nobleList)){
                        replaceCard=4;break;
                    }}
                if (i<coinList.size()){
                    if(mouseDis(i,90,75,coinList)){
                        replaceCard=2;break;
                    }}
            }});
        onBtnDown(MouseButton.SECONDARY,()->{
            for (int i = 0; i <= 11; i++) {
                if(mouseDis(i,124,172,s_card_12)){
                    replaceCard=3;break;
                }
            }});
    }
    @Override
    protected void onPreInit() {
        getAssetLoader().loadImage("cards_620_860.png");
        getAssetLoader().loadImage("nobles.png");
        getAssetLoader().loadImage("cards_372_172.png");
    }
    @Override
    protected void initGame() {
        entityBuilder().viewWithBBox(texture("backg (1).jpg",Config.WIDTH,Config.HEIGHT)).buildAndAttach();
        f_card_3=new ArrayList<>();
        s_card_12=new ArrayList<>();
        coinList=new ArrayList<>();
        nobleList=new ArrayList<>();
        devCard=new ArrayList<>();
        getGameWorld().addEntityFactory(new SplendorFactory());
        player = getGameWorld().spawn("player",new SpawnData(800,750));

        f_card_3.add(getGameWorld().spawn("f_level1",new SpawnData(100,500)));
        f_card_3.add(getGameWorld().spawn("f_level2",new SpawnData(100,300)));
        f_card_3.add(getGameWorld().spawn("f_level3",new SpawnData(100,100)));

        for (int i = 0; i < 6; i++) {
            coinList.add(getGameWorld().spawn("coin",new SpawnData(1200,100*(1+i)).put("style",Config.list.get(i))));
        }

        for (int i = 0; i < 3; i++) {
            nobleList.add(getGameWorld().spawn("noble",new SpawnData(1400,100+(i*200))));
        }


        for (int i = 2; i <= 5; i++) {
            s_card_12.add(getGameWorld().spawn("level1",new SpawnData(200*i,500)));
        }
        for (int i = 2; i <= 5; i++) {
            s_card_12.add(getGameWorld().spawn("level2",new SpawnData(200*i,300)));
        }
        for (int i = 2; i <= 5; i++) {
            s_card_12.add(getGameWorld().spawn("level3",new SpawnData(200*i,100)));
        }

    }

    @Override
    protected void onUpdate(double tpf) {
        mouse_x =getInput().mouseXWorldProperty().getValue();
        mouse_y =getInput().mouseYWorldProperty().getValue();
        if (replaceCard==1){
            for (int i = 0; i < devCard.size(); i++) {
                if(mouseDis(i,124,172,devCard)){
                    player.call("addScore",(Integer)devCard.get(i).call("getScore"));
                    player.call("addToken",(String)devCard.get(i).call("getToken"));

                    for (int j = 0; j < 5; j++) {
                        player.call("cutCoin", Config.list.get(j),devCard.get(i).call("numToken",Config.list.get(j)));
                    }
                    player.call("showInfo");

                    devCard.get(i).removeFromWorld();
                    devCard.remove(i);
                    replaceCard=0;break;
                }
            }

            for (int i = 0; i <= 11; i++) {
                if(mouseDis(i,124,172,s_card_12)){
                    player.call("addScore",(Integer)s_card_12.get(i).call("getScore"));
                    player.call("addToken",(String)s_card_12.get(i).call("getToken"));

                    for (int j = 0; j < 5; j++) {
                        player.call("cutCoin", Config.list.get(j),s_card_12.get(i).call("numToken",Config.list.get(j)));
                    }
                    player.call("showInfo");
                    s_card_12.get(i).removeFromWorld();

                    if ((Integer)s_card_12.get(i).call("getLevel")==1){
                        f_card_3.get(0).call("setCardNumber");
                        f_card_3.get(0).call("showInfo");

                    }else if ((Integer)s_card_12.get(i).call("getLevel")==2){
                        f_card_3.get(1).call("setCardNumber");
                        f_card_3.get(1).call("showInfo");

                    }else {
                        f_card_3.get(2).call("setCardNumber");
                        f_card_3.get(2).call("showInfo");

                    }
                    if (i<=3){
                        s_card_12.set(i,getGameWorld().spawn("level1",new SpawnData(200*(i+2),500)));
                    }else if(i<=7){
                        s_card_12.set(i,getGameWorld().spawn("level2",new SpawnData(200*(i-2),300)));
                    }else{
                        s_card_12.set(i,getGameWorld().spawn("level3",new SpawnData(200*(i-6),100)));
                    }
                    replaceCard=0;break;
                };
            }
        }else if (replaceCard==2){
            for (int i = 0; i < 6; i++) {
                if(mouseDis(i,90,75,coinList)){
                    player.call("addCoin",coinList.get(i).call("getStyle"),1);
                    coinList.get(i).call("setcoinNumber",1);
                    player.call("showInfo");
                    coinList.get(i).call("showInfo");
                    replaceCard=0;break;
                }
            }
        }else if (replaceCard==3) {
            for (int i = 0; i <= 11; i++) {
                if(mouseDis(i,124,172,s_card_12)){
                    if (devCard.size()<3){

                        s_card_12.get(i).setPosition(new Point2D(200*(devCard.size()+1),800));
                        if ((Integer)s_card_12.get(i).call("getLevel")==1){
                            f_card_3.get(0).call("setCardNumber");
                            f_card_3.get(0).call("showInfo");

                        }else if ((Integer)s_card_12.get(i).call("getLevel")==2){
                            f_card_3.get(1).call("setCardNumber");
                            f_card_3.get(1).call("showInfo");

                        }else {
                            f_card_3.get(2).call("setCardNumber");
                            f_card_3.get(2).call("showInfo");

                        }
                        devCard.add(s_card_12.get(i));
                        if (i<=3){
                            s_card_12.set(i,getGameWorld().spawn("level1",new SpawnData(200*(i+2),500)));
                        }else if(i<=7){
                            s_card_12.set(i,getGameWorld().spawn("level2",new SpawnData(200*(i-2),300)));
                        }else{
                            s_card_12.set(i,getGameWorld().spawn("level3",new SpawnData(200*(i-6),100)));
                        }
                        replaceCard=0;break;
                    }

                }
            }
        }else if (replaceCard==4){
            for (int i = 0; i < nobleList.size(); i++) {
                if(mouseDis(i,150,151,nobleList)){
                    player.call("addScore",(Integer)nobleList.get(i).call("getScore"));
                    nobleList.get(i).removeFromWorld();
                    nobleList.remove(i);
                    player.call("showInfo");
                    replaceCard=0;break;
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

}
