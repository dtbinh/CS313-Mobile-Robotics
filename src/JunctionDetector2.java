import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class JunctionDetector2 implements Behavior {
	
		private RobotState state;
	
	   private DifferentialPilot pilot;
	   private LightSensor leftSensor;
	   private LightSensor rightSensor;
	   
	   private Graph junctionGraph;
	   private Vertex currentVertex;
	   private int vertexCount = 0;
	   
	   private int L_ABS_DARK_TRESHOLD = 500;
	   private int R_ABS_DARK_TRESHOLD = 500;
	   private final int REL_DARK_TRESHOLD = 50;
	   
	   public JunctionDetector2(DifferentialPilot pilot,LightSensor leftSensor, LightSensor rightSensor) {
		   this.pilot = pilot;
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
	     
		 int leftVal = leftSensor.getNormalizedLightValue();
		 int rightVal = rightSensor.getNormalizedLightValue();
		 
		 while (takeControl()) {
			if (isLeftBlack() && isRightBlack()) {
				System.out.println("T junction or Cross");
				pilot.travel(75);

				// junction to the left
				if(leftVal < L_ABS_DARK_TRESHOLD)
				{
					boolean leftLineFound = false;
					boolean aheadLineFound = false;
					
					// do-while until the robot
					do{
						pilot.rotate(-45);

						leftVal = leftSensor.getNormalizedLightValue();

						rightVal = rightSensor.getNormalizedLightValue();

						if (isRightBlack()) {
							aheadLineFound = true;
						}

						if (isLeftBlack()) {
							leftLineFound = true;
						}

					} while (!leftLineFound || isLeftBlack());
					
					if (aheadLineFound && leftLineFound)
					{
						Vertex newVertex = new Vertex("V" + vertexCount++);
						
						junctionGraph.addVertex(currentVertex, false);
						
						junctionGraph.addEdge(currentVertex, newVertex);
						
					}
				}

				if(rightVal < R_ABS_DARK_TRESHOLD)
				{
					boolean rightLineFound = false;
					boolean aheadLineFound = false;
					
					do{
						pilot.rotate(45);

						leftVal = leftSensor.getNormalizedLightValue();
						
						rightVal = rightSensor.getNormalizedLightValue();

						if (isLeftBlack()) {
							aheadLineFound = true;
						}


						if (isRightBlack()) {
							rightLineFound = true;
						}

					} while (!rightLineFound || isRightBlack());
					
					if (aheadLineFound && rightLineFound)
					{
						Vertex newVertex = new Vertex("V" + vertexCount++);
						
						junctionGraph.addVertex(currentVertex, false);
						
						junctionGraph.addEdge(currentVertex, newVertex);
					
					}
				}
			} else 
			if (leftVal - rightVal < 0) { // Only Left is black
				boolean isLine = true;
				int turnsCnt = 0;
				while (leftVal - rightVal < 0) {
					pilot.rotate(-20);
					turnsCnt++;
					leftVal = leftSensor.getNormalizedLightValue();
					rightVal = rightSensor.getNormalizedLightValue();
					if (isLeftBlack() && isRightBlack()) {
						isLine = false;
					}
				}
				System.out.println("Turns l: " + turnsCnt);
				
				if (turnsCnt <= 2 && isLine) { // Probably a line
					System.out.println("Line");
					Thread.yield();
				} else 
				if (turnsCnt >= 3 || !isLine) { // Probably a corner of T junction
					System.out.println("Corner or T junction");
					Thread.yield();					
				}
				
			} else
			if (rightVal - leftVal < 0) { // Only Right is black
				boolean isLine = true;
				int turnsCnt = 0;
				while (rightVal - leftVal < 0) {
					pilot.rotate(20);
					turnsCnt++;
					leftVal = leftSensor.getNormalizedLightValue();
					rightVal = rightSensor.getNormalizedLightValue();
					if (isLeftBlack() && isRightBlack()) {
						isLine = false;
					}
				}
				System.out.println("Turns r: " + turnsCnt);
				
				if (turnsCnt <= 2 && isLine) { // Probably a line
					System.out.println("Line");
					Thread.yield();
				} else 
				if (turnsCnt >= 3 || !isLine) { // Probably a corner of T junction
					System.out.println("Corner or T junction");
					Thread.yield();					
				}
			} else {
				// impossible 
			}
			

			Thread.yield();
		 }
		 
		 pilot.stop();
	   }
	}