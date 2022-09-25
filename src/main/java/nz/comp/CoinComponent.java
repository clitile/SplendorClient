package nz.comp;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
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
    @Override
    public void onUpdate(double tpf) {
    }
    @Override
    public void onRemoved() {
        super.onRemoved();
    }

    public String getCoinNameAll() {
        return style;
    }
    public void cutcoinNumber() {
        this.coinNumber = this.coinNumber-1;
    }
    public String getCoinName() {
        return this.style.substring(0,this.style.length()-5)+"coin";
//        if(style.equals("whiteToken")) {
//            return "whitecoin";
//        }
//        if(style.equals("redToken")) {
//            return "redcoin";
//        }
//        if(style.equals("blueToken")) {
//            return "bluecoin";
//        }
//        if(style.equals("blackToken")) {
//            return "blackcoin";
//        }
//        if(style.equals("greenToken")) {
//            return "greencoin";
//        }
//        if(style.equals("goldToken")) {
//            return "goldcoin";
//        }
//        return "oh no ~";
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
        text.setStyle("-fx-font-size: 25;");
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        entity.getViewComponent().addChild(text);
    }
}
