package business.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import business.modelclasses.Car;
import business.modelclasses.Feature;
import business.modelclasses.Photo;
import persistence.IDatabase;

public class GoDaddyDatabase implements IDatabase {
	
	// Add basics here
	//ArrayList<Car> cars = new ArrayList<Car>();
	//ArrayList<Photo> photos = new ArrayList<Photo>();
	//ArrayList<String> features = new ArrayList<String>();
	Car car = new Car();
	Photo photo = new Photo();
	Feature feature = new Feature();
	
	public GoDaddyDatabase(){
	
	}

	private String DATABASE_PATH = "jdbc:mysql://www.thedetailshophanover.com:3306/TheDetailShopHanover";
	private String USERNAME = "dshanover";
	private String PASSWORD = "Da94eb8f$";
	private static final int MAX_ATTEMPTS = 10;
	
		// Create connection, statement, resultSet
		Connection conn;
		PreparedStatement statement;
		ResultSet resultSet;


	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException, IOException;
	}

	

	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) throws SQLException, IOException {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw e;
		}
	}

	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException, IOException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	private Connection connect() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection(DATABASE_PATH, USERNAME, PASSWORD); 
		
		return conn;
	}
	
	@Override
	public boolean execute(Connection conn) throws SQLException {
		conn.commit();
		return true;
	}

	// NEEDS TESTED
	@Override
	public boolean deleteCar(int carID) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement preparedStatement = null;
				try{
					// Prepare statement
					preparedStatement = conn.prepareStatement("DELETE FROM CAR WHERE CARID = ?");
						preparedStatement.setInt(1, carID);
					// Execute Query
					preparedStatement.executeUpdate();
					System.out.println("Car with ID of " + carID + " deleted");					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
				return true;
			}
		});			
	}
	
	// Should work
	@Override
	public boolean deleteFeatures(int carID) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement preparedStatement = null;
				try{
					// Prepare statement
					preparedStatement = conn.prepareStatement("DELETE FROM FEATURES WHERE CARID = ?");
						preparedStatement.setInt(1, carID);
					// Execute Query
					preparedStatement.executeUpdate();
					System.out.println("Features with ID of " + carID + " deleted");					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
				return true;
			}
		});			
	}
	
	// Works
	@Override
	public boolean deletePhotos(int carID) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement preparedStatement = null;
				try{
					// Prepare statement
					preparedStatement = conn.prepareStatement("DELETE FROM PHOTOS WHERE CARID = ?");
						preparedStatement.setInt(1, carID);
					// Execute Query
					preparedStatement.executeUpdate();
					System.out.println("Photos with ID of " + carID + " deleted");					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
				return true;
			}
		});			
	}
	 
	// Works
	@Override
	public boolean addFeatures(int carID, String featureName) throws SQLException, IOException {
		
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement preparedStatement = null;
				try{
					preparedStatement = conn.prepareStatement(
							"INSERT INTO FEATURES (CARID, FEATURENAME) VALUES (?, ?)");
						// Set Values to be inserted into DB
						preparedStatement.setInt(1, carID);
						preparedStatement.setString(2, featureName);
						// INSERT INTO DATABASE
						preparedStatement.executeUpdate();
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
			return true;
			}
		});
	}
	
	// Works
	@Override
	public boolean addPhoto(Photo addPhoto) throws SQLException, IOException {	
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement preparedStatement = null;
				try{
					preparedStatement = conn.prepareStatement(
							"INSERT INTO PHOTOS (CARID, FILEPATH) VALUES (?, ?)");
						// Set Values to be inserted into DB;
						preparedStatement.setInt(1, addPhoto.getCarID());
						preparedStatement.setString(2, addPhoto.getFilePath());
						// INSERT INTO DATABASE
						preparedStatement.executeUpdate();
						System.out.println("Photo added to the database");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
			return true;
			}
		});
	}
	
	// Works, Must call AddPhotos and AddFeatures with
	@Override
	public int addCar(Car newCar) throws SQLException, IOException {
		
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement preparedStatement = null;
				int newCarID = -1;
				try{
					preparedStatement = conn.prepareStatement(
							"INSERT INTO CAR (YEAR, MAKE, MODEL, PRICE, MILEAGE) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
						// Set Values to be inserted into DB
						preparedStatement.setInt(1, newCar.getCarYear());
						preparedStatement.setString(2, newCar.getCarMake());
						preparedStatement.setString(3, newCar.getCarModel());
						preparedStatement.setInt(4, newCar.getPrice());
						preparedStatement.setInt(5, newCar.getMileage());
						// INSERT INTO DATABASE
						preparedStatement.executeUpdate();
						
						try(ResultSet generatedKey = preparedStatement.getGeneratedKeys()){
							if(generatedKey.next()){
								newCarID = generatedKey.getInt(1);
							} else {
							throw new SQLException("FAIL, NO CAR ID FOUND!!");
						}
					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
			return newCarID;
			}
		});
	}
	
	// Works
	@Override
	public ArrayList<Car> returnAllCars() throws SQLException, IOException {
		return executeTransaction(new Transaction<ArrayList<Car>>() {
			@Override 
			public ArrayList<Car> execute(Connection conn) throws SQLException {
				ArrayList<Car> cars = new ArrayList<Car>();
				PreparedStatement preparedStatement = null;
				try{
					// Return a resultset That contains the photos from the hashtags the user is following.	
					// CORRECT PREPARESTATEMENT
					preparedStatement = conn.prepareStatement("SELECT * FROM CAR");
	 				resultSet = preparedStatement.executeQuery();
	 				while(resultSet.next()){
	 					Car newCar = new Car();
	 					getCar(newCar, resultSet);
	 					cars.add(newCar);
					}
				}
				finally
				{
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return cars;		
			}
		});
	}
	
	// Works
	@Override
	public ArrayList<Photo> returnCarPhotos(int carID) throws SQLException, IOException {
		return executeTransaction(new Transaction<ArrayList<Photo>>() {
			@Override 
			public ArrayList<Photo> execute(Connection conn) throws SQLException, IOException {
				ArrayList<Photo> carPhotos = new ArrayList<Photo>();
				PreparedStatement preparedStatement = null;
				try{
					// Return a resultset That contains the photos associated with the carID	
					preparedStatement = conn.prepareStatement("SELECT * FROM PHOTOS WHERE CARID=?");
						preparedStatement.setInt(1, carID);
	 				resultSet = preparedStatement.executeQuery();
	 				while(resultSet.next()){
	 					Photo carPhoto = new Photo();
	 					getPhoto(carPhoto, resultSet);
	 					carPhotos.add(carPhoto);
					}
				}
				finally
				{
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return carPhotos;		
			}
		});
	}
	
	@Override
	public ArrayList<String> returnCarFeatures(int carID) throws SQLException, IOException {
		return executeTransaction(new Transaction<ArrayList<String>>() {
			@Override 
			public ArrayList<String> execute(Connection conn) throws SQLException {
				ArrayList<String> features = new ArrayList<String>();
				PreparedStatement preparedStatement = null;
				try{
					// Return a resultset That contains the photos from the hashtags the user is following.	
					preparedStatement = conn.prepareStatement("SELECT * FROM FEATURES where FEATURES.CARID=?");
						preparedStatement.setInt(1, carID);
	 				resultSet = preparedStatement.executeQuery();
	 				while(resultSet.next()){
	 					Feature newFeature = new Feature();
	 					getFeature(newFeature, resultSet);
	 					features.add(newFeature.getFeatureName());
					}
				}
				finally
				{
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return features;		
			}
		});
	}

	// NEED TO IMPLEMENT STATEMENT FOR PHOTOS AND FEATURES
	@Override
	public Car returnCarByID(int carID) throws SQLException, IOException {
			return executeTransaction(new Transaction<Car>() {
				Car newCar;
				@Override 
				
				public Car execute(Connection conn) throws SQLException {
					PreparedStatement preparedStatement = null;
					try{
						
						// Return a resultset That contains the photos from the hashtags the user is following.	
						preparedStatement = conn.prepareStatement("SELECT * FROM CAR JOIN FEATURES where CAR.CARID=? AND FEATURES.CARID=?");
							preparedStatement.setInt(1, carID);
							preparedStatement.setInt(2, carID);
		 				resultSet = preparedStatement.executeQuery();
		 				while(resultSet.next()){
		 					System.out.println(resultSet);
		 					System.out.println("Feature from database is: " + resultSet.getString(8));
		 					// Doesnt Work
		 					// features.add(resultSet.getString(8));
		 					newCar = new Car(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3),
		 							resultSet.getString(4), resultSet.getInt(5), resultSet.getInt(6));
						}
					}
					finally
					{
						DBUtil.closeQuietly(preparedStatement);
						DBUtil.closeQuietly(conn);
					}
					return newCar;		
				}
			});
		}
	
	// Should work, needs tested!
	@Override
	public ArrayList<Photo> returnAllPhotos() throws SQLException, IOException{
		return executeTransaction(new Transaction<ArrayList<Photo>>() {
			@Override 
			public ArrayList<Photo> execute(Connection conn) throws SQLException, IOException {
				ArrayList<Photo> photos = new ArrayList<Photo>();
				PreparedStatement preparedStatement = null;
				try{
					// CORRECT PREPARESTATEMENT
					preparedStatement = conn.prepareStatement("SELECT * FROM PHOTOS");
	 				resultSet = preparedStatement.executeQuery();
	 				while(resultSet.next()){
	 					Photo newPhoto = new Photo();
	 					getPhoto(newPhoto, resultSet);
						photos.add(newPhoto);
					}
				}
				finally
				{
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return photos;		
			}
		});
	}

	@Override
	public Boolean removePhoto(int photoID) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				System.out.println(photoID);
				PreparedStatement preparedStatement = null;
				try{
					// Prepare statement
					preparedStatement = conn.prepareStatement("DELETE FROM PHOTOS WHERE PHOTOID = ?");
						preparedStatement.setInt(1, photoID);
					// Execute Query
					preparedStatement.executeUpdate();
					System.out.println("Photo with ID of " + photoID + " deleted");					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(preparedStatement);
				}
				return true;
			}
		});			
	}
	
	// Change DatabaseValue methods
	@Override
	public Boolean changeCarYear(int carID, int newYear) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override 
			public Boolean execute(Connection conn) throws SQLException, IOException {
				PreparedStatement preparedStatement = null;
				try{
					// CORRECT PREPARESTATEMENT
					preparedStatement = conn.prepareStatement("UPDATE CAR SET YEAR=? WHERE CARID=?");
					preparedStatement.setInt(1, newYear);
					preparedStatement.setInt(2, carID);
	 				preparedStatement.executeUpdate();
	 				
				} finally {
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return true;		
			}
		});
	}
	
	@Override
	public Boolean changeCarPrice(int carID, int newPrice) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override 
			public Boolean execute(Connection conn) throws SQLException, IOException {
				PreparedStatement preparedStatement = null;
				try{
					// CORRECT PREPARESTATEMENT
					System.out.println("Creating prepared statement to update year");
					preparedStatement = conn.prepareStatement("UPDATE CAR SET PRICE=? WHERE CARID=?");
					preparedStatement.setInt(1, newPrice);
					preparedStatement.setInt(2, carID);
	 				preparedStatement.executeUpdate();
	 				
				} finally {
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return true;		
			}
		});
	}

	@Override
	public Boolean changeCarMileage(int carID, int newMileage) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override 
			public Boolean execute(Connection conn) throws SQLException, IOException {
				PreparedStatement preparedStatement = null;
				try{
					// CORRECT PREPARESTATEMENT
					System.out.println("Creating prepared statement to update year");
					preparedStatement = conn.prepareStatement("UPDATE CAR SET MILEAGE=? WHERE CARID=?");
					preparedStatement.setInt(1, newMileage);
					preparedStatement.setInt(2, carID);
	 				preparedStatement.executeUpdate();
	 				
				} finally {
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return true;		
			}
		});
	}

	@Override
	public Boolean changeCarModel(int carID, String newModel) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override 
			public Boolean execute(Connection conn) throws SQLException, IOException {
				PreparedStatement preparedStatement = null;
				try{
					// CORRECT PREPARESTATEMENT
					System.out.println("Creating prepared statement to update year");
					preparedStatement = conn.prepareStatement("UPDATE CAR SET MODEL=? WHERE CARID=?");
					preparedStatement.setString(1, newModel);
					preparedStatement.setInt(2, carID);
	 				preparedStatement.executeUpdate();
	 				System.out.println("success or nah");
	 				
				} finally {
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return true;		
			}
		});
	}

	@Override
	public Boolean changeCarMake(int carID, String newMake) throws SQLException, IOException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override 
			public Boolean execute(Connection conn) throws SQLException, IOException {
				PreparedStatement preparedStatement = null;
				try{
					// CORRECT PREPARESTATEMENT
					System.out.println("Creating prepared statement to update year");
					preparedStatement = conn.prepareStatement("UPDATE CAR SET MAKE=? WHERE CARID=?");
					preparedStatement.setString(1, newMake);
					preparedStatement.setInt(2, carID);
	 				preparedStatement.executeUpdate();
	 				System.out.println("success or nah");
	 				
				} finally {
					DBUtil.closeQuietly(preparedStatement);
					DBUtil.closeQuietly(conn);
				}
				return true;		
			}
		});
	}

		
		
	
	// Utility Methods
	private void getPhoto(Photo photo, ResultSet resultSet) throws SQLException {	
		photo.setPhotoID(resultSet.getInt("PHOTOID"));
		photo.setCarID(resultSet.getInt("CARID"));
		photo.setFilePath(resultSet.getString("FILEPATH"));
	}
	
	private void getCar(Car car, ResultSet resultSet) throws SQLException {
		car.setCarID(resultSet.getInt("CARID"));
		car.setCarYear(resultSet.getInt("YEAR"));
		car.setCarMake(resultSet.getString("MAKE"));
		car.setCarModel(resultSet.getString("MODEL"));
		car.setPrice(resultSet.getInt("PRICE"));
		car.setMileage(resultSet.getInt("MILEAGE"));
	}

	private void getFeature(Feature feature, ResultSet resultSet) throws SQLException {	
		feature.setCarID(resultSet.getInt("CARID"));
		feature.setFeatureName(resultSet.getString("FEATURENAME"));
	}

	
	

}
