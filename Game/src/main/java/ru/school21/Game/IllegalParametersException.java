package ru.school21.Game;

public class IllegalParametersException extends Throwable {
    public IllegalParametersException() {
        System.out.println("IllegalParametersException");
        System.exit(-1);
    }
}
