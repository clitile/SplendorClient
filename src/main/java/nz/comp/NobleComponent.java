package nz.comp;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.proj.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NobleComponent extends Component {


    private AnimatedTexture at;
    private HashMap<String,Integer> mapToken;
    public NobleComponent() {
        mapToken=new HashMap<>(){{
            put("whiteToken",5);
            put("blueToken",5);
            put("greenToken",5);
//            put("redToken",0);
//            put("blackToken",0);
            put("score",5);
        }};
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("nobles.png")
                ,2, Config.NOBLE_WID,Config.NOBLE_HEI, Duration.seconds(1),0,0));

//        Iterator<String> it = mapToken.keySet().iterator();
//        while(it.hasNext())
//        {
//            String key=it.next();
//            mapToken.replace(key,FXGLMath.random(3,5));
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
    public int getScore(){
        return mapToken.get("score");
    }

    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(at);

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
