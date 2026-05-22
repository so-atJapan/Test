package Nonogram.model;

public class Cell {

    private CellState state;

    public Cell(CellState state) {
        this.state = state;
    }
    public Cell() {
        this(CellState.EMPTY);
    }

    public CellState getState() {
        return state;
    }

    public void toggle(CellState cellState) {
        if (cellState == this.state) {
            this.state = CellState.EMPTY;
        } else {
            this.state = cellState;
        }
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isFilled() {
        return state == CellState.FILLED;
    }

    public boolean isMarked() {
        return state == CellState.MARKED;
    }

    public boolean isEmpty() {
        return state == CellState.EMPTY;
    }

    // private CellState state = CellState.EMPTY;

    // public void toggle() {
    //     switch (state) {
    //         case EMPTY:
    //             state = CellState.FILLED;
    //             break;
    //         case FILLED:
    //             state = CellState.MARKED;
    //             break;
    //         case MARKED:
    //             state = CellState.EMPTY;
    //             break;
    //     }
    // }

    // public CellState getState() {
    //     return state;
    // }

    
    // private CellState state;

    // public Cell() {
    //     this.state = CellState.EMPTY;
    // }

    // public CellState getState() {
    //     return state;
    // }

    // public void setState(CellState state) {
    //     this.state = state;
    // }

    /**
     * 状態を切り替える
     */
    // public void toggle() {
    //     state = state.next();
    // }

    /**
     * 塗られているか（判定用）
     */
    // public boolean isFilled() {
    //     return state == CellState.FILLED;
    // }
}
