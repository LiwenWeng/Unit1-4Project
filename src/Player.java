
/**
 * This class represents a Player object
 *
 * @author Oscar Chong, Ojiro Moy, Li Wen Weng
 */

import java.util.Scanner;

public class Player {
    /** The name of the player */
    private String name;
    /** The level of the player */
    private int level;
    /** The max health of the player */
    private int maxHealth;
    /** The current health of the player */
    private int currentHealth;
    /** The attack power of the player */
    private int attack;
    /** The Skills of the player */
    private String[][] skills;
    private boolean isDead;
    /** The scanner object */
    private Scanner scan;

    /**
     * Initiates a Player object
     *
     * @param name The name
     * @param scan The scanner object
     */
    public Player(String name, Scanner scan) {
        this.name = Utils.color(name, "Purple", true);
        this.scan = scan;
        this.level = 1;
        this.maxHealth = 100;
        this.currentHealth = 100;
        this.attack = 20;
        this.isDead = false;
        this.skills = new String[5][3];
        skills[0][0] = "Attack";
        skills[0][1] = "2";
        skills[0][2] = "0";
        skills[1][0] = "Block";
        skills[1][1] = "1";
        skills[1][2] = "0";
    }

    /**
     * Returns the current name of the player.
     *
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the attack power of the player.
     *
     * @return The attack power of the player
     */
    public int getAttack() {
        return attack;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Returns the max health of the player.
     *
     * @return The max health of the player
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Returns the current health of the player.
     *
     * @return The current health of the player
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Decreases the value of the players current health by a certain amount.
     *
     * @param health is the amount of health to decrease by
     */
    public void decCurrentHealth(int health) {
        currentHealth -= health;
        if (currentHealth <= 0) {
            isDead = true;
        }
    }

    public void resetHealth() {
        currentHealth = maxHealth;
    }

    /**
     * Returns the max cooldown of the players specified skill.
     *
     * @param skillIndex The index of the players skill
     *
     * @return The max cooldown of the players skill
     */
    public double getMaxCD(int skillIndex) {
        return Double.parseDouble(skills[skillIndex][1]);
    }

    /**
     * Returns the current cooldown of the players specified skill.
     *
     * @param skillIndex The index of the players skill
     *
     * @return The current cooldown of the players skill
     */
    public double getCurrentCD(int skillIndex) {
        return Double.parseDouble(skills[skillIndex][2]);
    }

    /**
     * Decrements the cooldown of the players specified skill by 0.5 seconds.
     *
     * @param skillIndex is the index of the skill to decrease the cooldown of
     */
    public void decrementCD(int skillIndex) {
        skills[skillIndex][2] = String.valueOf(getCurrentCD(skillIndex) - .5);
    }

    public int getSkillAmount() {
        int count = 0;
        for (String[] skill: skills) {
            if (skill[0] != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Resets the specified skills cooldown to its default value.
     *
     * @param skillIndex The index of the skill to be reset
     */
    public void resetCD(int skillIndex) {
        skills[skillIndex][2] = skills[skillIndex][1];
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean hasSkill(int skillIndex) {
        if (skills[skillIndex][0] != null) {
            return true;
        }
        return false;
    }

    /**
     * Tells the user the player has leveled up and asks user what attribute to
     * increase.
     * Levels 2, 4, 7 gives the player a new skill.
     */
    public void levelUp() {
        Utils.clearScreen();
        int choice;
        level++;
        attack *= 1.2;
        maxHealth *= 1.3;

        System.out.println(Utils.color("""
         ___ _      _   ___ ___ _  _  ___  _    ___  ___ ___ 
        | _ \\ |    /_\\ / __| __| || |/ _ \\| |  |   \\| __| _ \\
        |  _/ |__ / _ \\ (__| _|| __ | (_) | |__| |) | _||   /
        |_| |____/_/ \\_\\___|___|_||_|\\___/|____|___/|___|_|_\\

        -----------------------------
                                           """, "Purple"));

        System.out.println(
                name + " leveled up to " + Utils.color("level " + String.valueOf(level), "Yellow", true) + "!");

        if (level == 3) {
            skills[2][0] = "Downward Strike";
            skills[2][1] = "4";
            skills[2][2] = "0";
            System.out.println("\nYou obtained " + Utils.color("Downward Strike", "Yellow") + "!");
            System.out.println(Utils.color("Downward Strike", "Yellow") + " - Targets one enemy, deals 2x damage. Cooldown - 4s");


        } else if (level == 5) {
            skills[3][0] = "Cleaving Sweep";
            skills[3][1] = "5";
            skills[3][2] = "0";
            System.out.println("\nYou obtained " + Utils.color("Cleaving Sweep", "Blue") + "!");
            System.out.println(Utils.color("Cleaving Sweep", "Blue") + " - Targets all enemies, deals 0.8x damage. Cooldown - 5s");
        } else if (level == 8) {
            skills[4][0] = "Ignite";
            skills[4][1] = "3";
            skills[4][2] = "0";
            System.out.println("\nYou obtained " + Utils.color("Ignite", "Red") + "!");
            System.out.println(Utils.color("Ignite", "Red") + " - Targets one enemy, deals 2.5x damage over 8 seconds. Cooldown - 3s");
        }
        System.out.println();

        System.out.println("[1] - " + Utils.color("Health", "Green"));
        System.out.println("[2] - " + Utils.color("Attack", "Red"));

        choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.print("\nChoose a stat to increase: ");
            choice = scan.nextInt();
            Utils.clearLine();
        }

        if (choice == 1) {
            maxHealth += 50;
            currentHealth = maxHealth;
            System.out.println(Utils.color("Health", "Green") + " increased to "
                    + Utils.color(String.valueOf(maxHealth), "Green"));

        } else if (choice == 2) {
            attack += 10;
            System.out.println(Utils.color("Attack", "Red") + " increased to "
                    + Utils.color(String.valueOf(attack), "Red"));
        }

        System.out.print("\nPress enter to continue...");
        scan.nextLine();
        scan.nextLine();
    }

    /**
     * Prints the players current available skills and cooldowns of the skills.
     */
    public void printInfo(int blockStreak) {
        System.out.println(Utils.color("Lvl. " + getLevel(), "Yellow") + " " + getName() + " - "
                + Utils.color(getCurrentHealth(), "Green") + "/"
                + Utils.color(getMaxHealth(), "Green"));
        System.out.println(Utils.color("[]", "Blue").repeat(blockStreak));
        System.out.println(Utils.color("----------------------------------", "Pink"));

        for (int i = 0; i < skills.length; i++) {
            if (skills[i][0] == null)
                continue;

            String seconds = "";
            String skillName = Utils.color(skills[i][0], "Yellow");
            if (Double.parseDouble(skills[i][2]) <= 0) {
                seconds = "Ready";
                skillName = Utils.color(skills[i][0], "Yellow", true);
            } else {
                seconds = skills[i][2] + "s";
            }

            System.out.println(
                    "[" + Utils.color(i + 1, "Yellow") + "]: " + skillName + " " + "("
                            + Utils.color(seconds, "Red") + ")\n");
        }

        Utils.clearLine();
        System.out.println(Utils.color("----------------------------------", "Pink"));
    }
}