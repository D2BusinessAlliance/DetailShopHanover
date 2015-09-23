package business.modelclasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Chris Davis
 *
 */

public class Car implements Serializable {

		private static final long serialVersionUID = 1L;
		private int carID;
		private String carMake;
		private String carModel;
		private int carYear;
		private int price;
		private int mileage;
		
		public Car(){
			
		}
		
		public Car(int carID,  int carYear, String carMake,String carModel, int price, int mileage) {	 
			this.carID=carID;
			this.carMake=carMake;
			this.carModel=carModel;
			this.carYear=carYear;
			this.price = price;
			this.mileage=mileage;
		}
		
		
		//		Set/Get carYear
		public void setCarID(int ID){
			this.carID = ID;
		}
		public int getCarID(){
			return carID;
		}
		
		
		//		Set/Get carYear
		public void setCarYear(int year){
			this.carYear = year;
		}
		public int getCarYear(){
			return carYear;
		}
		
		
		//		Set/Get carMake
		public void setCarMake(String carMake) {
			this.carMake = carMake;
		}
		public String getCarMake() {
			return carMake;
		}
					
		//		Get/Set carModel
		public void setCarModel(String carModel) {
			this.carModel = carModel;
		}
		public String getCarModel() {
			return carModel;
		}

		
		//	Set/Get Price
		public void setPrice(int price){
			this.price = price;
		}
		public int getPrice(){
			return price;
		}
		
		//	Set/Get Mileage
		public void setMileage(int mileage){
			this.mileage = mileage;
		}
		public int getMileage(){
			return mileage;
		}
		
}