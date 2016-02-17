import lejos.nxt.*;

// Moves away from light, despite the name
public class LightFollower {

	  static LightSensor lightLeft;
	  static LightSensor lightRight;
	
	  public static void main(String[] args) throws Exception {
		  lightLeft = new LightSensor(SensorPort.S2);
		  lightRight = new LightSensor(SensorPort.S3);

		  while (true) {
			  int leftVal = lightLeft.getNormalizedLightValue();
			  int rightVal = lightRight.getNormalizedLightValue();
			  
			  int stepValue = 450;
			  
			  if (leftVal - 100 > rightVal) {
				  Motor.B.stop();
				  
				  if(leftVal > stepValue)
					  Motor.A.forward();
				  else
					  Motor.A.backward();
			  } 
			  else if (rightVal - 100 > leftVal) {
				  Motor.A.stop();
				  
				  if(rightVal > stepValue)
					  Motor.B.forward();
				  else
					  Motor.B.backward();
			  }
		  }
		  
	  }
}
