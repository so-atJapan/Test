package Nonogram.controller;

import Nonogram.model.GameModel;
import Nonogram.model.Grid;
import Nonogram.model.GuestPlayer;
import Nonogram.model.LoginModel;
import Nonogram.model.Player;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;
import Nonogram.model.PuzzleList;
import Nonogram.model.ResultModel;
import Nonogram.model.SignupModel;
import Nonogram.model.SolverModel;
import Nonogram.view.GameView;
import Nonogram.view.LoginView;
import Nonogram.view.HomeView;
import Nonogram.view.PuzzleEditorView;
import Nonogram.view.PuzzleListView;
import Nonogram.view.ResultView;
import Nonogram.view.SignupView;
import Nonogram.view.SolverView;
import javafx.stage.Stage;

/**
 * 全体の画面遷移を管理するコントローラクラス
 * 太田
 */
public class AppController {

    private Stage stage;
    private Puzzle pendingPuzzle;
    private PuzzleListController puzzleListController;
    private GameController gameController;
    private PuzzleEditorController puzzleEditorController;
    private ResultController resultController;
    private Grid completedGrid;
    private int tickSeconds;
    private LoginController loginController;
    private SignupController signupController;
    private Player currentPlayer = new GuestPlayer();

    /**
     * コンストラクタ
     * 
     * @param stage 画面表示に使用するStage
     */
    public AppController(Stage stage) {
        this.stage = stage;
    }

    /**
     * 起動時の初期画面を表示する
     */
    public void initialize() {
        navigateTo("home");
    }

    /**
     * 指定された画面へ遷移する
     *
     * @param destination 遷移先を表す文字列
     */
    public void navigateTo(String destination) {
        switch (destination) {
            case "home":
                showHome();
                break;
            case "login":
                showLogin();
                break;
            case "signup":
                showSignup();
                break;
            case "list":
                showPuzzleList();
                break;
            case "game":
                showGame();
                break;
            case "create":
                showCreate();
                break;
            case "editor":
                showEditor();
                break;
            case "result":
                showResult();
                break;
            case "solver":
                showSolver();
                break;
            default:
                showPuzzleList();
                break;
        }
    }

    /**
     * 現在のプレイヤーを取得する
     *
     * @return 現在のプレイヤー
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * 現在のプレイヤーを設定する
     *
     * @param currentPlayer 設定するプレイヤー
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * 選択されたパズルを保持する
     *
     * @param puzzle 選択されたパズル
     */
    public void setPendingPuzzle(Puzzle puzzle) {
        this.pendingPuzzle = puzzle;
    }

    /**
     * リザルト画面で使用する結果データを保持する
     *
     * @param puzzle 完成したパズル
     * @param completedGrid 完成時の盤面
     * @param elapsedSeconds クリアまでにかかった秒数
     */
    public void setResultData(Puzzle puzzle, Grid completedGrid, int elapsedSeconds) {
        this.pendingPuzzle = puzzle;
        this.completedGrid = completedGrid;
        this.tickSeconds = elapsedSeconds;
    }

    /**
     * ホーム画面を生成して表示する
     */
    public void showHome() {

        HomeView homeView = new HomeView(stage);

        HomeController homeController = new HomeController(homeView, this);
        homeController.initialize();

    }

    /**
     * 問題リスト画面を生成して表示する
     */
    public void showPuzzleList() {
        PuzzleList puzzleList = new PuzzleList();
        puzzleList.initialize();

        PuzzleListView listView = new PuzzleListView(stage);
        listView.initialize(puzzleList);

        puzzleListController = new PuzzleListController(listView, puzzleList, this);
        puzzleListController.initialize();
    }

    /**
     * ゲーム画面を生成して表示する
     */
    public void showGame() {
        GameModel model = new GameModel(pendingPuzzle);
        GameView view = new GameView(stage);

        gameController = new GameController(model, view, this);
        gameController.initialize();
    }

    /**
     * パズル作成画面を生成して表示する
     */
    public void showCreate() {
        PuzzleEditorModel model = new PuzzleEditorModel();
        PuzzleEditorView view = new PuzzleEditorView(stage);

        puzzleEditorController = new PuzzleEditorController(model, view, this);
        puzzleEditorController.initialize();
    }

    /**
     * パズル編集画面を生成して表示する
     */
    public void showEditor() {
        Puzzle target = this.pendingPuzzle;
        PuzzleEditorModel model = new PuzzleEditorModel(target);
        PuzzleEditorView view = new PuzzleEditorView(stage);

        puzzleEditorController = new PuzzleEditorController(model, view, this);
        puzzleEditorController.initialize();
    }

    /**
     * リザルト画面を生成して表示する
     */
    public void showResult() {
        ResultModel model = new ResultModel(pendingPuzzle, completedGrid, tickSeconds);
        ResultView view = new ResultView(stage);

        resultController = new ResultController(model, view, this);
        resultController.initialize();
    }

    /**
     * ソルバー画面を生成して表示する
     */
    public void showSolver() {
        SolverModel solverModel = new SolverModel(pendingPuzzle);
        SolverView solverView = new SolverView(stage);

        SolverController solverController  = new SolverController(solverModel, solverView, null);
        solverController.initialize();
    }


    /**
     * ログイン画面を生成してセミモーダルで表示する
     */
    public void showLogin() {
        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView(stage);

        loginController = new LoginController(loginModel, loginView, this);
        loginController.initialize();
    }

    /**
     * サインアップ画面を生成してセミモーダルで表示する
     */
    public void showSignup() {
        SignupModel model = new SignupModel();
        SignupView  view  = new SignupView(stage);
 
        signupController = new SignupController(model, view, this);
        signupController.initialize();
    }

    public void exitGame(){
        this.stage.close();
    }
}
