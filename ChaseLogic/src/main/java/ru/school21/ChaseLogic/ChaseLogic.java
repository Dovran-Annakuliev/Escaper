package ru.school21.ChaseLogic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

class Node {

    boolean isEnable;
    int number;
    ArrayList<Node> near;

    public Node(Boolean isEnable, int number) {
        this.isEnable = isEnable;
        this.number = number;
        near = new ArrayList<Node>();
    }

    public void putNear(Node node) {
        this.near.add(node);
    }
}

class mapManager {
    private char[][] map;
    private int size;
    private Node[][] nodes;
    public char emptyChar;
    public char goalChar;
    public char playerChar;

    public mapManager(char[][] map, int size, char emptyChar, char goalChar, char playerChar) {
        this.map = map;
        this.size = size;
        this.nodes = new Node[size][size];
        this.emptyChar = emptyChar;
        this.goalChar = goalChar;
        this.playerChar = playerChar;
    }

    public void GetNodesArrey(char who) {
        for (int i = 0 ; i < size ; i++) {
            for (int j = 0 ; j < size ; j++) {
                if (who == playerChar) {
                    if (map[i][j] == emptyChar || map[i][j] == goalChar) {
                        nodes[i][j] = new Node(true, j + i * size);
                    } else {
                        nodes[i][j] = new Node(false, j + i * size);
                    }
                }
                else {
                    if (map[i][j] == emptyChar || map[i][j] == playerChar) {
                        nodes[i][j] = new Node(true, j + i * size);
                    } else {
                        nodes[i][j] = new Node(false, j + i * size);
                    }
                }
            }
        }
        for (int i = 0 ; i < size ; i++) {
            for (int j = 0 ; j < size ; j++) {
                if (i > 0) {
                    nodes[i][j].putNear(nodes[i - 1][j]);
                }
                if (j > 0) {
                    nodes[i][j].putNear(nodes[i][j - 1]);
                }
                if (i < size - 1) {
                    nodes[i][j].putNear(nodes[i + 1][j]);
                }
                if (j < size - 1) {
                    nodes[i][j].putNear(nodes[i][j + 1]);
                }
            }
        }
    }

    public void setMap(char[][] map, char Who) {
        this.map = map;
        GetNodesArrey(Who);
    }

    public int GetNumberOfAllNodes() {
        return size * size;
    }

    public Node GetNode(int i) {
        return (nodes[i / size][i % size]);
    }
}

public class ChaseLogic {

    private mapManager manager;
    private final int size;

    public ChaseLogic(char[][] map, int size, char emptyChar, char goalChar, char playerChar) {
        manager = new mapManager(map, size, emptyChar, goalChar, playerChar);
        this.size = size;
    }

    public int[] WavePropagation(int fromNode, int toNode) {

        int[] markedNode = new int[manager.GetNumberOfAllNodes()];
        int markNumber = 1;
        markedNode[fromNode] = markNumber;
        while (markedNode[toNode] == 0 && markNumber < (size * size) / 2) {
            for (int i = 0; i < markedNode.length; i++) {
                if (markedNode[i] == markNumber) {
                    for (int j = 0; j < manager.GetNode(i).near.size(); j++) {
                        if (markedNode[manager.GetNode(i).near.get(j).number] == 0
                                && manager.GetNode(i).near.get(j).isEnable) {
                            markedNode[manager.GetNode(i).near.get(j).number] = (markNumber + 1);
                        }
                    }
                }
            }
            markNumber++;
        }
        return markedNode;
    }

    public ArrayList<Integer> PathRecovery(int fromNode, int toNode, int[] markedNode) {

        ArrayList<Integer> paramsPaveTheRoute = new ArrayList<Integer>();

        paramsPaveTheRoute.add(toNode);
        Node currentNode = manager.GetNode(toNode);

        if (markedNode[toNode] != 0) {
            while (currentNode.number != fromNode) {
                for (int i = 0; i < currentNode.near.size(); i++) {
                    if (markedNode[manager.GetNode(currentNode.near.get(i).number).number]
                            == markedNode[currentNode.number] - 1) {
                        currentNode = manager.GetNode(currentNode.near.get(i).number);
                        paramsPaveTheRoute.add(currentNode.number);
                        if (fromNode == currentNode.number) {
                            break;
                        }
                    }
                }
            }
        }
        return paramsPaveTheRoute;
    }

    public boolean isPossibleStepDown(char map[][], Point enemyCoord) {
        if (enemyCoord.x + 1 < size) {
            if (map[enemyCoord.x + 1][enemyCoord.y] == manager.emptyChar) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossibleStepUp(char map[][], Point enemyCoord) {
        if (enemyCoord.x - 1 >= 0) {
            if (map[enemyCoord.x - 1][enemyCoord.y] == manager.emptyChar) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossibleStepLeft(char map[][], Point enemyCoord) {
        if (enemyCoord.y - 1 >= 0) {
            if (map[enemyCoord.x][enemyCoord.y - 1] == manager.emptyChar) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossibleStepRight(char map[][], Point enemyCoord) {
        if (enemyCoord.y + 1 < size) {
            if (map[enemyCoord.x][enemyCoord.y + 1] == manager.emptyChar) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossible(Point from, Point to, char[][] map, char who) {

        int fromNode = from.y + from.x * size;
        int toNode = to.y + to.x * size;

        this.manager.setMap(map, who);
        int[] markedNode = WavePropagation(fromNode, toNode);
        if (markedNode[toNode] == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Point enemyStep(Point enemyCoord, Point playerCoord, char[][] map) {
        int fromNode = enemyCoord.y + enemyCoord.x * size;
        int toNode = playerCoord.y + playerCoord.x * size;

        Point point = new Point();
        if (isPossible(enemyCoord, playerCoord, map, 'x')) {
            int[] markedNode = WavePropagation(fromNode, toNode);
            ArrayList<Integer> paramsPaveTheRoute = PathRecovery(fromNode, toNode, markedNode);
            point.x = paramsPaveTheRoute.get(paramsPaveTheRoute.size() - 2) / size;
            point.y = paramsPaveTheRoute.get(paramsPaveTheRoute.size() - 2) % size;
            return (point);
        } else {
            int wayX = Math.abs(enemyCoord.x - playerCoord.x);
            int wayY = Math.abs(enemyCoord.y - playerCoord.y);
            if (wayX <= wayY) {
                if (playerCoord.x < enemyCoord.x) {
                    if (isPossibleStepUp(map, enemyCoord)) {
                        point.y = enemyCoord.y;
                        point.x = enemyCoord.x - 1;
                        return point;
                    }
                } else {
                    if (isPossibleStepDown(map, enemyCoord)) {
                        point.y = enemyCoord.y;
                        point.x = enemyCoord.x + 1;
                        return point;
                    }
                }
            }
            if (playerCoord.y < enemyCoord.y) {
                if (isPossibleStepLeft(map, enemyCoord)) {
                    point.y = enemyCoord.y - 1;
                    point.x = enemyCoord.x;
                    return point;
                }
            } else {
                if (isPossibleStepRight(map, enemyCoord)) {
                    point.y = enemyCoord.y + 1;
                    point.x = enemyCoord.x;
                    return point;
                }
            }
            point.y = enemyCoord.y;
            point.x = enemyCoord.x;
            return point;
        }
    }
}
