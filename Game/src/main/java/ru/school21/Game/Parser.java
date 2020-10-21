package ru.school21.Game;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Parser {
    @Parameter(names = {"--enemiesCount", "-ec"}, description = "enemiesCount")
    private Integer enemiesCount = 1;

    @Parameter(names = {"--wallsCount", "-wc"}, description = "wallsCount")
    private Integer wallsCount = 1;

    @Parameter(names = {"--size", "-s"}, description = "size")
    private Integer size = 3;

    @Parameter(names = {"--profile", "-p"}, description = "profile")
    private String profile = "production";

    public Integer getEnemiesCount() {
        return enemiesCount;
    }

    public Integer getWallsCount() {
        return wallsCount;
    }

    public Integer getSize() {
        return size;
    }

    public String getProfile() {
        return profile;
    }

    public void checkArg() throws IllegalParametersException {
        if (enemiesCount + wallsCount + 2 > size * size) {
            throw new IllegalParametersException();
        }
    }

    public void printArgs() {
        System.out.format("enemiesCount = %d\twallsCount = %d\tsize = %d\tprofile = %s\n", enemiesCount, wallsCount, size, profile);
    }
}
