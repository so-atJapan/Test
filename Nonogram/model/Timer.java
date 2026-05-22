package Nonogram.model;

/**
 * ゲーム中の経過時間を管理するクラス
 * 太田
 */
public class Timer {


    private int timeCount ;

    /**
     * コンストラクタ
     */
    public Timer() {
        this.timeCount = 0;
    }

    /**
     * 一秒ごとに呼ばれる処理
     * 
     * @return 経過時間
     */
    public int tick() {
        timeCount++;
        return timeCount;
    }

    /**
     * 現在の経過秒数を取得する
     * 
     * @return 経過秒数
     */
    public int getTickSeconds() {
        return timeCount;
    }
}
