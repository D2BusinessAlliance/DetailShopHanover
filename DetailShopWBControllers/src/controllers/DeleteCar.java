package controllers;

import java.io.IOException;
import java.sql.SQLException;

import persistence.DatabaseProvider;
import persistence.IDatabase;

public class DeleteCar {
	public boolean deleteCar(int carID) throws SQLException, IOException {
		IDatabase db = DatabaseProvider.getInstance();
		return db.deleteCar(carID);
	}
}
