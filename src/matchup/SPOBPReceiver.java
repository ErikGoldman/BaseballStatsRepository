package matchup;

import java.util.Calendar;

import data.EventReceiver;
import data.PlayResult;

public class SPOBPReceiver implements EventReceiver {
	int atBats, onBase, numBases;
	
	@Override
	public void startGame(String home, String away, Calendar date) {
	}

	@Override
	public void setLineup(String team, String id, int position) {
	}

	@Override
	public void atBat(String team, int inning, String id, String pitches,
									PlayResult result) {
		if (result.isAtBat()) {
			atBats++;
			if (result.isOnBase()) {
				onBase++;
			}
			
			numBases += result.getBaseNum();
		}
	}
	
	public double getSPOBP() {
		return ( ((double)(onBase + numBases)) / atBats);
	}
	
	public static class SPOBPMatrix extends MatchupMatrix {
		@Override
		protected EventReceiver getEventReceiver() {
			return new SPOBPReceiver();
		}		
	}
}
