package nz.comp;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.proj.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class CardComponent extends Component {

    private AnimatedTexture at;
    private HashMap<String,Integer> mapToken;
    private String giveToken;
    private String clevel;
    private List<String> coins=new ArrayList<>();
    public CardComponent(String level) {

        int RandomFrame= FXGLMath.random(0,24);
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("cards_620_860.png")
                ,5,Config.CARD_WID,Config.CARD_HEI, Duration.seconds(1),RandomFrame,RandomFrame));
        
        int cardLevel;
        cardLevel=Integer.parseInt(level.substring(level.length()-1));


        clevel=level;
        giveToken=Config.list.get(FXGLMath.random(0,4));

        mapToken=new HashMap<>(){{
            put("score",2);
            put("cardLevel",cardLevel);
            put("blackToken",2);
            put("blueToken",2);
            put("whiteToken",2);

        }};
        coins.add("blackToken");
        coins.add("blueToken");
        coins.add("whiteToken");
//        Iterator<String> it = mapToken.keySet().iterator();
//        while(it.hasNext())
//        {
//            String key=it.next();
//            if (key=="score"){
//                if (level=="level1"){
//                    mapToken.replace(key,0);
//                }else if (level=="level2"){
//                    mapToken.replace(key,FXGLMath.random(1,3));
//                }else {
//                    mapToken.replace(key,FXGLMath.random(3,5));
//                }
//            }else {
//                mapToken.replace(key,FXGLMath.random(-1+cardLevel,2*cardLevel));
//            }
//
//        }
    }
    @Override
    public void onAdded() {
        showInfo();
    }
    @Override
    public void onUpdate(double tpf) {
    }
    @Override
    public void onRemoved() {
        super.onRemoved();
    }


    public String getGiveToken() {
        return giveToken;
    }
    public String getClevel() {
        return clevel;
    }
    public HashMap<String,Integer> getMap(){
        return mapToken;
    }
    public List<String> getCoins(){
        return coins;
    }
    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(at);
        at.play();

        Texture texture=FXGL.texture(giveToken+".png",50,50);
        texture.setTranslateX(70);
        entity.getViewComponent().addChild(texture);

        Iterator<String> it = mapToken.keySet().iterator();
        int its=1;
        while(it.hasNext())
        {
            String key=it.next();
            Text text = new Text(0,20*its,key+"="+mapToken.get(key));
            text.setStyle("-fx-font-size: 18;");
            entity.getViewComponent().addChild(text);
            its++;
        }
    }
}
