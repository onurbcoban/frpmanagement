// Rogue.java
package gui;

// OOP: Inheritance – Rogue sınıfı, Character sınıfından türetilmiştir
// OOP: Polymorphism – Kendi levelUp metodunu tanımlar
public class Rogue extends Character implements Levelable {
    public Rogue(String name) {
        super(name);
    }

    @Override
    public void levelUp() {
        int newLevel = getLevel() + 1;
        setLevel(newLevel);
        System.out.println(getName() + " leveled up to " + newLevel);
    }
}