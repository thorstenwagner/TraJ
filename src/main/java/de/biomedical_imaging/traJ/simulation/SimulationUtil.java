package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

public class SimulationUtil {
	
	public static Point3d randomPosition(int dimension, double length){
			
			Point3d p = null;
			Random r = CentralRandomNumberGenerator.getInstance();
			
			switch (dimension) {
				case 1:
					int sign = 1;
					if(r.nextDouble()>0.5){
						sign = -1;
					}
					p = new Point3d(sign*length, 0, 0);
					break;
				case 2:
					double alpha = 2*Math.PI*r.nextDouble();
					p = new Point3d(Math.cos(alpha)*length, Math.sin(alpha)*length, 0);
					break;
				case 3:
					double u = 2*r.nextDouble()-1;
			
					double theta = 2*Math.PI*r.nextDouble();
	
					double x = Math.sqrt(1-u*u)*Math.cos(theta)*length;
	
					double y = Math.sqrt(1-u*u)*Math.sin(theta)*length;
					double z = u*length;
					/*
					 * Pick random position on unit sphere:
					 * http://mathworld.wolfram.com/SpherePointPicking.html
					 */
					p = new Point3d(x, y, z);
					break;
				default:
					break;
			}
			
			return p;
			
		}


}
