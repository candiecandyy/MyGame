package program;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by KirimaSyaro on 2017/5/8.
 * 声音文件读取
 */
public class AddSound {

    //    private AudioInputStream ready;
    private InputStream ready;
    //    private Clip clipReady;
    private MyPlayer readyPlayer;
    private AudioInputStream normalStage;
    private Clip clipNormalStage;
    private AudioInputStream bossStage;
    private Clip clipBossStage;

    /**
     * 开始界面背景音乐
     */
//    public void readySound() {
//        String path = "program/static/music/ready.wav";
//        try {
//            if (ready == null) {
//                clipReady = AudioSystem.getClip();
//                ready = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
//                clipReady.open(ready);
//                clipReady.loop(Clip.LOOP_CONTINUOUSLY);
//            }
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            e.printStackTrace();
//        } finally {
//            if (ready != null) {
//                try {
//                    ready.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    public void readySound() {
        if (ready == null) {
            String path = "program/static/music/ready1.mp3";
            ready = AddSound.class.getClassLoader().getResourceAsStream(path);
            try {
                readyPlayer = new MyPlayer(ready);
                readyPlayer.loop();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始界面背景音乐关闭
     */
//    public void readySoundClose() {
//        if (clipReady != null) {
//            clipReady.close();
//            ready = null;
//        }
//    }
    public void readySoundClose() {
        if (readyPlayer != null) {
            readyPlayer.stopLoop();
            readyPlayer = null;
            ready = null;
        }
    }

    /**
     * 攻击声效
     */
    public void hitSound() {
        String path = "program/static/music/hit.wav";
        AudioInputStream hit = null;
        try {
            Clip clip = AudioSystem.getClip();
            hit = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
            clip.open(hit);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        } finally {
            if (hit != null) {
                try {
                    hit.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 砖块击碎声效
     */
    public void brickSound() {
        String path = "program/static/music/brick.wav";
        AudioInputStream brick = null;
        try {
            brick = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(brick);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (brick != null) {
                try {
                    brick.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开门效果音
     */
    public void openSound() {
        String path = "program/static/music/open.wav";
        AudioInputStream open = null;
        try {
            open = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(open);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 脚步声
     */
    public void moveSound() {
        String path = "program/static/music/move.wav";
        AudioInputStream move = null;
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            move = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
            clip.open(move);
            clip.start();

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        } finally {
            if (move != null) {
                try {
                    move.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 普通关卡背景音乐
     */
    public void normalStageSound() {
        String path = "program/static/music/normalstage.wav";
        try {
            if (normalStage == null) {
                clipNormalStage = AudioSystem.getClip();
                normalStage = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
                clipNormalStage.open(normalStage);
                clipNormalStage.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        } finally {
            if (normalStage != null) {
                try {
                    normalStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 普通关卡背景音乐结束
     */
    public void normalStageSoundClose() {
        if (clipNormalStage != null) {
            clipNormalStage.close();
            normalStage = null;
        }
    }

    /**
     * boss关背景音乐
     */
    public void bossStageSound() {
        String path = "program/static/music/bossstage.wav";
        try {
            if (bossStage == null) {
                bossStage = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
                clipBossStage = AudioSystem.getClip();
                clipBossStage.open(bossStage);
                clipBossStage.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (bossStage != null) {
                try {
                    bossStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * boss关背景音乐关闭
     */
    public void bossStageSoundClose() {
        if (clipBossStage != null) {
            clipBossStage.close();
            bossStage = null;
        }
    }

    /**
     * gameover声效
     */
    public void gameoverSound() {
        String path = "program/static/music/gameover.wav";
        AudioInputStream gameover = null;
        try {
            gameover = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(gameover);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (gameover != null) {
                try {
                    gameover.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 胜利声效
     */
    public void winSound() {
        String path = "program/static/music/win.wav";
        AudioInputStream win = null;
        try {
            win = AudioSystem.getAudioInputStream(new BufferedInputStream(AddSound.class.getClassLoader().getResourceAsStream(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(win);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (win != null) {
                try {
                    win.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 调用方法名不为methodName的其他Close()方法
     *
     * @param methodName 排除在外的方法名
     */
    public void allClose(String methodName) {
        Method[] methods = AddSound.class.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.matches("\\w+Close") && !name.equals(methodName) && !name.equals("allClose")) {
                try {
                    method.invoke(this);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 关闭所有背景音乐
     */
    public void allClose() {
        readySoundClose();
        normalStageSoundClose();
        bossStageSoundClose();
    }
}
