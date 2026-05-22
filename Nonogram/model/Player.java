package Nonogram.model;

public abstract class Player{
    private String userName;

    public Player(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public abstract int getPlayerId();

    public abstract String getEMail();

    public abstract String getHashedPassword();
}
