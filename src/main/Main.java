package main;

import java.util.Calendar;
import java.util.GregorianCalendar;

import data.EventGroup;
import matchup.SPOBPReceiver.SPOBPMatrix;

public class Main {
	public static void main(String[] args) {		
		Calendar start = new GregorianCalendar(), end = new GregorianCalendar();
		start.set(2010, 0, 1, 1, 1);
		end.set(2010, 11, 1, 1, 1);
		
		System.out.println("From: " + start.get(Calendar.YEAR) + " to: " + end.get(Calendar.YEAR));
		
		EventGroup data = new EventGroup("data", start, end);
		SPOBPMatrix mtx = new SPOBPMatrix();
		
		data.run(mtx);
		
		System.out.println("Analyzed " + mtx.getPlayCount() + " plays!");
	}
}
