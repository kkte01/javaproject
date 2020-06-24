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
		//FXML ���� ��������
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
		Parent p = fxmlLoader.load();
		Scene s= new Scene(p);
		// ��Ʈ�ѷ����� stage ��������
		MainRootController mainController = fxmlLoader.getController();
		mainController.stage = primaryStage;
		primaryStage.setScene(s);
		primaryStage.show();
	}
}
