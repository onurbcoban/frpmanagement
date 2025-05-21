package gui;
//Rogue sınıfı, karakter sınıfını genişletir ve seviye atlama davranışını tanımlar
public class Rogue extends Character implements Levelable {
 public Rogue(String name) {
     super(name);
 }

 // Seviye artırılır. XP sıfırlanmaz çünkü üst sınıf XP düşümünü yönetiyor.
 @Override
 public void levelUp() {
     int newLevel = getLevel() + 1;
     setLevel(newLevel);
     System.out.println(getName() + " leveled up to " + newLevel);
 }
}