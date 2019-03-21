package program;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Created by KirimaSyaro on 2017/4/17.
 * 游戏主类，入口类，在该类运行main方法
 */
public class World extends JPanel {

    private static final long serialVersionUID = -3408028698279739454L;

    private static final int READY = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int GAME_OVER = 3;
    private static final int WIN = 4;
    private static final int EXPLAIN = 5;
    private static final int FINISH = 6;
    //游戏主计时器
    private Timer timer;
    //游戏时间计算
    private Timer gameTimer;
    private int state = READY;
    //关卡数
    public static int stage = 1;
    private Back back;
    private Hero hero;
    private Door door;
    private Boss boss;
    private AddSound sound;
    private AllItems allAllItems;
    //生命值每一块
    private static Blood[] bloods = new Blood[24];

    private static BufferedImage ready;
    private static BufferedImage gameOver;
    private static BufferedImage explain;
    private static BufferedImage win;
    private static BufferedImage finish;
    private static BufferedImage bloodbar;
    private static BufferedImage speakBar;
    //调试模式校验数组：(Esc)上上下下左右左右BABA
    private int[] godCode = {27, 38, 38, 40, 40, 37, 39, 37, 39, 66, 65, 66, 65};
    private List<Integer> beGodCode = new ArrayList<Integer>();
    private int frameW = 820;
    private int frameH = 650;
    //分数
    public static int score = 0;
    private long startTime = 0;
    private String gameTime = "00:00:00";
    private String[] speaking;
    public boolean canWin = false;

    private Integer heroLife;
    private List<Users> rank = new ArrayList<Users>();

