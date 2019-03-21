package program;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2018/12/4.
 */
public class MyPlayer extends Player {

    private static volatile Map<String, String> stateMap = new ConcurrentHashMap<>();
    private static volatile Map<String, Player> playerMap = new ConcurrentHashMap<>();
    private static volatile Map<String, Long> timeMap = new ConcurrentHashMap<>();

    private InputStream is;
    private String id;
    private String status = "";
    private boolean isPlaying = false;
    private boolean isPausing = false;
    private ByteArrayOutputStream bos;

    public MyPlayer(InputStream stream) throws JavaLayerException {
        super(stream);
        bos = new ByteArrayOutputStream();
        int len;
        byte[] b = new byte[1024];
        try {
            while ((len = stream.read(b)) > -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            is = new ByteArrayInputStream(bos.toByteArray());
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MyPlayer(InputStream stream, AudioDevice device) throws JavaLayerException {
        super(stream, device);
        bos = new ByteArrayOutputStream();
        int len;
        byte[] b = new byte[1024];
        try {
            while ((len = stream.read(b)) > -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            is = new ByteArrayInputStream(bos.toByteArray());
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        if (isPlaying) {
            throw new IllegalArgumentException("请不要重复播放");
        }
        this.status = "loop";
        isPlaying = true;
        String id = UUID.randomUUID().toString();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                stateMap.put(id, "0");
                timeMap.put(id, 0L);
                while (stateMap.containsKey(id) && (stateMap.get(id).equals("0") || stateMap.get(id).equals("1"))) {
                    while (stateMap.get(id).equals("1")) {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    InputStream in = null;
                    try {
                        in = new ByteArrayInputStream(bos.toByteArray());
                        in.skip(timeMap.get(id) * 16);
                        Player player = new Player(new BufferedInputStream(in));
                        playerMap.put(id, player);
                        if (latch.getCount() > 0) {
                            latch.countDown();
                        }
                        player.play();
                        if (player.isComplete()) {
                            timeMap.put(id, 0L);
                        }
                    } catch (JavaLayerException | IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (latch.getCount() > 0) {
                    latch.countDown();
                }
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.id = id;
    }

    public void stopLoop() {
        if (!"loop".equals(status)) {
            throw new IllegalArgumentException("无法调用该方法");
        }
        if (stateMap.containsKey(id) && isPlaying) {
            stateMap.remove(id);
            playerMap.get(id).close();
            playerMap.remove(id);
            timeMap.remove(id);
            status = "";
            isPlaying = false;
        }
    }

    public void pauseLoop() {
        if (!"loop".equals(status)) {
            throw new IllegalArgumentException("无法调用该方法");
        }
        if (stateMap.containsKey(id)) {
            stateMap.put(id, "1");
            long time = playerMap.get(id).getPosition();
            time += timeMap.get(id);
            timeMap.put(id, time);
            playerMap.get(id).close();
            playerMap.remove(id);
        }
    }

    public void startLoop() {
        if (!"loop".equals(status)) {
            throw new IllegalArgumentException("无法调用该方法");
        }
        if (stateMap.containsKey(id)) {
            stateMap.put(id, "0");
        }
    }

    @Override
    public void play() throws JavaLayerException {
        if (isPlaying && !isPausing) {
            throw new IllegalArgumentException("请不要重复播放");
        }
        isPlaying = true;
        isPausing = false;
        status = "signal";
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        MyPlayer player = this;
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            stateMap.put(id, "0");
            if (!timeMap.containsKey(id)) {
                timeMap.put(id, 0L);
            }
            try {
                InputStream bis = new ByteArrayInputStream(bos.toByteArray());
                bis.skip(timeMap.get(id) * 16);
                Player player1 = new Player(new BufferedInputStream(bis));
                playerMap.put(id, player1);
                if (latch.getCount() > 0) {
                    latch.countDown();
                }
                player1.play();
                if (player1.isComplete()) {
                    stateMap.remove(id);
                    timeMap.remove(id);
                    playerMap.remove(id);
                    player.id = null;
                }
            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!"signal".equals(status)) {
            throw new IllegalArgumentException("无法调用该方法");
        }
        if (playerMap.containsKey(id)) {
            playerMap.get(id).close();
            stateMap.remove(id);
            timeMap.remove(id);
            playerMap.remove(id);
            this.id = null;
            status = "";
            isPlaying = false;
            isPausing = false;
        }
    }

    public void pause() {
        if (!"signal".equals(status)) {
            throw new IllegalArgumentException("无法调用该方法");
        }
        if (playerMap.containsKey(id)) {
            long time = playerMap.get(id).getPosition();
            time += timeMap.get(id);
            playerMap.get(id).close();
            timeMap.put(id, time);
            isPausing = true;
        }
    }

}
