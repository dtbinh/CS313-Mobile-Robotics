import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightTesting {
	
	  static LightSensor left;
	  static LightSensor right;

  public static void main(String[] args) throws Exception {
	  left = new LightSensor(SensorPort.S2);
	  right = new LightSensor(SensorPort.S3);
	  while(true)
	  {
		  Button.waitForAnyPress();
		  testSensor(left,right);
	  }
  }
  
    private static void testSensor(LightSensor left, LightSensor right){
    	System.out.println("Left: " + left.getNormalizedLightValue());
    	System.out.println("Right: " + right.getNormalizedLightValue());
    }
    
}
