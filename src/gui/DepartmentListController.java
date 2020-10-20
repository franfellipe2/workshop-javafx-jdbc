package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	@FXML
	private Button btNew;
	@FXML
	private TableView<Department> tableView;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;

	private DepartmentService departmentService;
	private ObservableList<Department> obsList;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initNodes();
	}

	@FXML
	public void newDeparment(ActionEvent event) {
		Department department = new Department();
		createDialogForm(department, utils.getStageOfEvent(event));
	}

	private void initNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableView.prefHeightProperty().bind(stage.heightProperty());

	}

	public void setDepartmentService(DepartmentService service) {
		this.departmentService = service;
	}

	public void updateTableView() {
		if (departmentService == null)
			throw new IllegalStateException("Field DepartmentService was null");
		obsList = FXCollections.observableArrayList(departmentService.findAll());
		tableView.setItems(obsList);
	}

	private void createDialogForm(Department department, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DepartmentFormView.fxml"));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController();
			controller.setDepartment(department);
			controller.updateFormData();
			
			Scene scene = new Scene(pane);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Create new department");
			dialogStage.setScene(scene);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
