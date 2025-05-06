package frpmanagement;

public class CharacterFactory {
    public static Character createCharacter(String type, String name) {
        switch(type.toLowerCase()) {
            case "warrior":
                return new Warrior(name);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}

