// Wizard.java
package gui;

// OOP: Inheritance – Wizard, Character sınıfını genişletir
// OOP: Polymorphism – levelUp metodu override edilir
public class Wizard extends Character implements Levelable {
    public Wizard(String name) {
        super(name);
    }

    @Override
    public void levelUp() {
        int newLevel = getLevel() + 1;
        setLevel(newLevel);
        System.out.println(getName() + " leveled up to " + newLevel);
    }
}