package controllers;

import java.io.IOException;
import java.sql.SQLException;

import persistence.DatabaseProvider;
import persistence.IDatabase;
import business.modelclasses.Car;

public class AddCar {
	public int addCar(Car newCar) throws SQLException, IOException {
		IDatabase db = DatabaseProvider.getInstance();
		return db.addCar(newCar);
	}
}
