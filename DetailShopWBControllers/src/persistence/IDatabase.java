/**
 *  
 * @author Chris Davis
 * 	 
 */

package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import business.modelclasses.Car;
import business.modelclasses.Photo;

public interface IDatabase {

	// Remove a car based on carID
	public boolean deleteCar(int carID) throws SQLException, IOException;
	
	// Add a new car
	public int addCar(Car newCar) throws SQLException, IOException;
	
	// Returns a list of all cars in the database
	public ArrayList<Car> returnAllCars() throws SQLException, IOException;
	
	// Returns a list of photos based on CarID
	public ArrayList<Photo> returnCarPhotos(int carID) throws SQLException, IOException;
	
	// Returns a Car based on an ID
	public Car returnCarByID(int carID) throws SQLException, IOException;

	boolean execute(Connection conn) throws SQLException, IOException;

	// Returns all features from a car based on ID
	public ArrayList<String> returnCarFeatures(int carID) throws SQLException, IOException;

	public ArrayList<Photo> returnAllPhotos() throws SQLException, IOException;

	public Boolean removePhoto(int photoID) throws SQLException, IOException;

	boolean addFeatures(int carID, String featurename) throws SQLException, IOException;

	boolean addPhoto(Photo addPhoto) throws SQLException, IOException;

	boolean deleteFeatures(int carID) throws SQLException, IOException;

	boolean deletePhotos(int carID) throws SQLException, IOException;

	public Boolean changeCarYear(int carID, int newYear) throws SQLException, IOException;

	public Boolean changeCarPrice(int carID, int newPrice) throws SQLException, IOException;

	public Boolean changeCarMileage(int carID, int newMileage) throws SQLException, IOException;

	public Boolean changeCarModel(int carID, String newModel) throws SQLException, IOException;

	public Boolean changeCarMake(int carID, String newMake) throws SQLException, IOException;

	
}
