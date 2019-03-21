package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/5/18.
 * 男队友
 */
public class BetterMen extends GameObject {

    private static BufferedImage img;
    private String[] speaking;
    static {
        try {
            String png = "program/static/bettermen.png";
            img = ImageIO.read(BetterMen.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BetterMen(){
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
                   world.setSpeaking(new String[]{"屠龙宝刀，点击就送！！！"});
                   hero.setHit(hero.getHit()+50);
                   hero.setMeetButter(hero.getMeetButter()+50);
                   this.state = GameObject.REMOVE;
               }else {
                   world.setSpeaking(new String[]{"战士：这是什么怪物","战士：吓尿了","我：..."});
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
