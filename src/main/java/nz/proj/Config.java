package nz.proj;

import javafx.scene.paint.Color;
import javafx.util.Duration;


import java.util.*;

import static javafx.util.Duration.seconds;

public interface Config {
    int WIDTH = 1920;
    int HEIGHT = 1080;
    Color BlockColor = Color.DARKBLUE;
    String BackMusic="rain.mp3";
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


}
