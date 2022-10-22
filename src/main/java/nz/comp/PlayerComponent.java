package nz.comp;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerComponent extends Component {
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
    
    String[] number2 = CardComponent.number2;
    Image[] imagesNumber2 = new Image[10];
	
	ImageView imageview = new ImageView();
	
	int saveposition = 0;
    
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
        saveCard.get(saveposition).setPosition(208*saveposition+entity.getX()+205,entity.getY()-195);
        saveposition += 1;
    }
    
    
    public List<Entity> buySaveCard(Entity entities) {
    	saveCard.remove(entities);
    	saveposition -= 1;
        for (int i = 0; i < saveposition; i++) { //208*i+player.getX()+205,player.getY()-195
            saveCard.get(i).setPosition(208*i+entity.getX()+205,entity.getY()-195);
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
        entity.getViewComponent().clearChildren();
        Texture texture = FXGL.texture("savebg.png", 1625, 370);
        texture.setTranslateX(-113);
        texture.setTranslateY(-230);
        Rectangle rect = new Rectangle(-113, -210, 1625, 350);
        Color c = Color.web("grey", 0.4);
        rect.setFill(c);
        Texture texture1= FXGL.texture("record9.png", 1200, 148);
        texture1.setTranslateX(-100);
        texture1.setTranslateY(0);
        Texture texture2 = FXGL.texture("saveboard.png", 570, 200);
        texture2.setTranslateX(200);
        texture2.setTranslateY(-200);
        entity.getViewComponent().addChild(rect);
        entity.getViewComponent().addChild(texture);
        entity.getViewComponent().addChild(texture1);
        entity.getViewComponent().addChild(texture2);
        
        
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
                Text text = new Text(95,80,mapToken.get(token).toString());
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.GOLD);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
            } else {
                Text text = new Text(215+its*125,50,mapToken.get(token).toString());
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.YELLOW);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
                its++;
            }
        }
        int iss=0;
        for (String coin : coins) {
            if (coin.equals("goldToken")) {
                Text text = new Text(840,75,mapCoin.get(coin).toString());
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.YELLOW);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
            } else {
                Text text = new Text(230+iss*125,105,mapCoin.get(coin).toString());
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.WHITE);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
                iss++;
            }
        }
        
        
    }
}



