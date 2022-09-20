package nz.comp;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.proj.Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NobleComponent extends Component {


    private AnimatedTexture at;
    private HashMap<String,Integer> mapToken;
    public NobleComponent() {
        List<String> lists=new ArrayList<>();

        while (lists.size()!=3){
            String a=Config.list.get(FXGLMath.random(0,4));
            if (!lists.contains(a)){
                lists.add(a);
            }
        }


        mapToken=new HashMap<>(){{
            put(lists.get(0),FXGLMath.random(3,5));
            put(lists.get(1),FXGLMath.random(3,5));
            put(lists.get(2),FXGLMath.random(3,5));
            put("score",FXGLMath.random(3,5));
        }};
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("nobles.png"),2, Config.NOBLE_WID,Config.NOBLE_HEI, Duration.seconds(1),0,0));

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
    public int getScore(){
        return mapToken.get("score");
    }

    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(at);

        Iterator<String> it = mapToken.keySet().iterator();
        int its=1;
        while(it.hasNext()){
            String key=it.next();
            
            Text text = new Text();
            if(key.equals("redToken")) {
            	text = new Text(0,35*its,mapToken.get(key).toString());
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            	text.setFill(Color.RED);
            	text.setStroke(Color.WHITE);  
            }
            if(key.equals("blackToken")) {
            	text = new Text(0,35*its,mapToken.get(key).toString());
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            	text.setFill(Color.BLACK);
            	text.setStroke(Color.WHITE);
            }
            if(key.equals("greenToken")) {
            	text = new Text(0,35*its,mapToken.get(key).toString());
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            	text.setFill(Color.GREEN);
            	text.setStroke(Color.WHITE);
            }
            if(key.equals("whiteToken")) {
            	text = new Text(0,35*its,mapToken.get(key).toString());
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            	text.setFill(Color.WHITE);
            	text.setStroke(Color.WHITE);
            }
            if(key.equals("blueToken")) {
            	text = new Text(0,35*its,mapToken.get(key).toString());
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            	text.setFill(Color.BLUE);
            	text.setStroke(Color.WHITE);
            }
            if(key.equals("score")) {
            	text = new Text(120,50*its,mapToken.get(key).toString());
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 45));
            	text.setFill(Color.GOLD);
            	text.setStroke(Color.WHITE);
            }
            entity.getViewComponent().addChild(text);
            
            its++;
        }
    }
}
