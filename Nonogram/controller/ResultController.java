package Nonogram.controller;

import Nonogram.model.ResultModel;
import Nonogram.view.ResultView;

/**
 * リザルト画面の処理を管理するコントローラクラス
 * 太田
 */
public class ResultController {

    private ResultModel model;
    private ResultView view;
    private AppController appController;
    // private PlayRecord record; // TODO: PlayRecordクラス実装後にResultModelの代わりに利用予定

    /**
     * コンストラクタ
     * 
     * @param model リザルト画面で使用するデータ
     * @param view リザルト画面のView
     * @param appController 画面遷移を管理するコントローラ
     */
    public ResultController(ResultModel model, ResultView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;
    }

    /**
     * リザルト画面を初期化し、ボタンイベントを登録する
     */
    public void initialize() {
        view.initialize(model);
        view.render();
        showResult();
        view.showHomeButton().setOnAction(e -> onPuzzleList());
        // view.showRetryButton().setOnAction(e -> onRetry()); // TODO: リトライ機能追加時に有効化
    }

    /**
     * リザルト内容をViewに表示する
     */
    public void showResult() {
        view.displayResult(model);
    }

    /**
     * 問題リスト画面へ戻る
     */
    public void onPuzzleList() {
        appController.navigateTo("list");
    }

    // public void onRetry() {
    //     appController.navigateTo("game");
    // }
}
