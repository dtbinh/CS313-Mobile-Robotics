import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class LineFollower {
	public static void main(String[] args) throws Exception {
		DifferentialPilot pilot = new DifferentialPilot(56, 26, Motor.A, Motor.B);
		Behavior b1 = new DriveForward(pilot);
		Behavior b2 = new LineDetector(pilot,
									   new LightSensor(SensorPort.S2),
				 					   new LightSensor(SensorPort.S3));
		Behavior [] bArray = {b1, b2};
		Arbitrator arby = new Arbitrator(bArray);
		arby.start();
	  }
}

class DriveForward  implements Behavior {
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
	    	 pilot.travel(10);
	    	 Thread.yield();
	     }
	   }
	}

class LineDetector  implements Behavior {
	   
	   private DifferentialPilot pilot;
	   private LightSensor leftSensor;
	   private LightSensor rightSensor;
	   private final int TRESHOLD = 50;
	   
	   private int[][] measurements = new int[500][2];
	   
	   public LineDetector(DifferentialPilot pilot,LightSensor leftSensor, LightSensor rightSensor) {
		   this.pilot = pilot;
		   this.leftSensor = leftSensor;
		   this.rightSensor = rightSensor;
	   }
	   
	   public boolean takeControl() {
		   
	      // takes Control when sensors differ more than a threshold
		  int leftVal = leftSensor.getNormalizedLightValue();
		  int rightVal = rightSensor.getNormalizedLightValue();
		  
		  if (Math.abs(leftVal - rightVal) > TRESHOLD) {
			  return true;
		  }
		  
		  return false;
	   }

	   public void suppress() {
	   }
	   
	   private int count = 0;
	   
	   public void action() {
	     
		 int leftVal = leftSensor.getNormalizedLightValue();
		 int rightVal = rightSensor.getNormalizedLightValue();
		 
		 measurements[count][0] = leftVal;
		 measurements[count][1] = rightVal;
		 
		 count++;

		 while (takeControl()) {
			 pilot.travel(75);
			 if(leftVal > rightVal)
			 {
				 while(rightVal < 500)
				 {
					 rightVal = rightSensor.getNormalizedLightValue();
					 pilot.rotate(20);
				 }
			 }
			 else
			 {
				 while(leftVal < 500)
				 {
					 leftVal = leftSensor.getNormalizedLightValue();
					 pilot.rotate(-20);
				 }
			 }

			 Thread.yield();
		 }
		 
		 pilot.stop();
	   }
	}
