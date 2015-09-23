package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import business.modelclasses.Photo;
import persistence.DatabaseProvider;
import persistence.IDatabase;

public class ReturnCarPhotos {
	public ArrayList<Photo> returnCarPhotos(int carID) throws SQLException, IOException{
		IDatabase db = DatabaseProvider.getInstance();
		return db.returnCarPhotos(carID);
	}
}
