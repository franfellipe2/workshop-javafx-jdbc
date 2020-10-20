package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import gui.util.utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	DepartmentService departmentService;

	@FXML
	TextField textFieldId;
	@FXML
	TextField textFieldName;
	@FXML
	Button btnSave;
	@FXML
	Button btnCancel;
	@FXML
	Label labelNameError;

	@FXML
	public void onBtnSaveAction() {
		Department department = new Department();
		department.setName(textFieldName.getText());
		departmentService.createDepartment(department);
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Stage stage = utils.getStageOfEvent(event);
		stage.close();
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		constraintsNodes();
	}

	private void constraintsNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 30);
	}

}
