import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

public class LightPhobic {

	  static LightSensor lightLeft;
	  static LightSensor lightRight;
	
	  public static void main(String[] args) throws Exception {
		  lightLeft = new LightSensor(SensorPort.S2);
		  lightRight = new LightSensor(SensorPort.S3);
		  DifferentialPilot pilot = new DifferentialPilot(56, 26, Motor.A, Motor.B);

		  while (true) {
			  int leftVal = lightLeft.getNormalizedLightValue();
			  int rightVal = lightRight.getNormalizedLightValue();
			  
			  int stepValue = 300;
			  
			  double angle = 90;
			  double distance = 20; // mm
			  
			  if (leftVal - 100 > rightVal) {
				  if(leftVal > stepValue)
					  pilot.rotate(angle);
				  else
					  pilot.rotate(-angle);
			  } 
			  else if (rightVal - 100 > leftVal) {
				  if(leftVal > stepValue)
					  pilot.rotate(-angle);
				  else
					  pilot.rotate(angle);
			  } else {
				  if (leftVal > stepValue)
					  pilot.travel(-distance);
			  }
		  }
		  
	  }
}
