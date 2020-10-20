package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class utils {

	public static Stage getStageOfEvent(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryPaserInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> void formatTableCollumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(collum -> {
			return new TableCell<>() {
				private SimpleDateFormat dateFormat = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date date, boolean empty) {
					// TODO Auto-generated method stub
					super.updateItem(date, empty);
					if (empty) {
						setText(null);
					} else {
						setText(dateFormat.format(date));
					}
				}

			};
		});
	}

	public static <T> void formatTableCollumDouble(TableColumn<T, Double> tableColumn, int numberDecimals) {

		tableColumn.setCellFactory(collumn -> {
			TableCell<T, Double> cell = new TableCell<>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(String.format("%." + numberDecimals + "f", item));
					}
				}
			};
			return cell;
		});
	}
}
