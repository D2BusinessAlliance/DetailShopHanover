package business.appdev;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import persistence.DatabaseProvider;
import business.database.GoDaddyDatabase;

//Import other projects
import business.modelclasses.*;

public class DataHandling {
	/**
	 * 
	 * UpdateData -> used in GUI when Database is changed
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<Car> retrieveCars() throws SQLException, IOException {
		/* Start new Go Daddy Connection*/
		DatabaseProvider.setInstance(new GoDaddyDatabase());
		
		/* ArrayList of cars to Store Field Values */
		ArrayList<Car> cars = new ArrayList<Car>();
		
		cars = DatabaseProvider.getInstance().returnAllCars();
		return cars;
	}
	
	public ArrayList<Photo> retrievePhotos(int carID) throws SQLException, IOException{
		/* Start new Go Daddy Connection*/
		DatabaseProvider.setInstance(new GoDaddyDatabase());
		
		/* ArrayList of cars to Store Field Values */
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		photos = DatabaseProvider.getInstance().returnCarPhotos(carID);
		return photos;
		
	}
	
	public ArrayList<String> retrieveFeatures(int carID) throws SQLException, IOException{
		/* Start new Go Daddy Connection*/
		DatabaseProvider.setInstance(new GoDaddyDatabase());
		
		/* ArrayList of cars to Store Field Values */
		ArrayList<String> features = new ArrayList<String>();
		
		features = DatabaseProvider.getInstance().returnCarFeatures(carID);
		return features;
		
	}
	
}
