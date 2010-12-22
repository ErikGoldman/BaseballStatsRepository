package data;

import java.util.Calendar;

public interface EventReceiver {
	public void startGame(String home, String away, Calendar date);
	
	public void setLineup(String team, String id, int position);
	
	public void atBat(String team, int inning, String id, String pitches, PlayResult result);
	
	// TODO: stolen bases, etc.
}
