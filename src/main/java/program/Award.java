package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/18.
 */
public class Award extends GameObject {

    private static BufferedImage img;

    static {
        try {
            String png = "program/static/award.png";
            img = ImageIO.read(Award.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Award(){
        super(80,80);
        this.image = img;
    }
}
