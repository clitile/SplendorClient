package nz.proj;

import javafx.scene.paint.Color;


import java.util.*;

public interface Config {
    int APP_WIDTH = 1920;
    int APP_HEIGHT = 1080;
    int CARD_WID = 124;
    int CARD_HEI=172;
    int COIN_WID = 90;
    int COIN_HEI=75;
    int NOBLE_WID = 151;
    int NOBLE_HEI=151;
    Color BlockColor = Color.DARKBLUE;
    String BackMusic="ba.mp3";
    List<String> list=new ArrayList<>(){{
        add("whiteToken");
        add("blueToken");
        add("greenToken");
        add("redToken");
        add("blackToken");
        add("goldToken");
    }};
    List<String> list_s=new ArrayList<>(){{
        add("level1");
        add("level2");
        add("level3");
    }};
    List<String> list_f=new ArrayList<>(){{
        add("f_level1");
        add("f_level2");
        add("f_level3");
    }};

    ModeScene MODE_SCENE = new ModeScene();

    String PORT = "10100";
    String IP = "localhost";
}
