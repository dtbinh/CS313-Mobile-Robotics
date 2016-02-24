import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class JunctionDetectorPro implements Behavior {
	
		private RobotState state = new RobotState();
	
	   private DifferentialPilot pilot;
	   private LightSensor leftSensor;
	   private LightSensor rightSensor;
	   
	   private Graph junctionGraph;
	   private Vertex currentVertex;
	   private int vertexCount = 0;
	   
	   private int L_ABS_DARK_TRESHOLD = 530;
	   private int R_ABS_DARK_TRESHOLD = 530;
	   private final int REL_DARK_TRESHOLD = 50;
	   
	   private final int FULL_360_TURN = 2560; // 2400
	   private final int TURN_90 = FULL_360_TURN/4;
	   
	   public JunctionDetectorPro(DifferentialPilot pilot,LightSensor leftSensor, LightSensor rightSensor) {
		   this.pilot = pilot;
		   this.pilot.setTravelSpeed(30);
		   this.pilot.setRotateSpeed(150);
		   this.leftSensor = leftSensor;
		   this.rightSensor = rightSensor;
		   
		   junctionGraph = new Graph();
		   currentVertex = new Vertex("start");
		   junctionGraph.addVertex(currentVertex, false);
		   
//		   this.leftSensor.calibrateHigh();
//		   this.leftSensor.calibrateLow();
//		   this.rightSensor.calibrateHigh();
//		   this.rightSensor.calibrateLow();
//		   
//		   
		   
//		   this.leftSensor.getNormalizedLightValue();
//		   this.rightSensor.getNormalizedLightValue();
//		   
//		   L_ABS_DARK_TRESHOLD = this.leftSensor.getNormalizedLightValue() - 10;
//		   R_ABS_DARK_TRESHOLD = this.rightSensor.getNormalizedLightValue() - 10;
//		   System.out.println("Thresh L: " + L_ABS_DARK_TRESHOLD);
//		   System.out.println("Thresh R: " + R_ABS_DARK_TRESHOLD);
	   }
	   
	   public boolean takeControl() {
		   
		  if (isLeftBlack() || isRightBlack()) { 
			  return true;
		  }
		  
		  return false;
	   }

	   public void suppress() {
	   }
	   
	   private boolean isLeftBlack() {
		   return leftSensor.getNormalizedLightValue() < L_ABS_DARK_TRESHOLD;
	   }

	   private boolean isRightBlack() {
		   return rightSensor.getNormalizedLightValue() < R_ABS_DARK_TRESHOLD;
	   }
	   
	   public void action() {
	     
		   while (takeControl()) {
			   
			   pilot.travel(75);
	
			   int currentAngle = 0, rotation = FULL_360_TURN/80; // 18/2 deg each turn, 2*5 turns for 90 deg
			   int sector = 0;
			   
			   while (currentAngle < FULL_360_TURN) {
				   
				   //System.out.println("l: " + (isLeftBlack()?1:0) + ", r: " + (isRightBlack()?1:0));
				   
				   if (isLeftBlack() || isRightBlack()) {
					   
					   //System.out.print("B, s=" + sector + ",cA=" + currentAngle);
					   
					   if (currentAngle >= TURN_90*sector-10*rotation && currentAngle <= TURN_90*sector+10*rotation) {
						   String direction = "def";
						   switch (sector) {
							   case 0: 
								   direction = "front";
								   break;
							   case 1: 
								   direction = "right";
								   break;
							   case 2: 
								   direction = "back";
								   break;
							   case 3: 
								   direction = "left";
								   break;
						   }
						   
						   System.out.println("Line " + direction);
					   }
					   
					   state.lightReadings.add(new Pair(isLeftBlack(), isRightBlack()));
				   }
				   				   		   
				   pilot.rotate(rotation);
				   currentAngle += rotation;
				   if (currentAngle % TURN_90 == 0) {
					   sector++;
				   }
			   }
			   state.lightReadings.add(new Pair(isLeftBlack(), isRightBlack()));
			   
			   for (int i = 0; i < state.lightReadings.size(); i++) {
				   System.out.println("l: " + state.lightReadings.get(i).l + " r: " + state.lightReadings.get(i).r);
			   }
			   
			   Thread.yield();
		   }
		 
		   pilot.stop();
	   }
	}