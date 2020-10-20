package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class utils {

	public static Stage getStageOfEvent(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();		
	}
}
