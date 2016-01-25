package de.biomedical_imaging.traJ.features;

import ij.IJ;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import cg.RotatingCalipers;
import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectorySplineFit;
import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;
/**
 * Implements the spline curve dynamics method to estimate D and D according to:
 * [1] B. R. Long and T. Q. Vu, 
 * “Spatial structure and diffusive dynamics from single-particle trajectories using spline analysis,” 
 * Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.
 * @author Thorsten Wagner
 *
 */
public class SplineCurveDynamicsFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	private int nSegments;
	private TrajectorySplineFit splinefit;
	private int timelag;
	/**
	 * 
	 * @param t Trajectory for calculate spline
	 * @param nSegments
	 */
	public SplineCurveDynamicsFeature(Trajectory t, int nSegments, int timelag) {
		this.t = t;
		this.nSegments = nSegments;
		this.timelag = timelag;
	}
	
	@Override
	public double[] evaluate() {

		splinefit = new TrajectorySplineFit(t,nSegments);
		PolynomialSplineFunction spline = splinefit.calculateSpline();
		
		UnivariateFunction derivative = spline.derivative();
		int N =0;
		double sumParallel = 0;
		double sumPerpendicular = 0;
		//Split each step into replacment rependicular and parallel to spline tangent
		for(int i = timelag; i < t.size(); i+=timelag){
			Point2D.Double pRef = splinefit.minDistancePointSpline(new Point2D.Double(t.get(i).x, t.get(i).y), 50);
			
			Point2D.Double pNormal = new Point2D.Double(pRef.x+1, -1*derivative.value(pRef.x)+pRef.y);
			Point2D.Double pTangend = new Point2D.Double(pRef.x+1, derivative.value(pRef.x)+pRef.y);
		//	pRef.setLocation(pRef.x+dp.x, pRef.y+dp.y);
			pNormal.setLocation(pNormal.x-pRef.x, pNormal.y-pRef.y);
			pTangend.setLocation(pTangend.x-pRef.x, pTangend.y-pRef.y);
			pRef.setLocation(0, 0);
			Point2D.Double dp = new Point2D.Double(t.get(i).x-t.get(i-timelag).x, t.get(i).y-t.get(i-timelag).y);
			sumParallel+=Math.pow(splinefit.distancePointLine(pRef, pNormal, dp), 2);
			sumPerpendicular+=Math.pow(splinefit.distancePointLine(pRef, pTangend, dp),2);
			N++;
		}
		//System.out.println("N: " +spline.getN());
		double msdParallel = sumParallel/N;
		double msdPerpendicular = sumPerpendicular/N;
		
		return new double[]{msdParallel,msdPerpendicular};
	}
	
	private double signedDistanceToLine(Point2D.Double p1, Point2D.Double p2, Point2D.Double p){
		double sign =splinefit.isLeft(p1, p2, p) ? 1:-1;
		
		return sign*splinefit.distancePointLine(p1, p2, p);
	}
	
	public TrajectorySplineFit getTrajectorySplineFitInstance(){
		return splinefit;
	}
	

	@Override
	public String getName() {
		return "Spline curve dynamics";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
