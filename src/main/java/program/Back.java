package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/18.
 * 游戏背景
 */
public class Back extends GameObject {

    private static BufferedImage img;

    static {
        try {
            String png = "program/static/back.png";
            img = ImageIO.read(Back.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Back(){
        this.width = 800;
        this.height = 600;
        this.image = img;
        this.x = 0.0;
        this.y = 0.0;
    }
}
