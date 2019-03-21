package program;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by admin on 2017/5/23.
 * 用户实体类
 */
public class Users implements Serializable{

    private String name;
    private int score;
    private String time;

    public Users() {
    }

    public Users(String name, int score, String time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "program.Users{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", time='" + time + '\'' +
                '}';
    }
}
