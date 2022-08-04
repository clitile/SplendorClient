package nz.comp;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Iterator;

public class CoinComponent extends Component {

    private int coinNumber;
    private String style;
    public CoinComponent(String coinStyle) {
        style=coinStyle;
        if (coinStyle=="goldToken"){
            coinNumber=5;
        }else {
            coinNumber=4;
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

    public int getcoinNumber() {
        return coinNumber;
    }
    public void setcoinNumber(int coinNumber) {
        this.coinNumber = this.coinNumber-coinNumber;
    }
    public String getStyle(){
        return style;
    }

    public void showInfo(){
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(FXGL.texture(style+".png"));
        Text text = new Text(0,40,String.valueOf(coinNumber));
        text.setStyle("-fx-font-size: 25;");
        entity.getViewComponent().addChild(text);
    }
}
