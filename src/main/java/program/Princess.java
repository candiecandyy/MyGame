package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by admin on 2017/5/23.
 * 公主
 */
public class Princess extends GameObject{

    private static BufferedImage img;
    private String[] speaking;
    static {
        try {
            String png = "program/static/princess.png";
            img = ImageIO.read(Princess.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Princess(){
        super(80,80);
        this.image = img;
        speaking = new String[]{"公主：对不起……","公主：我被魔王玷污了！","公主：呜……呜呜……","我：当然是选择原谅你了！"};
    }

    /**
     * 被对话
     * @param hero
     * @param world
     */
    public void beSpeak(Hero hero,World world){
        if(this.state == ACTIVE){
            hero.setSpeaked(true);
            world.setSpeaking(speaking);
            this.state = REMOVE;
            world.canWin = true;
        }
    }
}
