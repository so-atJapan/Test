package Nonogram.view;

import Nonogram.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SemiModal {

    private Stage primaryStage;
    private Stage settingStage;

    private Scene dialogScene;

    private TextField titleTextField;
    private TextField rowTextField;
    private TextField colTextField;
    private Button okButton;

    // コンストラクタ
    public SemiModal(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    
    // セミモーダル画面
    public void initialize(Puzzle puzzle) {

        settingStage = new Stage();

        // 親を指定
        settingStage.initOwner(this.primaryStage);

        // 親ウィンドウのみ操作不可
        settingStage.initModality(Modality.WINDOW_MODAL);

        Label titleLabel = new Label("タイトル");
        titleTextField = new TextField(puzzle.getTitle());
        
        Label rowLabel = new Label("row");
        rowTextField = new TextField(String.valueOf(puzzle.getGridSizeX()));
        rowTextField.setMaxWidth(30);
        TextFormatter<String> formatterRow = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // 数字のみ、0～2文字まで許可
            if (newText.matches("\\d{0,2}")) {
                return change;
            }

            return null; // 条件外は入力拒否
        });
        rowTextField.setTextFormatter(formatterRow);
        
        VBox rowVBox = new VBox(rowLabel, rowTextField);
        rowVBox.setAlignment(Pos.CENTER);
        
        Label timesLabel = new Label("x");
        timesLabel.setFont(new Font(20));
        
        Label colLabel = new Label("col");
        colTextField = new TextField(String.valueOf(puzzle.getGridSizeY()));
        colTextField.setMaxWidth(30);
        TextFormatter<String> formatterCol = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // 数字のみ、0～2文字まで許可
            if (newText.matches("\\d{0,2}")) {
                return change;
            }

            return null; // 条件外は入力拒否
        });
        colTextField.setTextFormatter(formatterCol);

        VBox colVBox = new VBox(colLabel, colTextField);
        colVBox.setAlignment(Pos.CENTER);

        // HBox → TilePane に変更
        TilePane sizeTilePane = new TilePane(
            rowVBox,
            timesLabel,
            colVBox
        );

        sizeTilePane.setOrientation(Orientation.HORIZONTAL); // 横並び
        sizeTilePane.setAlignment(Pos.BOTTOM_LEFT);          // 全体の配置
        sizeTilePane.setHgap(0);                             // 横の間隔

        okButton = new Button("OK");
        
        VBox dialogRoot = new VBox(15);
        dialogRoot.setPadding(new Insets(20));
        dialogRoot.getChildren().addAll(
                titleLabel,
                titleTextField,
                sizeTilePane,
                okButton
        );

        dialogScene = new Scene(dialogRoot, 300, 200);
    }

    public void render(){

        settingStage.setTitle("設定");
        settingStage.setScene(dialogScene);

        // 閉じるまで待つ
        settingStage.showAndWait();
    }

    public void settingConfirm(){
        settingStage.close();
    }

    public Button getOkButton() {
        return okButton;
    }

    public void setTitleTextField(String title){
        titleTextField.setText(title);
    }

    public void setRowTextField(int gridSizeX){
        rowTextField.setText(String.valueOf(gridSizeX));
    }

    public void setColTextField(int gridSizeY){
        colTextField.setText(String.valueOf(gridSizeY));
    }

    public String getTitleTextField(){
        return titleTextField.getText();
    }

    public int getGridSizeX(){
        return Integer.parseInt(rowTextField.getText());
    }

    public int getGridSizeY(){
        return Integer.parseInt(colTextField.getText());
    }
}
