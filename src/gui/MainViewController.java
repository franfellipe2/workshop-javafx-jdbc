package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	MenuItem menuItemSeller;
	@FXML
	MenuItem menuItemDepartment;
	@FXML
	MenuItem menuItemAbout;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void onMenuItemSellerAction() {		
		Alerts.showAlert("onMenuItemSellerAction", null, "Seller action", AlertType.INFORMATION);
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("DepartmentList");
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("About");
	}

	private void loadView(String fxmlPath) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/"+fxmlPath+"View.fxml"));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node menuBar = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(menuBar);
			mainVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
