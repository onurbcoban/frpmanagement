package gui;

// Karakterin temel soyut sınıfı. Tüm karakter tipleri buradan türetilir.
public abstract class Character {
    private String name;
    private int level;
    private int experience;
    private int gold;

    public Character(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.gold = 100;
    }

    // Alt sınıflar seviye atlama davranışını kendi içinde tanımlar
    public abstract void levelUp();

    // Getter metodları
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return gold; }

    // Gold miktarını artırır
    public void gainGold(int amount) {
        gold += amount;
    }

    // XP eklenir, her 200 XP'de otomatik level up yapılır
    public void gainXP(int xp) {
        experience += xp;
        while (experience >= 200) {
            if (this instanceof Levelable) {
                ((Levelable) this).levelUp();
            }
            experience -= 200;
        }
    }

    // Public setter'lar (veritabanından değerleri yüklemek için erişilebilir olmalı)
 // Character.java
    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }


    // ComboBox’ta gösterilecek string temsil
    @Override
    public String toString() {
        return name;
    }
}
