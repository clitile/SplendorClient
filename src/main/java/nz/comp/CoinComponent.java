package nz.comp;

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

public class CoinComponent extends Component {
    private int coinNumber;
    private String style;
    public CoinComponent(String coinStyle) {
        style=coinStyle;
        if (coinStyle=="goldToken"){
            coinNumber=6;
        }else {
            coinNumber=5;
        }
    }
    @Override
    public void onAdded() {
        showInfo();
    }
    public String getCoinNameAll() {
        return style;
    }
    public void cutcoinNumber() {
        this.coinNumber = this.coinNumber-1;
    }
    public String getCoinName() {
        return this.style.substring(0,this.style.length()-5)+"coin";
    }
    public void addCoin(int i){
        this.coinNumber=coinNumber+i;
    }
    public int getNum(){
        return this.coinNumber;
    }
    public String getStyle(){
        return style;
    }
    public void showInfo(){
        entity.getViewComponent().clearChildren();


        String coinStyle = getCoinNameAll();

        entity.getViewComponent().addChild(FXGL.texture(getCoinName()+".png", 100, 100));
        Text text = new Text(0,40,String.valueOf(coinNumber));
        
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        entity.getViewComponent().addChild(text);
    }
}
