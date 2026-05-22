package Nonogram.model;

/**
 * リザルト画面で表示する結果データを保持するモデルクラス
 * 太田
 */
public class ResultModel {

    private Puzzle puzzle;
    private Grid completedGrid;
    private int clearTimeSeconds;
    // private PlayRecord record; // TODO: PlayRecordクラス実装後、記録保存用に追加予定

    /**
     * コンストラクタ
     * 
     * @param puzzle クリアしたパズル
     * @param completedGrid 完成時の盤面
     * @param elapsedSeconds クリアまでにかかった秒数
     */
    public ResultModel(Puzzle puzzle, Grid completedGrid, int elapsedSeconds) {
        this.puzzle = puzzle;
        this.completedGrid = completedGrid;
        this.clearTimeSeconds = elapsedSeconds;
    }

    /**
     * クリアしたパズルを取得する
     * 
     * @return パズル
     */
    public Puzzle getPuzzle() {
        return puzzle;
    }

    /**
     * 完成時の盤面を取得する
     * 
     * @return 完成時の盤面
     */
    public Grid getCompletedGrid() {
        return completedGrid;
    }

    /**
     * クリアまでにかかった秒数を取得する
     * 
     * @return クリア時間
     */
    public int getTickSeconds() {
        return clearTimeSeconds;
    }

    // public PlayRecord getRecord() {
    //     return record;
    // }
}
