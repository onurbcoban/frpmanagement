// Warrior.java
package gui;

// OOP: Inheritance – Warrior, Character sınıfından miras alır
// OOP: Polymorphism – levelUp metodu Warrior'a özgü şekilde override edilir
public class Warrior extends Character implements Levelable {
    public Warrior(String name) {
        super(name);
    }

    @Override
    public void levelUp() {
        int newLevel = getLevel() + 1;
        setLevel(newLevel);
        System.out.println(getName() + " leveled up to " + newLevel);
    }
}