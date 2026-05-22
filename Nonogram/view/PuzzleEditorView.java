package Nonogram.view;
 
import Nonogram.model.Cell;
import Nonogram.model.Grid;
import Nonogram.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
import java.util.ArrayList;
 
public class PuzzleEditorView {

    private SemiModal semiModal;
 
    private Stage stage;
    private Scene scene;
 
    private GridPane gridPanel;
    private Button[][] buttons;
    private Button settingButton;
    private Button checkButton;
    private Label timerLabel;

    private HBox midRow;

    private int rows;
    private int cols;
 
    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private final int cellSize = 20;
 
    // コンストラクト
    public PuzzleEditorView(Stage stage) {
        this.stage = stage;
    }

    //初期化
    public void initialize(Puzzle puzzle){

        this.semiModal = new SemiModal(this.stage);
        this.semiModal.initialize(puzzle);

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();
    
        // ===== グリッド =====
        gridPanel = new GridPane();
        buttons = new Button[puzzle.getGridSizeX()][puzzle.getGridSizeY()];
    
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                Button btn = new Button();
                btn.setPrefSize(cellSize, cellSize);
                btn.setFocusTraversable(false);
                applyCellStyle(btn, "empty", x, y);
                
                buttons[x][y] = btn;
                gridPanel.add(btn, y, x);
                
                updateCell(x, y, puzzle.getSolution());
            }
        }
    
        // ===== 下部ボタン（設定 : 確認 = 1 : 3）=====
        settingButton = new Button("設定");
        checkButton = new Button("確認");

        // 横方向に伸ばせるようにする
        settingButton.setMaxWidth(Double.MAX_VALUE);
        checkButton.setMaxWidth(Double.MAX_VALUE);

        // 高さ統一
        settingButton.setPrefHeight(36);
        checkButton.setPrefHeight(36);

        // 比率設定
        HBox.setHgrow(settingButton, Priority.ALWAYS);
        HBox.setHgrow(checkButton, Priority.ALWAYS);

        // 下部ボタンレイアウト
        HBox bottomButtons = new HBox(settingButton, checkButton);
        bottomButtons.setSpacing(0);

        // 1:3 の比率
        settingButton.prefWidthProperty().bind(bottomButtons.widthProperty().multiply(0.25));
        checkButton.prefWidthProperty().bind(bottomButtons.widthProperty().multiply(0.75));


        // ===== 全体レイアウト =====
        midRow = new HBox(gridPanel);
        midRow.setSpacing(0);

        VBox root = new VBox(midRow, bottomButtons);
        root.setSpacing(0);
        root.setPadding(new Insets(8));

        scene = new Scene(root);
    }
 
    // パズル描画
    public void render() {
        stage.setTitle("Nonogram");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    //セミモーダル描画
    public void semiModalRender(Puzzle puzzle){
        this.semiModal.setTitleTextField(puzzle.getTitle());
        this.semiModal.setRowTextField(puzzle.getGridSizeX());
        this.semiModal.setColTextField(puzzle.getGridSizeY());
        this.semiModal.render();
    }
    
    // 設定確定
    public void settingConfirm(){
        this.semiModal.settingConfirm();
    }

    // セル更新（row, col の順で統一）
    public void updateCell(int x, int y, Grid grid) {
        Button btn = buttons[x][y];

        switch (grid.getCellAt(x, y).getState()) {
            case FILLED:
                applyCellStyle(btn, "filled", x, y);
                btn.setText("");
                break;
            case MARKED:
                applyCellStyle(btn, "marked", x, y);
                btn.setText("✕");
                break;
            default:
                applyCellStyle(btn, "empty", x, y);
                btn.setText("");
                break;
        }
    }

    // グリッド更新
    public void gridReSize(Grid grid) {

        this.gridPanel = new GridPane();
        this.buttons = new Button[grid.getSizeX()][grid.getSizeY()];
    
        for (int x = 0; x < grid.getSizeX(); x++) {
            for (int y = 0; y < grid.getSizeY(); y++) {
                Button btn = new Button();
                btn.setPrefSize(cellSize, cellSize);
                btn.setFocusTraversable(false);
                applyCellStyle(btn, "empty", x, y);
                
                buttons[x][y] = btn;
                gridPanel.add(btn, y, x);
                
                updateCell(x, y, grid);
            }
        }

        midRow.getChildren().setAll(gridPanel);

    }

    public void updateCellAll(Grid grid) {

        for (int x = 0; x < grid.getSizeX(); x++) {
            for (int y = 0; y < grid.getSizeY(); y++) {
                updateCell(x, y, grid);
            }
        }
    }
 
    // セルスタイル適用
    private void applyCellStyle(Button btn, String state, int row, int col) {
        // 5マスごとに太い線（0行目・0列目も太く）
        double top    = (row % 5 == 0) ? 2.0 : 0.5;
        double left   = (col % 5 == 0) ? 2.0 : 0.5;
        // 右端・下端も太く
        double bottom = (row == rows - 1) ? 2.0 : 0.5;
        double right  = (col == cols - 1) ? 2.0 : 0.5;

        String border =
            "-fx-border-color: #555555;" +
            "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + ";";

        String base = border +
            "-fx-padding: 0;" +
            "-fx-font-size: 11px;";

        switch (state) {
            case "filled":
                btn.setStyle(base + "-fx-background-color: #222222; -fx-text-fill: #222222;");
                break;
            case "marked":
                btn.setStyle(base + "-fx-background-color: #f0f0f0; -fx-text-fill: #cc0000;");
                break;
            default:
                btn.setStyle(base + "-fx-background-color: white; -fx-text-fill: black;");
                break;
        }
    }
 
    // タイマー更新（Controllerから秒数で受け取り mm:ss 形式で表示）
    public void updateTimer(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
 
 
    public Stage getStage() {
        return stage;
    }
 
    public Button[][] getButtons() {
        return buttons;
    }
 
    public Button getSettingButton() {
        return settingButton;
    }

    public Button getOkButton() {
        return this.semiModal.getOkButton();
    }
 
    public Button getCheckButton() {
        return checkButton;
    }

    public String getTitleTextField(){
        return this.semiModal.getTitleTextField();
    }

    public int getGridSizeX(){
        return this.semiModal.getGridSizeX();
    }

    public int getGridSizeY(){
        return this.semiModal.getGridSizeY();
    }
}
 