import java.sql.SQLOutput;
import java.util.Scanner;

public class Game {
    private static String input = null;
    private static String title = Utils.color("""
             _   _                       ____            _    
            | \\ | | _____  ___   _ ___  |  _ \\ _   _ ___| |__ 
            |  \\| |/ _ \\ \\/ / | | / __| | |_) | | | / __| '_ \\
            | |\\  |  __/>  <| |_| \\__ \\ |  _ <| |_| \\__ \\ | | |
            |_| \\_|\\___/_/\\_\\\\__,_|___/ |_| \\_\\\\__,_|___/_| |_|
                                                                                                                           
            -----------------------------
                                               """, "Purple");
    private static Enemy[][] waveEnemies = new Enemy[][] {
            // Wave 1
            new Enemy[] {
                    new Enemy(Utils.color("Zombie", "Green", true), 100, 18, 1, new int[] { 10, 10, 10 })
            },
            // Wave 2
            new Enemy[] {
                    new Enemy(Utils.color("Vampire", "Red", true), 200, 25, 2, new int[] { 10, 10, 10 })
            },
            //Wave 3
            new Enemy[] {
                    new Enemy(Utils.bold("Ghost"), 300, 35, 3, new int[] { 10, 5, 5 })
            },
            //Wave 4
            new Enemy[] {
                    new Enemy(Utils.bold("Skeleton"), 200, 20, 3, new int[] { 5, 5, 5 }),
                    new Enemy(Utils.color("Ogre", "Red", true), 500, 35, 5, new int[] { 18, 18, 18 })
            },
            //Wave 5
            new Enemy[] {
                    new Enemy(Utils.color("Vampire", "Red", true), 350, 35, 4, new int[] { 15, 10, 10 }),
                    new Enemy(Utils.color("Zombie", "Green", true), 420, 25, 3, new int[] { 10, 10, 10 })
            },
            //Wave 6
            new Enemy[] {
                    new Enemy(Utils.color("Alien", "Purple", true), 550, 30, 6, new int[] { 8, 4, 8 }),
                    new Enemy(Utils.color("Kraken", "Pink", true), 750, 50, 7, new int[] { 20, 15, 20 })
            },
            //Wave 7
            new Enemy[] {
                    new Enemy(Utils.color("Ogre", "Red", true), 500, 30, 5, new int[] { 20, 20, 20 }),
                    new Enemy(Utils.color("Ogre", "Red", true), 500, 30, 5, new int[] { 20, 20, 20 }),
                    new Enemy(Utils.color("Devil", "Red", true), 700, 40, 8, new int[] { 10, 10, 15 })
            },
            //Wave 8
            new Enemy[] {
                    new Enemy(Utils.bold("Cyborg"), 700, 40, 8, new int[] { 20, 20, 20 }),
                    new Enemy(Utils.bold("Cyborg"), 700, 40, 8, new int[] { 20, 20, 20 }),
                    new Enemy(Utils.color("Alien", "Purple", true), 900, 50, 7, new int[] { 9, 5, 9 })
            },
            //Wave 9
            new Enemy[] {
                    new Enemy(Utils.bold("Wrench"), 550, 3, 5, new int[] { 3, 3, 3 }),
                    new Enemy(Utils.bold("Wrench"), 550, 3, 5, new int[] { 3, 3, 3 }),
                    new Enemy(Utils.bold("Wrench"), 550, 3, 5, new int[] { 3, 3, 3 }),
                    new Enemy(Utils.bold("Wrench"), 550, 3, 5, new int[] { 3, 3, 3 }),
                    new Enemy(Utils.bold("Wrench"), 550, 3, 5, new int[] { 3, 3, 3 }),
                    new Enemy(Utils.color("Bob", "Yellow", true), 2000, 100, 9, new int[] { 12, 6, 12 })
            },

            //Wave 10
            new Enemy[] {
                    new Enemy(Utils.color("Dragon", "Green"), 10000, 1000, 10, new int[] { 25, 3, 10 }),
            }
    };

    public static void start() {
        Scanner scan = new Scanner(System.in);

        int menu = menu(scan);
        while (menu != 3) {
            if (menu == 0) {
                Utils.clearScreen();
                System.out.println(title);
                System.out.print(Utils.color("Enter your name: ", "DarkBlue"));
                Player player = new Player(scan.nextLine(), scan);
                startGame(player, scan);

            } else if (menu == 1) {
                printTutorial(scan);
                menu = menu(scan);

            } else if (menu == 2) {
                printCredits(scan);
                menu = menu(scan);
            }
        }
    }

    public static void test(Player player, int wave, Scanner scan) {
        for (int i = 1; i < wave; i++) {
            player.levelUp();
        }
        Battle.start(player, waveEnemies[wave-1], scan, wave);
    }

