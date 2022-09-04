package interaction;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartInterface extends Application{
	
	public static void main(String[] args) {
        launch(args);
    }

	Stage stgInterface = null;
	@Override
	public void start(Stage primaryStage) throws Exception {
	    
		stgInterface = new Stage();
		stgInterface.setResizable(false);//设置无法调节窗口大小
		stgInterface.initStyle(StageStyle.UTILITY);//窗口只有退出
		stgInterface.initModality(Modality.APPLICATION_MODAL);//只能点改窗口
		
	}

}
