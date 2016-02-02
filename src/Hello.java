import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

public class Hello{
	public static void main(String[] args) {
		 System.out.println("Press any key to start");
		 DifferentialPilot pilot = new DifferentialPilot(56, 26, Motor.A, Motor.B);
		 Button.waitForAnyPress();
		 LCD.clear();
		 pilot.travel(220);
		 System.out.println(" "+pilot.getMovement().getDistanceTraveled());
		 pilot.travel(-220);
		 System.out.println(" "+pilot.getMovement().getDistanceTraveled());
		 pilot.travel(220);
		 System.out.println(" "+pilot.getMovement().getDistanceTraveled());
		 Button.waitForAnyPress();
 }
}