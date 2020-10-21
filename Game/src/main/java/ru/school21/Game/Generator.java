package ru.school21.Game;

import java.awt.*;
import java.util.Properties;
import java.util.Random;

import static com.diogonunes.jcolor.Ansi.colorize;
import com.diogonunes.jcolor.Attribute;
import ru.school21.ChaseLogic.*;

public class Generator {
    private char[][] map;
    private Parser parser;
    private Properties prop;
    private Random random;
    private Point playerCoord;
    private Point goalCoord;
    private Point[] enemyCoord;
    private ChaseLogic logic;

    public Generator(Parser parser, Properties prop) {
        this.parser = parser;
        this.prop = prop;

        for (String key : prop.stringPropertyNames()) {
            if (prop.getProperty(key).equals(""))
                prop.setProperty(key, " ");
        }

        random = new Random();
        map = new char[parser.getSize()][parser.getSize()];
        playerCoord = new Point();
        goalCoord = new Point();
        enemyCoord = new Point[parser.getEnemiesCount()];
        for (int i = 0; i < parser.getEnemiesCount(); i++) {
            enemyCoord[i] = new Point();
        }
    }

    public char[][] generate() {
        this.createMap();
//        this.printMap(map);

        this.addWall();
//        this.printMap(map);

        this.addPlayer();
        this.addGoal();
        if (!this.checkPath()) {
            this.generate();
        } else {
            this.addEnemy();
            this.printMoves(0);
            this.printMap(map);
        }
        return (map);
    }

    private void createMap() {
        for (int i = 0; i < parser.getSize(); i++) {
            for (int j = 0; j < parser.getSize(); j++) {
                map[i][j] = prop.getProperty("empty.char").charAt(0);
            }
        }
    }

    private void addWall() {
        for (int countWall = 0; countWall < parser.getWallsCount(); ) {
            int x = random.nextInt(parser.getSize());
            int y = random.nextInt(parser.getSize());
            if (map[x][y] == prop.getProperty("empty.char").charAt(0)) {
                map[x][y] = prop.getProperty("wall.char").charAt(0);
                countWall++;
            }
        }
    }

    private void addPlayer() {
        for (int countPlayer = 0; countPlayer < 1; ) {
            int x = random.nextInt(parser.getSize());
            int y = random.nextInt(parser.getSize());
            if (map[x][y] == prop.getProperty("empty.char").charAt(0)) {
                map[x][y] = prop.getProperty("player.char").charAt(0);
                playerCoord.setLocation(x, y);
                countPlayer++;
            }
        }
    }

    private void addGoal() {
        for (int countGoal = 0; countGoal < 1; ) {
            int x = random.nextInt(parser.getSize());
            int y = random.nextInt(parser.getSize());
            if (map[x][y] == prop.getProperty("empty.char").charAt(0)) {
                map[x][y] = prop.getProperty("goal.char").charAt(0);
                goalCoord.setLocation(x, y);
                countGoal++;
            }
        }
    }

    private void addEnemy() {
        for (int countEnemy = 0; countEnemy < parser.getEnemiesCount(); ) {
            int x = random.nextInt(parser.getSize());
            int y = random.nextInt(parser.getSize());
            if (map[x][y] == prop.getProperty("empty.char").charAt(0)) {
                map[x][y] = prop.getProperty("enemy.char").charAt(0);
                enemyCoord[countEnemy].setLocation(x, y);
                countEnemy++;
            }
        }
    }

    public boolean checkPath() {
        this.logic = new ChaseLogic(map, parser.getSize(), prop.getProperty("empty.char").charAt(0),
                prop.getProperty("goal.char").charAt(0), prop.getProperty("player.char").charAt(0));
        if (logic.isPossible(playerCoord, goalCoord, map, prop.getProperty("player.char").charAt(0))) {
            System.out.println("есть путь");
            return (true);
        } else {
            System.out.println("нет пути");
            return (false);
        }
    }

    public char[][] getMap() {
        return map;
    }

    public Parser getParser() {
        return parser;
    }

    public Properties getProp() {
        return prop;
    }

    public Random getRandom() {
        return random;
    }

    public Point getPlayerCoord() {
        return playerCoord;
    }

    public Point getGoalCoord() {
        return goalCoord;
    }

    public Point[] getEnemyCoord() {
        return enemyCoord;
    }

    public ChaseLogic getLogic() {
        return logic;
    }

    public void printMap(char[][] array) {
        for (char[] i : array) {
            for (char j : i) {
                if (j == prop.getProperty("player.char").charAt(0)) {
                    System.out.print(colorize(prop.getProperty("player.char"),
                            Colors.getColor(prop.getProperty("player.color")), Attribute.BLACK_TEXT()));
                }
                else if (j == prop.getProperty("enemy.char").charAt(0)) {
                    System.out.print(colorize(prop.getProperty("enemy.char"),
                            Colors.getColor(prop.getProperty("enemy.color")), Attribute.BLACK_TEXT()));
                }
                else if (j == prop.getProperty("wall.char").charAt(0)) {
                    System.out.print(colorize(prop.getProperty("wall.char"),
                            Colors.getColor(prop.getProperty("wall.color")), Attribute.BLACK_TEXT()));
                }
                else if (j == prop.getProperty("goal.char").charAt(0)) {
                    System.out.print(colorize(prop.getProperty("goal.char"),
                            Colors.getColor(prop.getProperty("goal.color")), Attribute.BLACK_TEXT()));
                }
                else if (j == prop.getProperty("empty.char").charAt(0)) {
                    System.out.print(colorize(prop.getProperty("empty.char"),
                        Colors.getColor(prop.getProperty("empty.color")), Attribute.BLACK_TEXT()));}
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public void printMoves(int step){
        if (step == 0)
            System.out.println("Avalible coomands:\n1 - move left\n3 - move right\n2 - move down\n5 - move up\n9 - surrender\n");
        else if (step == 1)
            System.out.println("Avalible coomands:\n8 - step enemy\n");
    }
}
