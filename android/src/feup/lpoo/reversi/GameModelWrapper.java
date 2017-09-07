package feup.lpoo.reversi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.PlayerModel;
import feup.lpoo.reversi.model.UserModel;

public class GameModelWrapper implements Serializable {

    private GameModel game;
    private PlayerModel blackPlayer;
    private PlayerModel whitePlayer;

    public GameModelWrapper() {
        blackPlayer = new UserModel('B');
        blackPlayer.setActive(false);
        whitePlayer = new UserModel('W');
        whitePlayer.setActive(true);

        game = new GameModel(blackPlayer, whitePlayer);
    }

    public GameModelWrapper(GameModel data) {
        if(data.getCurrentPlayer().getPiece() == 'B') {
            blackPlayer = data.getCurrentPlayer();
            whitePlayer = data.getNonCurrentPlayer();
        }
        else {
            blackPlayer = data.getNonCurrentPlayer();
            whitePlayer = data.getCurrentPlayer();
        }

        game = data;
    }

    public void switchPlayers() {
        if(blackPlayer.isActive()) {
            blackPlayer.setActive(false);
            whitePlayer.setActive(true);
        }
        else {
            blackPlayer.setActive(true);
            whitePlayer.setActive(false);
        }
    }

    public GameModel getGame() {
        return game;
    }

    public byte[] convertToByteArray(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try{
            os = new ObjectOutputStream(out);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            os.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static GameModelWrapper convertFromByteArray(byte[] data){
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is;
        try{
            is = new ObjectInputStream(in);
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        GameModelWrapper result;
        try{
            result = (GameModelWrapper) is.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
