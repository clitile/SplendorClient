package nz.proj;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.localization.Language;
import javafx.application.Platform;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.net.SocketClient;
import nz.ui.LoseInterface;
import nz.ui.WinInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import static com.almasb.fxgl.dsl.FXGL.*;
public class SplendorApp extends GameApplication {
    //操作的玩家
    Entity player;
    //最左边的三张标志卡
    List<Entity> f_card_3;
    //中间的12张卡
    List<Entity> s_card_12;
    //硬币
    List<Entity> coinList;
    //贵族卡
    List<Entity> nobleList;
    //AI player的保留卡
    public static List<Entity> AIsaveList;
    
    int gamestate = 1;
    
    //鼠标坐标
    double mouse_x;
    double mouse_y;
    //ai是否操作
    int round=0;
    //Ai玩家
    List<Entity> ai_player;
    //真人玩家
    List<Entity> human_player;
    //用来存储玩家获取的硬币，判断是三个不同的硬币或两个相同的硬币
    List<Point2D> num=new ArrayList<>();
    //判断是不是ai的回合
    boolean ai_round=false;
    public static String lang = "english";
    @Override
    protected void initSettings(GameSettings settings) {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/assets/languages/language.txt"));
             BufferedReader br = new BufferedReader(reader)
        ) {
            lang = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SocketClient.getInstance().match = false;
        settings.setHeight(Config.APP_HEIGHT);
        settings.setWidth(Config.APP_WIDTH);
        settings.setTitle("Splendor");
        settings.setVersion("1.0.1");
        settings.setAppIcon("fp_token-1.png");
        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);
        settings.setMainMenuEnabled(true);
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        Language maori = new Language("Maori");
        settings.getSupportedLanguages().add(maori);
        settings.setDefaultLanguage(SplendorApp.lang.equals("english") ? Language.ENGLISH : maori);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu() {
                return new SplendorMainMenu();
            }

