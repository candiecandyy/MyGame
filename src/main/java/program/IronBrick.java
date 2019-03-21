package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by admin on 2017/5/23.
 * boss关铁砖
 */
public class IronBrick extends GameObject{

    private static BufferedImage img;
    static {
        try {
            String png = "program/static/ironbrick.png";
            img = ImageIO.read(IronBrick.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IronBrick(){
        super(80,80);
        this.image = img;
        this.life = 1;
    }

}
