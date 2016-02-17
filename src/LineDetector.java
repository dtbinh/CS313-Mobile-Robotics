import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class LineDetector implements Behavior {
	   
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
		  return true;
	   }

	   public void suppress() {
	   }
	   
	   private int count = 0;
	   
	   public void action() {
	     
		 int leftVal = leftSensor.getNormalizedLightValue();
		 int rightVal = rightSensor.getNormalizedLightValue();

		 if (rightVal > leftVal)
			 {
			 	pilot.rotate(-30);
			 }
			 else
				if(leftVal > rightVal)
			 {			 	
				pilot.rotate(30);
			 }
		 pilot.travel(8);

		 Thread.yield();
		 
	   }
	}