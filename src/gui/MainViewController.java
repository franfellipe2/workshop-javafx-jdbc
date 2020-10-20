package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

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
		loadView("SellerList", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});	
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("DepartmentList", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});		
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("About");
	}

	private synchronized void loadView(String fxmlPath) {
		loadView(fxmlPath, x -> {
		});
	}

	private synchronized <T> void loadView(String fxmlPath, Consumer<T> callback) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/" + fxmlPath + "View.fxml"));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node menuBar = mainVBox.getChildren().get(0);

			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(menuBar);
			mainVBox.getChildren().addAll(newVBox.getChildren());			

			T controller = loader.getController();
			callback.accept(controller);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
