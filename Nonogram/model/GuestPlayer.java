package Nonogram.model;

public class GuestPlayer extends Player {

    public GuestPlayer(){
        super("No Name");
    }
    
    @Override
    public int getPlayerId() {
        return 0;
    }

    @Override
    public String getEMail() {
        return null;
    }

    @Override
    public String getHashedPassword() {
        return null;
    }
}
