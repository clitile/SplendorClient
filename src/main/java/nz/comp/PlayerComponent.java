package nz.comp;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    @Override
    public void onUpdate(double tpf) {
    }
    @Override
    public void onRemoved() {
        super.onRemoved();
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
        entity.getViewComponent().addChild(new Rectangle(300,100, Color.GOLD));
        Iterator<String> it = mapToken.keySet().iterator();
        int its=1;
        while(it.hasNext()) {
            String key=it.next();
            Text text = new Text(0,15*its,key+"="+mapToken.get(key));
            text.setStyle("-fx-font-size: 15;");
            entity.getViewComponent().addChild(text);
            its++;
        }

        Iterator<String> is = mapCoin.keySet().iterator();
        int iss=1;
        while(is.hasNext()) {
            String key=is.next();
            Text text = new Text(120,15*iss,key+"Coin="+mapCoin.get(key));
            text.setStyle("-fx-font-size: 15;");
            entity.getViewComponent().addChild(text);
            iss++;
        }

    }
}



