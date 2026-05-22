package Nonogram.model;

public class LoginPlayer extends Player {
    private int playerId;
    private String eMail;
    private String hashedPassword;

    public LoginPlayer(String name, int playerId, String eMail, String hashedPassword){
        super(name);
        this.playerId = playerId;
        this.eMail = eMail;
        this.hashedPassword = hashedPassword;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String getEMail() {
        return eMail;
    }

    @Override
    public String getHashedPassword() {
        return hashedPassword;
    }
}
