package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import business.modelclasses.Car;
import persistence.DatabaseProvider;
import persistence.IDatabase;

public class ReturnAllCars {
	public ArrayList<Car> returnAllCars() throws SQLException, IOException {
		IDatabase db = DatabaseProvider.getInstance();
		return db.returnAllCars();
	}
}
