import lejos.nxt.*;
import lejos.robotics.Clock;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JuncDetectA {
	public static void main(String[] args) throws Exception {
		
		DifferentialPilot pilot = new DifferentialPilot(56, 26, Motor.A, Motor.B);
		Behavior b1 = new DriveForward(pilot);
		Behavior b2 = new JunctionDetectorPro(pilot,
									  new LightSensor(SensorPort.S2),
				 					  new LightSensor(SensorPort.S3));
							
		Behavior [] bArray = {b1, b2};
		Arbitrator arby = new Arbitrator(bArray);
		Thread.sleep(1000);
		arby.start();
	  }
}
