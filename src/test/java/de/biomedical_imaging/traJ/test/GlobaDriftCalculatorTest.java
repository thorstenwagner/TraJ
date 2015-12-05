package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.vecmath.Point3d;

import org.junit.Test;

import de.biomedical_imaging.traJ.GlobalDriftCalculator;
import de.biomedical_imaging.traJ.Trajectory;

public class GlobaDriftCalculatorTest {
	private static final double DOUBLE_PRECISION = 0.000000001;
	
	@Test
	public void testCalculateDrift_1D() {
		/*
		 * Tis test generates a straight track (in x direction) with a distance of 1 between the positions.
		 * The global drift for such a track should be 1. 
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, y, z));
		}
		ArrayList<Trajectory> tracks = new ArrayList<Trajectory>();
		tracks.add(t);
		
		GlobalDriftCalculator gdc = new GlobalDriftCalculator();
		double[] drift = gdc.calculateDrift(tracks);
		double[] expected = {1,0,0};
		
		assertArrayEquals(expected, drift, DOUBLE_PRECISION);
	}
	
	@Test
	public void testCalculateDrift_1D_WithGaps() {
		/*
		 * Tis test generates a straight track (in x direction) with a distance of 1 between the positions. 
		 * The track will have gaps so that every 10th position is null.
		 * The global drift for such a track should be 1. 
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i, y, z));
			}
		}
		ArrayList<Trajectory> tracks = new ArrayList<Trajectory>();
		tracks.add(t);
		
		GlobalDriftCalculator gdc = new GlobalDriftCalculator();
		double[] drift = gdc.calculateDrift(tracks);
		double[] expected = {1,0,0};
		
		assertArrayEquals(expected, drift, DOUBLE_PRECISION);
	}
	
	@Test
	public void testCalculateDrift_2D_WithGaps() {
		/*
		 * Tis test generates a straight track (2d).
		 * The track will have gaps so that every 10th position is null.
		 * The global drift for such a track should be 1. 
		 */
		Trajectory t = new Trajectory(1);
		int z = 0;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i, i, z));
			}
		}
		ArrayList<Trajectory> tracks = new ArrayList<Trajectory>();
		tracks.add(t);
		
		GlobalDriftCalculator gdc = new GlobalDriftCalculator();
		double[] drift = gdc.calculateDrift(tracks);
		double[] expected = {1,1,0};
		
		assertArrayEquals(expected, drift, DOUBLE_PRECISION);
	}
	
	@Test
	public void testCalculateDrift_3D_WithGaps() {
		/*
		 * Tis test generates a straight track (2d).
		 * The track will have gaps so that every 10th position is null.
		 * The global drift for such a track should be 1. 
		 */
		Trajectory t = new Trajectory(1);
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i, i, i));
			}
		}
		ArrayList<Trajectory> tracks = new ArrayList<Trajectory>();
		tracks.add(t);
		
		GlobalDriftCalculator gdc = new GlobalDriftCalculator();
		double[] drift = gdc.calculateDrift(tracks);
		double[] expected = {1,1,1};
		
		assertArrayEquals(expected, drift, DOUBLE_PRECISION);
	}

}
