package nz.proj;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.scene.image.Image;
import javafx.util.Duration;
import nz.comp.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class SplendorFactory implements EntityFactory {
    @Spawns("level1,level2,level3")
    public Entity newLevel(SpawnData data) throws FileNotFoundException {
        return entityBuilder(data)
                .at(data.getX(),data.getY())
                .with(new CardComponent(String.valueOf(data.getData().get("spawnName"))))
                .build();
    }
    @Spawns("f_level1,f_level2,f_level3")
    public Entity newF_level(SpawnData data){
        return entityBuilder(data)
                .at(data.getX(),data.getY())
                .with(new FirstCardComponent(String.valueOf(data.getData().get("spawnName"))))
                .build();
    }
    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        return entityBuilder(data)
                .with(new PlayerComponent())
                .at(data.getX(),data.getY())
                .build();
    }
    
    @Spawns("otherPlayers")
    public Entity newOtherPlayers(SpawnData data) {
    	return entityBuilder(data)
    			.with(new OtherPlayersComponent())
    			.at(data.getX(), data.getY())
    			.build();
    }
    
    @Spawns("coin")
    public Entity newCoin(SpawnData data){
        return entityBuilder(data)
                .with(new CoinComponent(data.get("style")))
                .at(data.getX(),data.getY())
                .build();
    }
    @Spawns("noble")
    public Entity newNoble(SpawnData data){
        return entityBuilder(data)
                .with(new NobleComponent())
                .at(data.getX(),data.getY())
                .build();
    }


    //@Spawns("back6")
    @Spawns("back-ingame")
    public Entity newBack(SpawnData data) {
    	return FXGL.entityBuilder(data)
    			.view("back-ingame.png")
    			.build();
    }
    
    @Spawns("matching")
    public Entity newMat(SpawnData data) {
    	
    	ArrayList<Image> imgList = new ArrayList<>();
    	for (int i=0; i<5; i++) {
    		imgList.add(FXGL.image(String.format("gem/gem_%02d.png", i)));
    	}
    	
    	
    	AnimationChannel ac = new AnimationChannel(imgList, Duration.seconds(0.5));
    	AnimatedTexture at = new AnimatedTexture(ac);
    	at.loop();
    	
		return FXGL.entityBuilder(data)
				.view(at)
				.build();
    	
    }

}
