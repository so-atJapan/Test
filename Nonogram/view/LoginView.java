package Nonogram.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ログイン画面をセミモーダルで表示するViewクラス
 */
public class LoginView {

    private Stage primaryStage;
    private Stage dialogStage;
    private Scene scene;

    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button cancelButton;
    private Hyperlink signupLink;
    private Label messageLabel;
    private boolean loginConfirmed;
    private boolean signupRequested;

    /**
     * コンストラクタ
     *
     * @param primaryStage 親画面のStage
     */
    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * ログイン画面の部品を生成する
     */
    public void initialize() {
        loginConfirmed = false;
        signupRequested = false;

        dialogStage = new Stage();
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Label titleLabel = new Label("ログイン");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        emailField = new TextField();
        emailField.setPromptText("example@email.com");

        passwordField = new PasswordField();
        passwordField.setPromptText("パスワード");

        loginButton = new Button("ログイン");
        cancelButton = new Button("キャンセル");
        loginButton.setPrefWidth(120);
        cancelButton.setPrefWidth(120);

        HBox buttonBox = new HBox(8, loginButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        messageLabel = new Label("");
        messageLabel.setTextFill(Color.FIREBRICK);
        messageLabel.setWrapText(true);

        Label guideLabel = new Label("アカウントをお持ちでない方");
        guideLabel.setStyle("-fx-text-fill: gray;");
        signupLink = new Hyperlink("アカウントを作成");

        VBox root = new VBox(10, titleLabel, emailField, passwordField, buttonBox, messageLabel, guideLabel, signupLink);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 360, 320);
    }

    /**
     * ログイン画面をセミモーダルで表示する
     */
    public void render() {
        dialogStage.setTitle("Login");
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    /**
     * ログイン画面を閉じる
     */
    public void close() {
        dialogStage.close();
    }

    /**
     * ログイン確定時にログイン画面を閉じる
     */
    public void confirmLogin() {
        loginConfirmed = true;
        dialogStage.close();
    }

    /**
     * サインアップ画面への遷移要求を保持してログイン画面を閉じる
     */
    public void requestSignup() {
        signupRequested = true;
        dialogStage.close();
    }

    /**
     * ログインが確定されたかどうかを取得する
     *
     * @return ログインが確定された場合はtrue
     */
    public boolean isLoginConfirmed() {
        return loginConfirmed;
    }

    /**
     * サインアップ画面への遷移が要求されたかどうかを取得する
     *
     * @return サインアップ画面への遷移が要求された場合はtrue
     */
    public boolean isSignupRequested() {
        return signupRequested;
    }

    /**
     * 入力されたメールアドレスを取得する
     *
     * @return メールアドレス
     */
    public String getEmail() {
        return emailField.getText();
    }

    /**
     * 入力されたパスワードを取得する
     *
     * @return パスワード
     */
    public String getPassword() {
        return passwordField.getText();
    }

    /**
     * ログインボタンを取得する
     *
     * @return ログインボタン
     */
    public Button getLoginButton() {
        return loginButton;
    }

    /**
     * キャンセルボタンを取得する
     *
     * @return キャンセルボタン
     */
    public Button getCancelButton() {
        return cancelButton;
    }

    /**
     * サインアップ画面へのリンクを取得する
     *
     * @return サインアップリンク
     */
    public Hyperlink getSignupLink() {
        return signupLink;
    }

    /**
     * ログイン結果メッセージを表示する
     *
     * @param message 表示するメッセージ
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
