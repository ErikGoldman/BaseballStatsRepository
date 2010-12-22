package matchup;

import java.util.Calendar;
import java.util.HashMap;

import data.EventFile;
import data.EventReceiver;
import data.PlayResult;

public abstract class MatchupMatrix implements EventReceiver {
	private HashMap<String, HashMap<String, EventReceiver>> matrix = new HashMap<String, HashMap<String,EventReceiver>>();
	
	private String homePitcher, awayPitcher;
	private String home, away;
	
	private int playCounter = 0;

	@Override
	public void startGame(String home, String away, Calendar date) {
		this.home = home;
		this.away = away;
	}

	@Override
	public void setLineup(String team, String id, int position) { 
		if (position == EventFile.Position.Pitcher.ordinal()) {
			if (team.equals(home)) {
				homePitcher = id;
			} else {
				awayPitcher = id;
			}
		}
	}

	@Override
	public void atBat(String team, int inning, String id, String pitches, PlayResult result) {
		playCounter++;
		
		// get the pitcher for the other team
		String pitcher = (team.equalsIgnoreCase(home) ? awayPitcher : homePitcher);
		
		HashMap<String, EventReceiver> matchups = matrix.get(pitcher);
		
		if (matchups == null) {
			matchups = new HashMap<String, EventReceiver>();
			matrix.put(pitcher, matchups);
		}
		
		EventReceiver evt = matchups.get(id);
		if (evt == null) {
			evt = getEventReceiver();
			matchups.put(id, evt);
		}
		
		evt.atBat(team, inning, id, pitches, result);
	}	
	
	protected abstract EventReceiver getEventReceiver();
	
	public int getPlayCount() {
		return playCounter;
	}
}
