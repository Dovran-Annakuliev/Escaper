package ru.school21.Game;

import java.util.Random;

public class Li {
    public static boolean li(char[][] map) {
        Random random = new Random();
        if (random.nextInt(2) == 1)
            return true;
        else
            return false;
    }
}
