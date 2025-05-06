package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CharacterApp extends Application {

    private Character currentCharacter;

    private Label infoLabel = new Label("No character selected.");
    private TextField nameField = new TextField();
    private ComboBox<String> classChoice = new ComboBox<>();
    private ComboBox<Character> characterList = new ComboBox<>();

    private Button createButton = new Button("Create Character");
    private Button addXPButton = new Button("Add 50 XP");
    private Button addGoldButton = new Button("Add 50 Gold");
    private Button levelUpButton = new Button("Level Up");
    private Button deleteButton = new Button("Delete Character");
    private Button resetButton = new Button("Reset Character");

    @Override
    public void start(Stage stage) {
        Database.initialize();

        nameField.setPromptText("Character Name");
        classChoice.getItems().addAll("Warrior");
        classChoice.setValue("Warrior");

        characterList.getItems().addAll(Database.getAllCharacters());

        createButton.setOnAction(e -> {
            createCharacter();
            characterList.getItems().clear();
            characterList.getItems().addAll(Database.getAllCharacters());
        });

        characterList.setOnAction(e -> {
            currentCharacter = characterList.getValue();
            updateInfo();
        });

        addXPButton.setOnAction(e -> {
            if (currentCharacter != null) {
                currentCharacter.gainXP(50);
                updateInfo();
            }
        });

        addGoldButton.setOnAction(e -> {
            if (currentCharacter != null) {
                currentCharacter.gainGold(50);
                updateInfo();
            }
        });

        levelUpButton.setOnAction(e -> {
            if (currentCharacter instanceof Levelable) {
                ((Levelable) currentCharacter).levelUp();
                updateInfo();
            }
            
        });
        deleteButton.setOnAction(e -> {
            if (currentCharacter != null) {
                Database.deleteCharacter(currentCharacter.getName());
                characterList.getItems().clear();
                characterList.getItems().addAll(Database.getAllCharacters());
                currentCharacter = null;
                infoLabel.setText("Character deleted.");
            }
        });

        resetButton.setOnAction(e -> {
            if (currentCharacter != null) {
                Database.resetCharacter(currentCharacter.getName());
                characterList.getItems().clear();
                characterList.getItems().addAll(Database.getAllCharacters());
                currentCharacter = characterList.getItems().stream()
                    .filter(c -> c.getName().equals(currentCharacter.getName()))
                    .findFirst().orElse(null);
                updateInfo();
            }
        });


        VBox root = new VBox(10,
                new Label("Create New Character:"),
                nameField,
                classChoice,
                createButton,
                new Label("Select Existing Character:"),
                characterList,
                addXPButton,
                addGoldButton,
                levelUpButton,
                resetButton,
                deleteButton,
                infoLabel
                
        );

        Scene scene = new Scene(root, 350, 400);
        stage.setTitle("FRP Character Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void createCharacter() {
        String name = nameField.getText();
        String type = classChoice.getValue();
        currentCharacter = CharacterFactory.createCharacter(type, name);
        Database.saveCharacter(currentCharacter, type);
        updateInfo();
    }	
    

    private void updateInfo() {
        if (currentCharacter != null) {
            infoLabel.setText("Name: " + currentCharacter.getName() +
                    "\nClass: " + classChoice.getValue() +
                    "\nLevel: " + currentCharacter.getLevel() +
                    "\nXP: " + currentCharacter.getExperience() +
                    "\nGold: " + currentCharacter.getGold());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
