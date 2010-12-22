package data;

public final class PitchTypes {
	/* The fifth field is of variable length and contains all  
          pitches to this batter in this plate appearance.  The  
          standard pitches are:  C for called strike, S for  
          swinging strike, B for ball, F for foul ball.  In  
          addition, pickoff throws are indicated by the number of 
          the base the throw went to.  For example, "1" means the 
          pitcher made a throw to first, "2" a throw to second,  
          etc.  If the base number is preceded by a "+" sign, the 
          pickoff throw was made by the catcher. Some of the less
          common pitch codes are L:foul bunt, M:missed bunt,
          Q:swinging strike on a pitchout, R:foul ball on a pitchout,
          I:intentional ball, P:pitchout, H:hit by pitch, K:strike of
          unknown type, U:unkown or missing pitch.  Most Retrosheet  
          games do not have pitch data and consequnetly this field  
          is blank for such games. 
  
          There is occasionally more than one event for each  
          plate appearance, such as stolen bases, wild pitches,  
          and balks in which the same batter remains at the  
          plate.  On these occasions the pitch sequence is  
          interrupted by a period, and there is another play  
          record for the resumption of the batter's plate  
          appearance.  */
	
	
	/* C for called strike, S for  
          swinging strike, B for ball, F for foul ball.
          
          pickoff throws are indicated by the number of 
          the base the throw went to
          
          If the base number is preceded by a "+" sign, the 
          pickoff throw was made by the catcher
          
          L:foul bunt, M:missed bunt,
          Q:swinging strike on a pitchout, R:foul ball on a pitchout,
          I:intentional ball, P:pitchout, H:hit by pitch, K:strike of
          unknown type, U:unkown or missing pitch         
     */
          
          
          
	
	public static boolean isBall(char c) {
		return (c == 'B' || c == 'I');
	}
	
	// pitch vs. pickoff
	public static boolean isPitch(char c) {
		return (!isPickoff(c));
	}
	
	public static boolean isPickoff(char c) {
		return (Character.isDigit(c));
	}
	
	public static boolean isFoul(char c) {
		return (c == 'F' || c == 'L');
	}
}
