package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/17.
 * 钥匙
 */
public class Key extends GameObject{

    private static BufferedImage img;

    static {
        try {
            String png = "program/static/key.png";
            img = ImageIO.read(Key.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Key(){
        super(100,100);
        this.image = img;
    }
}