            @Override
            public LoadingScene newLoadingScene() {
            	return new nz.ui.Loadingscene();
            }
        });
    }
    //鼠标对应动作
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction(SplendorApp.lang.equals("english") ? "Click" : "Pāwhiri") {
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
                        getCoin(3,entities.get(0),player,mouse_x,mouse_y);
                    }
                }else if (activity.equals("getTwoSameCoin")){
                    //获取两枚相同的硬币
                    entities=getGameWorld().getEntitiesInRange(
                            new Rectangle2D(mouse_x-Config.COIN_WID,mouse_y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                    if (entities.size()!=0){
                        getCoin(2,entities.get(0),player,mouse_x,mouse_y);
                    }
                }else if (activity.equals("getOneMidCard") &&entities.size()!=0){
                    //判断对中间的12张牌的实体操作
                    getOneCard("getOneMidCard",entities.get(0),player, mouse_x,mouse_y);
                }else if (activity.equals("getOneSaveCard") &&entities.size()!=0){
                    //对左下角的保留牌操作
                    getOneCard("getOneSaveCard",entities.get(0),player,mouse_x,mouse_y);
                }
            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction(SplendorApp.lang.equals("english") ? "Get saved Card" : "Tiki Kāri i tiakina") {
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
        
        //音效音量大小
        FXGL.getSettings().setGlobalMusicVolume(0.5);
        FXGL.getSettings().setGlobalSoundVolume(0.8);
    }
    
    
    @Override
    protected void initGame() {
    	//背景音乐
    	FXGL.loopBGM("ba.mp3");
    	
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
                    for (Entity entity : ai_player) {
                        Random random=new Random();
                        ArrayList<String> ranlist=ranList(entity);
                        if (ranlist.size()>0){
                            String s=ranlist.get(random.nextInt(ranlist.size()));
                            if (s=="getThreeCoin"){
                                ai_getThreeCoin(entity);
                            }else if (s=="getTwoSameCoin"){
                                ai_getTwoSameCoin(entity);
                            }else if (s=="getOneMidCard"){
                                ai_getOneMidCard(entity);
                            }else if (s=="getOneSaveCard"){
                                ai_getOneSaveCard(entity);
                            }else {
                                ai_getSaveCard(entity);
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
                            getCoin(3,entities.get(0),human_player.get(round),SocketClient.getInstance().x, SocketClient.getInstance().y);
                        }
                    } else if (Objects.equals(SocketClient.getInstance().activity, "getTwoSameCoin")){
                        //获取两枚相同的硬币
                        entities=getGameWorld().getEntitiesInRange(
                                new Rectangle2D(SocketClient.getInstance().x-Config.COIN_WID,SocketClient.getInstance().y-Config.COIN_HEI,Config.COIN_WID,Config.COIN_HEI));
                        if (entities.size()!=0){
                            getCoin(2,entities.get(0),human_player.get(round),SocketClient.getInstance().x, SocketClient.getInstance().y);
                        }
                    }else if (Objects.equals(SocketClient.getInstance().activity, "getOneMidCard") &&entities.size()!=0){
                        //判断对中间的12张牌的实体操作
                        getOneCard("getOneMidCard",entities.get(0),human_player.get(round), SocketClient.getInstance().x, SocketClient.getInstance().y);
                    }else if (Objects.equals(SocketClient.getInstance().activity, "getOneSaveCard")){
                        //对左下角的保留牌操作
                        entities=getGameWorld().getEntitiesInRange(
                                //new Rectangle2D(SocketClient.getInstance().x-Config.CARD_WID+human_player.get(round).getX(),SocketClient.getInstance().y-Config.CARD_HEI+human_player.get(round).getY(),Config.CARD_WID,Config.CARD_HEI));
                        		new Rectangle2D(SocketClient.getInstance().x+Config.CARD_WID+human_player.get(round).getX(),SocketClient.getInstance().y+human_player.get(round).getY(),Config.CARD_WID,Config.CARD_HEI));
                        if (entities.size()!=0){
                            getOneCard("getOneSaveCard",entities.get(0),human_player.get(round), SocketClient.getInstance().x + human_player.get(round).getX(), SocketClient.getInstance().y + human_player.get(round).getY());
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
    	
    	FXGL.getSettings().setGlobalMusicVolume(0.5);
    	
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
    public void getCoin(int size,Entity entities,Entity player,double mouse_x,double mouse_y){

        //获取鼠标点到位置的实体
        if ((mouse_x<=1500+Config.COIN_WID && mouse_x>=1500 && mouse_y>=100 && mouse_y<=500+Config.COIN_HEI)){
        	if(player.isType(ai_player) || player.isType(human_player)) {
        		System.out.println("not you");
        	}else {
        		FXGL.play("button.wav");
        		
        	}
        	
        	
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
                        .with(new ProjectileComponent(new Point2D(ani_x,ani_y),Math.sqrt(ani_x*ani_x+ani_y*ani_y)))
                        .with(new ExpireCleanComponent(Duration.seconds(1)))
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
    public void getOneCard(String actname,Entity entities,Entity player, double mouse_x, double mouse_y){
        boolean ra= actname.equals("getOneMidCard") ?
                //中间的12张牌
                mouse_x<=1270+Config.CARD_WID && mouse_x>=688 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI:
                //保留牌 ---- ??? 208*i+player.getX()+205,player.getY()-195
                mouse_x<=208*2+player.getX()+205+Config.CARD_WID && mouse_x>=player.getX()+205 && mouse_y>=player.getY()-195 && mouse_y<=player.getY()-195+Config.CARD_HEI;
                
                
        if (ra) {
        	FXGL.play("button.wav");
        	
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
            if (numisnull && jud){
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
                    saveList = player.call("buySaveCard", entities);
                    
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
                entities.addComponent(new ProjectileComponent(new Point2D(ani_x,ani_y),Math.sqrt(ani_x*ani_x+ani_y*ani_y)));
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

                        nobleList.get(i).addComponent(new ProjectileComponent(new Point2D(ani_noblex,ani_nobley),Math.sqrt(ani_noblex*ani_noblex+ani_nobley*ani_nobley)));
                        nobleList.get(i).addComponent(new ExpireCleanComponent(Duration.seconds(2)));
                        nobleList.remove(nobleList.get(i));
                        //玩家获得分数分是否胜利
                        player_win(player);

                        break;
                    }
                }

                //玩家获得分数分是否胜利
                player_win(player);
                
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
    public List<Entity> getSaveCard(Entity entities,Entity player, double mouse_x, double mouse_y){
        List<Entity> saveList=player.call("getSaveCard");
        
        if (mouse_x<=1270+Config.CARD_WID && mouse_x>=688 && mouse_y>=100 && mouse_y<=500+Config.CARD_HEI&&saveList.size()<=2) {
        	
        	FXGL.play("button.wav");
        	
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
            s_card_12.set(s_card_12.indexOf(entities),getGameWorld().spawn(entities.call("getClevel"),new SpawnData(entities.getX(),entities.getY())));
            //玩家操作
            saveList.add(entities);
            System.out.println("AAIAIAIAIA");
            System.out.println(saveList.size());
            player.call("setSaveCard",saveList);
            
        } else if (!ai_round){
            getNotificationService().pushNotification("Error");
        }
        player.call("setActivity","");
        ai_round=true;

        //发信息，玩家获取保留卡和一枚黄金硬
      
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
        
        return saveList;
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
    //玩家操作的五个按钮
    public void dealActPlayer(GameScene gameScene){
        if (deal_once==0){
            deal_once=1;

            List<Button> buttonList=new ArrayList<>(){{
                add(actionBut(SplendorApp.lang.equals("english") ? "Get three different coins" : "Tīkina kia toru ngā moni rerekē"));
                add(actionBut(SplendorApp.lang.equals("english") ? "Get two identical coins" : "Tīkina kia rua ngā moni ōrite"));
                add(actionBut(SplendorApp.lang.equals("english") ? "Purchase a card" : "Hokona he kāri"));
                add(actionBut(SplendorApp.lang.equals("english") ? "Buy a development card" : "Hokona he kāri whanaketanga"));
                add(actionBut(SplendorApp.lang.equals("english") ? "Reserve a development card" : "Rāhuitia he kāri whanaketanga"));

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
            	FXGL.play("button.wav");
            	
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
                    
                    getNotificationService().pushNotification("You get 3 different coins");
                }else {
                    getNotificationService().pushNotification("Coin type less than 3");
                }
            });
            buttonList.get(1).setOnMouseClicked(mouseEvent -> {
            	FXGL.play("button.wav");
            	
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
                    
                    getNotificationService().pushNotification("You get 2 same coins");
                }else {
                    getNotificationService().pushNotification("Number of coins less than 4");
                }
            });
            buttonList.get(2).setOnMouseClicked(mouseEvent -> {
            	FXGL.play("button.wav");
            	
                deal_once=0;
                player.call("setActivity",act_list.get(2));
                set("player_action", act_list.get(2));
                removeButton(buttonList);
                
                getNotificationService().pushNotification("You buy 1 card");
            });
            buttonList.get(3).setOnMouseClicked(mouseEvent -> {
            	FXGL.play("button.wav");
            	
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
            	FXGL.play("button.wav");
            	
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
    //玩家胜利或失败的效果
    public void player_win(Entity player){
        int the_score=player.call("getScore");
        if (the_score>=15) {
        	FXGL.getSettings().setGlobalMusicVolume(0);
            if (player.equals(this.player)) {
                for(int i=0; i<3000; i++) {
                	i += 1;
                }
                
            	FXGL.play("win.wav");
                getSceneService().pushSubScene(new WinInterface());
            	
            } else {
            	FXGL.getSettings().setGlobalMusicVolume(0);
            	for(int i=0; i<3000; i++) {
                	i += 1;
                }
            	FXGL.play("lose.wav");
            	getSceneService().pushSubScene(new LoseInterface());
            }
            // 添加一个动画效果，效果结束后再返回主菜单。
            //FXGL.getGameController().gotoMainMenu();
        }
    }
    
    public String tokenToCoin(String s) {
        return s.substring(0,s.length()-5)+"coin";
    }
    //判断选择那种行为
    public ArrayList<Integer> ThreeAndTwo(int coinnum){
        ArrayList<Integer> an=new ArrayList();
        for (int i = 0; i < 5; i++) {
            int o=coinList.get(i).call("getNum");
            if (o>coinnum){
                an.add(i);
            }
        }return an;
    }
    //反对是否有足够的硬币
    public boolean enCoin(Entity entities,Entity player){
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
            return true;
        }else {
            return false;
        }
    }
    //返回一个可以操作的事件列表
    public ArrayList<String> ranList(Entity ap){
        ArrayList<Integer> threelist=ThreeAndTwo(0);
        ArrayList<Integer> twolist=ThreeAndTwo(3);
        ArrayList<String> list=new ArrayList<>();
        if (threelist.size()>=3){
            list.add("getThreeCoin");
            list.add("getThreeCoin");
            list.add("getThreeCoin");
        }if (twolist.size()>0){
            list.add("getTwoSameCoin");
            list.add("getTwoSameCoin");
        }
        for (int i = 0; i < 12; i++) {
            if (enCoin(s_card_12.get(i),ap)){
                list.add("getOneMidCard");
                list.add("getOneMidCard");
                list.add("getOneMidCard");
                list.add("getOneMidCard");
                list.add("getOneMidCard");
            }
        }
        int u=coinList.get(5).call("getNum");
        List<Entity> saveList=ap.call("getSaveCard");
        if (u>0 && saveList.size()<3) {
            list.add("getSaveCard");
        }
        if (saveList.size()>0){
            for (Entity entity:saveList){
                if (enCoin(entity,ap)){
                    list.add("getOneSaveCard");
                    list.add("getOneSaveCard");
                    list.add("getOneSaveCard");
                    list.add("getOneSaveCard");
                }
            }
        }
        return list;
    }
    //ai获得三枚不一样的硬币
    public void ai_getThreeCoin(Entity aplayer){
        ArrayList<Integer> coin=ThreeAndTwo(0);
        Collections.shuffle(coin);
        for (int i = 0; i < 3; i++) {
            getCoin(3, coinList.get(coin.get(i)), aplayer, coinList.get(coin.get(i)).getX() + 1, coinList.get(coin.get(i)).getY() + 1);
        }
        getNotificationService().pushNotification("AI has got three different coins");
        System.out.println("ai_getThreeCoin");
    }
    //ai获得两个一样的硬币
    public void ai_getTwoSameCoin(Entity aplayer){
        ArrayList<Integer> coin=ThreeAndTwo(3);
        Collections.shuffle(coin);
        getCoin(2, coinList.get(coin.get(0)), aplayer, coinList.get(coin.get(0)).getX() + 1, coinList.get(coin.get(0)).getY() + 1);
        getCoin(2, coinList.get(coin.get(0)), aplayer, coinList.get(coin.get(0)).getX() + 1, coinList.get(coin.get(0)).getY() + 1);
        getNotificationService().pushNotification("AI has got two same coins");
        System.out.println("ai_getTwoSameCoin");
    }
    //ai获得中间的一张卡
    public void ai_getOneMidCard(Entity aplayer){
        for (int i = 11; i >= 0; i--) {
            if (enCoin(s_card_12.get(i),aplayer)){
                getOneCard("getOneMidCard",s_card_12.get(i),aplayer, s_card_12.get(i).getX()+1, s_card_12.get(i).getY()+1);
                break;
            }
        }
        getNotificationService().pushNotification("AI has bought one card");
        System.out.println("ai_getOneMidCard");
    }
    //从保留卡中获得一个卡
    public void ai_getOneSaveCard(Entity aplayer){
        List<Entity> saveList=aplayer.call("getSaveCard");
        for (Entity entity:saveList){
            if (enCoin(entity,aplayer)){
                getOneCard("getOneSaveCard",entity,aplayer, entity.getX()+1, entity.getY()+1);
                break;
            }
        }
        getNotificationService().pushNotification("AI has bought one saved card");
        System.out.println("ai_getOneSaveCard");
    }

    public void ai_getSaveCard(Entity aplayer){
        Entity en=s_card_12.get(0);
        //ai玩家获取保留卡，坐标可以视作没用
        getSaveCard(en,aplayer, 700, 500);
        getNotificationService().pushNotification("AI has got a saved card");
       
    }

}
