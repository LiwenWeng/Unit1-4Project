import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

public class Battle {
    private volatile static boolean fightOver;
    private volatile static Queue<String> choiceQueue;
    private volatile static boolean onCD;
    private volatile static boolean isBlocking;
    private volatile static boolean stunned;
    private static int blockStreak;
    private static String title = Utils.color("""
         ___ _      _   ___ ___ _  _  ___  _    ___  ___ ___ 
        | _ \\ |    /_\\ / __| __| || |/ _ \\| |  |   \\| __| _ \\
        |  _/ |__ / _ \\ (__| _|| __ | (_) | |__| |) | _||   /
        |_| |____/_/ \\_\\___|___|_||_|\\___/|____|___/|___|_|_\\

        -----------------------------
                                           """, "Purple");

    public static void start(Player player, Enemy[] enemies, Scanner scan, int wave) {
        Utils.clearScreen();
        countdown(3, wave);

        fightOver = false;
        choiceQueue = new LinkedList<>();
        onCD = false;
        isBlocking = false;
        stunned = false;
        blockStreak = 5;

        startAllCD(player);
        Thread[] enemyAttackCDThreads = startAllEnemyCD(enemies, player);
        Thread playerChoiceThread = playerChoice(scan);
        player.resetHealth();
        while (!fightOver) {
            Utils.clearScreen();
            System.out.println();
            System.out.println(title);
            System.out.println(Utils.color("Wave " + wave, "Blue", true));
            if (player.isDead() || remainingEnemies(enemies) == 0) {
                fightOver = true;
            }

            System.out.println(Utils.color("-----------------------------", "Pink"));
            for (Enemy enemy : enemies) {
                enemy.printInfo();
                System.out.println(Utils.color("[]", "Red").repeat(enemy.getCurrentCD()));
                System.out.println(Utils.color("-----------------------------\n", "Pink"));
            }

            player.printInfo(blockStreak);
            playerInput(player, enemies, scan);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        Utils.clearScreen();
        try {
            playerChoiceThread.join();
            for (Thread thread : enemyAttackCDThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {}
        choiceQueue.clear();
    }

    private static void countdown(int duration, int wave) {
        System.out.println(title + "\n");

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= duration * 1000) {
            long currentTime = System.currentTimeMillis();
            if ((startTime - currentTime) % 1000 == 0) {
                Utils.clearLine();
                System.out.println("Wave " + wave + " starting in... " + (duration - ((currentTime - startTime) / 1000)));
            }
        }
    }

    private static void startAllCD(Player player) {
        for (int i = 0; i < player.getSkillAmount(); i++) {
            startCD(player, i);
        }
    }

    private static void startCD(Player player, int skillIndex) {
        Runnable runnable = () -> {
            while (!fightOver) {
                player.decrementCD(skillIndex);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static Thread[] startAllEnemyCD(Enemy[] enemies, Player player) {
        Thread[] threads = new Thread[enemies.length];

        for (int i= 0; i < enemies.length; i++) {
            Thread thread = enemyAttack(enemies[i], player);
            threads[i] = thread;
        }

        return threads;
    }

    private static Thread enemyAttack(Enemy enemy, Player player) {
        Runnable runnable = () -> {
            while (!fightOver) {
                if (!enemy.isDead()) {
                    enemy.decrementCD();
                    if (enemy.getCurrentCD() <= 0) {
                        if (isBlocking) {
                            System.out.print(Utils.color("Blocked enemy attack!", "Green", true));
                        } else {
                            player.decCurrentHealth(enemy.getAttack());
                        }
                        enemy.resetCD();
                    }

                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    private static Thread playerChoice(Scanner scan) {
        Runnable runnable = () -> {
            while (!fightOver) {
                String userInput = scan.nextLine();
                if (userInput.matches("\\d+\\s\\d*|\\d")) {
                    choiceQueue.add(userInput);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    private static void globalCD(Player player, int skillIndex) {
        Runnable runnable = () -> {
            onCD = true;
            try {
                Thread.sleep((long) (player.getMaxCD(skillIndex) * 1000.0));
            } catch (Exception e) {
            }
            onCD = false;
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void startBlock() {
        Runnable runnable = () -> {
            isBlocking = true;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            isBlocking = false;
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void playerInput(Player player, Enemy[] enemies, Scanner scan) {
        System.out.print("> ");

        if (!choiceQueue.isEmpty()) {
            if (stunned) {
                System.out.println(Utils.color("You are stunned!", "Red"));
                return;
            }

            if (!onCD) {
                String choice = choiceQueue.poll();
                int index = Integer.parseInt(choice.indexOf(" ") == -1 ? choice : choice.substring(0, choice.indexOf(" "))) - 1;
                int defaultEnemy = 0;

                for (int i = 0; i < enemies.length; i++) {
                    if (!enemies[i].isDead()) {
                        defaultEnemy = i;
                        break;
                    }
                }
                int enemyToAttack = choice.indexOf(" ") == -1 ? defaultEnemy : Integer.parseInt(choice.substring(choice.indexOf(" ") + 1)) - 1;

                if (index == 0) {
                    enemies[enemyToAttack].takeDamage(player.getAttack());
                    blockStreak = 5;

                } else if (index == 1) {
                    startBlock();
                    blockStreak--;
                    if (blockStreak == 0) {
                        blockStreak = 5;
                        blockStun();
                    }

                } else if (index == 2 && player.hasSkill(index)) {
                    enemies[enemyToAttack].takeDamage((int) Math.round(player.getAttack() * 2.0));
                    blockStreak = 5;

                } else if (index == 3 && player.hasSkill(index)) {
                    for (Enemy enemy : enemies) {
                        enemy.takeDamage((int) Math.round(player.getAttack() * 0.8));
                    }
                    blockStreak = 5;

                } else if (index == 4 && player.hasSkill(index)) {
                    igniteEnemy(enemyToAttack, player, enemies);
                    blockStreak = 5;
                }

                player.resetCD(index);
                globalCD(player, index);
            } else {
                System.out.print(Utils.color("Skill on cooldown", "Red"));
            }
        }
    }

    private static void igniteEnemy(int enemyToAttack, Player player, Enemy[] enemies) {
        Runnable runnable = () -> {
            for (int i = 0; i < 8 && !enemies[enemyToAttack].isDead(); i++) {
                enemies[enemyToAttack].takeDamage((int) Math.round(player.getAttack() * 0.4));

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static int remainingEnemies(Enemy[] enemies) {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                count++;
            }
        }
        return count;
    }

    private static void blockStun() {
        Runnable runnable = () -> {
            stunned = true;
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            stunned = false;
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}