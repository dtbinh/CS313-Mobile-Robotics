import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

class DriveForward implements Behavior {
	   private boolean suppressed = false;
	   
	   private DifferentialPilot pilot;
	   
	   public DriveForward(DifferentialPilot pilot) {
		   this.pilot = pilot;
	   }
	   
	   public boolean takeControl() {
	      return true;
	   }

	   public void suppress() {
	      suppressed = true;
	   }

	   public void action() {
	     suppressed = false;

	     while( !suppressed ) {
	    	 pilot.travel(3);
	    	 Thread.yield();
	     }
	   }
	}