package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by admin on 2017/5/22.
 * 金宝箱
 */
public class GoldBox extends GameObject{

    private static BufferedImage[] imgs = new BufferedImage[2];
    private String[] speaking;
    static {
        try {
            for(int i=0;i<imgs.length;i++){
                String png = "program/static/goldbox" +i+".png";
                imgs[i] = ImageIO.read(GoldBox.class.getClassLoader().getResourceAsStream(png));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GoldBox(){
        super(80,80);
        this.image = imgs[0];
        speaking = new String[]{"分数+50"};
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
            this.image = imgs[1];
            World.score += 50;
            this.state = REMOVE;
        }
    }
}
