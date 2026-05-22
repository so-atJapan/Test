package Nonogram.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * パスワードをハッシュ化するためのクラス
 */
public class PasswordHasher {

    /**
     * 入力されたパスワードをSHA-256でハッシュ化する
     *
     * @param password ハッシュ化するパスワード
     * @return ハッシュ化された文字列
     */
    public String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for (byte b : encoded) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (Exception e) {
            throw new IllegalStateException("パスワードのハッシュ化に失敗しました。", e);
        }
    }
}