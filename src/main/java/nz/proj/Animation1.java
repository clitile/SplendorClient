package nz.proj;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getCutsceneService;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

import java.util.List;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;

import javafx.util.Duration;

public class Animation1 implements Runnable  {

	@Override
	public void run() {
		getGameWorld().spawn("animation1");
        FXGL.runOnce(() -> {
     	   List<String> lines = getAssetLoader().loadText("animation1.txt");
     	   Cutscene cutscene = new Cutscene(lines);
     	   getCutsceneService().startCutscene(cutscene);
        }, Duration.ONE);
		
	}

}
