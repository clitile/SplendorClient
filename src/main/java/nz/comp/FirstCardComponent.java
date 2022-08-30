package nz.comp;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.onBtn;
import static com.almasb.fxgl.dsl.FXGL.onBtnDown;

public class FirstCardComponent extends Component {
    private AnimatedTexture at;
    private int cardNumber;
    public FirstCardComponent(String level) {
        if (level=="f_level1"){
            at=new AnimatedTexture(new AnimationChannel(FXGL.image("cards_372_172.png")
                    ,3,124,172, Duration.seconds(1),0,0));
            cardNumber=40;
        }
        if(level=="f_level2"){
            at=new AnimatedTexture(new AnimationChannel(FXGL.image("cards_372_172.png")
                    ,3,372/3,172, Duration.seconds(1),1,1));
            cardNumber=30;
        }
        if (level=="f_level3") {
            at=new AnimatedTexture(new AnimationChannel(FXGL.image("cards_372_172.png")
                    ,3,372/3,172, Duration.seconds(1),2,2));
            cardNumber=20;
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
        Text text = new Text(0,40,String.valueOf(cardNumber));
        text.setStyle("-fx-font-size: 25;");
        entity.getViewComponent().addChild(text);
    }
}
