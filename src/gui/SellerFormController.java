package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import db.MyDbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.utils;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	Seller seller;
	SellerService sellerService;
	List<DataChangeListener> onDataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textFieldId;
	@FXML
	private TextField textFieldName;
	@FXML
	private TextField textFieldEmail;
	@FXML
	private DatePicker datePickerBirthDate;
	@FXML
	private TextField textFieldBaseSalary;
	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;

	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorDate;
	@FXML
	private Label labelErrorSalary;

	private Stage stage;
	private DepartmentService departmentSevice;
	private ObservableList<Department> obsDepartments;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initNodes();
	}

	@FXML
	public void onBtnSaveAction(ActionEvent event) {

		if (seller == null)
			throw new IllegalStateException("seller was null");
		if (sellerService == null)
			throw new IllegalStateException("sellerService was null");

		try {

			sellerService.save(getSellerFromForm());
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
		labelErrorName.setText("");
	}

	private void showErrors(ValidationException e) {
		Map<String, String> errors = e.getErrors();
		labelErrorName.setText(errors.get("textFieldName"));

	}

	private Seller getSellerFromForm() {

		ValidationException validateException = new ValidationException("Form errors!");

		if (textFieldName == null || textFieldName.getText() == null || textFieldName.getText().trim().isEmpty())
			validateException.addError("textFieldName", "Name is empty!");

		Seller d = new Seller();
		d.setId(utils.tryPaserInt(textFieldId.getText()));
		d.setName(textFieldName.getText());

		if (validateException.getErrors().size() > 0)
			throw validateException;

		return d;
	}

	private void initNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 80);
		Constraints.setTextFieldMaxLength(textFieldEmail, 120);
		utils.formatDatePicker(datePickerBirthDate, "dd/MM/yyyy");
		Locale.setDefault(Locale.US);
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		initializeCombobox();
	}

	public void setServices(SellerService sellerService, DepartmentService departmentSevice) {
		this.sellerService = sellerService;
		this.departmentSevice = departmentSevice;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public void updateFormData() {
		if (seller == null)
			throw new IllegalStateException("seller was null");
		textFieldId.setText(String.valueOf(seller.getId()));
		textFieldName.setText(seller.getName());
		textFieldEmail.setText(seller.getEmail());
		if (seller.getBirthDate() != null) {
			datePickerBirthDate
					.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		textFieldBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if (seller.getDepartment() != null)
			comboBoxDepartment.setValue(seller.getDepartment());
		else
			comboBoxDepartment.getSelectionModel().selectFirst();

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

	public void loadAssociateDepartments() {
		List<Department> list = departmentSevice.findAll();
		obsDepartments = FXCollections.observableList(list);
		comboBoxDepartment.setItems(obsDepartments);
	}

	private void initializeCombobox() {

		Callback<ListView<Department>, ListCell<Department>> callback = listView -> {
			ListCell<Department> cell = new ListCell<>() {
				@Override
				protected void updateItem(Department obj, boolean empty) {
					super.updateItem(obj, empty);
					setText(empty ? null : obj.getName());
				}

			};
			return cell;
		};

		comboBoxDepartment.setCellFactory(callback);
		comboBoxDepartment.setButtonCell(callback.call(null));

	}

}
