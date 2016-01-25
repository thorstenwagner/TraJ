package de.biomedical_imaging.traJ;

import java.awt.geom.Point2D;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.knowm.xchart.Chart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.Series;
import org.knowm.xchart.SeriesMarker;
import org.knowm.xchart.Series.SeriesType;
import org.knowm.xchart.SeriesLineStyle;
import org.knowm.xchart.SwingWrapper;

public class TrajectoryUtil {
	
	public static Trajectory combineTrajectory(Trajectory a, Trajectory b){
		if(a.getDimension()!=b.getDimension()){
			throw new IllegalArgumentException("Combination not possible: The trajectorys does not have the same dimension");
		}
		if(a.size()!=b.size()){
			throw new IllegalArgumentException("Combination not possible: The trajectorys does not "
					+ "have the same number of steps a="+a.size() + " b="+b.size());
		}
		Trajectory c = new Trajectory(a.getDimension());
		
		for(int i = 0 ; i < a.size(); i++){
			Point3d pos = new Point3d(a.get(i).x+b.get(i).x, 
					a.get(i).y+b.get(i).y, 
					a.get(i).z+b.get(i).z);
			c.add(pos);
		}
		
		return c;
	}
	
	public static void showTrajectoryAndSpline(Trajectory t, PolynomialSplineFunction spline, List<Point2D.Double> supportPoints){
		if(t.getDimension()==2){
		 	double[] xData = new double[t.size()];
		    double[] yData = new double[t.size()];
		    for(int i = 0; i < t.size(); i++){
		    	xData[i] = t.get(i).x;
		    	yData[i] = t.get(i).y;
		    }
		    // Create Chart
		    Chart chart = QuickChart.getChart("Spline+Traj", "X", "Y", "y(x)", xData, yData);
		    
		    //Add spline support points
		    double[] subxData  = new double[supportPoints.size()];
		    double[] subyData  = new double[supportPoints.size()];
		    
		    for(int i = 0; i < supportPoints.size(); i++){
		    	subxData[i] = supportPoints.get(i).x;
		    	subyData[i] = supportPoints.get(i).y;
		    }
		    Series s = chart.addSeries("Spline Support POints", subxData, subyData);
		    s.setLineStyle(SeriesLineStyle.NONE);
		    s.setSeriesType(SeriesType.Line);
		    
		    //ADd spline points
		    int numberInterpolatedPointsPerSegment = 20;
		    int numberOfSplines = spline.getN();
		    double[] sxData = new double[numberInterpolatedPointsPerSegment*numberOfSplines];
		    
		    double[] syData = new double[numberInterpolatedPointsPerSegment*numberOfSplines];
		    double[] knots = spline.getKnots();
		    for(int i = 0; i < numberOfSplines; i++){
		    	double x = knots[i];
		   
		    	double stopx = knots[i+1];
		    	double dx = (stopx-x)/numberInterpolatedPointsPerSegment;
		    	
		    	for(int j = 0; j < numberInterpolatedPointsPerSegment; j++){

		    		sxData[i*numberInterpolatedPointsPerSegment+j] = x;
		    		syData[i*numberInterpolatedPointsPerSegment+j] = spline.value(x);
		    		x += dx;
		    	}
		    	
		    }
		    s = chart.addSeries("Spline", sxData, syData);
		    s.setLineStyle(SeriesLineStyle.DASH_DASH);
		    s.setMarker(SeriesMarker.NONE);
		    s.setSeriesType(SeriesType.Line);
		 
		    
		    //Show it
		    new SwingWrapper(chart).displayChart();
		} 
	}

}
