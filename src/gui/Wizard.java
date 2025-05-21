package gui;
//Wizard sınıfı, karakter sınıfını genişletir ve seviye atlama davranışını tanımlar
public class Wizard extends Character implements Levelable {
 public Wizard(String name) {
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
