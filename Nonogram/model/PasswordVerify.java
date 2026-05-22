package Nonogram.model;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordVerify {

    // パスワード登録時：ハッシュ化して保存
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        // 12 = コストファクター（2^12回ハッシュを繰り返す）
    }

    // ログイン時：入力と保存済みハッシュを照合
    public static boolean verify(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}