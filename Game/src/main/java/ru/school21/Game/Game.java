package ru.school21.Game;

import com.beust.jcommander.JCommander;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Game {
    private static char[][] map;

    public static void main(String[] args) throws IllegalParametersException, InterruptedException {
        Parser parser = new Parser();
        JCommander.newBuilder().addObject(parser).build().parse(args);
        parser.printArgs();
        parser.checkArg();

        Properties prop = new Properties();

        if (parser.getProfile().equals("development")){
            try {
                prop.load(Game.class.getResourceAsStream("/application-dev.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (parser.getProfile().equals("production")){
            try {
                prop.load(Game.class.getResourceAsStream("/application-production.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                prop.load(Game.class.getResourceAsStream("/application-custom.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String key : prop.stringPropertyNames()) {
            String value = prop.getProperty(key);
            System.out.println(key + " = " + value);
        }

        Generator gen = new Generator(parser, prop);
        map = gen.generate();

        Play play = new Play(gen);
        play.play();
    }
}
