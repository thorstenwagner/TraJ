package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectorySplineFit;
import de.biomedical_imaging.traJ.simulation.ActiveTransportSimulator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class TrajectorySplineFitTest {

	@Test
	public void distancePointLineTest_SimpleXY() {
		TrajectorySplineFit sfit = new TrajectorySplineFit(null);
		Point2D.Double pref = new Point2D.Double(0, 0);
		Point2D.Double x = new Point2D.Double(1, 0);
		Point2D.Double y = new Point2D.Double(0, 1);
		Point2D.Double p = new Point2D.Double(5, 4);
		double dx = sfit.distancePointLine(pref, x, p);
		assertEquals(p.y, dx,0);
		double dy = sfit.distancePointLine(pref, y, p);
		assertEquals(p.x, dy,0);
	}
	
	@Test
	public void distancePointLineTest_SimpleRotated() {
		TrajectorySplineFit sfit = new TrajectorySplineFit(null);
		Point2D.Double pref = new Point2D.Double(0, 0);
		Point2D.Double x = new Point2D.Double(5, 5);
		Point2D.Double y = new Point2D.Double(5, -5);
		Point2D.Double p = new Point2D.Double(5, 5);
		double dy = sfit.distancePointLine(pref, x, p);
		assertEquals(0, dy,0);
		double dx = sfit.distancePointLine(pref, y, p);
		assertEquals(Math.sqrt(p.x*p.x+p.y*p.y), dx,0.0001);
	}
	
	

}
