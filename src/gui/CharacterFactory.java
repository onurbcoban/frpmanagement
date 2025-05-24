// CharacterFactory.java
package gui;

// OOP: Design Pattern – Factory Pattern kullanılarak karakter üretimi soyutlanmıştır
// OOP: Abstraction – nesne oluşturma süreci kullanıcıdan gizlenmiştir
public class CharacterFactory {
    public static Character createCharacter(String type, String name) {
        switch(type.toLowerCase()) {
            case "warrior":
                return new Warrior(name);
            case "wizard":
                return new Wizard(name);
            case "rogue":
                return new Rogue(name);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}