module Mini_project_javafx {
	requires javafx.controls;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
