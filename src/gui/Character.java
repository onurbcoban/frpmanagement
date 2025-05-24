// Character.java
package gui;

import java.util.ArrayList;
import java.util.List;

// Soyut karakter sınıfı – tüm karakterlerin temel özellikleri burada tanımlanır
// OOP: Abstraction – karakterler için ortak davranışlar soyut sınıfla tanımlanır
public abstract class Character {
    // OOP: Encapsulation – değişkenler private, erişim sadece metodlarla sağlanır
    private String name;
    private int level;
    private int experience;
    private int gold;
    private List<Item> inventory = new ArrayList<>(); // Karakterin sahip olduğu item listesi

    // Karakter oluşturulurken başlangıç değerleri atanır
    public Character(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.gold = 100;
    }

    // Her karakter kendi seviye atlama davranışını tanımlar
    // OOP: Polymorphism – alt sınıflar bu metodu kendine göre override eder
    public abstract void levelUp();

    // Getter metotları – Encapsulation
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return gold; }
    public List<Item> getInventory() { return inventory; }

    // Setter metotları – değer güncellemeleri için kullanılır
    public void setLevel(int level) { this.level = level; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setGold(int gold) { this.gold = gold; }

    // Karaktere altın eklemek için
    public void gainGold(int amount) { gold += amount; }

    // XP kazanıldığında; 200 XP'yi geçtiğinde otomatik level up yapılır
    public void gainXP(int xp) {
        experience += xp;
        while (experience >= 200) {
            // OOP: Interface & instanceof – Levelable arayüzüne göre levelUp çağrılır
            if (this instanceof Levelable) {
                ((Levelable) this).levelUp();
            }
            experience -= 200; // XP 200'ü geçerse yeni level'e geçilir
        }
    }

    // Envantere item ekleme işlemi
    public void addItem(Item item) {
        inventory.add(item);
    }

    // Envanterden item çıkarma işlemi
    public void removeItem(Item item) {
        inventory.remove(item);
    }

    // Karakteri yazdırırken sadece ismini göstermek için
    @Override
    public String toString() {
        return name;
    }
}
