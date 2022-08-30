package nz.comp;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PlayerComponent extends Component {
    private HashMap<String,Integer> mapToken;
    private HashMap<String,Integer> mapCoin;
    private List<Entity> saveCard;
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
    public void cutCoin(String name,int n){
        mapCoin.replace(name,mapCoin.get(name)-n);

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


    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(new Rectangle(1000,300, Color.GOLD));
        Iterator<String> it = mapToken.keySet().iterator();
        int its=1;
        while(it.hasNext())
        {
            String key=it.next();
            Text text = new Text(0,30*its,key+"="+mapToken.get(key));
            text.setStyle("-fx-font-size: 30;");
            entity.getViewComponent().addChild(text);
            its++;
        }

        Iterator<String> is = mapCoin.keySet().iterator();
        int iss=1;
        while(is.hasNext())
        {
            String key=is.next();
            Text text = new Text(300,30*iss,key+"Coin="+mapCoin.get(key));
            text.setStyle("-fx-font-size: 30;");
            entity.getViewComponent().addChild(text);
            iss++;
        }


    }

}



