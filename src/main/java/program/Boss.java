package program;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/26.
 * boss
 */
public class Boss extends GameObject{

    private static BufferedImage img;

    static {
        try {
            String png = "program/static/boss.png";
            img = ImageIO.read(Boss.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boss(){
        super(80,80);
        this.image = img;
        this.hit = 3;
        this.life = 500;
    }

    //boss渲染方法
    public void paint(Graphics g,int width,int height){
        g.drawImage(this.image, width*4, height*2,width*3,height*3,null);
//        Color color = new Color(254, 133, 0);
//        g.setColor(color);
//        g.setFont(new Font("微软雅黑",Font.LAYOUT_LEFT_TO_RIGHT+Font.BOLD,height/3));
//        g.drawString(String.valueOf(this.hit),(int)this.x,(int)(this.y+height));
//        g.drawString(String.valueOf(this.life),(int)(this.x+width-width/5),(int)(this.y+height));
    }

    /**
     * 被攻击时调用
     * @param hero
     * @return
     */
    public int beHited(Hero hero){
        this.life -= hero.getHit();
        if(this.life<1){
            this.state = DEAD;
            World.score += 100;
            this.image = null;
        }
        return this.hit;
    }

    public int getLife(){
        return this.life;
    }
}
