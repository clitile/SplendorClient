package nz.proj;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.net.SocketClient;

import java.util.*;

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
    //ai是否操作
    int round=0;
    //Ai玩家
    List<Entity> ai_player;
    //真人玩家
    List<Entity> human_player;
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(Config.APP_HEIGHT);
        settings.setWidth(Config.APP_WIDTH);
        settings.setTitle("Splendor");
        settings.setVersion("1.0.1");
        settings.setAppIcon("fp_token-1.png");
        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu() {
                return new SplendorMainMenu();
            }
        });
    }
    //用来存储玩家获取的硬币，判断是三个不同的硬币或两个相同的硬币
    List<Point2D> num=new ArrayList<>();
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Click") {
            @Override
            protected void onAction() {
                mouse_x =getInput().mouseXWorldProperty().getValue();
                mouse_y =getInput().mouseYWorldProperty().getValue();
                List<Entity> entities=getGameWorld().getEntitiesInRange(
                        new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
                String activity=player.call("getActivity");
                if (activity.equals("getThreeCoin")){
                    //获取三枚不同的硬币
                    entities=getGameWorld().getEntitiesInRange(
                            new Rectangle2D(mouse_x-Config.COIN_WID,mouse_y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                    if (entities.size()!=0){
                        getCoin(3,entities.get(0),player,false,mouse_x,mouse_y);
                    }
                }else if (activity.equals("getTwoSameCoin")){
                    //获取两枚相同的硬币
                    entities=getGameWorld().getEntitiesInRange(
                            new Rectangle2D(mouse_x-Config.COIN_WID,mouse_y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                    if (entities.size()!=0){
                        getCoin(2,entities.get(0),player,false,mouse_x,mouse_y);
                    }
                }else if (activity.equals("getOneMidCard") &&entities.size()!=0){
                    //判断对中间的12张牌的实体操作
                    getOneCard("getOneMidCard",entities.get(0),player,false, SocketClient.getInstance().x, SocketClient.getInstance().y);
                }else if (activity.equals("getOneSaveCard") &&entities.size()!=0){
                    //对左下角的保留牌操作
                    getOneCard("getOneSaveCard",entities.get(0),player,false, SocketClient.getInstance().x, SocketClient.getInstance().y);
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
            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction("Get saved Card") {
            @Override
            protected void onAction() {
                mouse_x =getInput().mouseXWorldProperty().getValue();
                mouse_y =getInput().mouseYWorldProperty().getValue();
                List<Entity> entities=getGameWorld().getEntitiesInRange(
                        new Rectangle2D(mouse_x-Config.CARD_WID,mouse_y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
                String activity=player.call("getActivity");
                if (activity.equals("getSaveCard") && entities.size()!=0){
                    //获取保留卡和一枚黄金硬币
                    getSaveCard(entities.get(0),player, mouse_x, mouse_y);
                }
            }
        }, MouseButton.SECONDARY);
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
        human_player=new ArrayList<>();
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
        if (!SocketClient.getInstance().login){
            for (int i = 0; i < Config.MODE_SCENE.mode - 1; i++) {
                ai_player.add(getGameWorld().spawn("player",new SpawnData(1500,150*(i+1))));
            }
        } else {
            //创建人类player,显示玩家的信息
            for (int i = 0; i < Config.MODE_SCENE.mode - 1; i++) {
                human_player.add(getGameWorld().spawn("player",new SpawnData(1500,150*(i+1))));
            }
        }
        Config.MODE_SCENE.mode = 0;
    }
    boolean ai_round=false;
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("login", false);
        vars.put("online", false);
        vars.put("name", "");
        vars.put("match", false);
        vars.put("id", 0);
        vars.put("playersNames", new ArrayList<>());
        vars.put("mode", 0);
        vars.put("player_action", "选取你想进行的操作");
    }
    @Override
    protected void onUpdate(double tpf) {
        if (SocketClient.getInstance().roomStop) {
            FXGL.getGameController().gotoMainMenu();
            FXGL.getNotificationService().pushNotification("Other Players left the game");
            SocketClient.getInstance().roomStop = false;
        }
        if (SocketClient.getInstance().match && !getb("match")) {
            getNotificationService().pushNotification("Match Find");
            set("match", true);
        }
        if (!SocketClient.getInstance().login){
            if (ai_player.size()!=0){
                if (player.call("getActivity")=="" && !ai_round){
                    set("player_action", "选取你想进行的操作");
                    dealActPlayer(getGameScene());
                }
                if (ai_round){
                    label:
                    for (Entity entity : ai_player) {
                        //ai的操作
                        for (int j = 0; j < 12; j++) {
                            HashMap<String, Integer> hashMap = s_card_12.get(j).call("getMap");
                            List<String> coins = s_card_12.get(j).call("getCoins");
                            boolean numisnull = f_card_3.get(hashMap.get("cardLevel") - 1).call("numIsNull");
                            boolean jud = true;
                            for (String coin : coins) {
                                boolean c = entity.call("enoughCoin", coin, hashMap.get(coin));
                                if (!c) {
                                    jud = false;
                                }
                            }
                            //卡片数量不为0，且拥有足够的宝石
                            if (numisnull && jud) {
                                getOneCard("", s_card_12.get(j), entity, true, s_card_12.get(j).getX() + 1, s_card_12.get(j).getY() + 1);
                                break label;
                            }
                        }
                        int lp = 0;
                        for (int j = 0; j < 5; j++) {
                            int a = coinList.get(j).call("getNum");
                            if (a > 0 && lp < 3) {
                                getCoin(3, coinList.get(j), entity, true, coinList.get(j).getX() + 1, coinList.get(j).getY() + 1);
                                lp++;
                            }
                        }
                    }
                    ai_round=false;
                }
            }
        } else {
            if (human_player.size()!=0){
                if (SocketClient.getInstance().isThis && player.call("getActivity")=="") {
                    System.out.println(SocketClient.getInstance().name + "is true");
                    set("player_action", "选取你想进行的操作");
                    dealActPlayer(getGameScene());


                }else if(!SocketClient.getInstance().activity.equals("")){
                    if (round==human_player.size()){
                        round=0;
                    }
                    List<Entity> entities=getGameWorld().getEntitiesInRange(
                            new Rectangle2D(SocketClient.getInstance().x-Config.CARD_WID,SocketClient.getInstance().y-Config.CARD_HEI,Config.CARD_WID,Config.CARD_HEI));
                    if (Objects.equals(SocketClient.getInstance().activity, "getThreeCoin")){
                        //获取三枚不同的硬币
                        entities=getGameWorld().getEntitiesInRange(
                                new Rectangle2D(SocketClient.getInstance().x-Config.COIN_WID,SocketClient.getInstance().y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                        if (entities.size()!=0){
                            getCoin(3,entities.get(0),human_player.get(round),false,SocketClient.getInstance().x, SocketClient.getInstance().y);
                        }
                    } else if (Objects.equals(SocketClient.getInstance().activity, "getTwoSameCoin")){
                        //获取两枚相同的硬币
                        entities=getGameWorld().getEntitiesInRange(
                                new Rectangle2D(SocketClient.getInstance().x-Config.COIN_WID,SocketClient.getInstance().y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                        if (entities.size()!=0){
                            getCoin(2,entities.get(0),human_player.get(round),false,SocketClient.getInstance().x, SocketClient.getInstance().y);
                        }
                    }else if (Objects.equals(SocketClient.getInstance().activity, "getOneMidCard") &&entities.size()!=0){
                        //判断对中间的12张牌的实体操作
                        getOneCard("getOneMidCard",entities.get(0),human_player.get(round),false, SocketClient.getInstance().x, SocketClient.getInstance().y);
                    }else if (Objects.equals(SocketClient.getInstance().activity, "getOneSaveCard") &&entities.size()!=0){
                        //对左下角的保留牌操作
                        getOneCard("getOneSaveCard",entities.get(0),human_player.get(round),false, SocketClient.getInstance().x, SocketClient.getInstance().y);
                    }
                    round++;
                    SocketClient.getInstance().activity="";

                }
            }
        }
    }

    @Override
    protected void initUI() {
        Text action = addVarText("player_action", 700, 750);
        action.fontProperty().unbind();
        action.setFont(Font.font(25));
    }

    public static void main(String[] args) {
        System.out.println("main");
        launch(args);
    }

    //获取一枚硬币
    public void getCoin(int size,Entity entities,Entity player,boolean isAi,double mouse_x,double mouse_y){

        //获取鼠标点到位置的实体
        if ((mouse_x<=1100+Config.COIN_WID && mouse_x>=1100 && mouse_y>=100 && mouse_y<=500+Config.COIN_HEI || isAi)){
            int a=entities.call("getNum");
            boolean bool=size==3?
                    //获取三枚不同的硬币
                    !num.contains(entities.getPosition())&&a>0:
                    //获取两枚一样的硬币
                    (num.contains(entities.getPosition()) || num.size()==0)&&a+num.size()>=4;
            if (bool){
                //玩家添加硬币
                String s=entities.call("getCoinName");
                player.call("addCoin",s);
                player.call("showInfo");
                //硬币数量减少
                entities.call("cutcoinNumber");
                entities.call("showInfo");

                num.add(entities.getPosition());

                if (SocketClient.getInstance().login && num.size()!=size) {
                    Bundle act = new Bundle("act");
                    act.put("name", SocketClient.getInstance().name);
                    act.put("x", mouse_x);
                    act.put("y", mouse_y);
                    act.put("id", SocketClient.getInstance().id);
                    act.put("activity", size == 2 ? "getTwoSameCoin" : "getThreeCoin");
                    SocketClient.getInstance().send(act);
                }
            }
        }
        if (num.size()==size){
            player.call("setActivity","");
            num=new ArrayList<>();

            if (SocketClient.getInstance().login) {
                SocketClient.getInstance().isThis=false;

                Bundle roundOver = new Bundle("roundOver");
                roundOver.put("name", SocketClient.getInstance().name);
                roundOver.put("x", mouse_x);
                roundOver.put("y", mouse_y);
                roundOver.put("id", SocketClient.getInstance().id);
                roundOver.put("activity", size == 2 ? "getTwoSameCoin" : "getThreeCoin");
                SocketClient.getInstance().send(roundOver);
            } else ai_round = true;
        }
    }
    //获取一张卡牌
    public void getOneCard(String actname,Entity entities,Entity player,boolean isAi, double mouse_x, double mouse_y){
        boolean ra= actname.equals("getOneMidCard") ?
                //中间的12张牌
                mouse_x<=900+Config.CARD_WID && mouse_x>=300 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI:
                //坐下的保留牌
                mouse_x<=500+Config.CARD_WID && mouse_x>=100 && mouse_y>=800 && mouse_y<=800+Config.CARD_HEI;
        if (ra||isAi){
            HashMap<String,Integer> hashMap=entities.call("getMap");
            List<String> coins=entities.call("getCoins");
            boolean numisnull=f_card_3.get(hashMap.get("cardLevel")-1).call("numIsNull");
            boolean jud=true;
            for (String coin : coins) {
                boolean c = player.call("enoughCoin", coin, hashMap.get(coin));
                if (!c) {
                    jud = false;
                }
            }
            //判断是否点到实体，卡片数量不为0，且拥有足够的宝石
            if (numisnull &&jud){
                HashMap<String,Integer> tokenMap=player.call("getMapToken");

                for (String coin : coins) {
                    //扣除玩家的硬币
                    player.call("cutCoin", coin, hashMap.get(coin) - tokenMap.get(coin));
                    //买卡时还回硬币
                    coinList.get(Config.list.indexOf(coin)).call("addCoin", hashMap.get(coin) - tokenMap.get(coin));
                    coinList.get(Config.list.indexOf(coin)).call("showInfo");
                }
                //玩家获得分数和宝石
                player.call("addTokenAndScore","score",hashMap.get("score"));
                player.call("addTokenAndScore",entities.call("getGiveToken"),1);
                player.call("showInfo");
                //添加保留卡
                if (actname.equals("getOneSaveCard")){
                    System.out.println("111111111111111111");
                    List<Entity> saveList=player.call("getSaveCard");
                    saveList.remove(entities);
                    for (int i = 0; i < saveList.size(); i++) {
                        saveList.get(i).setPosition(200*i+100,800);
                    }player.call("setSaveCard",saveList);
                }else {
                    //对最左边的三张等级牌操作
                    System.out.println("222222222222222222");
                    f_card_3.get(hashMap.get("cardLevel")-1).call("cutCardNumber");
                    f_card_3.get(hashMap.get("cardLevel")-1).call("showInfo");
                    //卡片替换成新的同等级的牌
                    s_card_12.set(s_card_12.indexOf(entities),
                            getGameWorld().spawn(entities.call("getClevel"),
                                    new SpawnData(entities.getX(),entities.getY())));
                }
                entities.removeFromWorld();
                player.call("setActivity","");
                ai_round=true;
                SocketClient.getInstance().isThis=false;
                //发信息，获取一张卡牌
                //改下一个人操作了
                if (SocketClient.getInstance().login) {
                    Bundle roundOver = new Bundle("roundOver");
                    roundOver.put("name", SocketClient.getInstance().name);
                    roundOver.put("x", mouse_x);
                    roundOver.put("y", mouse_y);
                    roundOver.put("id", SocketClient.getInstance().id);
                    roundOver.put("activity",mouse_y>=800?"getOneSaveCard":"getOneMidCard");
                    SocketClient.getInstance().send(roundOver);
                }
            }
        }
    }
    //获取保留卡和一枚黄金硬币
    public void getSaveCard(Entity entities,Entity player, double mouse_x, double mouse_y){

        List<Entity> saveList=player.call("getSaveCard");
        if (mouse_x<=900+Config.CARD_WID && mouse_x>=300 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI&&saveList.size()<=2) {
            HashMap<String,Integer> hashMap=entities.call("getMap");
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
            s_card_12.set(s_card_12.indexOf(entities),
                    getGameWorld().spawn(entities.call("getClevel"),new SpawnData(entities.getX(),entities.getY())));
            //玩家操作
            saveList.add(entities);
            for (int i = 0; i < saveList.size(); i++) {
                saveList.get(i).setPosition(200*i+100,800);
            }player.call("setSaveCard",saveList);
        }
        player.call("setActivity","");
        ai_round=true;

        SocketClient.getInstance().isThis=false;
        //发信息，玩家获取保留卡和一枚黄金硬币
        //改下一个人操作了
        if (SocketClient.getInstance().login) {
            Bundle roundOver = new Bundle("roundOver");
            roundOver.put("name", SocketClient.getInstance().name);
            roundOver.put("x", mouse_x);
            roundOver.put("y", mouse_y);
            roundOver.put("id", SocketClient.getInstance().id);
            roundOver.put("activity","getSaveCard");
            SocketClient.getInstance().send(roundOver);
        }
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
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        player.call("setActivity",act_list.get(t1.intValue()));
                        set("player_action", act_list.get(t1.intValue()));
                        getGameScene().removeChild(choicebox);
                    }
                });
        gameScene.addChild(choicebox);
    }
}
