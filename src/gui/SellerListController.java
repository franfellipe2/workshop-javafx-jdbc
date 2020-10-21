package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.MyDbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	@FXML
	private Button btNew;
	@FXML
	private TableView<Seller> tableView;
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	@FXML
	private TableColumn<Seller, String> tableCollumnEmail;
	@FXML
	private TableColumn<Seller, Date> tableCollumnBirthDate;
	@FXML
	private TableColumn<Seller, Double> tableCollumnBaseSalary;
	@FXML
	TableColumn<Seller, Seller> tableColumnEDIT;
	@FXML
	TableColumn<Seller, Seller> tableColumnDelete;

	private SellerService sellerService;
	private ObservableList<Seller> obsList;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initNodes();
	}

	@FXML
	public void newSeller(ActionEvent event) {
		Seller seller = new Seller();
		createDialogForm(seller, utils.getStageOfEvent(event));
	}

	private void initNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableCollumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableCollumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		utils.formatTableCollumnDate(tableCollumnBirthDate, "dd/MM/yyyy");
		tableCollumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		utils.formatTableCollumDouble(tableCollumnBaseSalary, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableView.prefHeightProperty().bind(stage.heightProperty());

	}

	public void setSellerService(SellerService service) {
		this.sellerService = service;
	}

	public void updateTableView() {
		if (sellerService == null)
			throw new IllegalStateException("Field SellerService was null");
		obsList = FXCollections.observableArrayList(sellerService.findAll());
		tableView.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller seller, Stage parentStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SellerFormView.fxml"));
			Pane pane = loader.load();

			Scene scene = new Scene(pane);
			Stage dialogStage = new Stage();

			SellerFormController controller = loader.getController();
			controller.setStage(dialogStage);
			controller.setSeller(seller);
			controller.addOnDataChanhgeListener(this);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociateDepartments();
			controller.updateFormData();

			dialogStage.setTitle("Create new seller");
			dialogStage.setScene(scene);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDataUpdate() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, utils.getStageOfEvent((event))));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDelete.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeSeller(obj));
			}
		});
	}

	protected void removeSeller(Seller obj) {

		Optional<ButtonType> button = Alerts.showConfirmation("Remove Seller", "You confirm delete?");
		if (button.get() == ButtonType.OK) {
			try {
				sellerService.remove(obj);
				updateTableView();
			} catch (MyDbException e) {
				Alerts.showAlert("Error", null, "Error on delete Seller: " + e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
