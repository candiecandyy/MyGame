package program;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/5/18.
 */
public class Enemy4 extends GameObject{

    private static BufferedImage img;

    static {
        try {
            String png = "program/static/enemy4.png";
            img = ImageIO.read(Enemy4.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Enemy4(){
        super(80,80);
        this.image = img;
        this.hit = 3;
        this.life = 2;
    }

    public void paint(Graphics g, int width, int height){
        super.paint(g,width,height);
        Color color = new Color(254, 133, 0);
        g.setColor(color);
        g.setFont(new Font("微软雅黑",Font.LAYOUT_LEFT_TO_RIGHT+Font.BOLD,height/3));
        g.drawString(String.valueOf(this.hit),(int)this.x,(int)(this.y+height));
        g.drawString(String.valueOf(this.life),(int)(this.x+width-width/5),(int)(this.y+height));
    }

    public int beHited(AllItems allItems, int i, int j, Hero hero){
        this.life -= hero.getHit();
        if(this.life<1){
            allItems.setAllItems(null,i,j);
            World.score += 3;
        }
        return this.hit;
    }

}
