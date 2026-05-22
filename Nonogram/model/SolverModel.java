package Nonogram.model;

public class SolverModel {

    private Puzzle puzzle;
    private Grid grid;
    private GridLog gridLog;

    // コンストラクタ
    public SolverModel(Puzzle puzzle) {
        this.puzzle = puzzle;

        // 初期化
        grid = new Grid(puzzle.getGridSizeX(), puzzle.getGridSizeY());

        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid.setCellAt(x, y, CellState.EMPTY);
            }
        }

        this.gridLog = new GridLog(grid);
    }

    // Puzzle取得
    public Puzzle getPuzzle() {
        return puzzle;
    }

    // 全セル取得
    public Grid getGrid() {
        return grid;
    }

    // セル状態切り替え
    public void toggle(int x, int y, CellState cellState) {
        grid.getCellAt(x, y).toggle(cellState);
    }

    // 盤面リセット
    public void reset() {
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid.getCellAt(x, y).setState(CellState.EMPTY);
            }
        }
    }

    public void setState(int x, int y, CellState state) {
        grid.getCellAt(x, y).setState(state);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void pushGridLog(){
        this.gridLog.push(this.grid);
        this.grid = this.gridLog.get();
    }
    
    public void undoGridLog(){
        this.gridLog.undo();
        this.grid = this.gridLog.get();
    }
    
    public void redoGridLog(){
        this.gridLog.redo();
    }
    
    public Grid getCurrentLog(){
        return this.gridLog.get();
    }
}
