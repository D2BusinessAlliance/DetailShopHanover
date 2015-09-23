/**
 * 
 * @author Chris Davis
 * Created for The Detail Shop Hanover, LLC.
 * 
 */

package business.modelclasses;

import java.io.Serializable;

public class Photo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int photoID;
	private int carID;
	private String filePath;
	
	public Photo(){
		
		
	}
	
	public Photo (int photoID, int carID, String filePath) {
		this.photoID=photoID;
		this.carID=carID;
		this.filePath=filePath;
	}
	
	// Get/Set photoID
	public void setPhotoID(int photoID){
		this.photoID = photoID;
	}
	public int getPhotoID(){
		return photoID;
	}
	
	
	//		Set/Get carYear
	public void setCarID(int ID){
		this.carID = ID;
	}
	public int getCarID(){
		return carID;
	}
	
	
	//	Get/Set PhotoURL
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath() {
		return filePath;
	}
	
}
