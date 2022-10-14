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
        entity.getViewComponent().clearChildren();

 
        Texture texture= FXGL.texture("ima.png", 1034,112);
        
        entity.getViewComponent().addChild(texture);
        
        
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
                Text text = new Text(240,43,mapToken.get(token).toString());
                //text.setStyle("-fx-font-size: 20;");
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.WHITE);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
            } else {
                Text text = new Text(40+its*71,79,mapToken.get(token).toString());
                //text.setStyle("-fx-font-size: 20;");
                //text.setStyle("-fx-font-size: 20;");
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.WHITE);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
                its++;
            }
        }
        int iss=0;
        for (String coin : coins) {
            if (coin.equals("goldToken")) {
                Text text = new Text(398,79,mapCoin.get(coin).toString());
                //text.setStyle("-fx-font-size: 20;");
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.WHITE);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
                //text.setStyle("-fx-font-size: 20;");
            } else {
                Text text = new Text(67+iss*71,79,mapCoin.get(coin).toString());
                //text.setStyle("-fx-font-size: 20;");
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setFill(Color.WHITE);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
                //text.setStyle("-fx-font-size: 20;");
                iss++;
            }
        }
    }
}



