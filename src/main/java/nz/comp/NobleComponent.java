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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.net.SocketClient;
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
    
    String[] number1 = FirstCardComponent.number1;
	String[] number2 = CardComponent.number2;
	Image[] imagesNumber1 = new Image[10];
	Image[] imagesNumber2 = new Image[10];
	
	ImageView imageview = new ImageView();
	String color;
	
    public NobleComponent() {
        List<String> lists=new ArrayList<>();
        while (lists.size()!=3){
            String a=Config.list.get(SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(0, 5) : FXGLMath.random(0,4));
            if (!lists.contains(a)){
                lists.add(a);
            }
        }

        mapToken=new HashMap<>(){{
            put(lists.get(0),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(3,5));
            put(lists.get(1),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(3,5));
            put(lists.get(2),SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 5) : FXGLMath.random(3,5));
            put("score",SocketClient.getInstance().match ? SocketClient.getInstance().r.nextInt(3, 6) : FXGLMath.random(3,5));
        }};
        at=new AnimatedTexture(new AnimationChannel(FXGL.image("nobles.png"),2, Config.NOBLE_WID,Config.NOBLE_HEI, Duration.seconds(1),0,0));

        for(int i=0; i<imagesNumber1.length; i++) {
        	imagesNumber1[i] = new Image(number1[i]);
        }
        
        
        for(int i=0; i<imagesNumber2.length; i++) {
        	imagesNumber2[i] = new Image(number2[i]);
        }
    }
    @Override
    public void onAdded() {

    	showInfo();

    }
    public int getScore(){
        return mapToken.get("score");
    }
    public HashMap<String,Integer> getMapToken(){
        return this.mapToken;
    }
    public void showInfo(){
        entity.getViewComponent().clearChildren();
        Texture texture = FXGL.texture("n2.png", 191, 191);
        at.setTranslateX(20);
        at.setTranslateY(20);
        entity.getViewComponent().addChild(texture);
        entity.getViewComponent().addChild(at);

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
            Text text=new Text();
            if(key.equals("score")) {
                //text = new Text(120,50*its,mapToken.get(key).toString());
                //text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 45));
                //text.setFill(Color.GOLD);
                //text.setStroke(Color.BLACK);
                //entity.getViewComponent().addChild(text);
                
            	Rectangle rect = new Rectangle(130, 111, 40, 60);
            	Color c = Color.web("#FFFFFF", 0.2);
                rect.setFill(c);
                
            	int num = mapToken.get(key);
            	if(num>=10) {
            		num =1;
            	}
            	imageview = new ImageView(imagesNumber1[num]);
            	imageview.setFitHeight(60);
            	imageview.setFitWidth(40);
            	imageview.setLayoutX(130);
            	imageview.setLayoutY(111);
            	
            	entity.getViewComponent().addChild(rect);
            	entity.getViewComponent().addChild(imageview);
                
                
                
            }else if (!key.equals("cardLevel")){
                //entity.getViewComponent().addChild(addNode(its,key,colormap.get(key), Color.WHITE));
                //Texture te=FXGL.texture(tokenToCoin(key) + ".png", 30,30);
                //te.setTranslateX(20);
                //te.setTranslateY(35*its-25);
                //entity.getViewComponent().addChild(te);
                
                Rectangle rect = new Rectangle(20, 20+45*(its-2), 35, 45);
            	
                if(key == "whiteToken") {
                	color = "white";
                }
                if(key == "blueToken") {
                	color = "blue";
                }
                if(key == "greenToken") {
                	color = "green";
                }
                if(key == "redToken") {
                	color = "red";
                }
                if(key == "blackToken") {
                	color = "black";
                }
                Color c = c = Color.web(color, 0.8);
                rect.setFill(c);
                
                int num = mapToken.get(key);
            	if(num>=10) {
            		num =1;
            	}
            	imageview = new ImageView(imagesNumber2[num]);
            	imageview.setFitHeight(45);
            	imageview.setFitWidth(30);
            	imageview.setLayoutX(20);
            	imageview.setLayoutY(20+45*(its-2));
            	
            	entity.getViewComponent().addChild(rect);
            	entity.getViewComponent().addChild(imageview);
                
                
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
        return s.substring(0,s.length()-5);
    }
}
