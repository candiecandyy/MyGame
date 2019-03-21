package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KirimaSyaro on 2017/4/17.
 * 砖块
 */
public class Brick extends GameObject{

    private static BufferedImage img;
    public static List<Brick> bricks = new ArrayList<Brick>();
    static {
        try {
            String png = "program/static/brick.png";
            img = ImageIO.read(Brick.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Brick(){
        super(80,80);
        this.image = img;
        this.life = 1;
    }

    //被攻击时调用
    public int beHited(AllItems allItems, int i, int j, Hero hero){
        AddSound sound = new AddSound();
        sound.brickSound();
        this.life -= hero.getHit();
        if(this.life<1){
            allItems.setAllItems(allItems.nextOne(i, j),i,j);
            bricks.add(this);
        }
        return this.hit;
    }
}
