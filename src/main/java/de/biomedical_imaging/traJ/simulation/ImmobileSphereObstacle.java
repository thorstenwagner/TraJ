package de.biomedical_imaging.traJ.simulation;

public class ImmobileSphereObstacle extends AbstractSphereObstacle {

	public ImmobileSphereObstacle(double[] position, double radius,
			int dimension) {
		super(position, radius, dimension);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePosition(AnomalousDiffusionScene s) {
		/*
		 * It's immobile, do nothing!
		 */
		
	}

}
