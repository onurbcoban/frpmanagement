// Character.java (güncel hali)
package gui;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    private String name;
    private int level;
    private int experience;
    private int gold;
    private List<Item> inventory = new ArrayList<>();

    public Character(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.gold = 100;
    }

    public abstract void levelUp();

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return gold; }
    public List<Item> getInventory() { return inventory; }

    public void setLevel(int level) { this.level = level; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setGold(int gold) { this.gold = gold; }

    public void gainGold(int amount) { gold += amount; }

    public void gainXP(int xp) {
        experience += xp;
        while (experience >= 200) {
            if (this instanceof Levelable) {
                ((Levelable) this).levelUp();
            }
            experience -= 200;
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    @Override
    public String toString() {
        return name;
    }
}
