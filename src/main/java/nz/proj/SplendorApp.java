package nz.proj;


import com.almasb.fxgl.app.GameApplication;
import javafx.application.Platform;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.net.SocketClient;
import nz.ui.UIFactory;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SplendorApp extends GameApplication {
	
	
    //玩家1 
    Entity player;
  
    //玩家1 的内容
    Entity player_content;
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
    	
        SocketClient.getInstance().match = false;
        settings.setHeight(Config.APP_HEIGHT);
        settings.setWidth(Config.APP_WIDTH);
        settings.setTitle("Splendor");
        settings.setVersion("1.0.1");
        settings.setAppIcon("fp_token-1.png");
        settings.setFullScreenAllowed(true);
//      settings.setFullScreenFromStart(true);
        settings.setMainMenuEnabled(true);
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu() {
                return new SplendorMainMenu();
            }
        });
        

       
    }

    //用来存储玩家获取的硬币，判断是三个不同的硬币或两个相同的硬币
    List<Point2D> num=new ArrayList<>();
    //鼠标对应动作
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Click") {
            @Override
            protected void onActionEnd() {
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
                    getOneCard("getOneMidCard",entities.get(0),player,false, mouse_x,mouse_y);
                }else if (activity.equals("getOneSaveCard") &&entities.size()!=0){
                    //对左下角的保留牌操作
                    getOneCard("getOneSaveCard",entities.get(0),player,false,mouse_x,mouse_y);
                }

            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction("Get saved Card") {
            @Override
            protected void onActionEnd() {
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
    
    //预先加载
    @Override
    protected void onPreInit() {
        getAssetLoader().loadImage("cards_620_860.png");
        getAssetLoader().loadImage("nobles.png");
        getAssetLoader().loadImage("cards_372_172.png");
        getAssetLoader().loadImage("S-castle.png");
    }
    
    
    @Override
    protected void initGame() {
    	
        getGameWorld().addEntityFactory(new SplendorFactory());
        
        f_card_3=new ArrayList<>();
        //中间的十二张牌 --- 卡片上需要的不同颜色的宝石用不同颜色的圆形底+数字呈现，最好旁边再加上宝石图片，上方加上有点透明的矩形
        s_card_12=new ArrayList<>();
        // 硬币 --- 代表 ？？？
        coinList=new ArrayList<>();
        //贵族卡 --- 代表 右边三张卡，需要换成不同的图片背景
        nobleList=new ArrayList<>();
        
        ai_player=new ArrayList<>();

        human_player=new ArrayList<>();
        Platform.runLater(new Runnable() {
    		@Override
        	public void run() {
    			ingame();
    		}
        });
        
        
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
        vars.put("player_action", "Choose one action :)");
    }
    @Override
    protected void onUpdate(double tpf) {
        if (SocketClient.getInstance().roomStop) {
            Config.MODE_SCENE.mode = 0;
            SocketClient.getInstance().match = false;
            set("match", false);
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
                    set("player_action", "Choose one action :)");
                   
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
                if (SocketClient.getInstance().round_begin && player.call("getActivity")=="") {
                    set("player_action", "Choose one action :)");
                    dealActPlayer(getGameScene());
                } else if (!SocketClient.getInstance().round_begin && !SocketClient.getInstance().activity.equals("")){
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
                    }else if (Objects.equals(SocketClient.getInstance().activity, "getOneSaveCard")){
                        //对左下角的保留牌操作
                        entities=getGameWorld().getEntitiesInRange(
                                new Rectangle2D(SocketClient.getInstance().x-Config.CARD_WID+human_player.get(round).getX(),SocketClient.getInstance().y-Config.CARD_HEI+human_player.get(round).getY(),Config.CARD_WID,Config.CARD_HEI));
                        if (entities.size()!=0){
                            getOneCard("getOneSaveCard",entities.get(0),human_player.get(round),false, SocketClient.getInstance().x + human_player.get(round).getX(), SocketClient.getInstance().y + human_player.get(round).getY());
                        }
                    } else if (Objects.equals(SocketClient.getInstance().activity, "getSaveCard") &&entities.size()!=0) {
                        getSaveCard(entities.get(0),human_player.get(round), SocketClient.getInstance().x, SocketClient.getInstance().y);
                    }
                    SocketClient.getInstance().activity = "";
                }
            }
        }
    }

    @Override
    protected void initUI() {
        Text action = addVarText("player_action", 1570, 780);
        action.fontProperty().unbind();
        action.setFont(Font.font(25));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public void ingame() {
    	spawn("back-ingame");
        
        player = getGameWorld().spawn("player",new SpawnData(400,950));
        
        BorderPane borderpane = new BorderPane();
        
        borderpane.setPrefHeight(215);
        borderpane.setPrefWidth(200);
        borderpane.setStyle("-fx-background-image: url('assets/textures/emm1.png')");
        borderpane.setLayoutX(0);
        borderpane.setLayoutY(800);
        
        int imageContent = ModeScene.current;
        String[] imageURLs = ModeScene.imageURLs;
        ImageView imageview = new ImageView();
        Image image = new Image(imageURLs[imageContent]);
        imageview.setImage(image);
        imageview.setFitWidth(120);
        imageview.setFitHeight(160);
        borderpane.setCenter(imageview);
        
        FXGL.addUINode(borderpane);
        
        BorderPane namepane = new BorderPane();
        namepane.setPrefHeight(61);
        namepane.setPrefWidth(101);
        namepane.setStyle("-fx-background-image: url('assets/textures/nameCard-you.png')");
        namepane.setLayoutX(50);
        namepane.setLayoutY(975);
        FXGL.addUINode(namepane);
        
        
        for (int i = 0; i < 6; i++) {
            coinList.add(getGameWorld().spawn("coin",new SpawnData(1500,100*(1+i)).put("style",Config.list.get(i))));
            if (i<3){
                f_card_3.add(getGameWorld().spawn(Config.list_f.get(i),new SpawnData(450,470-200*i)));
                nobleList.add(getGameWorld().spawn("noble",new SpawnData(1700,200*i+100)));
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 2; i <= 5; i++) {
                s_card_12.add(getGameWorld().spawn(Config.list_s.get(j),new SpawnData(300+194*i,500-200*j)));
            }
        }
        
        
        if (!SocketClient.getInstance().login){
            for (int i = 0; i < Config.MODE_SCENE.mode - 1; i++) {
                ai_player.add(getGameWorld().spawn("otherPlayers",new SpawnData(0,250*i+50)));
            }
        } else {
            //创建人类player,显示玩家的信息
            for (int i = 0; i < Config.MODE_SCENE.mode - 1; i++) {
                human_player.add(getGameWorld().spawn("otherPlayers",new SpawnData(0,250*i+50)));
            }
        }
        Config.MODE_SCENE.mode = 0;
        
        
    }
    int three_coin_aleast=0;
    //获取一枚硬币
    public void getCoin(int size,Entity entities,Entity player,boolean isAi,double mouse_x,double mouse_y){

        //获取鼠标点到位置的实体
        if ((mouse_x<=1500+Config.COIN_WID && mouse_x>=1500 && mouse_y>=100 && mouse_y<=500+Config.COIN_HEI || isAi)){
            int a=entities.call("getNum");
            for (int i = 0; i < 5; i++) {
                int o=coinList.get(i).call("getNum");
                if (o>=1){
                    three_coin_aleast++;
                }
            }
            boolean bool=size==3?
                    //获取三枚不同的硬币
                    !num.contains(entities.getPosition())&&a>0&&three_coin_aleast>=3:
                    //获取两枚一样的硬币
                    (num.contains(entities.getPosition()) || num.size()==0)&&a+num.size()>=4;
            if (bool) {
                //玩家添加硬币
                String s=entities.call("getCoinNameAll");
                player.call("addCoin",s);
                player.call("showInfo");
                //硬币数量减少
                entities.call("cutcoinNumber");
                entities.call("showInfo");

                num.add(entities.getPosition());

                if (SocketClient.getInstance().login && num.size()!=size && SocketClient.getInstance().round_begin) {
                    Bundle act = new Bundle("act");
                    act.put("name", SocketClient.getInstance().name);
                    act.put("x", mouse_x);
                    act.put("y", mouse_y);
                    act.put("id", SocketClient.getInstance().id);
                    act.put("activity", size == 2 ? "getTwoSameCoin" : "getThreeCoin");
                    SocketClient.getInstance().send(act);
                }
                //动画
                double ani_x=player.getX()-entities.getX()+200;
                double ani_y=player.getY()-entities.getY();
                Entity bullet=entityBuilder()
                        .at(entities.getX(),entities.getY())
                        .viewWithBBox(FXGL.texture(tokenToCoin(entities.call("getStyle"))+".png",100,100))
                        .with(new ProjectileComponent(new Point2D(ani_x,ani_y),Math.sqrt(ani_x*ani_x+ani_y*ani_y)*2))
                        .with(new ExpireCleanComponent(Duration.seconds(0.5)))
                        .buildAndAttach();
                getGameWorld().addEntity(bullet);
            }else if (!ai_round){
                getNotificationService().pushNotification("Error");
            }
        }
        if (num.size()==size){
            player.call("setActivity","");
            num=new ArrayList<>();
            three_coin_aleast=0;

            if (SocketClient.getInstance().login && SocketClient.getInstance().round_begin) {
                SocketClient.getInstance().isThis = false;
                Bundle roundOver = new Bundle("roundOver");
                roundOver.put("name", SocketClient.getInstance().name);
                roundOver.put("x", mouse_x);
                roundOver.put("y", mouse_y);
                roundOver.put("id", SocketClient.getInstance().id);
                roundOver.put("activity", size == 2 ? "getTwoSameCoin" : "getThreeCoin");

                for (int i = 0; i < SocketClient.getInstance().allP.size(); i++) {
                    System.out.println("round");
                    if (SocketClient.getInstance().allP.get(i).equals(SocketClient.getInstance().name)) {
                        if (i == SocketClient.getInstance().allP.size() - 1) {
                            round = 0;
                        } else {
                            round = i;
                        }
                        break;
                    }
                }
                SocketClient.getInstance().send(roundOver);
            } else ai_round = true;
            if (SocketClient.getInstance().match) {
                SocketClient.getInstance().round_begin = SocketClient.getInstance().isThis;
                for (int i = 0; i < SocketClient.getInstance().players.size(); i++) {
                    if (SocketClient.getInstance().players.get(i).equals(SocketClient.getInstance().playing)) {
                        round = i;
                        break;
                    }
                }
            }
        }
    }
    //获取一张卡牌
    public void getOneCard(String actname,Entity entities,Entity player,boolean isAi, double mouse_x, double mouse_y){
        boolean ra= actname.equals("getOneMidCard") ?
                //中间的12张牌
                mouse_x<=1270+Config.CARD_WID && mouse_x>=688 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI:
                //坐下的保留牌 ---- ??? 208*i+player.getX()+205,player.getY()-195
                mouse_x<=208*2+player.getX()+205+Config.CARD_WID && mouse_x>=player.getX()+205 && mouse_y>=player.getY()-195 && mouse_y<=player.getY()-195+Config.CARD_HEI;
        if (ra||isAi) {
            HashMap<String,Integer> hashMap=entities.call("getMap");
            List<String> coins=entities.call("getCoins");
            boolean numisnull=f_card_3.get(hashMap.get("cardLevel")-1).call("numIsNull");
            boolean jud=true;
            int gold_coin=0;
            for (String coin : coins) {
                int c = player.call("enoughCoinplayer", coin, hashMap.get(coin));
                if (c<0) {
                    gold_coin=gold_coin+c;
                }
            }
            int glod_num=player.call("getGoldNum");
            if ((gold_coin*-1)>glod_num){
                jud = false;
            }

            //判断是否点到实体，卡片数量不为0，且拥有足够的宝石
            if (numisnull &&jud){
                HashMap<String,Integer> tokenMap=player.call("getMapToken");
                HashMap<String,Integer> coinMap=player.call("getMapCoin");
                for (String coin : coins) {
                    //扣除玩家的硬币
                    if (hashMap.get(coin)>tokenMap.get(coin)){
                        int c = player.call("enoughCoinplayer", coin, hashMap.get(coin));
                        if (c>=0) {
                            //买卡时还回硬币
                            coinList.get(Config.list.indexOf(coin)).call("addCoin", hashMap.get(coin) - tokenMap.get(coin));
                            player.call("cutCoin", coin,hashMap.get(coin)-tokenMap.get(coin));
                            coinList.get(Config.list.indexOf(coin)).call("showInfo");
                        }else {
                            coinList.get(5).call("addCoin",hashMap.get(coin)-tokenMap.get(coin)-coinMap.get(coin));
                            coinList.get(Config.list.indexOf(coin)).call("addCoin", coinMap.get(coin));
                            player.call("cutCoin", "goldToken", hashMap.get(coin)-tokenMap.get(coin)-coinMap.get(coin));
                            player.call("cutCoin", coin, coinMap.get(coin));
                            //买卡时还回硬币
                            coinList.get(5).call("showInfo");
                            coinList.get(Config.list.indexOf(coin)).call("showInfo");
                        }
                    }
                }
                //玩家获得分数和宝石
                player.call("addTokenAndScore","score",hashMap.get("score"));
                player.call("addTokenAndScore",entities.call("getGiveTokenAll"),1);
                player.call("showInfo");
                //添加保留卡
                if (actname.equals("getOneSaveCard")){
                    List<Entity> saveList=player.call("getSaveCard");
                    saveList.remove(entities);
                    for (int i = 0; i < saveList.size(); i++) { //208*i+player.getX()+205,player.getY()-195
                        saveList.get(i).setPosition(208*i+player.getX()+205,player.getY()-195);
                    }player.call("setSaveCard",saveList);
                }else {
                    //对最左边的三张等级牌操作
                    f_card_3.get(hashMap.get("cardLevel")-1).call("cutCardNumber");
                    f_card_3.get(hashMap.get("cardLevel")-1).call("showInfo");
                    //卡片替换成新的同等级的牌
                    s_card_12.set(s_card_12.indexOf(entities),
                            getGameWorld().spawn(entities.call("getClevel"),
                                    new SpawnData(entities.getX(),entities.getY())));
                }
                //动画
                double ani_x=player.getX()-entities.getX()+200;
                double ani_y=player.getY()-entities.getY();
                entities.addComponent(new ProjectileComponent(new Point2D(ani_x,ani_y),Math.sqrt(ani_x*ani_x+ani_y*ani_y)/2));
                entities.addComponent(new ExpireCleanComponent(Duration.seconds(2)));


                player.call("setActivity","");
                ai_round=true;

                //对贵族实体操作,贵族自动归入用户
                for (int i = 0; i < nobleList.size(); i++) {
                    HashMap<String,Integer> noblieList=nobleList.get(i).call("getMapToken");
                    HashMap<String,Integer> playerList=player.call("getMapToken");
                    Object[] keys = noblieList.keySet().toArray();

                    int noble_sp=0;
                    for (int j = 1; j < 4; j++) {
                        if (noblieList.get(keys[j])<=playerList.get(keys[j])){
                            noble_sp++;
                        }
                    }if (noble_sp==3){
                        //玩家获得分数
                        player.call("addTokenAndScore","score",noblieList.get("score"));
                        player.call("showInfo");
                        //动画
                        double ani_noblex=player.getX()-nobleList.get(i).getX()+200;
                        double ani_nobley=player.getY()-nobleList.get(i).getY();

                        nobleList.get(i).addComponent(new ProjectileComponent(new Point2D(ani_noblex,ani_nobley),Math.sqrt(ani_noblex*ani_noblex+ani_nobley*ani_nobley)/2));
                        nobleList.get(i).addComponent(new ExpireCleanComponent(Duration.seconds(2)));
                        nobleList.remove(nobleList.get(i));
                        //玩家获得分数分是否胜利
                        player_win(player);

                        break;
                    }
                }

                //玩家获得分数分是否胜利
                player_win(player);
                //发信息，获取一张卡牌
                //改下一个人操作了
                if (SocketClient.getInstance().login && SocketClient.getInstance().round_begin) {
                    SocketClient.getInstance().isThis = false;
                    Bundle roundOver = new Bundle("roundOver");
                    roundOver.put("name", SocketClient.getInstance().name);
                    if (mouse_y < 800) {
                        roundOver.put("x", mouse_x);
                        roundOver.put("y", mouse_y);
                        roundOver.put("id", SocketClient.getInstance().id);
                        roundOver.put("activity","getOneMidCard");
                    } else {
                        double x = mouse_x - player.getX();
                        double y = mouse_y - player.getY();
                        System.out.println(x);
                        System.out.println(y);
                        roundOver.put("x", x);
                        roundOver.put("y", y);
                        roundOver.put("id", SocketClient.getInstance().id);
                        roundOver.put("activity","getOneSaveCard");
                    }

                    for (int i = 0; i < SocketClient.getInstance().allP.size(); i++) {
                        if (SocketClient.getInstance().allP.get(i).equals(SocketClient.getInstance().name)) {
                            if (i == SocketClient.getInstance().allP.size() - 1) {
                                round = 0;
                            } else {
                                round = i;
                            }
                            break;
                        }
                    }
                    SocketClient.getInstance().send(roundOver);
                }
                if (SocketClient.getInstance().match) {
                    SocketClient.getInstance().round_begin = SocketClient.getInstance().isThis;
                    for (int i = 0; i < SocketClient.getInstance().players.size(); i++) {
                        if (SocketClient.getInstance().players.get(i).equals(SocketClient.getInstance().playing)) {
                            round = i;
                            break;
                        }
                    }
                }
            } else if (!ai_round || SocketClient.getInstance().round_begin){
                getNotificationService().pushNotification("Purchase failure");
                dealActPlayer(getGameScene());
            }
        }
    }
    //获取保留卡和一枚黄金硬币
    public void getSaveCard(Entity entities,Entity player, double mouse_x, double mouse_y){
        List<Entity> saveList=player.call("getSaveCard");
        if (mouse_x<=1270+Config.CARD_WID && mouse_x>=688 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI&&saveList.size()<=2) {
            HashMap<String,Integer> hashMap=entities.call("getMap");
            //黄金硬币减少一枚
            coinList.get(5).call("cutcoinNumber");
            coinList.get(5).call("showInfo");
            //玩家增加一枚黄金硬币
            player.call("addCoin","goldToken");
            player.call("showInfo");
            //动画
            double ani_x=player.getX()-coinList.get(coinList.size() - 1).getX()+200;
            double ani_y=player.getY()-coinList.get(coinList.size() - 1).getY();
            Entity bullet=entityBuilder()
                    .at(coinList.get(coinList.size() - 1).getX(),coinList.get(coinList.size() - 1).getY())
                    .viewWithBBox(FXGL.texture("goldcoin.png",100,100))
                    .with(new ProjectileComponent(new Point2D(ani_x,ani_y),Math.sqrt(ani_x*ani_x+ani_y*ani_y)))
                    .with(new ExpireCleanComponent(Duration.seconds(1)))
                    .buildAndAttach();
            getGameWorld().addEntity(bullet);
            //对最左边的三张操作
            f_card_3.get(hashMap.get("cardLevel")-1).call("cutCardNumber");
            f_card_3.get(hashMap.get("cardLevel")-1).call("showInfo");


            //替换成新的
            s_card_12.set(s_card_12.indexOf(entities),
                    getGameWorld().spawn(entities.call("getClevel"),new SpawnData(entities.getX(),entities.getY())));
            //玩家操作
            saveList.add(entities);
            for (int i = 0; i < saveList.size(); i++) {
                saveList.get(i).setPosition(208*i+player.getX()+205,player.getY()-195);
            }player.call("setSaveCard",saveList);
        } else if (!ai_round){
            getNotificationService().pushNotification("Error");
        }
        player.call("setActivity","");
        ai_round=true;


        //发信息，玩家获取保留卡和一枚黄金硬币
        //改下一个人操作了
        if (SocketClient.getInstance().login && SocketClient.getInstance().round_begin) {
            SocketClient.getInstance().isThis = false;
            Bundle roundOver = new Bundle("roundOver");
            roundOver.put("name", SocketClient.getInstance().name);
            roundOver.put("x", mouse_x);
            roundOver.put("y", mouse_y);
            roundOver.put("id", SocketClient.getInstance().id);
            roundOver.put("activity","getSaveCard");

            for (int i = 0; i < SocketClient.getInstance().allP.size(); i++) {
                if (SocketClient.getInstance().allP.get(i).equals(SocketClient.getInstance().name)) {
                    if (i == SocketClient.getInstance().allP.size() - 1) {
                        round = 0;
                    } else {
                        round = i;
                    }
                    break;
                }
            }
            SocketClient.getInstance().send(roundOver);
        }
        if (SocketClient.getInstance().match) {
            SocketClient.getInstance().round_begin = SocketClient.getInstance().isThis;
            for (int i = 0; i < SocketClient.getInstance().players.size(); i++) {
                if (SocketClient.getInstance().players.get(i).equals(SocketClient.getInstance().playing)) {
                    round = i;
                    break;
                }
            }
        }
    }
    int deal_activity=0;
    int deal_once=0;
    //下拉框选择玩家选择的活动
    public void removeButton(List<Button> list){
        for (int i = 0; i < 5; i++) {
            getGameScene().removeChild(list.get(i));
        }
    }
    public Button actionBut(String s){
        Button b= FXGL.getUIFactoryService().newButton(s);
        b.fontProperty().unbind();
        b.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        b.setStyle("-fx-background-image: url('assets/textures/ac1.png')");
        b.setPrefWidth(290);
        b.setPrefHeight(30);
        b.setLayoutX(800);
        return b;
    }
    public void dealActPlayer(GameScene gameScene){
        if (deal_once==0){
            deal_once=1;

            List<Button> buttonList=new ArrayList<>(){{
                add(actionBut("Get three different coins"));
                add(actionBut("Get two identical coins"));
                add(actionBut("Purchase a card"));
                add(actionBut("Buy a development card"));
                add(actionBut("Reserve a development card"));

            }};
            for (int i = 0; i < 5; i++) {
            	//----------
                buttonList.get(i).setTranslateX(750);
                buttonList.get(i).setTranslateY(820+50*i);
            }
            deal_once=1;
            List<String> act_list=new ArrayList<>(){{
                add("getThreeCoin");
                add("getTwoSameCoin");
                add("getOneMidCard");
                add("getOneSaveCard");
                add("getSaveCard");
            }};
            buttonList.get(0).setOnMouseClicked(mouseEvent -> {
                deal_activity=0;
                for (int i = 0; i < 5; i++) {
                    int o=coinList.get(i).call("getNum");
                    if (o>=1){
                        deal_activity++;
                    }
                }
                if (deal_activity>=3){
                    deal_once=0;
                    player.call("setActivity",act_list.get(0));
                    set("player_action", act_list.get(0));
                    removeButton(buttonList);
                }else {
                    getNotificationService().pushNotification("Coin type less than 3");
                }
            });
            buttonList.get(1).setOnMouseClicked(mouseEvent -> {
                deal_activity=0;
                for (int i = 0; i < 5; i++) {
                    int o=coinList.get(i).call("getNum");
                    if (o>=4){
                        deal_activity++;
                    }
                }
                if (deal_activity>=1){
                    deal_once=0;
                    player.call("setActivity",act_list.get(1));
                    set("player_action", act_list.get(1));
                    removeButton(buttonList);
                }else {
                    getNotificationService().pushNotification("Number of coins less than 4");
                }
            });
            buttonList.get(2).setOnMouseClicked(mouseEvent -> {
                deal_once=0;
                player.call("setActivity",act_list.get(2));
                set("player_action", act_list.get(2));
                removeButton(buttonList);
            });
            buttonList.get(3).setOnMouseClicked(mouseEvent -> {
                List<Entity> saveList=player.call("getSaveCard");
                if (saveList.size()>0){
                    deal_once=0;
                    player.call("setActivity",act_list.get(3));
                    set("player_action", act_list.get(3));
                    removeButton(buttonList);
                }else {
                    getNotificationService().pushNotification("No development card");
                }
            });
            buttonList.get(4).setOnMouseClicked(mouseEvent -> {
                int u=coinList.get(5).call("getNum");
                List<Entity> saveList=player.call("getSaveCard");
                if (u>0 && saveList.size()<3){
                    deal_once=0;
                    player.call("setActivity",act_list.get(4));
                    set("player_action", act_list.get(4));
                    removeButton(buttonList);
                } else {
                    getNotificationService().pushNotification("Error");
                }
            });
            gameScene.addChild(buttonList.get(0));
            gameScene.addChild(buttonList.get(1));
            gameScene.addChild(buttonList.get(2));
            gameScene.addChild(buttonList.get(3));
            gameScene.addChild(buttonList.get(4));
        }
    }
    public void player_win(Entity player){
        int the_score=player.call("getScore");
        if (the_score>=15) {
            if (player.equals(this.player)) {
                getNotificationService().pushNotification("Oh~ You win the game");
            } else {
                getNotificationService().pushNotification("Oh~ You loss the game");
            }
            Config.MODE_SCENE.mode = 0;
            SocketClient.getInstance().match = false;
            set("match", false);
            FXGL.getGameController().gotoMainMenu();
        }
    }
    public String tokenToCoin(String s) {
        return s.substring(0,s.length()-5)+"coin";
    }
}
