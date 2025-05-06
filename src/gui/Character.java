package gui;

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

    public abstract void levelUp();

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return gold; }

    public void gainGold(int amount) { gold += amount; }
    public void gainXP(int xp) {
        experience += xp;

        // Her 200 XP'de otomatik level up
        while (experience >= 200) {
            if (this instanceof Levelable) {
                ((Levelable) this).levelUp();
            }
            experience -= 200;
        }
    }


    protected void setLevel(int level) { this.level = level; }
    protected void setExperience(int experience) { this.experience = experience; }

    @Override
    public String toString() {
        return name + " (Lv " + level + ")";
    }
}
