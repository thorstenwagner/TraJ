package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

public class CentralRandomNumberGenerator extends Random {
	
	private static CentralRandomNumberGenerator instance = null;
	private static long seed = -1;
	
	public CentralRandomNumberGenerator() {
		super();
	}
	
	public CentralRandomNumberGenerator(long seed) {
		super();
		this.setSeed(seed);
	}
	
	public static CentralRandomNumberGenerator getInstance(){
		if(instance==null){
			if(seed==-1){
				instance = new CentralRandomNumberGenerator();
			}
			else{
				instance = new CentralRandomNumberGenerator(seed);
			}
		}
		return instance;
	}
	
	/**
	 * 
	 * @return -1 or 1 randomly
	 */
	public int randomSign(){
		return (getInstance().nextDouble()>0.5?1:-1);
	}
	
	

}