    private static void printTutorial(Scanner scan) {
        int currentCount = 10;
        Runnable runnable = () -> {
            input = scan.nextLine();
        };
        Thread thread = new Thread(runnable);
        thread.start();

        while (input == null) {
            Utils.clearScreen();
            currentCount--;
            if (currentCount <= 0) {
                currentCount = 10;
            }
            System.out.println(Utils.color("""
     _____ _   _ _____ ___  ___ ___   _   _    
    |_   _| | | |_   _/ _ \\| _ \\_ _| /_\\ | |   
      | | | |_| | | || (_) |   /| | / _ \\| |__ 
      |_|  \\___/  |_| \\___/|_|_\\___/_/ \\_\\____|
    
    -------------------------
                                           """, "Yellow"));
            System.out.println("Welcome to Nexus Rush!");
            System.out.println(Utils.color("------------------------------------------------------", "Yellow"));
            System.out.println("Fight through 10 waves of enemies to win.");
            System.out.println();
            System.out.println("Each enemy will have an attack bar that looks like this: ");
            System.out.println(Utils.color("[]", "Red").repeat(currentCount));
            System.out.println("When this bar reaches 0, the enemy will attack and you will take damage.");
            System.out.println(Utils.color("------------------------------------------------------", "Yellow"));
            System.out.println("You start with the following skills: ");
            System.out.println("Attack - deals damage to one enemy. Cooldown: 2s");
            System.out.println("Block - blocks all damage recieved for the next 1 second. Cooldown: 1s");
            System.out.println("You can only use one skill at a time; you may not use another skill until all your skills are off cooldown.");
            System.out.println(Utils.color("------------------------------------------------------", "Yellow"));
            System.out.println("You will have a block bar that looks like this: ");
            System.out.println(Utils.color("[][][][][]", "Blue"));
            System.out.println("Each consecutive uses of block will decrease the bar by one,");
            System.out.println("once this reaches 0, you will be stunned for 3 seconds.");
            System.out.println(Utils.color("------------------------------------------------------", "Yellow"));
            System.out.println("Defeating all enemies in a wave will reset your health and you will move on to the next wave.");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        try {
            thread.join();
        } catch(Exception e) {}
    }

    private static void printCredits(Scanner scan) {
        Utils.clearScreen();
        System.out.println(Utils.color("""
  ___ ___ ___ ___ ___ _____ ___ 
 / __| _ \\ __|   \\_ _|_   _/ __|
| (__|   / _|| |) | |  | | \\__ \\
 \\___|_|_\\___|___/___| |_| |___/

----------------
                                       """, "Blue"));

        System.out.println("Made by: Oscar Chong, Ojiro Moy, and Liwen Weng");
        scan.nextLine();
    }

    private static int menu(Scanner scan) {
        int currentIndex = 0;
        String input = " ";
        String[] options = new String[]{
                "> " + "[" + Utils.color(" PLAY ", "Green") + "]",
                "  " + "[" + Utils.color(" TUTORIAL ", "Yellow") + "]",
                "  " + "[" + Utils.color(" CREDITS ", "Blue") + "]",
                "  " + "[" + Utils.color(" LEAVE ", "Red") + "]"
        };

        while (!input.isEmpty()) {
            Utils.clearScreen();
            System.out.println(title);
            for (int i = 0; i < 4; i++) {
                System.out.println(options[i] + "\n");
            }
            input = scan.nextLine().toLowerCase();

            if (input.equals("s")) {
                if (currentIndex == 3) {
                    options[3] = options[3].replace("> ", "  ");
                    options[0] = options[0].replace("  ", "> ");
                    currentIndex = 0;
                } else {
                    options[currentIndex] = options[currentIndex].replace("> ", "  ");
                    options[currentIndex + 1] = options[currentIndex + 1].replace("  ", "> ");
                    currentIndex++;
                }
            } else if (input.equals("w")) {
                if (currentIndex == 0) {
                    options[3] = options[3].replace("  ", "> ");
                    options[0] = options[0].replace("> ", "  ");
                    currentIndex = 3;
                } else {
                    options[currentIndex] = options[currentIndex].replace("> ", "  ");
                    options[currentIndex - 1] = options[currentIndex - 1].replace("  ", "> ");
                    currentIndex--;
                }
            }
        }

        return currentIndex;
    }

    private static void startGame(Player player, Scanner scan) {
        for (int wave = 0; wave < 10; wave++) {
            Battle.start(player, waveEnemies[wave], scan, wave + 1);
            if (player.isDead()) {
                System.out.println(Utils.color("""
                          ___   _   __  __ ___    _____   _____ ___ _ 
                         / __| /_\\ |  \\/  | __|  / _ \\ \\ / / __| _ \\ |
                        | (_ |/ _ \\| |\\/| | _|  | (_) \\ V /| _||   /_|
                         \\___/_/ \\_\\_|  |_|___|  \\___/ \\_/ |___|_|_(_)

                                                                          """, "Red"));
                scan.nextLine();
                start();
                break;
            } else {
                if (wave != 9) {
                    player.levelUp();
                }
            }
        }

        System.out.println(Utils.color("""
                   ___ ___  _  _  ___ ___    _ _____ _   _ _      _ _____ ___ ___  _  _ ___ _
                  / __/ _ \\| \\| |/ __| _ \\  /_\\_   _| | | | |    /_\\_   _|_ _/ _ \\| \\| / __| |
                 | (_| (_) | .` | (_ |   / / _ \\| | | |_| | |__ / _ \\| |  | | (_) | .` \\__ \\_|
                  \\___\\___/|_|\\_|\\___|_|_\\/_/ \\_\\_|  \\___/|____/_/ \\_\\_| |___\\___/|_|\\_|___(_)
                                                                                             
                """, "Yellow"));
        System.out.println("You beat the game!");
        scan.nextLine();
    }
}