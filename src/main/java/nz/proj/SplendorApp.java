package nz.proj;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.almasb.fxgl.dsl.FXGL.*;

public class SplendorApp extends GameApplication {
    Entity player;
    List<Entity> f_card_3;
    List<Entity> s_card_12;
    List<Entity> coinList;
    List<Entity> nobleList;

    double mouse_x;
    double mouse_y;
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(Config.APP_HEIGHT);
        settings.setWidth(Config.APP_WIDTH);
        settings.setTitle("Splendor");
        settings.setVersion("1.0.1");
        settings.setAppIcon("fp_token-1.png");

//        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);

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
            List<Entity> entities;
            if (mouse_x<=900+Config.CARD_WID && mouse_x>=300 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI){
                entities=getGameWorld().getEntitiesInRange(new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
                if (entities.size()!=0){
                    HashMap<String,Integer> hashMap=entities.get(0).call("getMap");
                    List<String> coins=entities.get(0).call("getCoins");
                    //对玩家进行操作
                    player.call("addTokenAndScore","score",hashMap.get("score"));
                    player.call("addTokenAndScore",entities.get(0).call("getGiveToken"),1);
                    player.call("cutCoin",coins.get(0),hashMap.get(coins.get(0)));
                    player.call("cutCoin",coins.get(1),hashMap.get(coins.get(1)));
                    player.call("cutCoin",coins.get(2),hashMap.get(coins.get(2)));
                    player.call("showInfo");
                    //对最右边的三张操作
                    f_card_3.get(hashMap.get("cardLevel")-1).call("cutCardNumber");
                    f_card_3.get(hashMap.get("cardLevel")-1).call("showInfo");
                    //替换成新的
                    entities.get(0).removeFromWorld();
                    s_card_12.set(s_card_12.indexOf(entities.get(0)),
                            getGameWorld().spawn(entities.get(0).call("getClevel"),new SpawnData(entities.get(0).getX(),entities.get(0).getY())));

                }
            }else if (mouse_x<=1100+Config.COIN_WID && mouse_x>=1100 && mouse_y>=100 && mouse_y<=600+Config.COIN_HEI){
                entities=getGameWorld().getEntitiesInRange(new Rectangle2D(mouse_x-Config.COIN_WID,mouse_y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                if (entities.size()!=0){
                    //对玩家操作
                    String s=entities.get(0).call("getCoinName");
                    player.call("addCoin",s);
                    player.call("showInfo");
                    //对硬币操作
                    entities.get(0).call("cutcoinNumber");
                    entities.get(0).call("showInfo");

                }
            }else if (mouse_x<=1300+Config.NOBLE_WID && mouse_x>=1300){
                entities=getGameWorld().getEntitiesInRange(new Rectangle2D(mouse_x-Config.NOBLE_WID,mouse_y-Config.NOBLE_HEI,Config.NOBLE_WID,Config.NOBLE_HEI));
                if (entities.size()!=0){
                    int i=entities.get(0).call("getScore");
                    player.call("addTokenAndScore","score",i);
                    player.call("showInfo");

                    //对贵族操作
                    nobleList.remove(entities.get(0));
                    entities.get(0).removeFromWorld();
                }
            }else if (mouse_x<=500+Config.CARD_WID && mouse_x>=100 && mouse_y>=800 && mouse_y<=800+Config.CARD_HEI) {
                entities=getGameWorld().getEntitiesInRange(new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
                if (entities.size()!=0){
                    List<Entity> saveList=player.call("getSaveCard");
                    HashMap<String,Integer> hashMap=entities.get(0).call("getMap");
                    List<String> coins=entities.get(0).call("getCoins");
                    //对玩家进行操作
                    player.call("addTokenAndScore","score",hashMap.get("score"));
                    player.call("addTokenAndScore",entities.get(0).call("getGiveToken"),1);
                    player.call("cutCoin",coins.get(0),hashMap.get(coins.get(0)));
                    player.call("cutCoin",coins.get(1),hashMap.get(coins.get(1)));
                    player.call("cutCoin",coins.get(2),hashMap.get(coins.get(2)));
                    player.call("showInfo");
                    //替换成新的
                    saveList.remove(entities.get(0));
                    entities.get(0).removeFromWorld();
                    for (int i = 0; i < saveList.size(); i++) {
                        saveList.get(i).setPosition(200*i+100,800);
                    }player.call("setSaveCard",saveList);

                }
            }
        });

        onBtnDown(MouseButton.SECONDARY,()->{
            if (mouse_x<=900+Config.CARD_WID && mouse_x>=300 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI) {
                List<Entity> saveList=player.call("getSaveCard");
                List<Entity> entities;
                entities=getGameWorld().getEntitiesInRange(new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));

                if (saveList.size()<=2 && entities.size()!=0){
                    HashMap<String,Integer> hashMap=entities.get(0).call("getMap");

                    //对最左边的三张操作
                    f_card_3.get(hashMap.get("cardLevel")-1).call("cutCardNumber");
                    f_card_3.get(hashMap.get("cardLevel")-1).call("showInfo");
                    //替换成新的
                    s_card_12.set(s_card_12.indexOf(entities.get(0)),
                            getGameWorld().spawn(entities.get(0).call("getClevel"),new SpawnData(entities.get(0).getX(),entities.get(0).getY())));
                    //玩家操作
                    saveList.add(entities.get(0));
                    for (int i = 0; i < saveList.size(); i++) {
                        saveList.get(i).setPosition(200*i+100,800);
                    }player.call("setSaveCard",saveList);
                }
            }

        });
    }
    @Override
    protected void onPreInit() {
        getAssetLoader().loadImage("cards_620_860.png");
        getAssetLoader().loadImage("nobles.png");
        getAssetLoader().loadImage("cards_372_172.png");


    }
    @Override
    protected void initGame() {
        getGameScene().setBackgroundRepeat(image("backg (6).png"));
        getGameWorld().addEntityFactory(new SplendorFactory());
        f_card_3=new ArrayList<>();
        s_card_12=new ArrayList<>();
        coinList=new ArrayList<>();
        nobleList=new ArrayList<>();
        player = getGameWorld().spawn("player",new SpawnData(800,750));
        for (int i = 0; i < 6; i++) {
            coinList.add(getGameWorld().spawn("coin",new SpawnData(1100,100*(1+i)).put("style",Config.list.get(i))));
            if (i<3){
                f_card_3.add(getGameWorld().spawn(Config.list_f.get(i),new SpawnData(100,500-200*i)));
                nobleList.add(getGameWorld().spawn("noble",new SpawnData(1300,100+(i*200))));
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 2; i <= 5; i++) {
                s_card_12.add(getGameWorld().spawn(Config.list_s.get(j),new SpawnData(200*i-100,500-200*j)));
            }
        }

    }

    @Override
    protected void onUpdate(double tpf) {
        mouse_x =getInput().mouseXWorldProperty().getValue();
        mouse_y =getInput().mouseYWorldProperty().getValue();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
