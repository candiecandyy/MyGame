package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by admin on 2017/5/22.
 * 木宝箱
 */
public class WoodBox extends GameObject{

    private static BufferedImage[] imgs = new BufferedImage[2];
    private String[] speaking;
    static {
        try {
            for(int i=0;i<imgs.length;i++){
                String png = "program/static/woodbox" +i+".png";
                imgs[i] = ImageIO.read(WoodBox.class.getClassLoader().getResourceAsStream(png));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WoodBox(){
        super(80,80);
        this.image = imgs[0];
        speaking = new String[]{"分数+10"};
    }

    /**
     * 被对话
     * @param hero
     * @param world
     */
    public void beSpeak(Hero hero,World world){
        if(this.state == ACTIVE){
            hero.setSpeaked(true);
            this.image = imgs[1];
            world.setSpeaking(speaking);
            World.score += 10;
            this.state = REMOVE;
        }
    }
}
