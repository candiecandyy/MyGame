package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by admin on 2017/5/22.
 * 女队友
 */
public class BetterWomen extends GameObject {

    private static BufferedImage img;
    private String[] speaking;

    static {
        try {
            String png = "program/static/betterwomen.png";
            img = ImageIO.read(BetterMen.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BetterWomen(){
        super(80,80);
        this.image = img;
        speaking = new String[]{"你变强了，骚年！","攻击力UP"};
    }

    /**
     * 被对话时调用
     * @param hero
     * @param world
     */
    public void beSpeak(Hero hero, World world){
        if(this.state == GameObject.ACTIVE){
            hero.setSpeaked(true);
            if(World.stage>=5){
                if(hero.getLife()<=3){
                    world.setSpeaking(new String[]{"天使给你的护盾哟！"});
                    hero.state = 4;
                    world.setHeroLife(hero.getLife());
                    this.state = GameObject.REMOVE;
                }else {
                    world.setSpeaking(new String[]{"要来一发吗？"});
                }
            }else {
                world.setSpeaking(speaking);
                hero.setHit(hero.getHit() + 1);
                hero.setLife(24);
                hero.setMeetButter(hero.getMeetButter() + 1);
                this.state = GameObject.REMOVE;
            }
        }
    }
}
