package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/26.
 * 门
 */
public class Door extends GameObject{
    private static BufferedImage open;
    private static BufferedImage close;

    public static boolean opened = false;

    static {
        try {
            String png = "program/static/open.png";
            open = ImageIO.read(Door.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/close.png";
            close = ImageIO.read(Door.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Door(){
        super(80,80);
        this.x = -80;
        this.y = -80;
        this.image = close;
        this.state = ACTIVE;
    }

    /**
     * 检测门是否为打开状态
     */
    public void checkOpen(){
        if(state == DEAD){
            this.image = null;
            return;
        }
        if(opened){
            this.image = open;
        }else {
            this.image = close;
        }
    }
}
