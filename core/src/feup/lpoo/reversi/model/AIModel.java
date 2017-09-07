package feup.lpoo.reversi.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import feup.lpoo.reversi.presenter.ai.AIPresenter;

/**
 * Represents an AI player
 */
public class AIModel extends PlayerModel{
    /**
     * Bridge between the AI and the board
     */
    private AIPresenter presenter;

    /**
     * Calculating status
     */
    private boolean calculating;

    /**
     * Move calculation scheduler
     */
    private final ScheduledExecutorService scheduler;

    /**
     * Constructor, initializes all fields
     * @param piece AI's piece color for the session
     * @param presenter AIPresenter to aid in move calculation
     */
    public AIModel(char piece, AIPresenter presenter) {
        super(piece);
        this.presenter = presenter;
        calculating = false;
        active = true;
        scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Getter for the ready status (depends on calculating status)
     * @return true if ready status is true (next move calculated), false otherwise (starts move calculation with 500ms delay)
     */
    @Override
    public boolean isReady() {
        if(ready) {
            ready = false;
            return true;
        }

        if(!calculating) {
            calculating = true;
            scheduler.schedule(calculateMove, 100, TimeUnit.MILLISECONDS);
        }

        return false;
    }

    /**
     * Calculates the AI's next move in a separate concurrent thread based on strategy present in the AIPresenter
     */
    private Runnable calculateMove = new Runnable() {
        @Override
        public void run() {
            move = presenter.findMove(piece);
            calculating = false;
            ready = true;
        }
    };

}
