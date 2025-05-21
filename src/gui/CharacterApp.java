package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CharacterApp extends Application {

    // Seçilen karakteri tutar
    private Character currentCharacter;

    // Arayüz bileşenleri
    private Label infoLabel = new Label("No character selected.");
    private Label levelLabel = new Label("Level: ");
    private Label goldLabel = new Label("Gold: ");    
    private Label experienceLabel = new Label("Xp: ");    
    private TextField nameField = new TextField();
    private ComboBox<String> classChoice = new ComboBox<>();
    private ComboBox<Character> characterList = new ComboBox<>();

    // Butonlar
    private Button createButton = new Button("Create Character");
    private Button addXPButton = new Button("Add 50 XP");
    private Button addGoldButton = new Button("Add 50 Gold");
    private Button levelUpButton = new Button("Level Up");
    private Button deleteButton = new Button("Delete Character");
    private Button resetButton = new Button("Reset Character");

    @Override
    public void start(Stage stage) {
        Database.initialize(); // Veritabanını başlat (tablo yoksa oluştur)

        // Karakter adı ve sınıf seçimi alanı
        nameField.setPromptText("Character Name");
        classChoice.getItems().addAll("Warrior", "Wizard", "Rogue");
        classChoice.setValue("Warrior");
   
        // Veritabanındaki karakterleri ComboBox'a yükle
        characterList.getItems().addAll(Database.getAllCharacters());

        // Karakter oluşturulunca veritabanına kaydet ve listeyi güncelle
        createButton.setOnAction(createEvent -> {
            createCharacter();
            characterList.getItems().clear();
            characterList.getItems().addAll(Database.getAllCharacters());
        });

        // ComboBox'tan karakter seçildiğinde bilgileri göster
        characterList.setOnAction(selectEvent -> {
            currentCharacter = characterList.getValue();
            updateInfo();
        });

        // 50 XP ekle ve seviye kontrolü yap
        addXPButton.setOnAction(xpEvent -> {
            if (currentCharacter != null) {
                currentCharacter.gainXP(50);
                Database.updateCharacter(currentCharacter);
                updateInfo();
            }
        });

        // 50 altın ekle
        addGoldButton.setOnAction(goldEvent -> {
            if (currentCharacter != null) {
                currentCharacter.gainGold(50);
                Database.updateCharacter(currentCharacter);
                updateInfo();
            }
        });

        // Elle level up butonu (gerekmiyor ama duruyor)
        levelUpButton.setOnAction(levelEvent -> {
            if (currentCharacter instanceof Levelable) {
                ((Levelable) currentCharacter).levelUp();
                Database.updateCharacter(currentCharacter);
                updateInfo();
            }
        });

        // Karakteri veritabanından tamamen sil
        deleteButton.setOnAction(deleteEvent -> {
            if (currentCharacter != null) {
                Database.deleteCharacter(currentCharacter.getName());
                characterList.getItems().clear();
                characterList.getItems().addAll(Database.getAllCharacters());
                currentCharacter = null;
                infoLabel.setText("Character deleted.");
                levelLabel.setText("Level: ");
                experienceLabel.setText("Xp: ");
                goldLabel.setText("Gold: ");
            }
        });

        // Karakterin level, XP, gold değerlerini sıfırla
        resetButton.setOnAction(resetEvent -> {
            if (currentCharacter != null) {
                String name = currentCharacter.getName(); // önce adını sakla
                Database.resetCharacter(name); // veritabanında sıfırla

                // Listeyi güncelle ve karakteri yeniden bul
                characterList.getItems().clear();
                characterList.getItems().addAll(Database.getAllCharacters());
                currentCharacter = characterList.getItems().stream()
                    .filter(c -> c.getName().equals(name))
                    .findFirst().orElse(null);
                updateInfo();
            }
        });

        // Arayüz düzeni
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
                infoLabel,
                levelLabel,
                experienceLabel,
                goldLabel
        );


        // Sahne ve pencere ayarları
        Scene scene = new Scene(root, 350, 400);
        stage.setTitle("FRP Character Manager");
        stage.setScene(scene);
        stage.show();
    }

    // Yeni karakter oluşturur ve kaydeder
    private void createCharacter() {
        String name = nameField.getText();
        String type = classChoice.getValue();

        if (name == null || name.isEmpty()) {
            infoLabel.setText("Please enter a name.");
            return;
        }

        Character newChar = CharacterFactory.createCharacter(type, name);
        Database.saveCharacter(newChar, type);

        // Veritabanından geri al ve currentCharacter olarak ata:
        characterList.getItems().clear();
        characterList.getItems().addAll(Database.getAllCharacters());
        currentCharacter = characterList.getItems().stream()
            .filter(c -> c.getName().equals(name))
            .findFirst().orElse(null);
        updateInfo();

    }

    // Seçili karakterin bilgilerini GUI'de gösterir
    private void updateInfo() {
        if (currentCharacter != null) {
            infoLabel.setText("Name: " + currentCharacter.getName() +
                    "\nClass: " + currentCharacter.getClass().getSimpleName());
            levelLabel.setText("Level: " + currentCharacter.getLevel());
            experienceLabel.setText("Xp: " + currentCharacter.getExperience());
            goldLabel.setText("Gold: " + currentCharacter.getGold());
        }
    }

    // JavaFX uygulamasını başlatır
    public static void main(String[] args) {
        launch(args);
    }
}
