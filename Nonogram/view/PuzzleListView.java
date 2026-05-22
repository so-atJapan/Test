package Nonogram.view;
 
import java.util.ArrayList;


import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class PuzzleListView{
 
    private Stage stage;
    private Scene scene;

    private ArrayList<Puzzle> puzzleList;

    private Button[] selectButtons;
    private MenuItem[] editMenuItems;
    private MenuItem[] solverMenuItems;

    public PuzzleListView(Stage stage){
        this.stage = stage;
    }
    
    public void initialize(PuzzleList puzzleList){

        this.puzzleList = puzzleList.getPuzzleList();
        
        Label selectLabel = new Label("問題選択");
        selectLabel.setFont(new Font(20));


        int length = this.puzzleList.size();

        selectButtons = new Button[length];
        ContextMenu[] contextMenus = new ContextMenu[length];
        editMenuItems = new MenuItem[length];
        solverMenuItems = new MenuItem[length];
        MenuItem[] deleteMenuItems = new MenuItem[length];
        
        Menu menu1 = new Menu("メニュー");
        MenuItem homeMenuItem = new MenuItem("ホーム");
        MenuItem synchroMenuItem = new MenuItem("同期");
        MenuItem helpMenuItem = new MenuItem("ヘルプ");
        MenuItem finMenuItem = new MenuItem("終了");
        menu1.getItems().addAll(homeMenuItem,synchroMenuItem,helpMenuItem,finMenuItem);
        // homeMenuItem.setOnAction((ActionEvent e) -> {
        //     this.mainScene(stage);
        // });
        // synchroMenuItem.setOnAction((ActionEvent e) -> {
        //     this.mainScene(stage);
        // });
        // helpMenuItem.setOnAction((ActionEvent e) -> {
        //     //ヘルプ画面表示
        // });
        // finMenuItem.setOnAction((ActionEvent e) -> {
        //     if(this.ShowAlert_Confim("終了確認", "アプリを終了しますか")){
        //         Platform.exit();
        //     }
        // });
        MenuBar menuBar = new MenuBar(menu1);
        menuBar.setStyle("-fx-font-size: 15px;" + "-fx-background-color: #cccccc");
        
        
        for (int i = 0; i < length; i++) {
            int index = i;
            String tatle = this.puzzleList.get(index).getTitle();
            selectButtons[index] = new Button(tatle);
            selectButtons[index].setPrefSize(140, 40);
            selectButtons[index].setFont(new Font(20));
            editMenuItems[index] = new MenuItem("編集");
            editMenuItems[index].setStyle("-fx-font-size: 17px;");
            solverMenuItems[index] = new MenuItem("ソルバー");
            solverMenuItems[index].setStyle("-fx-font-size: 17px;");
            deleteMenuItems[index] = new MenuItem("削除");
            deleteMenuItems[index].setStyle("-fx-font-size: 17px;");
            contextMenus[index] = new ContextMenu(editMenuItems[index], solverMenuItems[index], deleteMenuItems[index]);
            selectButtons[index].setOnContextMenuRequested(e -> {
                contextMenus[index].show(selectButtons[index], e.getScreenX(), e.getScreenY());
            });           
        }
 
        
        
        VBox temp1VBox = new VBox();
        temp1VBox.getChildren().addAll(selectButtons);
        
        ScrollPane scrollPane = new ScrollPane (temp1VBox);
        scrollPane.setPrefSize(155, 350);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        HBox hBox = new HBox(scrollPane);
        hBox.setPrefWidth(155);

        VBox selectVBox = new VBox(menuBar,selectLabel,hBox);
        scene = new Scene(selectVBox, 300, 500);
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

    public Button[] getSelectButtons() {return selectButtons;}
    
    public MenuItem[] getEditMenuItems() {return editMenuItems;}
    
    public MenuItem[] getSolverMenuItems() {return solverMenuItems;}
 
}