package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/25.
 * 生命值
 */
public class Blood extends GameObject{

    private static BufferedImage img;

    static {
        try {
            String png = "program/static/blood.png";
            img = ImageIO.read(Blood.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Blood(){
        super(30,30);
        this.image = img;
        this.x = 0;
        this.y = 0;
    }
}
