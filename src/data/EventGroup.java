package data;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;

public class EventGroup {
	private ArrayList<String> folders = new ArrayList<String>();
	private Calendar start, end;
	
	public EventGroup(String topLevelFolder, Calendar start, Calendar end) {		
		this.start = start;
		this.end = end;
		
		for (int i=start.get(Calendar.YEAR); i <= end.get(Calendar.YEAR); i++) {
			folders.add(topLevelFolder + "/" + Integer.toString(i));
		}
	}
	
	public void run(EventReceiver er) {
		for (String folder : folders) {
			File f = new File(folder);
			
			if (!f.exists() || !f.isDirectory()) {
				throw new RuntimeException("Error with directory " + f);
			}
			
			for (File evt : f.listFiles(new FileFilter() {				
				@Override
				public boolean accept(File pathname) {
					return (pathname.isFile() && !pathname.isHidden() && pathname.getName().length() > 3);
				}
			})) {
				
				try {
					EventFile event = new EventFile(evt);
											
					event.readInto(er, start, end);					
				} catch (Exception e) {
					System.err.println("Error in file " + evt.getName());
					e.printStackTrace();
				}				
			}
		}
	}
}
