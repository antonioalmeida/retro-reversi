package feup.lpoo.reversi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds set of game states, part of the Memento design pattern
 */
public class GameCareTaker implements Serializable {
    /**
     * The game states set
     */
    private ArrayList<GameMemento> mementoList = new ArrayList<GameMemento>();

    /**
     * Adds a new game state to the stored list
     * @param state state to be added
     */
    public void add(GameMemento state) {
        mementoList.add(state);
    }

    /**
     * Getter for the most recently stored state
     * @return last stored state
     */
    public GameMemento getLast() {
        return mementoList.get(mementoList.size()-1);
    }

    /**
     * Deletes the last saved state
     */
    public void removeLast() {
        mementoList.remove(mementoList.size()-1);
    }

    /**
     * Evaluate how many states are stored
     * @return amount of stored game states
     */
    public int getSize() {
        return mementoList.size();
    }
}
