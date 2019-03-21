package program;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by KirimaSyaro on 2017/4/17.
 * 泛化出的父类（引擎）
 */
public abstract class GameObject {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected BufferedImage image;
    protected int state;
    protected int life;
    protected int hit;
    public static final int ACTIVE = 0;
    public static final int DEAD = 1;
    public static final int REMOVE = 2;

    public GameObject(double width, double height) {
        this();
        this.width = width;
        this.height = height;
        this.x = 0.0;
        this.y = 0.0;
    }

    public GameObject(){
        this.life = 1;
        this.state = ACTIVE;
        this.hit = 0;
    }

    /**
     * 渲染方法
     * @param g
     */
    public void paint(Graphics g) {
        g.drawImage(this.image, (int)this.x, (int)this.y,null);
    }

    /**
     * 渲染方法
     * @param g
     * @param width
     * @param height
     */
    public void paint(Graphics g,int width,int height) {
        g.drawImage(this.image, (int)this.x, (int)this.y,width,height,null);
    }

    /**
     * 被攻击方法
     * @param allItems
     * @param i
     * @param j
     * @param hero
     * @return
     */
    public int beHited(AllItems allItems, int i, int j, Hero hero){
        return this.hit;
    }

    /**
     * 去死(宣布该对象死亡)
     */
    public void goDead(){
        this.life = 0;
        this.state = DEAD;
    }

    /**
     * 被对话
     * @param hero
     * @param world
     */
    public void beSpeak(Hero hero,World world){

    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }
    @Override
    public String toString() {
        return "GameObject{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", image=" + image +
                ", state=" + state +
                ", life=" + life +
                '}';
    }
}
