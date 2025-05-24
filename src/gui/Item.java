// Item.java
package gui;

// Oyunda kullanılan item sınıfı
// OOP: Encapsulation – name ve value değişkenleri private olarak tanımlanmış
// OOP: Object Responsibility – bu sınıf sadece bir item nesnesinin davranışlarını ve karşılaştırmalarını içerir
public class Item {
    private String name;
    private int value; // Altın değeri

    // Yapıcı metod – item oluştururken isim ve değer verilir
    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // OOP: Encapsulation – sadece getter ile dışarıya bilgi verilir
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    // GUI'de item'ı listelerken kullanıcı dostu bir metin döner
    @Override
    public String toString() {
        return name + " (" + value + "g)";
    }

    // OOP: Polymorphism – Object sınıfından gelen equals metodunu override ediyoruz
    // Item'ların eşitliği hem isme hem değere göre kontrol edilir
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item)) return false;
        Item other = (Item) obj;
        return this.name.equals(other.name) && this.value == other.value;
    }

    // hashCode metodunu eşitlik ile uyumlu hale getirir
    @Override
    public int hashCode() {
        return name.hashCode() + value;
    }
}
