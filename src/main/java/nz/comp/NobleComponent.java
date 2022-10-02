package nz.comp;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.net.SocketClient;
import nz.proj.Config;

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
            String a=Config.list.get(SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(0, 5) : FXGLMath.random(0,4));
            if (!lists.contains(a)){
                lists.add(a);
            }
        }


        mapToken=new HashMap<>(){{
            put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(1,2));
            put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(1,2));
            put(lists.get(2),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(1,2));
            put("score",SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 6) : FXGLMath.random(3,5));
        }};
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("nobles.png")
                ,2, Config.NOBLE_WID,Config.NOBLE_HEI, Duration.seconds(1),0,0));

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
    public HashMap<String,Integer> getMapToken(){
        return this.mapToken;
    }
    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(at);

        Iterator<String> it = mapToken.keySet().iterator();
        int its=1;
        while(it.hasNext()){
            String key=it.next();

            if(key.equals("redToken")) {
                entity.getViewComponent().addChild(addNode(its,key,Color.RED, Color.WHITE));
                Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                te.setTranslateX(20);
                te.setTranslateY(35*its-25);
                entity.getViewComponent().addChild(te);
            }
            if(key.equals("blackToken")) {
                entity.getViewComponent().addChild(addNode(its,key,Color.BLACK, Color.WHITE));
                Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                te.setTranslateX(20);
                te.setTranslateY(35*its-25);
                entity.getViewComponent().addChild(te);
            }
            if(key.equals("greenToken")) {
                entity.getViewComponent().addChild(addNode(its,key,Color.GREEN, Color.WHITE));
                Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                te.setTranslateX(20);
                te.setTranslateY(35*its-25);
                entity.getViewComponent().addChild(te);
            }
            if(key.equals("whiteToken")) {
                entity.getViewComponent().addChild(addNode(its,key,Color.WHITE, Color.BLACK));
                Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                te.setTranslateX(20);
                te.setTranslateY(35*its-25);
                entity.getViewComponent().addChild(te);
            }
            if(key.equals("blueToken")) {
                entity.getViewComponent().addChild(addNode(its,key,Color.BLUE, Color.WHITE));
                Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                te.setTranslateX(20);
                te.setTranslateY(35*its-25);
                entity.getViewComponent().addChild(te);
            }
            Text text=new Text();
            if(key.equals("score")) {
                text = new Text(120,50*its,mapToken.get(key).toString());
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 45));
                text.setFill(Color.GOLD);
                text.setStroke(Color.BLACK);
                entity.getViewComponent().addChild(text);
            }


            its++;
        }
    }


    public Text addNode(int its,String key,Color a,Color b){
        Text text= new Text(0,35*its,mapToken.get(key).toString());
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        text.setFill(a);
        text.setStroke(b);
        return text;
    }

    public String tokenToCoin(String s) {
        return s.substring(0,s.length()-5)+"coin";
    }
}
