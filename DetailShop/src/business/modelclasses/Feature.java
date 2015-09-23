package business.modelclasses;

import java.io.Serializable;

public class Feature implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int carID;
	private String featureName;
	
	public Feature(){
		
	}
	
	public Feature(int carID, String featureName){
		this.carID = carID;
		this.featureName = featureName;
	}
	
//	Set/Get carYear
	public void setCarID(int ID){
		this.carID = ID;
	}
	public int getCarID(){
		return carID;
	}
	
//	Set/Get carYear
	public void setFeatureName(String featureName){
		this.featureName = featureName;
	}
	public String getFeatureName(){
		return featureName;
	}
}
