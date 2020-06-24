package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;

public class Function {
	//알림창 만들기
	public static void getAlert(int type,String title, String headerText, String contentText) {
		Alert alert = null;
		switch(type) {
		case 1 : alert = new Alert(AlertType.CONFIRMATION);  break;
		case 2 : alert = new Alert(AlertType.ERROR);  break;
		case 3 : alert = new Alert(AlertType.INFORMATION);  break;
		case 4 : alert = new Alert(AlertType.WARNING);  break;
		}
		
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	}

