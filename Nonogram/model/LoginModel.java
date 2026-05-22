package Nonogram.model;

/**
 * ログイン処理とログイン結果を保持するModelクラス
 */
public class LoginModel {

    private static DAO dao = new DAO();
    private LoginPlayer loginPlayer;

    /**
     * メールアドレスとパスワードを検証してログインする
     *
     * @param email 入力されたメールアドレス
     * @param password 入力されたパスワード
     * @return ログインに成功した場合はtrue
     */
    public boolean login(String email, String plainPassword) {
        // String passwordHash = passwordHasher.hash(password);
        LoginPlayer challengePlayer = dao.getLoginPlayer(email);
        
        if(challengePlayer == null) return false;


        if(PasswordVerify.verify(plainPassword, challengePlayer.getHashedPassword())){
            this.loginPlayer = challengePlayer;
        }
        

        return loginPlayer != null;
    }

    /**
     * ログインに成功したプレイヤーを取得する
     *
     * @return ログイン済みプレイヤー
     */
    public LoginPlayer getLoginPlayer() {
        return loginPlayer;
    }
}