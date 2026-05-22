package Nonogram.view;

import Nonogram.model.CellState;
import Nonogram.model.Grid;
import Nonogram.model.ResultModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * リザルト画面を表示するViewクラス
 * 太田
 */
public class ResultView {

    private Stage stage;
    private Scene scene;
    private Label clearTimeLabel;
    private Button backButton;

    private final int CELLSIZE = 24;

    /**
     * コンストラクタ
     * 
     * @param stage 画面表示に使用するStage
     */
    public ResultView(Stage stage) {
        this.stage = stage;
    }

    /**
     * リザルト画面の部品を生成する
     * 
     * @param model リザルト画面で表示するデータ
     */
    public void initialize(ResultModel model) {
        Label titleLabel = new Label("クリア");
        titleLabel.setFont(new Font(28));

        Label puzzleTitleLabel = new Label(model.getPuzzle().getTitle());
        puzzleTitleLabel.setFont(new Font(18));

        clearTimeLabel = new Label("経過時間 00:00");
        clearTimeLabel.setFont(new Font(24));
        clearTimeLabel.setAlignment(Pos.CENTER);

        GridPane completedGridPane = createCompletedGrid(model.getCompletedGrid());

        backButton = new Button("問題リストに戻る");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setPrefHeight(36);

        VBox root = new VBox(12, titleLabel, puzzleTitleLabel, clearTimeLabel, completedGridPane, backButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(16));

        scene = new Scene(root);
    }

    /**
     * リザルト情報を画面に反映する
     * 
     * @param result 表示するリザルトデータ
     */
    public void displayResult(ResultModel result) {
        updateTimer(result.getTickSeconds());
    }

    /**
     * 完成したノノグラムの盤面を生成する
     * 
     * @param grid 完成時の盤面
     * @return 盤面表示用のGridPane
     */
    private GridPane createCompletedGrid(Grid grid) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < grid.getSizeX(); row++) {
            for (int col = 0; col < grid.getSizeY(); col++) {
                Label cell = new Label();
                cell.setPrefSize(CELLSIZE, CELLSIZE);
                cell.setMinSize(CELLSIZE, CELLSIZE);
                cell.setMaxSize(CELLSIZE, CELLSIZE);

                if (grid.getCellAt(row, col).getState() == CellState.FILLED) {
                    cell.setStyle("-fx-background-color: #222222; -fx-border-color: #555555; -fx-border-width: 1;");
                } else {
                    cell.setStyle("-fx-background-color: white; -fx-border-color: #555555; -fx-border-width: 1;");
                }

                gridPane.add(cell, col, row);
            }
        }

        return gridPane;
    }

    /**
     * リザルト画面をStageに表示する
     */
    public void render() {
        stage.setTitle("Nonogram Result");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * 経過時間の表示を更新する
     * 
     * @param totalSeconds クリアまでにかかった秒数
     */
    public void updateTimer(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        clearTimeLabel.setText(String.format("経過時間 %02d:%02d", minutes, seconds));

    }

    /**
     * 問題リストに戻るボタンを取得する
     * 
     * @return 問題リストに戻るボタン
     */
    public Button showHomeButton() {
        return backButton;
    }

    // public Button showRetryButton() {
    //     return retryButton;
    // }
}
