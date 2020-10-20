module workshopJavaFxJDBC {	
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;	
	opens application to javafx.graphics, javafx.fxml;
	opens gui to javafx.graphics, javafx.fxml;	
	opens model.entities to javafx.base;
}
