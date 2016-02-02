import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightTesting {
	
  static LightSensor light;

  public static void main(String[] args) throws Exception {
	  light = new LightSensor(SensorPort.S2);
	  while(true)
	  {
		  Button.waitForAnyPress();
		  testSensor(light);
	  }
  }
  
    private static void testSensor(LightSensor light){
        LCD.drawInt(light.getNormalizedLightValue(), 4, 0, 1);
    }
    
}
