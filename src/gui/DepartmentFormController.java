package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import db.MyDbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	Department department;
	DepartmentService departmentService;
	List<DataChangeListener> onDataChangeListeners = new ArrayList<>();

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

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		constraintsNodes();
	}

	@FXML
	public void onBtnSaveAction(ActionEvent event) {

		if (department == null)
			throw new IllegalStateException("department was null");
		if (departmentService == null)
			throw new IllegalStateException("departmentService was null");

		try {

			departmentService.save(getDepartmentFromForm());
			notifyOnDataChengeListeners();
			stage.close();

		} catch (ValidationException e) {
			showErrors(e);
		} catch (MyDbException e) {
			Alerts.showAlert("Form errors!", null, e.getMessage(), AlertType.ERROR);
		}

	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		/*
		 * Stage stage = utils.getStageOfEvent(event); stage.close();
		 */
		stage.close();
	}

	@FXML
	public void onTextFieldNameKeyPress() {
		labelNameError.setText("");
	}

	private void showErrors(ValidationException e) {
		Map<String, String> errors = e.getErrors();		
		labelNameError.setText(errors.get("textFieldName"));

	}

	private Department getDepartmentFromForm() {

		ValidationException validateException = new ValidationException("Form errors!");

		if (textFieldName == null || textFieldName.getText() == null || textFieldName.getText().trim().isEmpty())
			validateException.addError("textFieldName", "Name is empty!");

		Department d = new Department();
		d.setId(utils.tryPaserInt(textFieldId.getText()));
		d.setName(textFieldName.getText());

		if (validateException.getErrors().size() > 0)
			throw validateException;

		return d;
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

	public void addOnDataChanhgeListener(DataChangeListener listener) {
		onDataChangeListeners.add(listener);
	}

	public void removeOnDataChanhgeListener(DataChangeListener listener) {
		onDataChangeListeners.remove(listener);
	}

	private void notifyOnDataChengeListeners() {
		for (DataChangeListener dataChangeListener : onDataChangeListeners) {
			dataChangeListener.onDataUpdate();
		}
	}

}
