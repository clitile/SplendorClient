package nz.comp;

import static com.almasb.fxgl.dsl.FXGL.getSceneService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import nz.net.SocketClient;
import nz.proj.MatchScene;
import nz.proj.ModeScene;
import nz.proj.SplendorApp;
import nz.ui.OtherPlayersInfo;

public class OtherPlayersComponent extends Component{
	String[] nameCards = {"assets/textures/nameCard1.png", "assets/textures/nameCard2.png", "assets/textures/nameCard3.png",
	"assets/textures/nameCard4.png"};
	
	//存储玩家持有的宝石和分数
    private HashMap<String,Integer> mapToken;
    //存储玩家持有的硬币
    private HashMap<String,Integer> mapCoin;
    //存储玩家持有的保留卡
    public static  List<Entity> saveCard;
    //存储玩家当前的活动
    private String activity="";
    //登录用户名称
    private String user_name="";
    
    //private OtherPlayersInfo subScene = new OtherPlayersInfo();
    private LazyValue<OtherPlayersInfo> subscene = new LazyValue<>(() -> {
    	return new OtherPlayersInfo();
    });
    
    int saveposition = 0;
    
    public static String score = "0";
    public static String goldcoin = "0";
    public static String[] othercoins = {"0", "0", "0", "0", "0"};
    public static String[] gems = {"0", "0", "0", "0", "0"};
    
    @Override
    public void onAdded() {
        saveCard=new ArrayList<>();
        mapToken=new HashMap<>(){{
            put("whiteToken",0);
            put("blueToken",0);
            put("greenToken",0);
            put("redToken",0);
            put("blackToken",0);
            put("score",0);
        }};
        mapCoin=new HashMap<>(){{
            put("whiteToken",0);
            put("blueToken",0);
            put("greenToken",0);
            put("redToken",0);
            put("blackToken",0);
            put("goldToken",0);
        }};
        showInfo();
    }
    public void addTokenAndScore(String name,int n) {
        mapToken.replace(name,n+mapToken.get(name));
    }
    public HashMap<String,Integer> getMapToken(){
        return this.mapToken;
    }
    public HashMap<String,Integer> getMapCoin(){
        return this.mapCoin;
    }
    public void cutCoin(String name,int n){
        mapCoin.replace(name,mapCoin.get(name)-n);
    }
    public int getScore(){
        return mapToken.get("score");
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return user_name;
    }
    public void addCoin(String name){
        mapCoin.replace(name,1+mapCoin.get(name));
    }
    
    
    public List<Entity> getSaveCard() {
        return saveCard;
    }
    public void setSaveCard(ArrayList<Entity> saveCard) {
        this.saveCard=saveCard;
        saveCard.get(saveposition).setScaleX(0.5);
        saveCard.get(saveposition).setScaleY(0.5);
        saveCard.get(saveposition).setPosition(60*saveposition+entity.getX()+200,entity.getY()+50*saveposition);
        saveposition += 1;
    }
    public List<Entity> buySaveCard(Entity entities) {
    	saveCard.remove(entities);
    	saveposition -= 1;
        for (int i = 0; i < saveposition; i++) { //208*i+player.getX()+205,player.getY()-195
            saveCard.get(i).setPosition(40*saveposition+entity.getX()+20,entity.getY()+50*saveposition);
        }
        
        return saveCard;
    }
    
    
    public boolean enoughCoin(String a,int b){
        if (b>mapCoin.get(a)+mapToken.get(a)){
                return false;
        }
        return true;
    }
    public int enoughCoinplayer(String a,int b){
        int k=mapCoin.get(a)+mapToken.get(a)-b;
        return k;
    }
    public int getGoldNum(){
        return this.mapCoin.get("goldToken");
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getActivity() {
        return activity;
    }
    public void showInfo(){
    	
    	int[] y = {50, 300, 550};
    	int i = 0;
    	
        entity.getViewComponent().clearChildren();
        
        Button button = FXGL.getUIFactoryService().newButton("");
        button.setMaxSize(30, 30);
        button.setStyle("-fx-background-image: url('assets/textures/hit5.png')");
        button.setTranslateX(146);
        button.setTranslateY(18);
        button.setOnAction(event -> {
        	FXGL.play("open.wav");
            getSceneService().pushSubScene(new OtherPlayersInfo());
        });
        
        
        
        Texture texture= FXGL.texture("emm1.png", 200, 215);
        
        entity.getViewComponent().addChild(texture);
        entity.getViewComponent().addChild(button);
        
        if(entity.getY() == 50) {
        	i = 0;
        }
        if(entity.getY() == 300) {
        	i = 1;
        }
        if(entity.getY() == 550) {
        	i = 2;
        }
        
        int imageContent = ModeScene.current;
        int[] index = {0, 1, 2, 3, 4, 5, 6, 7};
        List<Integer> indexList = new ArrayList<Integer>();
        for(int j = 0; j< 8; j++) {
        	if(index[j] != imageContent) {
        		indexList.add(index[j]);
        	}
        	
        }
        
        
        String[] imageURLs = ModeScene.imageURLs;
        int position = indexList.get(i);
        ImageView headview = new ImageView();
        Image head = new Image(imageURLs[position]);
        headview.setImage(head);
        headview.setFitWidth(120);
        headview.setFitHeight(160);
        headview.setLayoutX(40);
        headview.setLayoutY(250*i+50+30);
        FXGL.addUINode(headview);
        
        ImageView nameview = new ImageView();
        Image imagename = new Image(nameCards[i]);
        nameview.setImage(imagename);
        nameview.setFitWidth(207);
        nameview.setFitHeight(60);
        nameview.setLayoutX(0);
        nameview.setLayoutY(250*i+50+175);
        FXGL.addUINode(nameview);
        
        
        ArrayList<String> coins = new ArrayList<>(){{
            add("whiteToken");
            add("blueToken");
            add("greenToken");
            add("redToken");
            add("blackToken");
            add("goldToken");
        }};
        ArrayList<String> tokens = new ArrayList<>(){{
            add("whiteToken");
            add("blueToken");
            add("greenToken");
            add("redToken");
            add("blackToken");
            add("score");
        }};
        int its=0;
        
        
        for (String token : tokens) {
            if (token.equals("score")) {
            	score = mapToken.get(token).toString();
            } else {
            	gems[its] = mapToken.get(token).toString();
                its++;
            }
        }
        int iss=0;
        for (String coin : coins) {
            if (coin.equals("goldToken")) {
            	goldcoin = mapCoin.get(coin).toString();
            } else {
            	othercoins[iss] = mapCoin.get(coin).toString();
                iss++;
            }
        }
    }
}
