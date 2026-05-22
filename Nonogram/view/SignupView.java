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
 * サインアップ画面をセミモーダルで表示するViewクラス
 */
public class SignupView {

    private Stage primaryStage;
    private Stage dialogStage;
    private Scene scene;

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button signupButton;
    private Button cancelButton;
    private Hyperlink loginLink;
    private Label messageLabel;
    private boolean signupConfirmed;
    private boolean loginRequested;

    /**
     * コンストラクタ
     *
     * @param primaryStage 親画面のStage
     */
    public SignupView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * サインアップ画面の部品を生成する
     */
    public void initialize() {
        signupConfirmed = false;
        loginRequested = false;

        dialogStage = new Stage();
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Label titleLabel = new Label("サインアップ");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        nameField = new TextField();
        nameField.setPromptText("ユーザー名");

        emailField = new TextField();
        emailField.setPromptText("example@email.com");

        passwordField = new PasswordField();
        passwordField.setPromptText("パスワード");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("パスワード確認");

        signupButton = new Button("作成");
        cancelButton = new Button("キャンセル");
        signupButton.setPrefWidth(120);
        cancelButton.setPrefWidth(120);

        HBox buttonBox = new HBox(8, signupButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        messageLabel = new Label("");
        messageLabel.setTextFill(Color.FIREBRICK);
        messageLabel.setWrapText(true);

        Label guideLabel = new Label("アカウントをお持ちの方");
        guideLabel.setStyle("-fx-text-fill: gray;");
        loginLink = new Hyperlink("ログインへ");

        VBox root = new VBox(10, titleLabel, nameField, emailField, passwordField, confirmPasswordField, buttonBox,
                messageLabel, guideLabel, loginLink);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 360, 400);
    }

    /**
     * サインアップ画面をセミモーダルで表示する
     */
    public void render() {
        dialogStage.setTitle("Signup");
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    /**
     * サインアップ画面を閉じる
     */
    public void close() {
        dialogStage.close();
    }

    /**
     * サインアップ確定時にサインアップ画面を閉じる
     */
    public void confirmSignup() {
        signupConfirmed = true;
        dialogStage.close();
    }

    /**
     * ログイン画面への遷移要求を保持してサインアップ画面を閉じる
     */
    public void requestLogin() {
        loginRequested = true;
        dialogStage.close();
    }

    /**
     * サインアップが確定されたかどうかを取得する
     *
     * @return サインアップが確定された場合はtrue
     */
    public boolean isSignupConfirmed() {
        return signupConfirmed;
    }

    /**
     * ログイン画面への遷移が要求されたかどうかを取得する
     *
     * @return ログイン画面への遷移が要求された場合はtrue
     */
    public boolean isLoginRequested() {
        return loginRequested;
    }

    /**
     * 入力されたユーザー名を取得する
     *
     * @return ユーザー名
     */
    public String getUserName() {
        return nameField.getText();
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
     * 入力された確認用パスワードを取得する
     *
     * @return 確認用パスワード
     */
    public String getConfirmPassword() {
        return confirmPasswordField.getText();
    }

    /**
     * サインアップボタンを取得する
     *
     * @return サインアップボタン
     */
    public Button getSignupButton() {
        return signupButton;
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
     * ログイン画面へのリンクを取得する
     *
     * @return ログインリンク
     */
    public Hyperlink getLoginLink() {
        return loginLink;
    }

    /**
     * サインアップ結果メッセージを表示する
     *
     * @param message 表示するメッセージ
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
