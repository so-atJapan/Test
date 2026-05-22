package Nonogram.controller;

import Nonogram.model.SignupModel;
import Nonogram.view.SignupView;

/**
 * サインアップ画面の入力処理と画面遷移を管理するコントローラクラス
 */
public class SignupController {

    private SignupModel model;
    private SignupView view;
    private AppController appController;
    private String nextDestination;

    /**
     * コンストラクタ
     *
     * @param model サインアップ処理で使用するModel
     * @param view サインアップ画面のView
     * @param appController 画面遷移を管理するコントローラ
     */
    public SignupController(SignupModel model, SignupView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;
    }

    /**
     * サインアップ画面を初期化し、ボタンイベントを登録する
     */
    public void initialize() {
        view.initialize();

        view.getSignupButton().setOnAction(e -> onSignup());
        view.getLoginLink().setOnAction(e -> onLogin());
        view.getCancelButton().setOnAction(e -> view.close());

        view.render();

        if (nextDestination != null) {
            appController.navigateTo(nextDestination);
        }
    }

    /**
     * サインアップボタンが押されたときの処理
     * 入力情報が正しければアカウントを作成し、問題リスト画面へ遷移する
     */
    private void onSignup() {
        if (isBlank(view.getUserName()) || isBlank(view.getEmail())
                || isBlank(view.getPassword()) || isBlank(view.getConfirmPassword())) {
            view.showMessage("すべての項目を入力してください。");
            return;
        }

        if (!view.getEmail().contains("@")) {
            view.showMessage("メールアドレスの形式を確認してください。");
            return;
        }

        if (view.getPassword().length() < 4) {
            view.showMessage("パスワードは4文字以上で入力してください。");
            return;
        }

        if (!view.getPassword().equals(view.getConfirmPassword())) {
            view.showMessage("確認用パスワードが一致しません。");
            return;
        }

        if (model.existsPlayerByEmail(view.getEmail())) {
            view.showMessage("このメールアドレスはすでに登録されています。");
            return;
        }

        boolean success = model.signup(
                view.getUserName(),
                view.getEmail(),
                view.getPassword(),
                view.getConfirmPassword()
        );

        if (success) {
            appController.setCurrentPlayer(model.getCreatedPlayer());
            nextDestination = "list";
            view.confirmSignup();
        } else {
            view.showMessage("アカウントを作成できませんでした。");
        }
    }

    /**
     * ログイン画面へのリンクが押されたときの処理
     */
    private void onLogin() {
        nextDestination = "login";
        view.confirmSignup();
    }

    /**
     * 文字列が未入力かどうかを判定する
     *
     * @param value 判定する文字列
     * @return nullまたは空文字の場合はtrue
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}