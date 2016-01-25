package de.biomedical_imaging.traJ.features;


import java.awt.geom.Point2D;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectorySplineFit;
/**
 * Implements the spline curve spatial analysis method according to:
 * [1] B. R. Long and T. Q. Vu, 
 * “Spatial structure and diffusive dynamics from single-particle trajectories using spline analysis,” 
 * Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.
 * @author Thorsten Wagner
 *
 */
public class SplineCurveSpatialFeature extends AbstractTrajectoryFeature{
	private Trajectory t;
	private int nSegments;
	private TrajectorySplineFit splinefit;
	
	public SplineCurveSpatialFeature(Trajectory t, int nSegments) {
		this.t =t;
		this.nSegments = nSegments;
	}
	
	@Override
	public double[] evaluate() {
		splinefit = new TrajectorySplineFit(t,nSegments);
		PolynomialSplineFunction spline = splinefit.calculateSpline();
		UnivariateFunction derivative = spline.derivative();
		
		double[] data = new double[t.size()];
		for(int i = 0; i < t.size(); i++){
			Point2D.Double help = new Point2D.Double(splinefit.getRotatedTrajectory().get(i).x, splinefit.getRotatedTrajectory().get(i).y);
			data[i] = help.distance(splinefit.minDistancePointSpline(new Point2D.Double(splinefit.getRotatedTrajectory().get(i).x, splinefit.getRotatedTrajectory().get(i).y), 50));
		}
		Mean m = new Mean();
		StandardDeviation sd = new StandardDeviation();
		return new double[] {m.evaluate(data),sd.evaluate(data)};
	
		 
	}
	
	public TrajectorySplineFit getTrajectorySplineFitInstance(){
		return splinefit;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Spline curve spatial feature";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}