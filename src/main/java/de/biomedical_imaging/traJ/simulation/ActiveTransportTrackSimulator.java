package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

import de.biomedical_imaging.traJ.Trajectory;

public class ActiveTransportTrackSimulator extends AbstractSimulator {
	
	private double velocity;
	private double angularVelocity;
	private double direction;
	private double timelag;
	private int dimension;
	private int numberOfSteps;
	private CentralRandomNumberGenerator r;
	/**
	 * @param velocity Distance per second
	 * @param angularVelocity Angeluar velocity [rad/s]. Change in direction per second
	 *            between 0 and 2PI. For 1D the current sign of sin(angle) determines the direction. 
	 *            For 3d we suppose that change apply to the polar angle and the azimuthal angle.
	 * @param direction This will the start direction of the active transport. 
	 * 		  If angular velocity is zero, the direction will not change.
	 * @param timelag Time lag between two steps [s]
	 * @param dimension Dimension of the trajectory
	 * @param numberOfSteps Number of steps
	 */
	public ActiveTransportTrackSimulator(double velocity,
			double angularVelocity, double direction ,double timelag, int dimension,
			int numberOfSteps) {
		r = CentralRandomNumberGenerator.getInstance();
		this.velocity = velocity;
		this.angularVelocity = angularVelocity;
		this.direction = direction;
		this.timelag = timelag;
		this.dimension = dimension;
		this.numberOfSteps = numberOfSteps;
		
	}
	
	/**
	 * @param velocity Distance per second
	 * @param angularVelocity Angeluar velocity [rad/s]. Change in direction per second
	 *            between 0 and 2PI. For 1D the current sign of sin(angle) determines the direction. 
	 *            For 3d we suppose that change apply to the polar angle and the azimuthal angle.
	 * @param timelag Time lag between two steps [s]
	 * @param dimension Dimension of the trajectory
	 * @param numberOfSteps Number of steps
	 */
	public ActiveTransportTrackSimulator(double velocity,
			double angularVelocity ,double timelag, int dimension,
			int numberOfSteps) {
		r = CentralRandomNumberGenerator.getInstance();
		this.velocity = velocity;
		this.angularVelocity = angularVelocity;
		this.direction = r.nextDouble()*2*Math.PI;
		this.timelag = timelag;
		this.dimension = dimension;
		this.numberOfSteps = numberOfSteps;
	}


	@Override
	public Trajectory generateTrajectory() {
		Trajectory t = new Trajectory(dimension);
		t.addPosition(new Point3d(0, 0, 0));
		CentralRandomNumberGenerator r = CentralRandomNumberGenerator.getInstance();
		double startBeta = direction;
		double transportStepLength = timelag * velocity;
		double angularChangePerStep = angularVelocity * timelag;
		double currentBeta = startBeta;
		Point3d lastPos;
		Point3d newPos;
		switch (dimension) {
		case 1:
			
			lastPos = t.getPositions().get(0);
			for(int i = 0; i < numberOfSteps; i++){
				newPos = new Point3d(lastPos.x+Math.signum(Math.sin(currentBeta))*transportStepLength, 
						0, 
						0);
				t.addPosition(newPos);
				lastPos = newPos;
				currentBeta = currentBeta + r.randomSign()*angularChangePerStep;
			}
			break;
		case 2:
			
			for(int i = 0; i < numberOfSteps; i++){
				lastPos = t.getPositions().get(i);
				
				newPos = new Point3d(lastPos.x+Math.cos(currentBeta)*transportStepLength, 
						lastPos.y+Math.sin(currentBeta)*transportStepLength, 
						0);
				currentBeta = currentBeta + r.randomSign()*angularChangePerStep;
				t.addPosition(newPos);
				
			}
			break;
		case 3:
			
			double theta = startBeta;
			double phi = startBeta;
			for(int i = 0; i < numberOfSteps; i++){
				lastPos = t.getPositions().get(i);
				newPos = new Point3d(lastPos.x+transportStepLength*Math.cos(theta)*Math.sin(phi), 
						lastPos.y+transportStepLength*Math.sin(theta)*Math.sin(phi),
						lastPos.z+transportStepLength*Math.cos(phi));
				t.addPosition(newPos);
				theta = theta+ r.randomSign()*angularChangePerStep;

				
				double dPhi = r.randomSign()*angularChangePerStep;
				phi = phi + dPhi;
			
				
	
				
			}
			break;

		default:
			break;
		}
		return t;
	}
	

}
