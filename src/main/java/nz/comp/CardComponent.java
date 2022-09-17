package nz.comp;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.net.SocketClient;
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

        int RandomFrame= SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(0, 25) : FXGLMath.random(0,24);
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("cards_620_860.png")
                ,5,Config.CARD_WID,Config.CARD_HEI, Duration.seconds(1),RandomFrame,RandomFrame));

        int cardLevel;
        cardLevel=Integer.parseInt(level.substring(level.length()-1));


        clevel=level;
        giveToken=Config.list.get(SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(0, 5) : FXGLMath.random(0,4));

        List<String> lists=new ArrayList<>();
        int length= SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(1, 4) : FXGLMath.random(1,3);


        while (lists.size()!=length){
            String a=Config.list.get(SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(0, 5) : FXGLMath.random(0,4));
            if (!lists.contains(a)){
                lists.add(a);
            }
        }


        mapToken=new HashMap<>(){{
            put("cardLevel",cardLevel);
        }};

        if (level == "level1") {
            mapToken.put("score",0);
            if (length==1){
                mapToken.put(lists.get(0),3);
            }else if (length==2){
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(1, 3) : FXGLMath.random(1,2));
                mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(1, 3) : FXGLMath.random(1,2));
            }else {
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(1, 3) : FXGLMath.random(1,2));
                mapToken.put(lists.get(1),1);
                mapToken.put(lists.get(2),1);
            }


        }else if (level=="level2"){
            mapToken.put("score",SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(1, 4) : FXGLMath.random(1,3));
            if (length==1){
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(5, 7) : FXGLMath.random(5,6));
            }else if (length==2){
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(4, 7) : FXGLMath.random(4,6));
                mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(1, 4) : FXGLMath.random(1,3));
            }else {
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(2, 4) : FXGLMath.random(2,3));
                mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(2, 4) : FXGLMath.random(2,3));
                mapToken.put(lists.get(2),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(2, 4) : FXGLMath.random(2,3));
            }

        }else {
            mapToken.put("score",SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 6) : FXGLMath.random(3,5));
            if (length==1){
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(6, 8) : FXGLMath.random(6,7));
            }else if (length==2){
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(5, 8) : FXGLMath.random(5,7));
                mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(3,4));
            }else {
                mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 7) : FXGLMath.random(3,6));
                mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 7) : FXGLMath.random(3,6));
                mapToken.put(lists.get(2),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 7) : FXGLMath.random(3,6));
            }

        }
        for (int i = 0; i < length; i++) {
            coins.add(lists.get(i));
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
