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

        int lev=Integer.parseInt(String.valueOf(level.charAt(5)));
        setCoinValue(length,lists,lev);

        mapToken.put("score",SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt((lev-1)*2, (lev-1)*4) : FXGLMath.random((lev-1)*2, (lev-1)*4));
        for (int i = 0; i < length; i++) {
            coins.add(lists.get(i));
        }
    }
    @Override
    public void onAdded() {
        showInfo();
    }
    public String getGiveTokenAll() {
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
        Texture texture=FXGL.texture(giveToken.substring(0,giveToken.length()-5) + ".png", 50,50);
        texture.setTranslateX(70);
        entity.getViewComponent().addChild(texture);

        Iterator<String> it = mapToken.keySet().iterator();

        HashMap<String,Color> colormap=new HashMap<>(){{
            put("whiteToken",Color.WHITE);
            put("blueToken",Color.BLUE);
            put("greenToken",Color.GREEN);
            put("redToken",Color.RED);
            put("blackToken",Color.BLACK);
        }};
        int its=1;
        while(it.hasNext()){
            String key=it.next();
            if(key.equals("score")){
                if(!clevel.equals("level1")) {
                    entity.getViewComponent().addChild(addNode(its,key,Color.WHITE, Color.BLACK));
                }
            }else if (!key.equals("cardLevel")){
                entity.getViewComponent().addChild(addNode(its,key,colormap.get(key), Color.WHITE));
                Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                te.setTranslateX(20);
                te.setTranslateY(35*its-25);
                entity.getViewComponent().addChild(te);
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
    private void setCoinValue(int leng,List<String> lists,int lev){
        if (leng==1){
            mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(lev*2, 3*lev) : FXGLMath.random(lev*2, 3*lev));
        }else if (leng==2){
            mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(lev*2-1, 3*lev) : FXGLMath.random(lev*2, 3*lev));
            mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(lev*2-1, 3*lev) : FXGLMath.random(lev*2, 3*lev));
        }else {
            mapToken.put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(lev+1-1, 2*lev+1) : FXGLMath.random(lev+1, 2*lev+1));
            mapToken.put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(lev+1-1, 2*lev+1) : FXGLMath.random(lev+1, 2*lev+1));
            mapToken.put(lists.get(2),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(lev+1-1, 2*lev+1) : FXGLMath.random(lev+1, 2*lev+1));
        }
    }
}
