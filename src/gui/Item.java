package gui;

public class Item {
    private String name;
    private int value;

    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + " (" + value + "g)";
    }

    // İsteğe bağlı equals & hashCode -> silme işlemi için önerilir
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item)) return false;
        Item other = (Item) obj;
        return this.name.equals(other.name) && this.value == other.value;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + value;
    }
}
