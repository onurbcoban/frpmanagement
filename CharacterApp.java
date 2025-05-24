package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CharacterApp extends Application {

    private Character currentCharacter;

    private Label infoLabel = new Label("No character selected.");
    private Label levelLabel = new Label("Level: ");
    private Label goldLabel = new Label("Gold: ");
    private Label experienceLabel = new Label("XP: ");
    private TextField nameField = new TextField();
    private ComboBox<String> classChoice = new ComboBox<>();
    private ComboBox<Character> characterList = new ComboBox<>();

    private Button createButton = new Button("Create Character");
    private Button addXPButton = new Button("Add 50 XP");
    private Button addGoldButton = new Button("Add 50 Gold");
    private Button levelUpButton = new Button("Level Up");
    private Button deleteButton = new Button("Delete Character");
    private Button resetButton = new Button("Reset Character");

    private ListView<Item> inventoryList = new ListView<>();
    private TextField itemNameField = new TextField();
    private TextField itemValueField = new TextField();
    private Button buyItemButton = new Button("Buy Item");
    private Button sellItemButton = new Button("Sell Item");
    private Button addItemButton = new Button("Add Item");
    private Button removeItemButton = new Button("Remove Item");
    private ComboBox<Item> itemSelectBox = new ComboBox<>();

    {
        itemNameField.setPromptText("Item Name");
        itemValueField.setPromptText("Item Value");
    }

    @Override
    public void start(Stage stage) {
        Database.initialize();

        nameField.setPromptText("Character Name");
        classChoice.getItems().addAll("Warrior", "Wizard", "Rogue");
        classChoice.setValue("Warrior");
        characterList.getItems().addAll(Database.getAllCharacters());
        itemSelectBox.getItems().addAll(Database.getAllItems());

        createButton.setOnAction(e -> {
            String name = nameField.getText();
            String type = classChoice.getValue();
            if (name == null || name.isEmpty()) {
                infoLabel.setText("Please enter a name.");
                return;
            }
            Character newChar = CharacterFactory.createCharacter(type, name);
            Database.saveCharacter(newChar, type);
            characterList.getItems().clear();
            characterList.getItems().addAll(Database.getAllCharacters());
            characterList.setValue(newChar);
            currentCharacter = newChar;
            updateInfo();
        });

        characterList.setOnAction(e -> {
            currentCharacter = characterList.getValue();
            updateInfo();
        });

        addXPButton.setOnAction(e -> {
            if (currentCharacter != null) {
                currentCharacter.gainXP(50);
                Database.updateCharacter(currentCharacter);
                updateInfo();
            }
        });

        addGoldButton.setOnAction(e -> {
            if (currentCharacter != null) {
                currentCharacter.gainGold(50);
                Database.updateCharacter(currentCharacter);
                updateInfo();
            }
        });

        levelUpButton.setOnAction(e -> {
            if (currentCharacter instanceof Levelable) {
                ((Levelable) currentCharacter).levelUp();
                Database.updateCharacter(currentCharacter);
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
                levelLabel.setText("Level: ");
                experienceLabel.setText("XP: ");
                goldLabel.setText("Gold: ");
                inventoryList.getItems().clear();
            }
        });

        resetButton.setOnAction(e -> {
            if (currentCharacter != null) {
                String name = currentCharacter.getName();
                Database.resetCharacter(name);
                characterList.getItems().clear();
                characterList.getItems().addAll(Database.getAllCharacters());
                currentCharacter = characterList.getItems().stream()
                        .filter(c -> c.getName().equals(name)).findFirst().orElse(null);
                updateInfo();
            }
        });

        buyItemButton.setOnAction(e -> {
            if (currentCharacter != null) {
                Item selected = itemSelectBox.getValue();
                if (selected != null && !currentCharacter.getInventory().contains(selected)) {
                    if (currentCharacter.getGold() >= selected.getValue()) {
                        currentCharacter.gainGold(-selected.getValue());
                        currentCharacter.addItem(selected);
                        Database.assignItemToCharacter(currentCharacter.getName(), selected.getName());
                        Database.updateCharacter(currentCharacter);
                        updateInfo();
                    } else {
                        infoLabel.setText("Not enough gold to buy item.");
                    }
                }
            }
        });

        sellItemButton.setOnAction(e -> {
            if (currentCharacter != null) {
                Item selected = inventoryList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    currentCharacter.gainGold(selected.getValue());
                    currentCharacter.removeItem(selected);
                    Database.removeItemFromCharacter(currentCharacter.getName(), selected.getName());
                    Database.updateCharacter(currentCharacter);
                    updateInfo();
                }
            }
        });

        addItemButton.setOnAction(e -> {
            try {
                String name = itemNameField.getText();
                int value = Integer.parseInt(itemValueField.getText());
                Item item = new Item(name, value);
                Database.saveItem(item);
                itemSelectBox.getItems().clear();
                itemSelectBox.getItems().addAll(Database.getAllItems());
                itemNameField.clear();
                itemValueField.clear();
                infoLabel.setText("Item added to database.");
            } catch (NumberFormatException ex) {
                infoLabel.setText("Invalid item value.");
            }
        });

        removeItemButton.setOnAction(e -> {
            if (currentCharacter != null) {
                Item selected = inventoryList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    currentCharacter.removeItem(selected);
                    Database.removeItemFromCharacter(currentCharacter.getName(), selected.getName());
                    updateInfo();
                }
            }
        });

        HBox topButtons = new HBox(10, addXPButton, addGoldButton);
        HBox bottomButtons = new HBox(10, resetButton, deleteButton);

        VBox createBox = new VBox(10, new Label("Create New Character:"), nameField, classChoice, createButton);
        VBox characterBox = new VBox(10, new Label("Select Character:"), characterList, topButtons, levelUpButton, bottomButtons);
        VBox infoBox = new VBox(5, infoLabel, levelLabel, experienceLabel, goldLabel);

        VBox itemBox = new VBox(10,
                new Label("Character Inventory:"), inventoryList,
                new Label("Buy/Sell Existing Item:"), itemSelectBox, buyItemButton, sellItemButton,
                new Label("Add/Remove Item:"), itemNameField, itemValueField, addItemButton, removeItemButton);
        itemBox.setPadding(new Insets(10));

        HBox mainLayout = new HBox(20, new VBox(15, createBox, characterBox, infoBox), itemBox);
        mainLayout.setPadding(new Insets(15));

        Scene scene = new Scene(mainLayout, 800, 500);
        stage.setTitle("FRP Character Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void updateInfo() {
        if (currentCharacter != null) {
            infoLabel.setText("Name: " + currentCharacter.getName() +
                    "\nClass: " + currentCharacter.getClass().getSimpleName());
            levelLabel.setText("Level: " + currentCharacter.getLevel());
            experienceLabel.setText("XP: " + currentCharacter.getExperience());
            goldLabel.setText("Gold: " + currentCharacter.getGold());
            inventoryList.getItems().setAll(currentCharacter.getInventory());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
