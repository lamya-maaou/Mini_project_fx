package application;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.*;


public class Main extends Application {

    
	
    Button addButton = new Button("Ajouter");
    Button QuitButton = new Button("Quitter");
    Button updateButton = new Button("Update");
    Button deleteButton = new Button("Supprimer");
    
    TextField text = new TextField();
    PasswordField password = new PasswordField();
    
    ToggleGroup groupbut = new ToggleGroup();
    private ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    
    private TableView<Student> tableView;
    private TextField nomField;
    private TextField prenomField;
    private TextField cneField;
    private TextField idField;
    

    public void start(Stage primaryStage) {
    	
        connectToDatabase();

        // Création de la table des etudiant
        tableView = new TableView<>();
        tableView.setItems(list);
        // Ajouter un événement de sélection de ligne dans le TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                printStudentInfo(newSelection);
            }
        });

        TableColumn<Student, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Student, String> prenomColumn = new TableColumn<>("Prénom");
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Student, String> cneColumn = new TableColumn<>("CNE");
        cneColumn.setCellValueFactory(new PropertyValueFactory<>("cne"));

        TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        tableView.getColumns().addAll(idColumn, nomColumn, prenomColumn, cneColumn);
        
        // button delete

        // creation de la scene
        
        deleteButton.setOnAction(e -> deleteStudent());
        ToolBar toolbar = new ToolBar(addButton, deleteButton, updateButton);
        toolbar.setPadding(new Insets(20));
                
        VBox root = new VBox(20, tableView,toolbar);
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Scene scene = new Scene(root, 345, 400);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Page Etudiants ENSAO");
        primaryStage.setScene(scene);

        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evnt) {
                evnt.consume();
                fermer();

            }
        });

        // creation de la troisieme scene pour imprimer information de personne
        tableView.setOnMouseClicked(event -> {
            Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                printStudentInfo(selectedStudent);

                VBox infoBox = new VBox(20);
                Label idLabel = new Label("ID: " + selectedStudent. getId());
                Label nomLabel = new Label("Nom: " + selectedStudent. getNom());
                Label prenomLabel = new Label("Prénom: " + selectedStudent.getPrenom());
                Label cneLabel = new Label("Cne: " + selectedStudent.getCne());
                Scene sceneimprimer = new Scene(infoBox);
                infoBox.getChildren().addAll(idLabel, nomLabel, prenomLabel, cneLabel);

                Button btnRetour = new Button("retour");
                Button btnQuitter1 = new Button("Quitter");
                Button imprimer = new Button("imprimer");

                HBox buttonBox = new HBox(30, btnRetour, btnQuitter1, imprimer);
                buttonBox.setPadding(new Insets(20));
                buttonBox.setSpacing(20);
                updateButton.setOnAction(e -> updateStudent());
                VBox scene3Layout = new VBox(10, infoBox, buttonBox);
                scene3Layout.setPadding(new Insets(10));
                scene3Layout.setSpacing(10);
                Scene scene3 = new Scene(scene3Layout, 400, 500);
                scene3.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

                primaryStage.setScene(scene3);
                primaryStage.show();
                primaryStage.setTitle("Imprimation d'un etudiant");
                imprimer.setOnAction(e -> {
                    print(sceneimprimer.getRoot());
                });
                btnRetour.setOnAction(e -> {
                    primaryStage.setScene(scene);
                    text.setText("");
                    password.setText("");
                });

                btnQuitter1.setOnAction(e -> fermer());
            }

        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent evet) {

                nomField = new TextField();
                nomField.setPromptText("Nom");
                prenomField = new TextField();
                prenomField.setPromptText("Prenom");
                cneField = new TextField();
                cneField.setPromptText("Cne");
                idField = new TextField();
                idField.setPromptText("Id");

                Button btnretour = new Button("retour");
                Button btnquitter = new Button("Quitter");
                Button btnajouter = new Button("valider");
                ToolBar tool1 =new ToolBar(btnretour, btnquitter, btnajouter);
                VBox inputBox = new VBox(20, idField, nomField, prenomField, cneField,tool1);
                inputBox.getStyleClass().add("input-box");
                inputBox.setPadding(new Insets(20));
                inputBox.setSpacing(20);

                // Création de la disposition principale
                VBox root = new VBox(10, inputBox);
                root.setPadding(new Insets(10));
                root.setSpacing(10);
                Scene scene2 = new Scene(root, 400, 500);
                scene2.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
                primaryStage.setScene(scene2);

                primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    // pour ne pas fermer le travaille avant d'enregistrer
                    public void handle(WindowEvent evt) {
                        evt.consume();
                        fermer();
                    }

                });
                btnajouter.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent evnt) {
                        addStudent();
                    }
                });
                btnretour.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent evnt) {
                        primaryStage.setScene(scene);
                        text.setText("");
                        password.setText("");
                    }
                });
                btnquitter.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent eve) {
                        fermer();
                    }
                });

            }
        });// this car la class App est devenir handler donc tous les act
           // action ce fait dans la class handle
        QuitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                fermer();
            }

        });

        // Chargement des données depuis la base de données
        loadPeople();
    }

    private void updateStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            // Création de la scène de modification
            TextField nomField = new TextField(selectedStudent.getNom());
            TextField prenomField = new TextField(selectedStudent.getPrenom());
            TextField cneField = new TextField(selectedStudent.getCne());

            Button updateButton = new Button("Mettre à jour");
            updateButton.setOnAction(e -> {
                selectedStudent.setNom(nomField.getText());
                selectedStudent.setPrenom(prenomField.getText());
                selectedStudent.setCne(cneField.getText());
                try {
                    updateStudentInDatabase(selectedStudent);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                loadPeople();
                clearFields();
            });

            VBox inputBox = new VBox(10, nomField, prenomField, cneField, updateButton);
            inputBox.setPadding(new Insets(10));
            inputBox.setSpacing(10);

            Scene updateScene = new Scene(inputBox, 400, 300);
            updateScene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            Stage updateStage = new Stage();
            updateStage.setTitle("Modifier l'étudiant");
            updateStage.setScene(updateScene);
            updateStage.show();
            updateStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                // pour ne pas fermer le travaille avant d'enregistrer
                public void handle(WindowEvent evt) {
                    evt.consume();
                    fermer();
                }

            });
        } else {
            afficherAlerte("Erreur", "Veuillez sélectionner un étudiant à mettre à jour.");
        }

    }

    private void updateStudentInDatabase(Student student) throws SQLException {
        String query = "UPDATE students SET Nom=?, Prenom=?, Cne=? WHERE ID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, student.getNom());
        statement.setString(2, student.getPrenom());
        statement.setString(3, student.getCne());
        statement.setInt(4, student.getId());
        statement.executeUpdate();
        statement.close();
    }

    private void print(Node scene3) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean success = printerJob.showPrintDialog(null);
            if (success) {
                boolean printed = printerJob.printPage(scene3);
                if (printed) {
                    printerJob.endJob();
                }
            }
        }
    }

    private void printStudentInfo(Student student) {
        String id = String.valueOf(student.getId());
        String nom = student.getNom();
        String prenom = student.getPrenom();
        String cne = student.getCne();

    }

    public void fermer() {
        Alert confirmer = new Alert(Alert.AlertType.CONFIRMATION);
        confirmer.setTitle("confirmation de fermer");
        confirmer.setContentText("voulez vous vrais quitter ?");
        confirmer.setHeaderText(null);
        confirmer.setGraphic(null);
        confirmer.getButtonTypes().removeAll(ButtonType.CANCEL, ButtonType.OK);
        ButtonType btnOUI = new ButtonType("Oui");
        ButtonType btnNON = new ButtonType("Non");
        confirmer.getButtonTypes().addAll(btnOUI, btnNON);
        Optional<ButtonType> resulta = confirmer.showAndWait();
        if (resulta.get() == btnOUI) {
            System.exit(0);
        }

    }

    private void connectToDatabase() {
        try {
            // Modifier les informations de connexion en fonction de votre base de données
            String url = "jdbc:mysql://localhost:3306/ensao";
            String username = "root";
            String password = "Root@123";

            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPeople() {
        list.clear();

        try {
            resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String prenom = resultSet.getString("Prenom");
                String cne = resultSet.getString("Cne");
                int id = resultSet.getInt("ID");

                Student student = new Student(id, nom, prenom, cne);
                list.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStudent() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String cne = cneField.getText();
        int id = Integer.parseInt(idField.getText());

        try {
            Student newStudent = new Student(id, nom, prenom, cne);
            addStudent.addPerson(connection, newStudent);
            loadPeople();
            clearFields();
            loadPeople();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            try {
                delete.deleteStudent(connection, selectedStudent);
                list.remove(selectedStudent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            afficherAlerte("Erreur", "Veuillez sélectionner un étudiant à supprimer.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        cneField.clear();
        idField.clear();
        tableView.getSelectionModel().clearSelection();
    }

    public static void main(String[] args) {
        launch(args);
    }

}