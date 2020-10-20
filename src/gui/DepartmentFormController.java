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

	Department department;
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

	private Stage stage;

	@FXML
	public void onBtnSaveAction(ActionEvent event) {

		if (department == null)
			throw new IllegalStateException("department was null");
		if (departmentService == null)
			throw new IllegalStateException("departmentService was null");

		Department department = new Department();
		department.setId(utils.tryPaserInt(textFieldId.getText()));
		department.setName(textFieldName.getText());
		departmentService.save(department);

		stage.close();
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		/*
		 * Stage stage = utils.getStageOfEvent(event); stage.close();
		 */
		stage.close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		constraintsNodes();
	}

	private void constraintsNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 30);
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void updateFormData() {
		if (department == null)
			throw new IllegalStateException("department was null");
		textFieldId.setText(String.valueOf(department.getId()));
		textFieldName.setText(department.getName());
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
