package Nonogram.view;
 
import Nonogram.model.Grid;
import Nonogram.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
import java.util.ArrayList;
 
public class GameView {
 
    private Stage stage;
    private Scene scene;
 
    private GridPane gridPanel;
    private Button[][] buttons;
    private Button prevButton;
    private Button nextButton;
    private Button checkButton;
    private Label timerLabel;
 
    private int rows;
    private int cols;
 
    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private final int cellSize = 20;
 
    // コンストラクト
    public GameView(Stage stage) {
        this.stage = stage;
    }

    //初期化
    public void initialize(Puzzle puzzle){

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();
    
        ArrayList<ArrayList<Integer>> rowHints = puzzle.getClue().getRowClues();
        ArrayList<ArrayList<Integer>> colHints = puzzle.getClue().getColClues();
    
        // ヒントの最大数から左・上のヒントエリアサイズを決定
        int maxColHintRows = colHints.stream().mapToInt(ArrayList::size).max().orElse(1);
        int maxRowHintCols = rowHints.stream().mapToInt(ArrayList::size).max().orElse(1);
    
        // すべて cellSize 単位で計算
        int hintAreaWidth  = maxRowHintCols * cellSize; // 左ヒントエリアの幅
        int hintAreaHeight = maxColHintRows * cellSize; // 上ヒントエリアの高さ
    
        // ===== 左上コーナー（タイマー）=====
        timerLabel = new Label("00:00");
        timerLabel.setPrefSize(hintAreaWidth, hintAreaHeight);
        timerLabel.setAlignment(Pos.CENTER);
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    
        // ===== 列ヒント（上）=====
        // 各列を VBox（下揃え）で表現し、横に HBox で並べる
        HBox hintPanelTop = new HBox();
        hintPanelTop.setSpacing(0);
    
        for (int col = 0; col < cols; col++) {
            VBox colBox = new VBox();
            colBox.setPrefSize(cellSize, hintAreaHeight);
            colBox.setAlignment(Pos.BOTTOM_CENTER);
            colBox.setSpacing(0);
    
            ArrayList<Integer> hints = colHints.get(col);
    
            // 上を空白で埋めて下揃えにする
            for (int p = 0; p < maxColHintRows - hints.size(); p++) {
                Label pad = new Label();
                pad.setPrefSize(cellSize, cellSize);
                colBox.getChildren().add(pad);
            }
            for (int num : hints) {
                Label lbl = new Label(String.valueOf(num));
                lbl.setPrefSize(cellSize, cellSize);
                lbl.setAlignment(Pos.CENTER);
                lbl.setStyle(
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-width: 0 0.5 0.5 0.5;" +
                    "-fx-font-size: 11px;"
                );
                colBox.getChildren().add(lbl);
            }
            hintPanelTop.getChildren().add(colBox);
        }
    
        // ===== 行ヒント（左）=====
        // 各行を HBox（右揃え）で表現し、縦に VBox で並べる
        VBox hintPanelSide = new VBox();
        hintPanelSide.setSpacing(0);
    
        for (int row = 0; row < rows; row++) {
            HBox rowBox = new HBox();
            rowBox.setPrefSize(hintAreaWidth, cellSize);
            rowBox.setAlignment(Pos.CENTER_RIGHT);
            rowBox.setSpacing(0);
    
            ArrayList<Integer> hints = rowHints.get(row);
    
            // 左を空白で埋めて右揃えにする
            for (int p = 0; p < maxRowHintCols - hints.size(); p++) {
                Label pad = new Label();
                pad.setPrefSize(cellSize, cellSize);
                rowBox.getChildren().add(pad);
            }
            for (int num : hints) {
                Label lbl = new Label(String.valueOf(num));
                lbl.setPrefSize(cellSize, cellSize);
                lbl.setAlignment(Pos.CENTER);
                lbl.setStyle(
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-width: 0.5 0.5 0.5 0;" +
                    "-fx-font-size: 11px;"
                );
                rowBox.getChildren().add(lbl);
            }
            hintPanelSide.getChildren().add(rowBox);
        }
    
        // ===== グリッド =====
        gridPanel = new GridPane();
        buttons = new Button[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button btn = new Button();
                btn.setPrefSize(cellSize, cellSize);
                btn.setFocusTraversable(false);
                applyCellStyle(btn, "empty", row, col);  // ← row, col を追加

                buttons[row][col] = btn;
                gridPanel.add(btn, col, row);
            }
        }
    
        // ===== チェックボタン（下）=====
        prevButton = new Button("<");
        prevButton.setPrefHeight(36);
        prevButton.setPrefWidth(48);

        nextButton = new Button(">");
        nextButton.setPrefHeight(36);
        nextButton.setPrefWidth(48);

        checkButton = new Button("確認");
        checkButton.setMaxWidth(Double.MAX_VALUE);  // 残りの幅を全部占有
        checkButton.setPrefHeight(36);

        HBox bottomRow = new HBox(prevButton, nextButton, checkButton);
        HBox.setHgrow(checkButton, javafx.scene.layout.Priority.ALWAYS);  // checkButtonを伸ばす
        bottomRow.setSpacing(0);
    
        // ===== 全体レイアウト =====
        //
        //  [ cornerLabel   | hintPanelTop  ]
        //  [ hintPanelSide | gridPanel     ]
        //  [   checkButton（全幅）         ]
        //
        HBox topRow = new HBox(timerLabel, hintPanelTop);
        topRow.setSpacing(0);
    
        HBox midRow = new HBox(hintPanelSide, gridPanel);
        midRow.setSpacing(0);
    
        VBox root = new VBox(topRow, midRow, bottomRow);
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
 
    // セル更新（row, col の順で統一）
    public void updateCell(int x, int y, Grid grid) {
        Button btn = buttons[x][y];

        switch (grid.getCellAt(x, y).getState()) {
            case FILLED:
                applyCellStyle(btn, "filled", x, y);  // ← 追加
                btn.setText("");
                break;
            case MARKED:
                applyCellStyle(btn, "marked", x, y);  // ← 追加
                btn.setText("✕");
                break;
            default:
                applyCellStyle(btn, "empty", x, y);   // ← 追加
                btn.setText("");
                break;
        }
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
 
    // 結果表示
    public void showResult(boolean result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("結果");
        alert.setHeaderText(null);
        alert.setContentText(result ? "クリア！おめでとう！" : "不正解... もう一度！");
        alert.showAndWait();
    }
 
    public Stage getStage() {
        return stage;
    }
 
    public Button[][] getButtons() {
        return buttons;
    }
 
    public Button getCheckButton() {
        return checkButton;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public Button getNextButton() {
        return nextButton;
    }
}
 
