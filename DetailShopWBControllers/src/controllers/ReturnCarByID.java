package controllers;

import java.io.IOException;
import java.sql.SQLException;

import business.modelclasses.Car;
import persistence.IDatabase;
import persistence.DatabaseProvider;

public class ReturnCarByID {
	public Car returnCarByID(int carID) throws SQLException, IOException{
		IDatabase db = DatabaseProvider.getInstance();
		return db.returnCarByID(carID);
	}
}
