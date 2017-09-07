package feup.lpoo.reversi;

import feup.lpoo.reversi.model.GameModel;

/**
 * Interface to encapsulate Google Play Games Services' related calls;
 */
public interface PlayServices {
        /**
         * Logs in if not signed already
         */
        public void signIn();

        /**
         * Logs in if not signed already
         */
        public void signOut();

        /**
         * Calls match ending related achievements
         * @param victory true if user won the match
         */
        public void matchCompleted(boolean victory);

        /**
         * Shows the user's achievements board, if logged in
         */
        public void showAchievements();

        /**
         * Checks if user is signed in
         * @return true if user is signed in
         */
        public boolean isSignedIn();

        /**
         * Opens the user's online matches history screen
         */
        public void checkGames();

        /**
         * Starts an online match with
         */
        public void quickMatch();

        /**
         * Gets the current match's info
         * @return current match's GameModel instance
         */
        public GameModel getMatchData();

        /**
         * Takes turn on current online match
         * @param data game object with updated turn info
         */
        public void takeTurn(GameModel data);

        /**
         * Takes turn on current online match and makes appropriate ending calls
         * @param data game object with updated turn info
         */
        public void takeLastTurn(GameModel data);

        /**
         * Makes online match ending calls
         */
        public void finishMatch();

        /**
         * Creates a new online match with the same users
         */
        public void rematch();
}
