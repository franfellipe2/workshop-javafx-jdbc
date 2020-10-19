package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onMenuItemSellerAction");
	}
	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("onMenuItemDepartmentAction");
	}
	@FXML
	public void onMenuItemAboutAction() {
		System.out.println("onMenuItemAboutAction");
	}

}
