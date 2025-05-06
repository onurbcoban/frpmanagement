package frpmanagement;

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
    public void gainXP(int xp) { experience += xp; }

    protected void setLevel(int level) { this.level = level; }
    protected void setExperience(int experience) { this.experience = experience; }
}
