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


package de.biomedical_imaging.traJ;


import java.util.ArrayList;
import javax.vecmath.Point3d;

/**
 * 
 * @author Thorsten Wagner
 */
public class TrajectoryUtil {
	
	/**
	 * Combines two trajectories by adding the corresponding positions. The trajectories have to have the same length.
	 * @param a The first trajectory
	 * @param b The second trajectory
	 * @return The combined trajectory
	 */
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
	
	public static boolean isZero(double v){
		return Math.abs(v)<Math.pow(10, -18);
	}
	
	/**
	 * Splits a trajectory in overlapping / non-overlapping sub-trajectories.
	 * @param t
	 * @param windowWidth
	 * @param overlapping
	 * @return
	 */
	public static ArrayList<Trajectory> splitTrackInSubTracks(Trajectory t, int windowWidth, boolean overlapping){
		int increment = 1;
		if(overlapping==false){
			increment=windowWidth;
		}
		ArrayList<Trajectory> subTrajectories = new ArrayList<Trajectory>();
		boolean trackEndReached = false;
		for(int i = 0; i < t.size(); i=i+increment)
		{
			int upperBound = i+windowWidth;
			if(upperBound>t.size()){
				upperBound=t.size();
				trackEndReached=true;
			}
			Trajectory help = new Trajectory(2, i);
			for(int j = i; j < upperBound; j++){
				help.add(t.get(j));
			}
			subTrajectories.add(help);
			if(trackEndReached){
				i=t.size();
			}
		}
		System.out.println(subTrajectories.size() + " Subtrajectories returned");
		return subTrajectories;
	}

}
