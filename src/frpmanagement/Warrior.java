package frpmanagement;

public class Warrior extends Character implements Levelable {
    public Warrior(String name) {
        super(name);
    }

    @Override
    public void levelUp() {
        int newLevel = getLevel() + 1;
        setLevel(newLevel);
        setExperience(0);
        System.out.println(getName() + " leveled up to " + newLevel);
    }
}

