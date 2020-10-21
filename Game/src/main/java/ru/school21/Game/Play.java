package ru.school21.Game;

import java.awt.*;
import java.util.Scanner;
import ru.school21.ChaseLogic.*;

public class Play {
    private Generator gen;

    public Play(Generator gen) {
        this.gen = gen;
    }

    public void play() throws InterruptedException {
        Scanner s = new Scanner(System.in);
        while (true) {
            playerMove(s);
            enemyMove(s);
        }
    }

    private void playerMove(Scanner s){
        boolean endMove = false;
        Point currentPoint = new Point(gen.getPlayerCoord());
        Point nextPoint = new Point();

        Flush.fl();
        gen.printMap(gen.getMap());
        gen.printMoves(0);
        while (!endMove){
            String move = s.nextLine();
            switch (move) {
                case "1" :
                    if (checkCoord(currentPoint.x, currentPoint.y - 1)) {
                        nextPoint.setLocation(currentPoint.x, currentPoint.y - 1);
                        endMove = true;
                    }
                    else
                        System.out.println("You cant move there!");
                    break;
                case "2" :
                    if (checkCoord(currentPoint.x + 1, currentPoint.y)) {
                        nextPoint.setLocation(currentPoint.x + 1, currentPoint.y);
                        endMove = true;
                    }
                    else
                        System.out.println("You cant move there!");
                    break;
                case "3" :
                    if (checkCoord(currentPoint.x, currentPoint.y + 1)) {
                        nextPoint.setLocation(currentPoint.x, currentPoint.y + 1);
                        endMove = true;
                    }
                    else
                        System.out.println("You cant move there!");
                    break;
                case "5" :
                    if (checkCoord(currentPoint.x - 1, currentPoint.y)) {
                        nextPoint.setLocation(currentPoint.x - 1, currentPoint.y);
                        endMove = true;
                    }
                    else
                        System.out.println("You cant move there!");
                    break;
                case "9" :
                    YOU_DIED();
                    break;
                default:
                    System.out.println("Wrong command!");
                    break;
            }
        }

        gen.getMap()[currentPoint.x][currentPoint.y] = gen.getProp().getProperty("empty.char").charAt(0);
        gen.getMap()[nextPoint.x][nextPoint.y] = gen.getProp().getProperty("player.char").charAt(0);
        gen.getPlayerCoord().setLocation(nextPoint.x, nextPoint.y);
        if (gen.getPlayerCoord().x == gen.getGoalCoord().x && gen.getPlayerCoord().y == gen.getGoalCoord().y){
            gen.printMap(gen.getMap());
            System.out.println("WIN!");
            System.exit(0);
        }
        gen.printMap(gen.getMap());
        if (gen.getParser().getProfile().equals("production"))
            gen.printMoves(0);
    }

    private void enemyMove(Scanner s) throws InterruptedException {
        for (int i = 0; i < gen.getEnemyCoord().length; i++){
            Flush.fl();
            gen.printMap(gen.getMap());
            System.out.format("Enemy left: %d\n\n", gen.getEnemyCoord().length - i);
            if (gen.getParser().getProfile().equals("development")) {
                gen.printMoves(1);
                boolean nextMove = false;
                while (!nextMove) {
                    String move = s.nextLine();
                    if (move.equals("8"))
                        nextMove = true;
                    else
                        System.out.println("Wrong command!");
                }
            }
            Point currentPoint = new Point(gen.getEnemyCoord()[i].x, gen.getEnemyCoord()[i].y);

            Point  tmpPoint = new Point(gen.getEnemyCoord()[i].x, gen.getEnemyCoord()[i].y);
            Point nextPoint;
            nextPoint = gen.getLogic().enemyStep(tmpPoint, gen.getPlayerCoord(), gen.getMap());

            if (nextPoint.x == gen.getPlayerCoord().x && nextPoint.y == gen.getPlayerCoord().y){
                YOU_DIED();
            }
            gen.getMap()[currentPoint.x][currentPoint.y] = gen.getProp().getProperty("empty.char").charAt(0);
            gen.getMap()[nextPoint.x][nextPoint.y] = gen.getProp().getProperty("enemy.char").charAt(0);
            gen.getEnemyCoord()[i].setLocation(nextPoint.x, nextPoint.y);

            Flush.fl();
            gen.printMap(gen.getMap());
            if (gen.getParser().getProfile().equals("development"))
                gen.printMoves(1);
            Thread.sleep(400);
        }
    }

    private boolean checkCoord(int x, int y){
        if (x >= 0 && x <= gen.getParser().getSize() - 1 && y >= 0 && y <= gen.getParser().getSize() - 1)
            return gen.getMap()[x][y] == gen.getProp().getProperty("empty.char").charAt(0) || gen.getMap()[x][y] == gen.getProp().getProperty("goal.char").charAt(0);
        return (false);
    }

    private void YOU_DIED(){
        System.out.println("YOU DIED");
        System.exit(0);
    }
}


