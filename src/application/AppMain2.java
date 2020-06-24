package application;

import controller.DBUtil;
import controller.MainRootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain2 extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		//FXML 파일 가져오기
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
		Parent p = fxmlLoader.load();
		Scene s= new Scene(p);
		// 컨트롤러에서 stage 가져오기
		MainRootController mainController = fxmlLoader.getController();
		mainController.stage = primaryStage;
		primaryStage.setScene(s);
		primaryStage.show();
	}
}
