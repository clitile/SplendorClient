package nz.ui;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import nz.comp.*;

import java.io.FileNotFoundException;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class UIFactory implements EntityFactory{
    @Spawns("animation1")
    public Entity newBg(SpawnData data) {
    	return FXGL.entityBuilder(data)
    			.view("S-castle.png")
    			.build();
    }

}
