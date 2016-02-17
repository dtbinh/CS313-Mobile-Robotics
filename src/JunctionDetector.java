import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class JunctionDetector implements Behavior {
	   
	   private DifferentialPilot pilot;
	   private LightSensor leftSensor;
	   private LightSensor rightSensor;
	   
	   private Graph junctionGraph;
	   private Vertex currentVertex;
	   private int vertexCount = 0;
	   
	   private final int DARK_TRESHOLD = 400;
	   
	   
	   public JunctionDetector(DifferentialPilot pilot,LightSensor leftSensor, LightSensor rightSensor) {
		   this.pilot = pilot;
		   this.leftSensor = leftSensor;
		   this.rightSensor = rightSensor;
		   
		   junctionGraph = new Graph();
		   currentVertex = new Vertex("start");
		   junctionGraph.addVertex(currentVertex, false);
	   }
	   
	   public boolean takeControl() {
		   
	      // takes Control when sensors differ more than a threshold
		  int leftVal = leftSensor.getNormalizedLightValue();
		  int rightVal = rightSensor.getNormalizedLightValue();
		  
		  System.out.println(leftVal);
		  System.out.println(rightVal);
		  
		  if (leftVal < DARK_TRESHOLD || rightVal < DARK_TRESHOLD) {
			  return true;
		  }
		  
		  return false;
	   }

	   public void suppress() {
	   }
	   
	   public void action() {
	     
		 int leftVal = leftSensor.getNormalizedLightValue();
		 int rightVal = rightSensor.getNormalizedLightValue();
		 
		 while (takeControl()) {
			pilot.travel(75);

			// junction to the left
			if(leftVal < DARK_TRESHOLD)
			{
				boolean leftLineFound = false;
				boolean aheadLineFound = false;
				
				// do-while until the robot
				do{
					pilot.rotate(-45);

					leftVal = leftSensor.getNormalizedLightValue();

					rightVal = rightSensor.getNormalizedLightValue();

					if (rightVal < DARK_TRESHOLD) {
						aheadLineFound = true;
					}

					if (leftVal < DARK_TRESHOLD) {
						leftLineFound = true;
					}

				} while (!leftLineFound || leftVal < DARK_TRESHOLD);
				
				if (aheadLineFound && leftLineFound)
				{
					Vertex newVertex = new Vertex("V" + vertexCount++);
					
					junctionGraph.addVertex(currentVertex, false);
					
					junctionGraph.addEdge(currentVertex, newVertex);
					
				}
			}

			if(rightVal < DARK_TRESHOLD)
			{
				boolean rightLineFound = false;
				boolean aheadLineFound = false;
				
				do{
					pilot.rotate(45);

					leftVal = leftSensor.getNormalizedLightValue();
					
					rightVal = rightSensor.getNormalizedLightValue();

					if (rightVal < DARK_TRESHOLD) {
						aheadLineFound = true;
					}


					if (rightVal < DARK_TRESHOLD) {
						rightLineFound = true;
					}

				} while (!rightLineFound || rightVal < DARK_TRESHOLD);
				
				if (aheadLineFound && rightLineFound)
				{
					Vertex newVertex = new Vertex("V" + vertexCount++);
					
					junctionGraph.addVertex(currentVertex, false);
					
					junctionGraph.addEdge(currentVertex, newVertex);
					
				}
			}

			Thread.yield();
		 }
		 
		 pilot.stop();
	   }
	}