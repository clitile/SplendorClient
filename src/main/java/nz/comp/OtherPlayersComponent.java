package nz.comp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import nz.proj.ModeScene;

public class OtherPlayersComponent extends Component{
	String[] nameCards = {"assets/textures/nameCard1.png", "assets/textures/nameCard2.png", "assets/textures/nameCard3.png",
	"assets/textures/nameCard4.png"};
	
	//存储玩家持有的宝石和分数
    private HashMap<String,Integer> mapToken;
    //存储玩家持有的硬币
    private HashMap<String,Integer> mapCoin;
    //存储玩家持有的保留卡
    private List<Entity> saveCard;
    //存储玩家当前的活动
    private String activity="";
    //登录用户名称
    private String user_name="";
    
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
        Texture texture= FXGL.texture("emm1.png", 200, 215);
        entity.getViewComponent().addChild(texture);
        
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
        /*
        for (String token : tokens) {
            if (token.equals("score")) {
                Text text = new Text(240,43,mapToken.get(token).toString());
                text.setStyle("-fx-font-size: 20;");
                entity.getViewComponent().addChild(text);
            } else {
                Text text = new Text(40+its*71,79,mapToken.get(token).toString());
                text.setStyle("-fx-font-size: 20;");
                entity.getViewComponent().addChild(text);
                its++;
            }
        }
        int iss=0;
        for (String coin : coins) {
            if (coin.equals("goldToken")) {
                Text text = new Text(398,79,mapCoin.get(coin).toString());
                text.setStyle("-fx-font-size: 20;");
                entity.getViewComponent().addChild(text);
            } else {
                Text text = new Text(67+iss*71,79,mapCoin.get(coin).toString());
                text.setStyle("-fx-font-size: 20;");
                entity.getViewComponent().addChild(text);
                iss++;
            }
        }*/
    }
}
