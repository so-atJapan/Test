package Nonogram.controller;


import Nonogram.model.CellState;
import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.Timer;
import Nonogram.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

/**
 * ゲーム画面の操作と判定を管理するコントローラクラス
 */
public class GameController {

    private GameModel model;
    private GameView view;
    private AppController appController;
    private Timer timer = new Timer();
    private Timeline timeline;
    private int startX;
    private int startY;

    // ドラッグ中に適用するアクション（FILLED or MARKED or EMPTY）
    private CellState dragAction = null;
    // ドラッグ中に処理済みのセルを記録
    private final Set<String> draggedCells = new HashSet<>();


    /**
     * コンストラクタ
     *
     * @param model 
     * @param view  
     * @param appController 画面遷移を管理するコントローラ
     */
    public GameController(GameModel model, GameView view, AppController appController) {
        this.model = model;
        this.view  = view;
        this.appController = appController;
    }

    /**
     * ゲームを起動
     * ボタン描画呼び出し、初期化
     */
    public void initialize() {
        // PuzzleのデータをViewに渡す
        view.initialize(model.getPuzzle());
        view.render();


        bindAllCellEvents();


        // リセットボタン
        // view.getResetButton().addActionListener(e -> onReset());

        // undoボタン
        view.getPrevButton().setOnAction(e -> onUndo());

        // redoボタン
        view.getNextButton().setOnAction(e -> onRedo());

        // チェックボタン
        view.getCheckButton().setOnAction(e -> onJudge());


        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> view.updateTimer(timer.tick())));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
    }

    /**
     * セルが左クリックされたときの処理。
     *
     * @param x クリックされたセルのX座標
     * @param y クリックされたセルのY座標
     * @return  更新後のセルの状態を返す
     */
    public CellState onCellLeftClicked(int x, int y) {
        
        model.toggle(x, y, CellState.FILLED);
        view.updateCell(x, y, model.getGrid());

        return model.getGrid().getCellAt(x, y).getState();
    }

    /**
     * セルが右クリックされた時の処理
     * 
     * @param x
     * @param y
     * @return
     */
    public CellState onCellRightClicked(int x, int y) {
        
        model.toggle(x, y, CellState.MARKED);
        view.updateCell(x, y, model.getGrid());

        return model.getGrid().getCellAt(x, y).getState();
    }

    /**
     * ドラッグ中に確定済みアクションをセルへ適用する
     *
     * @param x 適用するセルのX座標
     * @param y 適用するセルのY座標
     */
    private void applyDragAction(int x, int y) {
        if (dragAction == null) return;
        model.setState(x, y, dragAction);
        view.updateCell(x, y, model.getGrid());
    }

    // /**
    //  * リセットボタンが押されたときの処理。
    //  */
    // public void onReset() {
    //     model.reset();
    //     view.updateCell(0, 0, model.getCell());
    // }

    /**
     * チェックボタンが押されたときの処理
     * 正解の場合はリザルト画面に必要なデータをAppControllerへ渡す
     */
    public void onJudge() {
        boolean result = model.check();
        if (result) {
            timeline.stop();
            appController.setResultData(model.getPuzzle(), model.getGrid(), timer.getTickSeconds());
            appController.navigateTo("result");
        } else {
            view.showResult(false);
        }
    }

    public void onUndo(){
        model.undoGridLog();
        model.setGrid(model.getCurrentLog().copy());
        view.updateCellAll(model.getGrid());
    }

    public void onRedo(){
        model.redoGridLog();
        model.setGrid(model.getCurrentLog().copy());
        view.updateCellAll(model.getGrid());
    }

    // 全イベントをまとめて設定するメソッド
    private void bindAllCellEvents() {

        Puzzle puzzle = model.getPuzzle();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {

                int finalX = x;
                int finalY = y;

                var button = view.getButtons()[finalX][finalY];

                // クリック（単体操作用）
                button.setOnMouseClicked(e -> {

                    if (e.getButton() == MouseButton.PRIMARY) {
                        onCellLeftClicked(finalX, finalY);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        onCellRightClicked(finalX, finalY);
                    } else if (e.getButton() == MouseButton.BACK) {
                        onUndo();
                    } else if (e.getButton() == MouseButton.FORWARD) {
                        onRedo();
                    }
                });

                // ドラッグ開始
                button.setOnDragDetected(e -> {

                    // スタート位置を保存
                    startX = finalX;
                    startY = finalY;

                    // 開始セルをtoggleし、その結果をドラッグ中のアクションとして固定
                    if (e.isPrimaryButtonDown()) {
                        dragAction = onCellLeftClicked(finalX, finalY);
                    } else if (e.isSecondaryButtonDown()) {
                        dragAction = onCellRightClicked(finalX, finalY);
                    }

                    draggedCells.clear();
                    draggedCells.add(finalX + "," + finalY);

                    // フルドラッグ開始（必須）
                    button.startFullDrag();
                });

                // ドラッグ中にマスへ入ったとき
                button.setOnMouseDragEntered(e -> {

                    // 直線判定（縦 or 横のみ許可、斜めは禁止）
                    int dx = finalX - startX;
                    int dy = finalY - startY;

                    boolean isStraight = (dx == 0 || dy == 0);

                    if (!isStraight) {
                        return;
                    }

                    // 同一セルへの重複適用を防止
                    String key = finalX + "," + finalY;
                    if (draggedCells.contains(key)) return;
                    draggedCells.add(key);

                    applyDragAction(finalX, finalY);
                });

                // ドラッグ終了時にリセット
                button.setOnMouseReleased(e -> {
                    dragAction = null;
                    draggedCells.clear();

                    if(e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY)
                    model.pushGridLog();
                });
            }
        }

    }

}
