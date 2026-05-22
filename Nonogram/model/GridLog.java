package Nonogram.model;

import java.util.LinkedList;

public class GridLog {
    private LinkedList<Grid> gridLogs;
    private int pointer;

    //コンストラクタ
    public GridLog(){
        this.gridLogs = new LinkedList<Grid>();
        this.pointer = -1;
    }
    public GridLog(Grid grid){
        this();
        this.push(grid);
    }

    // 新しい操作を記録
    public void push(Grid grid) {
        // pointer より先を削除
        while (gridLogs.size() > pointer + 1) {
            gridLogs.removeLast();
        }
        gridLogs.addLast(grid.copy());
        pointer++;
        System.out.println(pointer);
    }

    public void undo() {
        if (pointer > 0) pointer--;
        System.out.println(pointer);
    }

    public void redo() {
        if (pointer < gridLogs.size() - 1) pointer++;
        System.out.println(pointer);
    }

    // 任意ステップ参照
    public Grid get(int step) {
        return gridLogs.get(step);
    }

    public Grid get() {
        return gridLogs.get(pointer).copy();
    }

    public boolean canUndo() { return pointer > 0; }
    public boolean canRedo() { return pointer < gridLogs.size() - 1; }
}