package nz.comp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class FirstCardComponent extends Component {
	public static String[] number1 = {"assets/textures/num1-0.png", "assets/textures/num1-1.png", "assets/textures/num1-2.png",
			"assets/textures/num1-3.png","assets/textures/num1-4.png", "assets/textures/num1-5.png", 
			"assets/textures/num1-6.png", "assets/textures/num1-7.png","assets/textures/num1-8.png", "assets/textures/num1-9.png"};
	
	Image[] images = new Image[10];
    
    ImageView geview = new ImageView();
    ImageView shiview = new ImageView();
    
    private AnimatedTexture at;
    private int cardNumber;
    
    public FirstCardComponent(String level) {
        if (level=="f_level1"){
            at=new AnimatedTexture(new AnimationChannel(FXGL.image("levelcard.png"),3,124,230, Duration.seconds(1),0,0));
            cardNumber=40;
        }
        if(level=="f_level2"){
            at=new AnimatedTexture(new AnimationChannel(FXGL.image("levelcard.png"),3,372/3,230, Duration.seconds(1),1,1));
            cardNumber=30;
        }
        if (level=="f_level3") {
            at=new AnimatedTexture(new AnimationChannel(FXGL.image("levelcard.png"),3,372/3,230, Duration.seconds(1),2,2));
            cardNumber=20;
        }
        for(int i=0; i<images.length; i++) {
        	images[i] = new Image(number1[i]);
        }
    }
    @Override
    public void onAdded() {
        showInfo();
    }
    public void cutCardNumber() {
        this.cardNumber = this.cardNumber-1;
    }
    public boolean numIsNull(){
        if (this.cardNumber==0){
            return false;
        }else {
            return true;
        }
    }
    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(at);
        at.play();
        
        int ge = cardNumber % 10;
        int shi = (cardNumber-ge) / 10 % 10;
       
        geview.setImage(images[ge]);
        shiview.setImage(images[shi]);
        
        geview.setFitWidth(40);
        geview.setFitHeight(65);
        shiview.setFitWidth(40);
        shiview.setFitHeight(65);
        
        
        geview.setLayoutX(80);
        geview.setLayoutY(160);
        shiview.setLayoutX(60);
        shiview.setLayoutY(160);
               
        entity.getViewComponent().addChild(geview);
        entity.getViewComponent().addChild(shiview);
    }
}
