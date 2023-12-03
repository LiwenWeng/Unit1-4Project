public class Enemy {
    private String name;
    private int maxHealth;
    private int currentHealth;
    private int baseAttack;
    private int attack;
    private int level;
    private int[] attackCD;
    public int currentCD;
    private int currentRotation;
    private boolean isDead;

    public Enemy(String name, int maxHealth, int baseAttack, int level, int[] attackCD) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attack = baseAttack * 2;
        this.baseAttack = baseAttack;
        this.level = level;
        this.attackCD = attackCD;
        this.currentCD = attackCD[0];
        this.currentRotation = 2;
        this.isDead = false;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentCD() {
        return currentCD;
    }

    public void decrementCD() {
        currentCD--;
    }

    public void resetCD() {
        if (currentRotation == 1) {
            currentCD = attackCD[0];
            attack = (int) Math.round(baseAttack * (attackCD[0] / 3.0 ));
            currentRotation++;

        } else if (currentRotation == 2) {
            currentCD = attackCD[1];
            attack = (int) Math.round(baseAttack * (attackCD[1] / 3.0));
            currentRotation++;

        } else {
            currentCD = attackCD[2];
            attack = (int) Math.round(baseAttack * (attackCD[2] / 3.0));
            currentRotation = 1;
        }
    }

    public void takeDamage(int dmg) {
        currentHealth -= dmg;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void printInfo() {
        if (!isDead) {
            System.out.println(Utils.color("Lvl. " + getLevel(), "Yellow") + " " + getName() + " - " + Utils.color(getCurrentHealth(), "Green") + "/" + Utils.color(getMaxHealth(), "Green"));

        } else {
            System.out.println(Utils.color("Lvl. " + getLevel() + " " + getName() + " - Dead", "Gray"));
        }
    }
}