package application;

import controller.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/root.fxml"));
		Parent root = fxmlLoader.load();
		RootController rootController = fxmlLoader.getController();
		Scene scene = new Scene(root);
		rootController.stage = primaryStage;
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
