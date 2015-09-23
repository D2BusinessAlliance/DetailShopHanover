package business.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import business.modelclasses.Car;
import business.modelclasses.Photo;
import persistence.IDatabase;

public class FakeDatabase implements IDatabase {
	private int carID = 1;
	//private int photoID = 1;
	private ArrayList<Car> cars;
	private ArrayList<Photo> photos;
	private ArrayList<String> features;
	private int wrxMileage = 51234;
	private int wrxPrice = 15444;
	//private Photo newPhoto;
	
	public FakeDatabase(){
		// NEED TO CREATE ALL DATA OBJECTS TO TEST DATABASE!!	
		cars = new ArrayList<Car>();
		//newPhoto = new Photo(1, carID, urlPath, f);
		photos = new ArrayList<Photo>();
		//photos.add(newPhoto);
		features = new ArrayList<String>();
		features.add("AC");
		features.add("Power Windows");
		features.add("Heated Seats");
		Car WRX = new Car(2, 2012 , "Subaru","WRX", wrxPrice, wrxMileage);
		Car Fusion = new Car(1, 2010, "Ford", "Fusion", 10000, 100000);
		cars.add(WRX);
		cars.add(Fusion);
	}
	
	
	/**
	 * 
	 *  DATABASE METHODS
	 *  
	 */
	
	@Override
	public boolean deleteCar(int carID) {
		for(Car car: cars){
			if(car.getCarID() == carID){
				cars.remove(carID);
				System.out.println("Car with ID of " + carID + " was removed!");
			}
		}
		return true;
	}

	@Override
	public int addCar(Car newCar) {
		cars.add(newCar);
		System.out.println("New car added to Database!");
		return 1;
	}

	@Override
	public ArrayList<Car> returnAllCars() {
		return cars;
	}

	@Override
	public ArrayList<Photo> returnCarPhotos(int carID) {
		ArrayList<Photo> carPhotos = new ArrayList<Photo>();
		for(Car car: cars){
			if(car.getCarID()==carID){
				
			}
		}
		return carPhotos;
	}

	@Override
	public Car returnCarByID(int carID) {
		for(Car car: cars){
			if(car.getCarID() == carID){
				System.out.println("Returning car based on ID");
				return car;
			}
		}
		return null;
	}


	public boolean execute(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public ArrayList<String> returnCarFeatures(int carID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Photo> returnAllPhotos() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean removePhoto(int photoID) throws SQLException {
		return null;
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean addFeatures(int carID, String featurename)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean addPhoto(Photo addPhoto) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean deleteFeatures(int carID) throws SQLException, IOException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean deletePhotos(int carID) throws SQLException, IOException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Boolean changeCarYear(int carID, int carYear) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean changeCarPrice(int carID, int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean changeCarMileage(int carID, int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean changeCarModel(int carID, String text) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean changeCarMake(int carID, String text) {
		// TODO Auto-generated method stub
		return null;
	}


}
