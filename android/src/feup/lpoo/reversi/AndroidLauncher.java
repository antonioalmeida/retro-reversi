package feup.lpoo.reversi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.example.games.basegameutils.GameHelper;

import java.util.ArrayList;

import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.UserModel;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, PlayServices, GoogleApiClient.ConnectionCallbacks, OnTurnBasedMatchUpdateReceivedListener, OnInvitationReceivedListener {
	private GameHelper gameHelper;
	private Reversi reversi;
    private int requestCode = -1;

	// For our intents
	private static final int RC_SIGN_IN = 9001;
	final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_LOOK_AT_MATCHES = 10001;

	public static final String TAG = "Retro Reversi";
	private AlertDialog mAlertDialog;

	// Current turn-based match
	private TurnBasedMatch mTurnBasedMatch;

	// Should I be showing the turn API?
	public boolean isDoingTurn = false;

	// This is the current match we're in; null if not loaded
	public TurnBasedMatch mMatch;

	// This is the current match data after being unpersisted.
	// Do not retain references to match data once you have
	// taken an action on the match, such as takeTurn()
	public GameModelWrapper mTurnData;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		reversi = new Reversi(this);
		initialize(reversi, config);
		gameHelper.setup(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_LOOK_AT_MATCHES) {
			// Returning from the 'Select Match' dialog

			if (resultCode != Activity.RESULT_OK) {
				// user canceled
				return;
			}

			TurnBasedMatch match = data.getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

			if (match.getData() == null)
			    startMatch(match);
			else
				updateMatch(match);

			Log.d(TAG, "Match = " + match);
		}

		
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {

	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		}
		catch (Exception e) {
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e) {
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void matchCompleted(boolean victory) {
		if(!isSignedIn())
			return;

		Games.Achievements.unlock(gameHelper.getApiClient(),
				getString(R.string.achievement_your_first_match));
		Games.Achievements.increment(gameHelper.getApiClient(),
				getString(R.string.achievement_5_matches), 1);
		Games.Achievements.increment(gameHelper.getApiClient(),
				getString(R.string.achievement_25_matches), 1);
		Games.Achievements.increment(gameHelper.getApiClient(),
				getString(R.string.achievement_50_matches), 1);

		if(victory) {
			Games.Achievements.increment(gameHelper.getApiClient(),
					getString(R.string.achievement_10_wins), 1);
			Games.Achievements.increment(gameHelper.getApiClient(),
					getString(R.string.achievement_25_wins), 1);
			Games.Achievements.increment(gameHelper.getApiClient(),
					getString(R.string.achievement_50_wins), 1);
		}
	}

	@Override
	public void showAchievements() {
		if (isSignedIn())
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 5001);
		else
			signIn();
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void checkGames() {
		if(isSignedIn()) {
			Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(gameHelper.getApiClient());
			startActivityForResult(intent, RC_LOOK_AT_MATCHES);
		}
		else
			signIn();
	}

	@Override
	public void quickMatch() {
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
				1, 1, 0);

		TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
				.setAutoMatchCriteria(autoMatchCriteria).build();

		

		// Start the match
		ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
			@Override
			public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
				processResult(result);
			}
		};
		Games.TurnBasedMultiplayer.createMatch(gameHelper.getApiClient(), tbmc).setResultCallback(cb);
	}

	@Override
	public GameModel getMatchData() {
        if(mTurnData != null) {
			mTurnData.switchPlayers();
			return mTurnData.getGame();
		}

		return null;
	}

	@Override
	public void takeTurn(GameModel data) {
		String nextParticipantId = getNextParticipantId();
		// Create the next turn
        mTurnData = new GameModelWrapper(data);

		

		Games.TurnBasedMultiplayer.takeTurn(gameHelper.getApiClient(), mMatch.getMatchId(),
				mTurnData.convertToByteArray(), nextParticipantId).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});

		mTurnData = null;
	}

	@Override
	public void takeLastTurn(GameModel data) {
		
		mTurnData = new GameModelWrapper(data);

		Games.TurnBasedMultiplayer.finishMatch(gameHelper.getApiClient(), mMatch.getMatchId(), mTurnData.convertToByteArray())
				.setResultCallback(new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});

		isDoingTurn = false;
	}

	@Override
	public void finishMatch() {
		Games.TurnBasedMultiplayer.finishMatch(gameHelper.getApiClient(), mMatch.getMatchId())
				.setResultCallback(new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});

		isDoingTurn = false;
	}

	@Override
	public void rematch() {
		Games.TurnBasedMultiplayer.rematch(gameHelper.getApiClient(), mMatch.getMatchId()).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
					@Override
					public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
						processResult(result);
					}
				});
		mMatch = null;
		isDoingTurn = false;
	}

	public void startMatch(TurnBasedMatch match) {
		System.out.println("Started match");

		mTurnData = new GameModelWrapper();

		mMatch = match;

		String playerId = Games.Players.getCurrentPlayerId(gameHelper.getApiClient());
		String myParticipantId = mMatch.getParticipantId(playerId);

		Games.TurnBasedMultiplayer.takeTurn(gameHelper.getApiClient(), match.getMatchId(),
				mTurnData.convertToByteArray(), myParticipantId).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});
	}

	// This is the main function that gets called when players choose a match
	// from the inbox, or else create a match and want to start it.
	public void updateMatch(TurnBasedMatch match) {
		mMatch = match;

		int status = match.getStatus();
		int turnStatus = match.getTurnStatus();

		switch (status) {
			case TurnBasedMatch.MATCH_STATUS_CANCELED:
				showWarning("Canceled!", "This game was canceled!");
				return;
			case TurnBasedMatch.MATCH_STATUS_EXPIRED:
				showWarning("Expired!", "This game is expired.  So sad!");
				return;
			case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
				showWarning("Waiting for auto-match...",
						"We're still waiting for an automatch partner.");
				return;
			case TurnBasedMatch.MATCH_STATUS_COMPLETE:
				if (turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
					showWarning(
							"Complete!",
							"This game is over!");
					break;
				}

				// Note that in this state, you must still call "Finish" yourself,
				// so we allow this to continue.
				showWarning("Complete!",
						"This game is over! You can only finish it now.");
		}

		// OK, it's active. Check on turn status.
		switch (turnStatus) {
			case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
				mTurnData = GameModelWrapper.convertFromByteArray(mMatch.getData());
				reversi.setOnlineMatchScreen();
				return;
			case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
				// Should return results.
				showWarning("Alas...", "It's not your turn.");
				break;
			case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
				showWarning("Good inititative!",
						"Still waiting for invitations.\n\nBe patient!");
		}

		mTurnData = null;

	}

	public String getNextParticipantId() {
		String playerId = Games.Players.getCurrentPlayerId(gameHelper.getApiClient());
		String myParticipantId = mMatch.getParticipantId(playerId);

		ArrayList<String> participantIds = mMatch.getParticipantIds();

		int desiredIndex = -1;

		for (int i = 0; i < participantIds.size(); i++) {
			if (participantIds.get(i).equals(myParticipantId)) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < participantIds.size()) {
			return participantIds.get(desiredIndex);
		}

		if (mMatch.getAvailableAutoMatchSlots() <= 0) {
			// You've run out of automatch slots, so we start over.
			return participantIds.get(0);
		} else {
			// You have not yet fully automatched, so null will find a new
			// person to play against.
			return null;
		}
	}

	private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
		if (!checkStatusCode(null, result.getStatus().getStatusCode())) {
			return;
		}

		isDoingTurn = false;

		showWarning("Match",
				"This match is canceled.  All other players will have their game ended.");
	}
	private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
		TurnBasedMatch match = result.getMatch();

		if (!checkStatusCode(match, result.getStatus().getStatusCode()))
			return;

		if (match.getData() != null) {
			// This is a game that has already started, so I'll just start
			updateMatch(match);
			return;
		}

		startMatch(match);
	}


	private void processResult(TurnBasedMultiplayer.LeaveMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		
		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}
		isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
		showWarning("Left", "You've left this match.");
	}


	public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		
		if (!checkStatusCode(match, result.getStatus().getStatusCode()))
			return;

		isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

		if (isDoingTurn) {
			updateMatch(match);
			return;
		}
	}

	private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
		switch (statusCode) {
			case GamesStatusCodes.STATUS_OK:
				return true;
			case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
				// This is OK; the action is stored by Google Play Services and will
				// be dealt with later.
				Toast.makeText(
						this,
						"Stored action for later.  (Please remove this toast before release.)",
						Toast.LENGTH_SHORT).show();
				// NOTE: This toast is for informative reasons only; please remove
				// it from your final application.
				return true;
			case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
				showErrorMessage(match, statusCode,
						R.string.status_multiplayer_error_not_trusted_tester);
				break;
			case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
				showErrorMessage(match, statusCode,
						R.string.match_error_already_rematched);
				break;
			case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
				showErrorMessage(match, statusCode,
						R.string.network_error_operation_failed);
				break;
			case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
				showErrorMessage(match, statusCode,
						R.string.client_reconnect_required);
				break;
			case GamesStatusCodes.STATUS_INTERNAL_ERROR:
				showErrorMessage(match, statusCode, R.string.internal_error);
				break;
			case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
				showErrorMessage(match, statusCode,
						R.string.match_error_inactive_match);
				break;
			case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
				showErrorMessage(match, statusCode,
						R.string.match_error_locally_modified);
				break;
			default:
				showErrorMessage(match, statusCode, R.string.unexpected_status);
				Log.d(TAG, "Did not have warning or string to deal with: "
						+ statusCode);
		}

		return false;
	}

	public void showErrorMessage(TurnBasedMatch match, int statusCode,
								 int stringId) {

		showWarning("Warning", getResources().getString(stringId));
	}

	// Generic warning/info dialog
	public void showWarning(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title).setMessage(message);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
					}
				});

		// create alert dialog
		mAlertDialog = alertDialogBuilder.create();

		// show it
		mAlertDialog.show();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d(TAG, "onConnected(): Connection successful");

		// Retrieve the TurnBasedMatch from the connectionHint
		if (connectionHint != null) {
			mTurnBasedMatch = connectionHint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);

			if (mTurnBasedMatch != null) {
				if (gameHelper.getApiClient() == null || !gameHelper.getApiClient().isConnected()) {
					Log.d(TAG, "Warning: accessing TurnBasedMatch when not connected");
				}

				updateMatch(mTurnBasedMatch);
				return;
			}
		}


		// As a demonstration, we are registering this activity as a handler for
		// invitation and match events.

		// This is *NOT* required; if you do not register a handler for
		// invitation events, you will get standard notifications instead.
		// Standard notifications may be preferable behavior in many cases.
		Games.Invitations.registerInvitationListener(gameHelper.getApiClient(), this);

		// Likewise, we are registering the optional MatchUpdateListener, which
		// will replace notifications you would get otherwise. You do *NOT* have
		// to register a MatchUpdateListener.
		Games.TurnBasedMultiplayer.registerMatchUpdateListener(gameHelper.getApiClient(), this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		Toast.makeText(this, "A match was updated.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTurnBasedMatchRemoved(String matchId) {
		Toast.makeText(this, "A match was removed.", Toast.LENGTH_SHORT).show();
	}

	// Handle notification events.
	@Override
	public void onInvitationReceived(Invitation invitation) {
		Toast.makeText(
				this,
				"An invitation has arrived from "
						+ invitation.getInviter().getDisplayName(), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onInvitationRemoved(String invitationId) {
		Toast.makeText(this, "An invitation was removed.", Toast.LENGTH_SHORT).show();
	}
}
