package program;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by KirimaSyaro on 2017/4/17.
 * 英雄
 */
public class Hero extends GameObject {

    private static String turned = "down";
    private static BufferedImage[] heroRight = new BufferedImage[4];
    private static BufferedImage[] heroDown = new BufferedImage[4];
    private static BufferedImage[] heroLeft = new BufferedImage[4];
    private static BufferedImage[] heroUp = new BufferedImage[4];
    private static BufferedImage[] hits = new BufferedImage[4];
    private static int i = 0;
    private static int j = 0;
    private static boolean existKey = false;
    private int meetButter;
    private boolean speaked;
    private int baseAttackTime;
    static {
        for (int i = 0; i < heroDown.length; i++) {
            try {
                String png = "program/static/heroright" + i + ".png";
                heroRight[i] = ImageIO.read(Hero.class.getClassLoader().getResourceAsStream(png));
                png = "program/static/herodown" + i + ".png";
                heroDown[i] = ImageIO.read(Hero.class.getClassLoader().getResourceAsStream(png));
                png = "program/static/heroleft" + i + ".png";
                heroLeft[i] = ImageIO.read(Hero.class.getClassLoader().getResourceAsStream(png));
                png = "program/static/heroup" + i + ".png";
                heroUp[i] = ImageIO.read(Hero.class.getClassLoader().getResourceAsStream(png));
                png = "program/static/hit" + i + ".png";
                hits[i] = ImageIO.read(Hero.class.getClassLoader().getResourceAsStream(png));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Hero() {
        this.width = 80;
        this.height = 110;
        this.image = heroDown[0];
        this.x = 0;
        this.y = 100;
        this.life = 15;
        this.hit = 1;
        this.meetButter = 0;
        this.speaked = false;
        this.baseAttackTime = 400;//攻击间隔400毫秒
    }

    private int index;

    long nextHited;
    long nextMove;
    /**
     * 英雄原地踏步
     */
    public void move() {
        if (!hited) {
            long nowMove = System.currentTimeMillis();
            if (nowMove > nextMove) {
                new AddSound().moveSound();
                if ("right".equals(turned)) {
                    this.image = heroRight[index++ % 4];
                }
                if ("down".equals(turned)) {
                    this.image = heroDown[index++ % 4];
                }
                if ("up".equals(turned)) {
                    this.image = heroUp[index++ % 4];
                }
                if ("left".equals(turned)) {
                    this.image = heroLeft[index++ % 4];
                }
                nextMove = nowMove + 300;
            }
        } else {
            long nowHited = System.currentTimeMillis();
            if (nowHited > nextHited) {
                hited = false;
                nextHited = nowHited + 120;
            }
        }
    }

    /**
     * 向左移动
     * @param allAllItems
     */
    public void moveLeft(AllItems allAllItems) {
        if (!speaked) {
            hited = false;
             //如果没有朝向移动方向，则先转身
            if (turned != "left") {
                turned = "left";
            } else {
                //碰到钥匙，开门
                if (i > 0 && allAllItems.getAllItems(i - 1, j) instanceof Key) {
                    Door.opened = true;
                    AddSound sound = new AddSound();
                    sound.openSound();
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), --i, j);
                    allAllItems.setAllItems(null, i + 1, j);
                    return;
                }
                //碰到血瓶，回复生命
                if (i > 0 && allAllItems.getAllItems(i - 1, j) instanceof Award) {
                    if (life < 24) {
                        life++;
                    }
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), --i, j);
                    allAllItems.setAllItems(null, i + 1, j);
                    return;
                }
                if (i > 0 && allAllItems.getAllItems(i - 1, j) == null) {
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), --i, j);
                    allAllItems.setAllItems(null, i + 1, j);
                }
            }
            move();
        }
    }

    /**
     * 向右移动
     * @param allAllItems
     * @param boss
     */
    public void moveRight(AllItems allAllItems, Boss boss) {
        if (!speaked) {
            hited = false;
            if (turned != "right") {
                turned = "right";
            } else {
                if (World.stage >= 5 && boss.state == ACTIVE) {
                    if (i >= 3) {
                        return;
                    }
                }
                if (i < AllItems.getCellSide() - 1 && allAllItems.getAllItems(i + 1, j) instanceof Key) {
                    Door.opened = true;
                    AddSound sound = new AddSound();
                    sound.openSound();
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), ++i, j);
                    allAllItems.setAllItems(null, i - 1, j);
                    return;
                }
                if (i < AllItems.getCellSide() - 1 && allAllItems.getAllItems(i + 1, j) instanceof Award) {
                    if (life < 24) {
                        life++;
                    }
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), ++i, j);
                    allAllItems.setAllItems(null, i - 1, j);
                    return;
                }
                if (i < AllItems.getCellSide() - 1 && allAllItems.getAllItems(i + 1, j) instanceof Door) {
                    if (Door.opened) {
                        allAllItems.setAllItems(allAllItems.getAllItems(i, j), ++i, j);
                        allAllItems.setAllItems(null, i - 1, j);
                    }
                    return;
                }
                if (i < AllItems.getCellSide() - 1 && allAllItems.getAllItems(i + 1, j) == null) {
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), ++i, j);
                    allAllItems.setAllItems(null, i - 1, j);
                }
            }
            move();
        }
    }

    /**
     * 向上移动
     * @param allAllItems
     */
    public void moveUp(AllItems allAllItems) {
        if (!speaked) {
            hited = false;
            if (turned != "up") {
                turned = "up";
            } else {
                if (j > 0 && allAllItems.getAllItems(i, j - 1) instanceof Key) {
                    Door.opened = true;
                    AddSound sound = new AddSound();
                    sound.openSound();
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, --j);
                    allAllItems.setAllItems(null, i, j + 1);
                    return;
                }
                if (j > 0 && allAllItems.getAllItems(i, j - 1) instanceof Award) {
                    if (life < 24) {
                        life++;
                    }
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, --j);
                    allAllItems.setAllItems(null, i, j + 1);
                    return;
                }
                if (j > 0 && allAllItems.getAllItems(i, j - 1) == null) {
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, --j);
                    allAllItems.setAllItems(null, i, j + 1);
                }
            }
            move();
        }
    }

    /**
     * 向下移动
     * @param allAllItems
     */
    public void moveDown(AllItems allAllItems) {
        if (!speaked) {
            hited = false;
            if (turned != "down") {
                turned = "down";
            } else {
                if (j < AllItems.getCellRelease() - 1 && allAllItems.getAllItems(i, j + 1) instanceof Key) {
                    Door.opened = true;
                    AddSound sound = new AddSound();
                    sound.openSound();
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, ++j);
                    allAllItems.setAllItems(null, i, j - 1);
                    return;
                }
                if (j < AllItems.getCellRelease() - 1 && allAllItems.getAllItems(i, j + 1) instanceof Award) {
                    if (life < 24) {
                        life++;
                    }
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, ++j);
                    allAllItems.setAllItems(null, i, j - 1);
                    return;
                }
                if (j < AllItems.getCellRelease() - 1 && allAllItems.getAllItems(i, j + 1) instanceof Door) {
                    if (Door.opened) {
                        allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, ++j);
                        allAllItems.setAllItems(null, i, j - 1);
                    }
                    return;
                }
                if (j < AllItems.getCellRelease() - 1 && allAllItems.getAllItems(i, j + 1) == null) {
                    allAllItems.setAllItems(allAllItems.getAllItems(i, j), i, ++j);
                    allAllItems.setAllItems(null, i, j - 1);
                }
            }
            move();
        }
    }

    private boolean hited = false;

    /**
     * 攻击方法
     * @param allAllItems
     * @param boss
     */
    public void hit(AllItems allAllItems, Boss boss) {
        if (!speaked) {
            AddSound sound = new AddSound();
            sound.hitSound();
            hited = true;
            if ("left".equals(turned)) {
                this.image = hits[1];
                if (i > 0 && allAllItems.getAllItems(i - 1, j) != null) {
                    life -= allAllItems.getAllItems(i - 1, j).beHited(allAllItems, i - 1, j, this);

                }
                return;
            }
            if ("right".equals(turned)) {
                this.image = hits[3];
                if (World.stage >= 5 && boss.state == ACTIVE && i == 3) {
                    this.life -= boss.beHited(this);
                    return;
                }
                if (i < AllItems.getCellSide() - 1 && allAllItems.getAllItems(i + 1, j) != null) {
                    life -= allAllItems.getAllItems(i + 1, j).beHited(allAllItems, i + 1, j, this);
                }
                return;
            }
            if ("up".equals(turned)) {
                this.image = hits[2];
                if (j > 0 && allAllItems.getAllItems(i, j - 1) != null) {
                    life -= allAllItems.getAllItems(i, j - 1).beHited(allAllItems, i, j - 1, this);
                }
                return;
            }
            if ("down".equals(turned)) {
                this.image = hits[0];
                if (j < AllItems.getCellRelease() - 1 && allAllItems.getAllItems(i, j + 1) != null) {
                    life -= allAllItems.getAllItems(i, j + 1).beHited(allAllItems, i, j + 1, this);
                }
                return;
            }
        }
    }

    /**
     * 对话方法
     * @param allAllItems
     * @param world
     */
    public void speak(AllItems allAllItems, World world) {
        if ("left".equals(turned)) {
            if(allAllItems.getAllItems(i - 1, j) != null) {
                allAllItems.getAllItems(i - 1, j).beSpeak(this, world);
            }
            return;
        }
        if ("right".equals(turned)) {
            if(allAllItems.getAllItems(i + 1, j)!=null) {
                allAllItems.getAllItems(i + 1, j).beSpeak(this, world);
            }
            return;
        }
        if ("up".equals(turned)) {
            if(allAllItems.getAllItems(i, j - 1)!=null) {
                allAllItems.getAllItems(i, j - 1).beSpeak(this, world);
            }
            return;
        }
        if ("down".equals(turned)) {
            if(allAllItems.getAllItems(i, j + 1)!=null) {
                allAllItems.getAllItems(i, j + 1).beSpeak(this, world);
            }
            return;
        }
    }

    public boolean isDead() {
        if (state == ACTIVE && life < 1) {
            state = DEAD;
            return true;
        }
        return false;
    }


    public static int getI() {
        return i;
    }

    public static void setI(int i) {
        Hero.i = i;
    }

    public static int getJ() {
        return j;
    }

    public static void setJ(int j) {
        Hero.j = j;
    }

    public static boolean isExistKey() {
        return existKey;
    }

    public static void setExistKey(boolean existKey) {
        Hero.existKey = existKey;
    }


    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setTurned(String turned) {
        Hero.turned = turned;
    }

    public void setMeetButter(int meetButter) {
        this.meetButter = meetButter;
    }

    public int getMeetButter() {
        return meetButter;
    }

    public boolean isHited() {
        return hited;
    }

    public void setHited(boolean hited) {
        this.hited = hited;
    }

    public boolean isSpeaked() {
        return speaked;
    }

    public void setSpeaked(boolean speaked) {
        this.speaked = speaked;
    }

    public int getBaseAttackTime() {
        return baseAttackTime;
    }

    public void setBaseAttackTime(int baseAttackTime) {
        this.baseAttackTime = baseAttackTime;
    }
}