    static {
        try {
            String png = "program/static/logo.png";
            ready = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/gameover.png";
            gameOver = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/explain.png";
            explain = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/win.png";
            win = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/finish.png";
            finish = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/bloodbar.png";
            bloodbar = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
            png = "program/static/speakbar.png";
            speakBar = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public World() {
        File file = new File("rank.jas");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameTimer = new Timer();
        back = new Back();
        allAllItems = new AllItems();
        hero = new Hero();
        door = new Door();
        boss = new Boss();
        sound = new AddSound();
        allAllItems.setAllItems(hero, 0, 0);
        allAllItems.setAllItems(door, AllItems.getCellSide() - 1, AllItems.getCellRelease() - 1);
    }

    /**
     * 满足条件进入下一关
     */
    public void nextStage() {
        if (Door.opened && Hero.getI() == AllItems.getCellSide() - 1 && Hero.getJ() == AllItems.getCellRelease() - 1) {
            Door.opened = false;
            canWin = false;
            allAllItems = new AllItems();
            hero.setTurned("down");
            stage++;
            hited = true;
            moved = true;
            Brick.bricks.removeAll(Brick.bricks);
            if (stage < 5) {
                Hero.setExistKey(false);
                allAllItems.setAllItems(hero, 0, 0);
                Hero.setI(0);
                Hero.setJ(0);
                allAllItems.setAllItems(door, AllItems.getCellSide() - 1, AllItems.getCellRelease() - 1);
            } else {
                door.goDead();
                allAllItems.bossStage();
                allAllItems.setAllItems(hero, 1, 3);
                Hero.setI(1);
                Hero.setJ(3);
                //allAllItems.setAllItems(boss, AllItems.getCellSide() - 1, AllItems.getCellRelease() - 1);
            }
        }
    }

    /**
     * 重置游戏方法
     */
    public void reset() {
        score = 0;
        heroLife = null;
        startTime = 0;
        back = new Back();
        Door.opened = false;
        allAllItems = new AllItems();
        hero = new Hero();
        boss = new Boss();
        door = new Door();
        //allAllItems.setAllItems(hero, 0, 0);
        //allAllItems.setAllItems(door, AllItems.getCellSide() - 1, AllItems.getCellRelease() - 1);
        Hero.setI(0);
        Hero.setJ(0);
        hero.setTurned("down");
        //Hero.setExistKey(false);
        Brick.bricks.removeAll(Brick.bricks);
        canWin = false;
        stage = 1;
        if (stage < 5) {
            Hero.setExistKey(false);
            allAllItems.setAllItems(hero, 0, 0);
            Hero.setI(0);
            Hero.setJ(0);
            allAllItems.setAllItems(door, AllItems.getCellSide() - 1, AllItems.getCellRelease() - 1);
        } else {
            Hero.setExistKey(true);
            door.goDead();
            allAllItems.bossStage();
            allAllItems.setAllItems(hero, 1, 3);
            Hero.setI(1);
            Hero.setJ(3);
            //allAllItems.setAllItems(boss, AllItems.getCellSide() - 1, AllItems.getCellRelease() - 1);
        }
        hited = true;
        moved = true;
        this.beGodCode.removeAll(this.beGodCode);
    }

    /**
     * 游戏时间计时
     */
    public void timing() {
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {
                    startTime++;
                    StringBuffer sb = new StringBuffer("");
                    long hour = startTime / 60 / 60 % 24;
                    long minute = startTime / 60 % 60;
                    long second = startTime % 60;
                    if (hour < 10) {
                        sb.append("0").append(String.valueOf(hour));
                    } else {
                        sb.append(String.valueOf(hour));
                    }
                    sb.append(":");
                    if (minute < 10) {
                        sb.append("0").append(String.valueOf(minute));
                    } else {
                        sb.append(String.valueOf(minute));
                    }
                    sb.append(":");
                    if (second < 10) {
                        sb.append("0").append(String.valueOf(second));
                    } else {
                        sb.append(String.valueOf(second));
                    }
                    gameTime = sb.toString();
                }
            }
        }, 0, 1000);
    }

    /**
     * 用户名称检测
     *
     * @param name 用户名称
     * @return 是否满足条件
     */
    public boolean checkName(String name) {
        //正则表达式，匹配数字，字母，下划线以及汉字
        if (name.matches("[\\w，\\u4e00-\\u9fa5]+")) {
            int m = 0;
            char[] arr = name.toCharArray();
            for (char c : arr) {
                //unicode编码中文范围为[0x0391,0xFFE5]
                //中文两个字符，英文一个字符
                if (c >= 0x0391 && c <= 0xFFE5) {
                    m += 2;
                } else {
                    m += 1;
                }
            }
            if (m <= 12) {
                return true;
            }
        }
        return false;
    }

    /**
     * 胜利检测方法，满足条件则胜利
     */
    public void checkWin() {
        if (boss.getLife() <= 0 && hero.getLife() >= 0 && !hero.isSpeaked() && canWin) {
            String name = null;
            Dao dao = new Dao();
            do {
                name = JOptionPane.showInputDialog("请输入您的姓名(12字符以下):");
            } while (name != null && (name.trim().isEmpty() || !checkName(name.trim())));
            if (name != null) {
                name = name.trim();
                Users users = dao.findUserByName(name);
                if (users != null) {
                    users.setTime(gameTime);
                    users.setScore(score);
                    dao.modifyUsers(users);
                } else {
                    users = new Users(name, score, gameTime);
                    dao.saveUsers(users);
                }
            }
            rank = dao.findAllUsers();
            if (rank.size() > 10) {
                rank = rank.subList(0, 10);
            }
            state = WIN;
            canWin = false;
            sound.winSound();
        }
    }

    /**
     * 死亡检测方法，满足条件，游戏失败
     */
    public void checkGameOver() {
        if (hero.isDead()) {
            state = GAME_OVER;
            sound.gameoverSound();
        }

    }

    /**
     * 播放声音
     */
    public void sound() {
        if (state == RUNNING) {
            if (stage >= 5) {
                sound.allClose("bossStageSoundClose");
                sound.bossStageSound();
            } else {
                sound.allClose("normalStageSoundClose");
                sound.normalStageSound();
            }
        } else if (state == READY) {
            sound.allClose("readySoundClose");
            sound.readySound();
        } else if (state == PAUSE) {
            sound.allClose("readySoundClose");
            sound.readySound();
        } else if (state == EXPLAIN) {
            sound.allClose();
        } else if (state == WIN) {
            sound.allClose();
        } else if (state == GAME_OVER) {
            sound.allClose();
        }
    }

    /**
     * 生命值渲染
     *
     * @param g
     */
    public void healthBar(Graphics g) {
        for (int i = 0; i < hero.getLife(); i++) {
            Blood blood = new Blood();
            blood.x = i * (frameW / 29) + (frameW / 8);
            blood.y = frameH / 12 - (frameH / 34);
            blood.paint(g, frameW / 30, frameH / 42);
            bloods[i] = blood;
        }
    }

    /**
     * 调试模式检测方法
     */
    public void beGod() {
        if (heroLife != null) {
            hero.setLife(heroLife);
        }
        if (arrEquals(this.godCode, this.beGodCode)) {
            hero.setLife(bloods.length);
            hero.setHit(50000);
        } else {
            hero.setHit(1 + hero.getMeetButter());
        }
    }

    /**
     * 检测数组与集合数据是否相等
     *
     * @param arr1 数组
     * @param list 集合
     * @return 是否相等
     */
    private boolean arrEquals(int[] arr1, List<Integer> list) {
        if (arr1.length != list.size()) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != list.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回被对话对象的下一句话
     */
    private int speakIndex = -1;

    public String nextSpeak() {
        if (speaking != null) {
            if (speakIndex < speaking.length) {
                return speaking[speakIndex];
            }
        }
        return null;
    }

    /**
     * 画出对话框
     *
     * @param g
     */
    public void drawSpeakBar(Graphics g) {
        if (hero.isSpeaked()) {
            g.drawImage(speakBar, frameW / 16, frameH / 2 + frameH / 20, frameW - frameW / 8, frameH / 4, null);
            g.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, frameH / 15));
            Color color = new Color(0, 0, 0);
            g.setColor(color);
            if (nextSpeak() != null) {
                g.drawString(nextSpeak(), frameW / 5, frameH / 2 + frameH / 5);
            }
            //g.drawString("攻击力UP",frameW/3,frameH/2+frameH/7+(frameH/15+10));
        } else {
            allAllItems.checkRemove();
        }
    }

    /**
     * 胜利界面用户数据展示
     *
     * @param g
     */
    public void drawRank(Graphics g) {
        if (rank.size() > 0) {
            g.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, frameH / 20));
            Color color = new Color(255, 255, 255);
            g.setColor(color);
            g.drawString("NAME", frameW / 4 - frameW / 7, frameH / 3 - (frameH / 20));
            g.drawString("SCORE", frameW / 2, frameH / 3 - (frameH / 20));
            g.drawString("TIME", frameW / 2 + frameW / 5, frameH / 3 - (frameH / 20));
        }
        for (int i = 0; i < rank.size(); i++) {
            g.drawString(rank.get(i).getName(), frameW / 4 - frameW / 7, frameH / 3 + (frameH / 20 * i + 10));
            g.drawString(String.valueOf(rank.get(i).getScore()), frameW / 2, frameH / 3 + (frameH / 20 * i + 10));
            g.drawString(rank.get(i).getTime(), frameW / 2 + frameW / 5, frameH / 3 + (frameH / 20 * i + 10));
        }
    }

    /**
     * 重写Jpanel方法 最核心的界面渲染方法
     *
     * @param g 画笔
     */
    public void paint(Graphics g) {
        switch (state) {
            case READY:
            case PAUSE:
                g.drawImage(ready, 0, 0, frameW, frameH, null);
                break;
            case RUNNING:
                back.paint(g, frameW, frameH);
                //hero.paint(g,frameW/10,frameH/6);
                //door.paint(g,frameW/10,frameH/6);
                if (stage >= 5) {
                    boss.paint(g, frameW / 10, frameH / 6);
                }
                g.drawImage(bloodbar, frameW / 11, frameH / 12 - frameH / 15, frameW - frameW / 9, frameH / 9, null);
                healthBar(g);
                g.setFont(new Font("Tahoma", Font.LAYOUT_LEFT_TO_RIGHT, frameW / 40));
                g.drawString("LIFE: ", frameW / 40, frameH / 12);
                g.drawString("Hit: " + String.valueOf(hero.getHit()), frameW / 2, frameH / 7);
                g.drawString("Score: " + String.valueOf(score), frameW / 2, frameH / 5 - frameH / 300);
                g.drawString("STAGE: " + stage, frameW - (frameW / 5), frameH / 7);
                g.drawString("Time: " + gameTime, frameW - (frameW / 5), frameH / 5 - frameH / 300);
                allAllItems.createItems(g, frameW, frameH);
                drawSpeakBar(g);
                break;
            case GAME_OVER:
                g.drawImage(gameOver, 0, 0, frameW, frameH, null);
                break;
            case EXPLAIN:
                g.drawImage(explain, 0, 0, frameW, frameH, null);
                break;
            case WIN:
                g.drawImage(win, 0, 0, frameW, frameH, null);
//                g.setFont(new Font("微软雅黑",Font.LAYOUT_LEFT_TO_RIGHT,frameH/15));
//                Color color = new Color(255,0,0);
//                g.setColor(color);
//                g.drawString("Score:"+score,frameW/2,frameH/2);
//                g.drawString("Time:"+gameTime,frameW/2,frameH/2+(frameH/15+10));
                drawRank(g);
                break;
            case FINISH:
                g.drawImage(finish, 0, 0, frameW, frameH, null);
        }
    }

    /**
     * 游戏运行方法，所有对象的活动在该方法调用
     *
     * @param frame 游戏窗口
     */
    private boolean hited = true;
    private boolean moved = true;

    public void action(final JFrame frame) {
        /**
         * 鼠标事件
         */
        MouseAdapter mouseAdapter = new MouseAdapter() {
            int x, y;

            /**
             * 鼠标移动事件
             * @param e
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            /**
             * 鼠标点击事件
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (state == READY) {
                    if (frameW / 3 + (frameW / 25) <= x && x <= frameW / 2 + (frameW / 8) && frameH / 3 + (frameH / 16) <= y && y <= frameH / 2 + (frameH / 22)) {
                        state = EXPLAIN;
                        //reset();
                    }
                    if (frameW / 3 + (frameW / 25) <= x && x <= frameW / 2 + (frameW / 8) && frameH / 2 + (frameH / 7) <= y && y <= frameH - (frameH / 4)) {
                        System.exit(0);
                    }
                    return;
                }
                if (state == GAME_OVER) {
                    state = READY;
                    return;
                }
                if (state == WIN) {
                    state = FINISH;
                    return;
                }
                if (state == FINISH) {
                    state = READY;
                    return;
                }
                if (state == EXPLAIN) {
                    state = RUNNING;
                    reset();
                    return;
                }
                if (state == PAUSE) {
                    if (frameW / 3 + (frameW / 25) <= x && x <= frameW / 2 + (frameW / 8) && frameH / 3 + (frameH / 16) <= y && y <= frameH / 2 + (frameH / 22)) {
                        state = RUNNING;
                        //reset();
                    }
                    if (frameW / 3 + (frameW / 25) <= x && x <= frameW / 2 + (frameW / 8) && frameH / 2 + (frameH / 7) <= y && y <= frameH - (frameH / 4)) {
                        System.exit(0);
                    }
                    return;
                }
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        final World world = this;
        /**
         * 键盘按下事件
         */
        KeyAdapter keyAdapter = new KeyAdapter() {
            long nextHit;

            @Override
            public void keyPressed(KeyEvent e) {
                if (state == RUNNING) {
                    if (moved) {
                        if (e.getKeyCode() == 38) {
                            hero.moveUp(allAllItems);
                            moved = false;
                        } else if (e.getKeyCode() == 40) {
                            hero.moveDown(allAllItems);
                            moved = false;
                        } else if (e.getKeyCode() == 37) {
                            hero.moveLeft(allAllItems);
                            moved = false;
                        } else if (e.getKeyCode() == 39) {
                            hero.moveRight(allAllItems, boss);
                            moved = false;
                        }
                    }
                    if (e.getKeyCode() == 27) {
                        state = PAUSE;
                        beGodCode.removeAll(beGodCode);
                    }
                    long nowHit = System.currentTimeMillis();
                    if (e.getKeyCode() == 88 && hited && nowHit > nextHit) {
                        hited = false;
                        nextHit = nowHit + hero.getBaseAttackTime();//攻击间隔
                        hero.hit(allAllItems, boss);
                    }
                    if (e.getKeyCode() == 32) {
                        //if (!hero.isSpeaked()) {
                        hero.speak(allAllItems, world);
                        speakIndex++;
                        if (nextSpeak() == null) {
                            speakIndex = -1;
                            speaking = null;
                            hero.setSpeaked(false);
                        }
                        //}
                    }
                } else if (state == PAUSE) {
                    if (e.getKeyCode() == 27) {
                        state = RUNNING;
                    }
                }
            }

            /**
             * 键盘抬起事件
             * @param e
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (state == RUNNING) {
                    if (e.getKeyCode() == 88) {
                        hited = true;
                    }
                    if (e.getKeyCode() == 37 || e.getKeyCode() == 38 || e.getKeyCode() == 39 || e.getKeyCode() == 40) {
                        moved = true;
                    }
                }
                if (state == PAUSE) {
                    if (beGodCode.size() < godCode.length) {
                        beGodCode.add(e.getKeyCode());
                    }
                }
            }
        };
        frame.addKeyListener(keyAdapter);

        /**
         * 窗口改变事件
         */
        ComponentAdapter componentAdapter = new ComponentAdapter() {
            //窗口大小改变事件
            @Override
            public void componentResized(ComponentEvent e) {
//                SwingUtilities.invokeLater(new Runnable(){
//                    public void run() {
                frameW = frame.getWidth();
                frameH = frame.getHeight();
                back.height = frameH;
                back.width = frameW;
                // }
//                }
//                );
            }
        };
        addComponentListener(componentAdapter);

        /**
         * 游戏计时器，每过一帧调用一次
         */
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (state == RUNNING) {
                    hero.move();
                    nextStage();
                    checkGameOver();
                    checkWin();
                    door.checkOpen();
                    beGod();
                }
                sound();
                repaint();//来自JPanel的方法
            }
        }, 0, 1000 / 24);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("myGAME");
        frame.setSize(820, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        World world = new World();
        frame.add(world);
        frame.setVisible(true);
        world.action(frame);
        world.timing();
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setHeroLife(Integer heroLife) {
        this.heroLife = heroLife;
    }

    public String[] getSpeaking() {
        return speaking;
    }

    public void setSpeaking(String[] speaking) {
        this.speaking = speaking;
    }

}
