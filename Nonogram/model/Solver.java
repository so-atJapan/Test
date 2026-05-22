package Nonogram.model;

import java.util.ArrayList;

public class Solver {
    private Clue clue;
    private Grid grid;
    private ArrayList<Grid> gridLogs;

    public Solver(Clue clue, Grid grid){
        this.clue = clue;
        this.grid = grid;
        this.gridLogs = new ArrayList<Grid>();
    }
    public Solver(Clue clue){
        this(clue, new Grid(clue.getRowClues().size(), clue.getColClues().size()));
    }

    public Grid getGrid() {
        return grid;
    }
    
    public void solveAtOnce(){
        for (int i = 0; i < 100; i++) {
            // if(currentCellState.finCheck())break;
            solveStepByStep();
        }
    }
    public void solveStepByStep(){
        step1();
    }

    private void step1(){
        for (int x = 0; x < clue.getRowClues().size(); x++) {
            Cell[] row = grid.getRow(x);
            step1Line(clue.getRowClues().get(x), row);
            grid.setRow(row, x);
        }
        for (int y = 0; y < clue.getColClues().size(); y++) {
            Cell[] col = grid.getCol(y);
            step1Line(clue.getColClues().get(y), col);
            grid.setCol(col, y);
        }
    }

    private void step1Line(ArrayList<Integer> question, Cell[] line){
        Cell[][] patterns = generatePatterns(line.length, question);
        commit1(patterns, line);
    }

    public Cell[][] generatePatterns(int width, ArrayList<Integer> questionNum) {

        // ① パターン数を数える
        int count = countPatterns(width, questionNum, 0, 0);

        // ② 配列確保
        Cell[][] result = new Cell[count][width];

        // ③ 実際に埋める
        Cell[] line = new Cell[width];
        fillPatterns(result, line, questionNum, 0, 0, new int[]{0});

        return result;
    }

    // ----------------------------
    // パターン数カウント
    // ----------------------------
    private int countPatterns(int width, ArrayList<Integer> q, int index, int pos) {

        if (index == q.size()) {
            return 1;
        }

        int blockSize = q.get(index);

        // 残り必要最小長
        int minRemaining = 0;
        for (int i = index; i < q.size(); i++) {
            minRemaining += q.get(i);
        }
        minRemaining += (q.size() - index - 1);

        int count = 0;

        for (int start = pos; start <= width - minRemaining; start++) {

            int nextPos = start + blockSize;

            if (index < q.size() - 1) {
                nextPos++; // 空白1つ
            }

            count += countPatterns(width, q, index + 1, nextPos);
        }

        return count;
    }

    // ----------------------------
    // 実データ生成
    // ----------------------------
    private void fillPatterns(Cell[][] result, Cell[] line, ArrayList<Integer> q,
                                     int index, int pos, int[] writeIndex) {

        if (index == q.size()) {
            // 残り白
            for (int i = pos; i < line.length; i++) {
                line[i] = new Cell(CellState.EMPTY);
            }

            result[writeIndex[0]] = line.clone();
            writeIndex[0]++;
            return;
        }

        int blockSize = q.get(index);

        int minRemaining = 0;
        for (int i = index; i < q.size(); i++) {
            minRemaining += q.get(i);
        }
        minRemaining += (q.size() - index - 1);

        for (int start = pos; start <= line.length - minRemaining; start++) {

            // 白で埋める
            for (int i = pos; i < start; i++) {
                line[i] = new Cell(CellState.EMPTY);
            }

            // 黒ブロック
            for (int i = 0; i < blockSize; i++) {
                line[start + i] = new Cell(CellState.FILLED);
            }

            int nextPos = start + blockSize;

            if (index < q.size() - 1) {
                line[nextPos] = new Cell(CellState.EMPTY);
                nextPos++;
            }

            fillPatterns(result, line, q, index + 1, nextPos, writeIndex);
        }
    }

    private void commit1(Cell[][] patterns, Cell[] line) {

        // 現在の確定情報と矛盾するパターンを除外
        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i] == null) continue;
            for (int j = 0; j < line.length; j++) {
                if (line[j].getState() == CellState.FILLED
                        && patterns[i][j].getState() != CellState.FILLED) {
                    patterns[i] = null;
                    break;
                }
                if (line[j].getState() == CellState.MARKED
                        && patterns[i][j].getState() != CellState.EMPTY) {
                    patterns[i] = null;
                    break;
                }
            }
        }

        // 全パターンで共通するセルを確定
        // パターン内の白はEMPTY、黒はFILLEDで表現されている
        Cell[] allMarked = new Cell[line.length]; // 全パターンでEMPTY → MARKED（白）確定
        Cell[] allFilled = new Cell[line.length]; // 全パターンでFILLED → FILLED（黒）確定
        for (int i = 0; i < line.length; i++) {
            allMarked[i] = new Cell(CellState.MARKED);
            allFilled[i] = new Cell(CellState.FILLED);
        }

        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i] == null) continue;
            for (int j = 0; j < line.length; j++) {
                if (patterns[i][j].getState() != CellState.EMPTY) {   // 修正：EMPTY で比較
                    allMarked[j].setState(CellState.EMPTY);
                }
                if (patterns[i][j].getState() != CellState.FILLED) {
                    allFilled[j].setState(CellState.EMPTY);
                }
            }
        }

        for (int i = 0; i < line.length; i++) {
            if (allMarked[i].getState() == CellState.MARKED) line[i].setState(CellState.MARKED);
            if (allFilled[i].getState() == CellState.FILLED) line[i].setState(CellState.FILLED);
        }
    }

}