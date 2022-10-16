package nz.proj;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import nz.comp.*;

import java.io.FileNotFoundException;

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

    @Spawns("white_gem")
    public Entity newWhiteGem(SpawnData data) {
        return entityBuilder(data)
                .view("white.png")
                .build();
    }

    @Spawns("red_gem")
    public Entity newRedGem(SpawnData data) {
        return entityBuilder(data)
                .view("red.png")
                .build();
    }

    @Spawns("green_gem")
    public Entity newGreenGem(SpawnData data) {
        return entityBuilder(data)
                .view("green.png")
                .build();
    }

    @Spawns("blue_gem")
    public Entity newBlueGem(SpawnData data) {
        return entityBuilder(data)
                .view("blue.png")
                .build();
    }

    @Spawns("black_gem")
    public Entity newBlackGem(SpawnData data) {
        return entityBuilder(data)
                .view("black.png")
                .build();
    }

    //@Spawns("back6")
    @Spawns("back-ingame")
    public Entity newBack(SpawnData data) {
    	return FXGL.entityBuilder(data)
    			.view("back-ingame.png")
    			.build();
    }
    
    @Spawns("cutscene")
    public Entity newCut(SpawnData data) {
    	return FXGL.entityBuilder(data)
    			.view("S-castle.png")
    			.build();
    }

}
