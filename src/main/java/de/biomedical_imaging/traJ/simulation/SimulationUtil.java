/*
The MIT License (MIT)

Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

import de.biomedical_imaging.traJ.Trajectory;

public class SimulationUtil {
	
	public static void visulaizeSzeneAndTrajectory(AnomalousDiffusionScene s, Trajectory t){
			
	}
	
	/**
	 * Adds position noise to the trajectories
	 * @param t
	 * @param sd
	 * @return trajectory with position noise
	 */
	public static Trajectory addPositionNoise(Trajectory t, double sd){
		CentralRandomNumberGenerator r = CentralRandomNumberGenerator.getInstance();
		Trajectory newt = new Trajectory(t.getDimension());
		
		for(int i = 0; i < t.size(); i++){
			newt.add(t.get(i));
			for(int j = 1; j <= t.getDimension(); j++){
				switch (j) {
				case 1:
					newt.get(i).setX(newt.get(i).x + r.nextGaussian()*sd);
					break;
				case 2:
					newt.get(i).setY(newt.get(i).y + r.nextGaussian()*sd);
					break;
				case 3:
					newt.get(i).setZ(newt.get(i).z + r.nextGaussian()*sd);
					break;
				default:
					break;
				}
			}
		}
		
		return newt;
		
	}
	
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
