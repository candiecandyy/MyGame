package program;

import java.awt.*;

/**
 * Created by KirimaSyaro on 2017/4/19.
 * 游戏界面所有对象管理
 */
public class AllItems extends GameObject {

    private final static int CELL_SIDE = 10;
    private final static int CELL_RELEASE = 6;
    //游戏界面二维数组
    private  GameObject[][] allItems = new GameObject[CELL_SIDE][CELL_RELEASE];

    public AllItems(){
        for(int i=0;i<CELL_SIDE;i++){
            for(int j=0;j<CELL_RELEASE;j++){
                allItems[i][j] = new Brick();
            }
        }
    }

    //循环向游戏内添加对象
    public void createItems(Graphics g,int width,int height){
        for(int i=0;i<CELL_SIDE;i++){
            for(int j=0;j<CELL_RELEASE;j++){
                if(allItems[i][j]!=null) {
                    allItems[i][j].x = width/10*i;
                    allItems[i][j].y = height/8*(j+1)+(height/8/2)+(height/70);
                    allItems[i][j].paint(g,width/10,height/8);
                }
            }
        }
    }

    /**
     * 检测除了主角面对的之外，是否有其他砖块
     * @param m 主角面对方向
     * @param n 主角面对方向
     * @return 是否有其他砖块
     */
    public boolean checkBrick(int m,int n){
        for(int i=0;i<CELL_SIDE;i++){
            for(int j=0;j<CELL_RELEASE;j++){
                if(i!=m&&j!=n&&allItems[i][j] instanceof Brick){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * boss关对象添加
     */
    public void bossStage(){
        for(int i=0;i<CELL_SIDE;i++){
            for(int j=0;j<CELL_RELEASE;j++){
                if(i!=0&&j!=0&&i!=CELL_SIDE-1&&j!=CELL_RELEASE-1) {
                    if(i>6){
                        allItems[i][j] = new GoldBox();
                    }else {
                        allItems[i][j] = null;
                    }
                }else {
                    allItems[i][j] = new IronBrick();
                }
            }
        }
        allItems[CELL_SIDE-2][CELL_RELEASE-2] = new Princess();
        allItems[1][1] = new BetterMen();
        allItems[2][1] = new BetterWomen();
    }

    /**
     * 当前关卡是否有队友存在
     * @return
     */
    public boolean checkBettermen(){
        for(int i=0;i<CELL_SIDE;i++){
            for(int j=0;j<CELL_RELEASE;j++){
                if(allItems[i][j] instanceof BetterMen){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 砖块被击碎时 随机生成一个对象
     * @param i
     * @param j
     * @return
     */
    public GameObject nextOne(int i, int j){
        if(!Hero.isExistKey()&& !checkBrick(i, j)){//当所有砖都被击碎并且钥匙没有出现
            Hero.setExistKey(true);
            return new Key();//返回一个钥匙
        }else {
            int r = (int)(Math.random()*9);
            switch (r){
                case 0:
                {
                    //如果没有钥匙，返回一个钥匙
                    if (!Hero.isExistKey()&& Brick.bricks.size()>10) {
                        Hero.setExistKey(true);
                        return new Key();
                    } else {
                        //有钥匙，重新调用该方法
                        return nextOne(i,j);
                    }
                }
                case 1: {
                    int r1 = (int) (Math.random() * 10) + 1;
                    switch (r1){
                        case 1:
                        case 2:{
                            //如果地图上没有队友，生成一个队友
                            if(!checkBettermen()){
                                return new BetterMen();
                            }else {
                                //否则，返回空
                                return null;
                            }
                        }
                        default:
                            return null;
                    }
                }
                case 2:{
                    int r1 = (int)(Math.random()*2);
                    switch (r1){
                        case 0:
                            //返回一个血瓶
                            return new Award();
                        default:
                            //返回一个木宝箱
                            return new WoodBox();
                    }
                }
                case 3:
                case 4:
                    return null;
                default: {
                    //返回一个敌人
                   return nextEnemy();
                }
            }
        }
    }

    /**
     * 随机产生一个敌人
     * @return
     */
    public GameObject nextEnemy(){
        int r = (int) (Math.random()*3);
        switch (r) {
            case 0:{
                if(World.stage==1){
                    return new Enemy();
                }else if(World.stage ==2){
                    return new Enemy2();
                }else if(World.stage ==3){
                    return new Enemy4();
                }else if(World.stage ==4){
                    return new Enemy7();
                }
            }
            case 1:{
                if(World.stage==1){
                    return new Enemy8();
                }else if(World.stage ==2){
                    return new Enemy3();
                }else if(World.stage ==3){
                    return new Enemy5();
                }else if(World.stage ==4){
                    return new Enemy9();
                }
            }
            default:{
                if(World.stage==1){
                    return new Enemy1();
                }else if(World.stage ==2){
                    return new Enemy2();
                }else if(World.stage ==3){
                    return new Enemy6();
                }else if(World.stage ==4){
                    return new Enemy9();
                }else {
                    return null;
                }
            }
        }
    }

    /**
     * 移除被标记为可以移除的对象
     */
    public void checkRemove(){
        for(int i=0;i<CELL_SIDE;i++){
            for(int j=0;j<CELL_RELEASE;j++){
                if(allItems[i][j]!=null&&allItems[i][j].state == 2) {
                    allItems[i][j] = null;
                }
            }
        }
    }

    public GameObject getAllItems(int i, int j) {
        return allItems[i][j];
    }


    public void setAllItems(GameObject gameObject, int i, int j) {
        this.allItems[i][j] = gameObject;
    }

    public static int getCellSide(){
        return CELL_SIDE;
    }
    public static int getCellRelease() {
        return CELL_RELEASE;
    }


}
