package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	@FXML
	Button btNew;
	@FXML
	TableView<Department> tableView;
	@FXML
	TableColumn<Department, Integer> tableColumnId;
	@FXML
	TableColumn<Department, String> tableColumnName;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initNodes();
	}

	private void initNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableView.prefHeightProperty().bind(stage.heightProperty());
	}

}
