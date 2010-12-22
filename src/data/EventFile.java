package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class EventFile {
	
	public static enum Position {
		Pitcher, Catcher, FirstBase, SecondBase, ThirdBase, Shortstop, LeftField, CenterField, RightField 
	}
	
	private File file;
		
	public EventFile(File f) throws IOException {
		file = f;
	}
	
	private String currHome, currAway;
	private Calendar currDate, start, end;
	private boolean newGame = false, countGame = false;
	
	public Calendar getDate() {
		return currDate;
	}
	
	public void readInto(EventReceiver er, Calendar start, Calendar end) throws IOException {		
		this.start = start;
		this.end = end;
		
		BufferedReader br = new BufferedReader(new FileReader(file));

		while (true) {
			String line = br.readLine();
			
			if (line == null)
				break;
			
			line = line.toUpperCase();
			
			if (!parseLine(line, er))
				break;
		}
	}
	
	private boolean parseLine(String line, EventReceiver er) {
		String[] parts = line.split(",");
		
		if (parts[0].equals("ID")) {
			newGame = true;
		} else if (parts[0].equals("INFO")) {
			if (parts[1].equals("VISTEAM")) {
				currAway = parts[2];
			} else if (parts[1].equals("HOMETEAM")) {
				currHome = parts[2];
			} else if (parts[1].equals("DATE")) {
				String[] dateParts = parts[2].split("/");
				
				Calendar c = Calendar.getInstance();
				c.set(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
				
				countGame = true;
				if (c.after(end))
					return false;
				if (c.before(start))
					countGame = false;
			}
		} else if (parts[0].equals("START")) {
			if (newGame) {
				er.startGame(currHome, currAway, currDate);
				newGame = false;
			}
			
			if (!countGame) return true;
			
			// subtract 1 to line up with the ordinals (0 --> whatever)
			er.setLineup( (Integer.parseInt(parts[3]) == 1) ? currHome : currAway, parts[1], Integer.parseInt(parts[5]) - 1);
		} else if (parts[0].equals("PLAY")) {
			if (!countGame) return true;
			er.atBat( (parts[2].charAt(0) == '1') ? currHome : currAway, Integer.parseInt(parts[1]), parts[3], parts[5], new PlayResult(parts[6]));
		} else if (parts[0].equals("SUB")) {
			if (!countGame) return true;
			er.setLineup((Integer.parseInt(parts[3]) == 1) ? currHome : currAway, parts[1], Integer.parseInt(parts[5]) - 1);
		}
		
		return true;
	}
}
