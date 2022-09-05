package nz.proj;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SplendorApp extends GameApplication {
    //玩家1
    Entity player;
    //最左边的三张标志卡
    List<Entity> f_card_3;
    //中间的12张卡
    List<Entity> s_card_12;
    //硬币
    List<Entity> coinList;
    //贵族卡
    List<Entity> nobleList;
    //鼠标坐标
    double mouse_x;
    double mouse_y;
    //输出玩家当前的活动
    Text player_action;
    //ai是否操作
    int round=0;
    //Ai玩家
    List<Entity> ai_player;
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
    List<Point2D> num=new ArrayList<>();
    @Override
    protected void initInput() {
        onBtnDown(MouseButton.PRIMARY,()->{
            List<Entity> entities=getGameWorld().getEntitiesInRange(
                    new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
            String activity=player.call("getActivity");
            if (activity=="getThreeCoin"){
                //获取三枚不同的硬币
                entities=getGameWorld().getEntitiesInRange(
                        new Rectangle2D(mouse_x-Config.COIN_WID,mouse_y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                getCoin(3,entities,player);
            }else if (activity=="getTwoSameCoin"){
                //获取两枚相同的硬币
                entities=getGameWorld().getEntitiesInRange(
                        new Rectangle2D(mouse_x-Config.COIN_WID,mouse_y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                getCoin(2,entities,player);
            }else if (activity=="getOneMidCard"){
                //判断对中间的12张牌的实体操作
                getOneCard("getOneMidCard",entities,player);

            }else if (activity=="getOneSaveCard"){
                //对左下角的保留牌操作
                getOneCard("getOneSaveCard",entities,player);
            }
            //对贵族实体操作,贵族自动归入门下
//            else if (mouse_x<=1300+Config.NOBLE_WID && mouse_x>=1300){
//                entities=getGameWorld().getEntitiesInRange(new Rectangle2D(mouse_x-Config.NOBLE_WID,mouse_y-Config.NOBLE_HEI,Config.NOBLE_WID,Config.NOBLE_HEI));
//                if (entities.size()!=0){
//                    int i=entities.get(0).call("getScore");
//                    player.call("addTokenAndScore","score",i);
//                    player.call("showInfo");
//
//                    //对贵族操作
//                    nobleList.remove(entities.get(0));
//                    entities.get(0).removeFromWorld();
//                }
//
//            }

        });
        //添加保留卡
        onBtnDown(MouseButton.SECONDARY,()->{
            List<Entity> entities=getGameWorld().getEntitiesInRange(
                    new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
            String activity=player.call("getActivity");
            if (activity=="getSaveCard"){
                //获取保留卡和一枚黄金硬币
                getSaveCard(entities,player);
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
        ai_player=new ArrayList<>();
        player = getGameWorld().spawn("player",new SpawnData(1000,750));
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

        player_action = new Text(0,40,"请选择你想游玩的AI人数");
        player_action.setLayoutX(700);
        player_action.setLayoutY(750);
        player_action.setStyle("-fx-font-size: 25;");
        getGameScene().addChild(player_action);
        ///////////////////////////////////////////

        var choicebox=getUIFactoryService().newChoiceBox(FXCollections.observableArrayList(
                "2 player", "3 player", "4 player"));
        choicebox.setLayoutX(700);
        choicebox.setLayoutY(850);
        choicebox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1){
                        round=t1.intValue()+2;
                        for (int i = 0; i < round-1; i++) {
                            ai_player.add(getGameWorld().spawn("player",new SpawnData(1500,150*(i+1))));
                        }
                        System.out.println(ai_player.size());
                        getGameScene().removeChild(choicebox);
                    }
                });
        getGameScene().addChild(choicebox);

    }
    boolean ai_round=false;
    @Override
    protected void onUpdate(double tpf) {
        mouse_x =getInput().mouseXWorldProperty().getValue();
        mouse_y =getInput().mouseYWorldProperty().getValue();
        if (round!=0){
            if (player.call("getActivity")=="" && !ai_round){

                player_action.setText("选取你想进行的操作");
                dealActPlayer(getGameScene());
            }
            if (ai_round){
                for (int i = 0; i < ai_player.size(); i++) {
                    System.out.println("i am player"+i);
                }ai_round=false;
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }



    //获取一枚硬币
    public void getCoin(int size,List<Entity> entities,Entity player){

        //获取鼠标点到位置的实体
        if (mouse_x<=1100+Config.COIN_WID && mouse_x>=1100 && mouse_y>=100 && mouse_y<=500+Config.COIN_HEI&&entities.size()!=0){
            int a=entities.get(0).call("getNum");
            boolean bool=size==3?
                    //获取三枚不同的硬币
                    !num.contains(entities.get(0).getPosition())&&a>0:
                    //获取两枚一样的硬币
                    (num.contains(entities.get(0).getPosition()) || num.size()==0)&&a+num.size()>=4;
            if (bool){
                //玩家添加硬币
                String s=entities.get(0).call("getCoinName");
                player.call("addCoin",s);
                player.call("showInfo");
                //硬币数量减少
                entities.get(0).call("cutcoinNumber");
                entities.get(0).call("showInfo");

                num.add(entities.get(0).getPosition());
            }
        }
        if (num.size()==size){
            player.call("setActivity","");
            num=new ArrayList<>();
            ai_round=true;
        }
    }
    //获取一张卡牌
    public void getOneCard(String actname,List<Entity> entities,Entity player){
        boolean ra=actname=="getOneMidCard"?
                //中间的12张牌
                mouse_x<=900+Config.CARD_WID && mouse_x>=300 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI:
                //坐下的保留牌
                mouse_x<=500+Config.CARD_WID && mouse_x>=100 && mouse_y>=800 && mouse_y<=800+Config.CARD_HEI;
        if (ra && entities.size()!=0){
            HashMap<String,Integer> hashMap=entities.get(0).call("getMap");
            List<String> coins=entities.get(0).call("getCoins");
            boolean numisnull=f_card_3.get(hashMap.get("cardLevel")-1).call("numIsNull");

            boolean jud=true;
            for (int i = 0; i < coins.size(); i++) {
                boolean c=player.call("enoughCoin",coins.get(i),hashMap.get(coins.get(i)));
                if (!c){
                    jud=false;
                }
            }
            //判断是否点到实体，卡片数量不为0，且拥有足够的宝石
            if (numisnull &&jud){
                HashMap<String,Integer> tokenMap=player.call("getMapToken");

                for (int i = 0; i < coins.size(); i++) {
                    //扣除玩家的硬币
                    player.call("cutCoin",coins.get(i),hashMap.get(coins.get(i))-tokenMap.get(coins.get(i)));
                    //买卡时还回硬币
                    coinList.get(Config.list.indexOf(coins.get(i))).call("addCoin",hashMap.get(coins.get(i))-tokenMap.get(coins.get(i)));
                    coinList.get(Config.list.indexOf(coins.get(i))).call("showInfo");
                }
                //玩家获得分数和宝石
                player.call("addTokenAndScore","score",hashMap.get("score"));
                player.call("addTokenAndScore",entities.get(0).call("getGiveToken"),1);
                player.call("showInfo");
                //添加保留卡
                if (actname=="getOneSaveCard"){
                    List<Entity> saveList=player.call("getSaveCard");
                    saveList.remove(entities.get(0));
                    for (int i = 0; i < saveList.size(); i++) {
                        saveList.get(i).setPosition(200*i+100,800);
                    }player.call("setSaveCard",saveList);
                }else {
                    //对最右边的三张等级牌操作
                    f_card_3.get(hashMap.get("cardLevel")-1).call("cutCardNumber");
                    f_card_3.get(hashMap.get("cardLevel")-1).call("showInfo");
                    //卡片替换成新的同等级的牌
                    s_card_12.set(s_card_12.indexOf(entities.get(0)),
                            getGameWorld().spawn(entities.get(0).call("getClevel"),
                                    new SpawnData(entities.get(0).getX(),entities.get(0).getY())));
                }
                entities.get(0).removeFromWorld();
            }
            player.call("setActivity","");
            ai_round=true;
        }

    }
    //获取保留卡和一枚黄金硬币
    public void getSaveCard(List<Entity> entities,Entity player){

        List<Entity> saveList=player.call("getSaveCard");
        if (mouse_x<=900+Config.CARD_WID && mouse_x>=300 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI&&
                entities.size()!=0&&saveList.size()<=2) {
            HashMap<String,Integer> hashMap=entities.get(0).call("getMap");
            //黄金硬币减少一枚
            coinList.get(5).call("cutcoinNumber");
            coinList.get(5).call("showInfo");
            //玩家增加一枚黄金硬币
            player.call("addCoin","goldToken");
            player.call("showInfo");
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
        player.call("setActivity","");
        ai_round=true;
    }
    //下拉框选择玩家选择的活动
    public void dealActPlayer(GameScene gameScene){
        List<String> act_list=new ArrayList<>(){{
            add("getThreeCoin");
            add("getTwoSameCoin");
            add("getOneMidCard");
            add("getOneSaveCard");
            add("getSaveCard");
        }};

        var choicebox=getUIFactoryService().newChoiceBox(FXCollections.observableArrayList(
                "获取三枚不同的硬币", "获取两枚相同的硬币", "购买中间的牌","购买一张保留牌","获取保留卡和一枚黄金硬币"));
        choicebox.setLayoutX(700);
        choicebox.setLayoutY(800);
        choicebox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1){
                        player.call("setActivity",act_list.get(t1.intValue()));
                        player_action.setText(act_list.get(t1.intValue()));

                        getGameScene().removeChild(choicebox);
                    }
                });
        gameScene.addChild(choicebox);


    }

}
