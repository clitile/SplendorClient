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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class CardComponent extends Component {

    private String giveToken;
    private AnimatedTexture at;
    private HashMap<String,Integer> mapToken;
    private int clevel;
    public CardComponent(String level) {
        mapToken=new HashMap<>(){{
            put("whiteToken",0);
            put("blueToken",0);
            put("greenToken",0);
            put("redToken",0);
            put("blackToken",0);
            put("goldToken",0);
            put("score",0);
        }};
        int RandomFrame= FXGLMath.random(0,24);
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("cards_620_860.png")
                ,5,620/5,860/5, Duration.seconds(1),RandomFrame,RandomFrame));
        
        int cardLevel;
        cardLevel=Integer.parseInt(level.substring(level.length()-1));
        clevel=cardLevel;
        mapToken.remove("goldToken");
        
        List<String> list=new ArrayList<>();
        list.add("whiteToken");
        list.add("blueToken");
        list.add("greenToken");
        list.add("redToken");
        list.add("blackToken");

        giveToken=list.get(FXGLMath.random(0,4));

        Iterator<String> it = mapToken.keySet().iterator();
        while(it.hasNext())
        {
            String key=it.next();
            if (key=="score"){
                if (level=="level1"){
                    mapToken.replace(key,0);
                }else if (level=="level2"){
                    mapToken.replace(key,FXGLMath.random(1,3));
                }else {
                    mapToken.replace(key,FXGLMath.random(3,5));
                }
            }else {
                mapToken.replace(key,FXGLMath.random(-1+cardLevel,2*cardLevel));
            }

        }
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

    public int numToken(String name){
        return mapToken.get(name);
    }

    public int getScore() {
        return mapToken.get("score");
    }
    public String getToken() {
        return giveToken;
    }
    public int getLevel(){
        return clevel;
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
