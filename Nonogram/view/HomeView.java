package Nonogram.view;
 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
 
public class HomeView{
 
    private Stage stage;
    private Scene scene;

    Button playButton;
    Button createButton;
    Button exitButton;

    Button loginButton;
    Button signupButton;

    public HomeView(Stage stage){
        this.stage = stage;
    }
    
    public void initialize(){

        // ── タイトル ────────────────────────────────────────
        Label titleLabel = new Label("Nonogram");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        Label subtitleLabel = new Label("ピクロス パズルゲーム");
        subtitleLabel.setFont(Font.font("Arial", 13));
        subtitleLabel.setStyle("-fx-text-fill: gray;");

        Separator separator = new Separator();

        // ── メニューボタン ──────────────────────────────────
        playButton   = new Button("PLAY");
        createButton = new Button("CREATE");
        exitButton   = new Button("EXIT");

        for (Button b : new Button[]{playButton, createButton, exitButton}) {
            b.setPrefWidth(200);
        }


        // ── 右上: ログイン / サインアップ ──────────────────
        // ログイン → テキストリンク風（背景なし・下線なし）
        loginButton = new Button("ログイン");
        loginButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;"
        );
        loginButton.setOnMouseEntered(e ->
            loginButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-text-fill: #000000;" +
                "-fx-underline: true;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;"
            )
        );
        loginButton.setOnMouseExited(e ->
            loginButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-text-fill: #333333;" +
                "-fx-underline: false;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;"
            )
        );

        // サインアップ → 塗りつぶしボタン（Webの「Sign up」ボタン風）
        signupButton = new Button("サインアップ");
        signupButton.setStyle(
            "-fx-background-color: #2563EB;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-cursor: hand;"
        );
        signupButton.setOnMouseEntered(e ->
            signupButton.setStyle(
                "-fx-background-color: #1D4ED8;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 6 14 6 14;" +
                "-fx-cursor: hand;"
            )
        );
        signupButton.setOnMouseExited(e ->
            signupButton.setStyle(
                "-fx-background-color: #2563EB;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 6 14 6 14;" +
                "-fx-cursor: hand;"
            )
        );

        HBox topBar = new HBox(4, loginButton, signupButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10, 14, 0, 14));

        // ── 中央レイアウト ──────────────────────────────────
        VBox root = new VBox(12);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                separator,
                playButton,
                createButton,
                exitButton
        );

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(root);

        this.scene = new Scene(layout);

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

    public Button getPlayButton() {return playButton;}
    public Button getCreateButton() {return createButton;}
    public Button getExitButton() {return exitButton;}
    public Button getLoginButton() {return loginButton;}
    public Button getSignupButton() {return signupButton;}

 
}