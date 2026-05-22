package Nonogram.controller;

import java.util.HashSet;
import java.util.Set;

import Nonogram.model.CellState;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;
import Nonogram.view.PuzzleEditorView;
import javafx.scene.input.MouseButton;

public class PuzzleEditorController {

    private PuzzleEditorModel model;
    private PuzzleEditorView view;
    private AppController appController;

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
     */
    public PuzzleEditorController(PuzzleEditorModel model, PuzzleEditorView view, AppController appController) {
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


        bindAllCellEvents();
        
        
        // リセットボタン
        // view.getResetButton().setOnAction(e -> onReset());
        
        // 設定ボタン
        view.getSettingButton().setOnAction(e -> view.semiModalRender(model.getPuzzle()));
        
        //　OKボタン
        view.getOkButton().setOnAction(e -> onSettingConfirm());
        
        // チェックボタン
        view.getCheckButton().setOnAction(e -> onCheck());
        
        // 描画
        view.render();
        view.semiModalRender(model.getPuzzle());
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

    /**
     * 設定の決定ボタンが押されたときの処理。
     */
    public void onSettingConfirm() {
        model.updatePuzzleTitle(view.getTitleTextField());
        model.updatePuzzleGridSizeX(view.getGridSizeX());
        model.updatePuzzleGridSizeY(view.getGridSizeY());
        model.gridReSize();
        view.gridReSize(model.getGrid());
        view.settingConfirm();

        bindAllCellEvents();

        view.render();
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

    /**
     * リセットボタンが押されたときの処理。
     */
    public void onReset() {
        model.reset();
        view.updateCell(0, 0, model.getGrid());
    }

    /**
     * チェックボタンが押されたときの処理。
     */
    public void onCheck() {
        model.updateDB();
        appController.navigateTo("home");
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
                    }

                    draggedCells.clear();
                    draggedCells.add(finalX + "," + finalY);

                    // フルドラッグ開始
                    button.startFullDrag();
                });

                // ドラッグ中にマスへ入ったとき
                button.setOnMouseDragEntered(e -> {

                    // 直線判定（縦 or 横のみ許可、斜めは禁止）
                    int dx = finalX - startX;
                    int dy = finalY - startY;

                    // 同一セルへの重複適用を防止
                    if (dx != 0 && dy != 0) return;
                    String key = finalX + "," + finalY;
                    if (draggedCells.contains(key)) return;
                    draggedCells.add(key);
                    applyDragAction(finalX, finalY);
                });

                // ドラッグ終了時にリセット
                button.setOnMouseReleased(e -> {
                    dragAction = null;
                    draggedCells.clear();
                    if (e.getButton() == MouseButton.PRIMARY)
                        model.pushGridLog();
                });
            }
        }
    }
}
