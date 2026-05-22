package Nonogram.model;
import java.sql.*;
import java.util.ArrayList;


public class DAO {
    
    private final String DB_PATH = "jdbc:sqlite:src\\Nonogram\\model\\Nonogram.DB";

    private final String SELECT_ALL_PUZZLES =
        "SELECT" +
        "    p.puzzle_id," +
        "    p.title," +
        "    p.grid_size_x," +
        "    p.grid_size_y," +
        "    d.difficulty_name AS difficulty," +
        "    p.is_public," +
        "    p.created_at," +
        "    p.created_by," +
        "    p.solution," +
        "    p.clue_row, " +
        "    p.clue_col " +
        "FROM puzzles p " +
        "JOIN difficulty d" +
        "    ON p.difficulty_id = d.difficulty_id;";

    private final String INSERT_PUZZLE = 
        "INSERT INTO puzzles ( " +
        "    title, " +
        "    grid_size_x, " +
        "    grid_size_y, " +
        "    difficulty_id, " +
        "    is_public, " +
        "    created_by, " +
        "    solution, " +
        "    clue_row, " +
        "    clue_col " +
        "    ) " +
        "VALUES ( " +
        "    ?, " +
        "    ?, " +
        "    ?, " +
        "    ( " +
        "        SELECT difficulty_id " +
        "        FROM difficulty " +
        "        WHERE difficulty_name = ? " +
        "    ), " +
        "    ?, " +
        "    ?, " +
        "    ?, " +
        "    ?, " +
        "    ? " +
        "    ); ";

    private final String UPDATE_PUZZLE =
        "UPDATE puzzles " +
        "SET " +
        "    title = ?, " +
        "    grid_size_x = ?, " +
        "    grid_size_y = ?, " +
        "    difficulty_id = ( " +
        "        SELECT difficulty_id " +
        "        FROM difficulty " +
        "        WHERE difficulty_name = ? " +
        "    ), " +
        "    is_public = ?, " +
        "    created_at = ?, " +
        "    created_by = ?, " +
        "    solution = ?, " +
        "    clue_row = ?, " +
        "    clue_col = ? " +
        "WHERE puzzle_id = ?";


    private final String SELECT_PLAYER_BY_EMAIL =
    "SELECT player_id, user_name, e_mail, password_hash " +
    "FROM players " +
    "WHERE e_mail = ?";

    private final String INSERT_PLAYER =
    "INSERT INTO players (user_name, password_hash, e_mail) VALUES (?, ?, ?)";

    public  ArrayList<Puzzle> getPuzzleAll(){
        ArrayList<Puzzle> puzzleList = new ArrayList<Puzzle>();

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_PUZZLES);
        ){
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next()){
                    Puzzle puzzle = new Puzzle();

                    puzzle.setPuzzleId(rs.getInt(1));
                    puzzle.setTitle(rs.getString(2));
                    puzzle.setGridSizeX(rs.getInt(3));
                    puzzle.setGridSizeY(rs.getInt(4));
                    puzzle.setDifficulty(rs.getString(5));
                    puzzle.setIsPublic(rs.getBoolean(6));
                    puzzle.setCreatedAt(rs.getTimestamp(7).toLocalDateTime());
                    puzzle.setCreatedBy(rs.getString(8));
                    puzzle.setSolution(rs.getString(9));
                    puzzle.setClue(new Clue(rs.getString(10), rs.getString(11)));

                    puzzleList.add(puzzle);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("呼出失敗");
        }
        return puzzleList;
    }

    public void setPuzzle(Puzzle puzzle){

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(INSERT_PUZZLE);
        ){

            ps.setString(1, puzzle.getTitle());
            ps.setInt(2, puzzle.getGridSizeX());
            ps.setInt(3, puzzle.getGridSizeY());
            ps.setString(4, puzzle.getDifficulty().toString());
            ps.setBoolean(5, puzzle.getIsPublic());
            ps.setString(6, puzzle.getCreatedBy());
            ps.setString(7, puzzle.getSolution().toString());
            ps.setString(8, puzzle.getClue().rowToString());
            ps.setString(9, puzzle.getClue().colToString());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("更新失敗");
        }
    }

    public void updatePuzzle(Puzzle puzzle){

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(UPDATE_PUZZLE);
        ) {

            ps.setString(1, puzzle.getTitle());
            ps.setInt(2, puzzle.getGridSizeX());
            ps.setInt(3, puzzle.getGridSizeY());
            ps.setString(4, puzzle.getDifficulty().toString());
            ps.setBoolean(5, puzzle.getIsPublic());
            ps.setTimestamp(6, Timestamp.valueOf(puzzle.getCreatedAt()));
            ps.setString(7, puzzle.getCreatedBy());
            ps.setString(8, puzzle.getSolution().toString());
            ps.setString(9, puzzle.getClue().rowToString());
            ps.setString(10, puzzle.getClue().colToString());
            ps.setInt(11, puzzle.getPuzzleId());

            ps.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * メールアドレスからログインプレイヤー情報を取得する
     *
     * @param email 入力されたメールアドレス
     * @return 見つかったログインプレイヤー。存在しない場合はnull
     */
    public LoginPlayer getLoginPlayer(String email) {

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(SELECT_PLAYER_BY_EMAIL);
        ) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LoginPlayer(
                        rs.getString("user_name"),
                        rs.getInt("player_id"),
                        rs.getString("e_mail"),
                        rs.getString("password_hash")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 指定されたメールアドレスのプレイヤーが存在するか判定する
     *
     * @param email 確認するメールアドレス
     * @return 存在する場合はtrue
     */
    public boolean existsPlayerByEmail(String email) {
        return getLoginPlayer(email) != null;
    }

    /**
     * 新しいプレイヤーをDBへ登録する
     *
     * @param userName 登録するユーザー名
     * @param email 登録するメールアドレス
     * @param passwordHash ハッシュ化済みパスワード
     * @return 登録に成功した場合はtrue
     */
    public boolean insertPlayer(String userName, String email, String passwordHash) {

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(INSERT_PLAYER);
        ) {
            ps.setString(1, userName);
            ps.setString(2, passwordHash);
            ps.setString(3, email);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
