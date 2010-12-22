package data;

public class PlayResult {
	private boolean strikeout = false, walk = false, isAtBat = true, isError = false, isSacrifice = false, isFieldersChoice = false;
	private int toBase = 0;
	
	public PlayResult(String data) {
		data = data.toUpperCase();
		
		String[] baseRunners = data.split("\\.");
		
		String[] playInfo = baseRunners[0].split("/");
		
		parsePlay(playInfo[0]);
		
		if (playInfo.length > 1)
			parseTrajectory(playInfo[1]);
		
		if (baseRunners.length == 2) {
			parseBaserunning(baseRunners[1]);
		}
	}
	
	// if this was a real at bat or just some jenky half-play (pickoff throw --> out, for example)
	public boolean isAtBat() {
		return isAtBat;
	}
	
	public boolean isOnBase() {
		return (toBase > 0);
	}
	
	public boolean wasSacrifice() {
		return (toBase < 0);
	}
	
	public int getBaseNum() {
		return toBase;
	}
	
	private void parseBaserunning(String data) {
		String[] runners = data.split(";");
		
		for (String r : runners) {
			if (r.charAt(0) == 'B') {				
				String toBaseStr = Character.toString(r.charAt(2));
				
				if (toBaseStr.equalsIgnoreCase("H")) {
					toBase = 4;
				} else {
					toBase = Integer.parseInt(toBaseStr);
				}
			}
		}
	}
	
	private void parseTrajectory(String traj) {
		if (traj.equals("SH")) {
			toBase = -1; // reserved for sacrifices
		}
	}
	
	private void parsePlay(String play) {
		char type = play.charAt(0);
		char secondChar = '?', thirdChar = '?';
		
		if (play.length() > 1) 
			secondChar = play.charAt(1);
		if (play.length() > 2)
			thirdChar = play.charAt(2);
		
		if (type == 'K') {
			strikeout = true;
		} else if (type == 'W' || (type == 'I' && secondChar == 'W') || (type == 'H' && secondChar == 'P')) {
			walk = true;
			toBase = 1;
		} else if (type == 'S') {
			if (secondChar == 'B') {
				// SB means stolen base-- has nothing to do with us
				isAtBat = false;
			} else {
				// otherwise, it's a single
				toBase = 1;
			}
		} else if (type == 'D') {
			toBase = 2;
		} else if (type == 'T') {
			toBase = 3;
		} else if (type == 'H' && secondChar == 'R') {
			toBase = 4;
		} else if (type == 'E') {			
			toBase = 1; // assume unless baserunning info says otherwise
			isError = true;
		} else if (type == 'F' && secondChar == 'L' && thirdChar == 'E') {
			// foul out error, doesn't concern us, only for other runners
			isAtBat = false;
		} else if (type == 'P') {
			// TODO: wtf is this?
			isAtBat = false;
		} else if (Character.isDigit(type)) {
			if (Character.isDigit(secondChar)) {
				// two digits in a row?
				
				if (play.length() < 3) {
					// if it's just 2 digits, base runner is out
					return;
				}
				
				for (int i=3; i < play.length(); i++) {
					if (play.charAt(i) == '(') {
						// skip over the parens-- that's for other base runners that are out
						i+=2;
						continue;
					} else {
						// no parens --> batter is out
						return;
					}
				}
				// otherwise, assume it's a single?
				toBase = 1;
			} else {
				// next value isn't a digit --> that's definitely an out.
				return;
			}
		} else if (type == 'N' && secondChar == 'P') {
			// something interrupted us.
			isAtBat = false;
		} else if (type == 'F' && secondChar == 'C') {
			// fielder's choice.
			isFieldersChoice = true;
			toBase = 1; // TODO: read baserunning to get this value
		} else if (type == 'C' && secondChar == 'S') {
			// caught stealing, doesn't concern us
			isAtBat = false;
		} else if (type == 'B' && secondChar == 'K') {
			// balk: batter remains on base, all runners advance
			// TODO: show advancement of runners?
			isAtBat = false;
		} else if (type == 'O' && secondChar == 'A') {
			// the fuck is this?  it's not an at-bat, though.
			isAtBat = false;
		} else if (type == 'C') {
			// TODO: not gonna lie, I have no idea what this is
			isAtBat = false; // for now, but it's not true....
		} else {
			throw new RuntimeException("WTF is " + play);
		}
	}
}
