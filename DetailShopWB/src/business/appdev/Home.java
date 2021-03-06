package business.appdev;

// Import other projects
import business.modelclasses.*;
import business.database.*;
import business.appdev.DataHandling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import persistence.DatabaseProvider;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;

import javax.swing.JToolBar;

import java.awt.CardLayout;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;


public class Home {
	
	//	Globals for temp photos
	private ArrayList<Photo> tempPhotos = new ArrayList<Photo>();

	
	// FTP Connection Variables
	int PORT = 21;
	String HOST = "ftp.thedetailshophanover.com";
	String USERNAME = "detailshophan";
	String PASSWORD = "Cayman667";
	
	// Globals for windows!
	private JFrame editFrame;
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel addPanel;
	private JPanel removePanel;
	private JPanel editPanel;
	private JPanel editCarPanel;
	private JPanel editFramePanel;
	private JScrollPane editScrollPane;
	private JScrollPane removeScrollPane;
	private int addPanelPhotoID;
	public static final int TYPE_INT_RGB=1;
	// Globals to handle the images
	private JScrollPane imageScrollPane;
	private JPanel imagePanel;
	private JLabel[] imageLabel;
	private JButton[] removeImageButton;
	private int removeImageStartX = 280;
	private int imageSize = 200;
	private int imageX = 40;
	private int imageY = 20;
	private BufferedImage img;
	private FileInputStream is;

	
	// Edit image globals
	private JScrollPane editImageScrollPane;
	private JPanel editImagesPanel;
	private JLabel[] editImageLabel;
	private JButton[] editRemoveImageButton;
	private ArrayList<File> files = new ArrayList<File>();
	// Globals for addPanel + editPanel
	private Car editCar;
	private JLabel yearLabel;
	private JLabel makeLabel;
	private JLabel modelLabel;
	private JLabel mileageLabel;
	private JLabel priceLabel;
	private JLabel featuresLabel;
	private JTextField yearField;
	private JTextField makeField;
	private JTextField modelField;
	private JTextField mileageField;
	private JTextField priceField;
	private JTextArea featuresArea;
	private JButton submitButton;
	private int delete;
	private JButton submitChangesButton;
	private JFileChooser fileChooser;
	
	// Create inputStream
	private InputStream inputStream;
	// Globals to display information
	private ArrayList<Car> cars = new ArrayList<Car>();
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private ArrayList<Photo> addPanelPhotos = new ArrayList<Photo>();
	private ArrayList<Photo> editPanelPhotos = new ArrayList<Photo>();
	private ArrayList<String> editPanelFeatures = new ArrayList<String>();
	
	// Fields to create car
	private Car submitCar;
	private String carMake;
	private String carModel;
	private int carYear;
	private int carPrice;
	private int carMileage;
	private int newCarID;
	
	// Fields to create photo
	private File f;
	private int photoID;
	
	//private Photo newPhoto;
	private JButton uploadButton;
	private String urlPath;
	
	// Dynamic building of the panel
	private JButton[] removeButton;
	private JButton[] editButton;
	private JLabel[] carNames;
	
	// Variables for spacing on Dynamic Panels 
	private int labelX = 50;
	private int buttonX = 550;
	private int buttonXSize = 40;
	private int startingY = 25;
	
	/**
	 * 
	 * Temp Variables to store information on Initial Start
	 * 
	 */
	ArrayList<Car> tempCars = new ArrayList<Car>();
	ArrayList<Photo> tempPhotos1 = new ArrayList<Photo>();
	ArrayList<Feature> tempFeatures = new ArrayList<Feature>();
	
	/** Variables to store data for addPanel car */
	Car addCar = new Car();
	ArrayList<String> addFeatures = new ArrayList<String>();
	ArrayList<Photo> addPhotos = new ArrayList<Photo>();
	
	// DataHandling object
	DataHandling data = new DataHandling();
	
	// Frame close
	private int frameValue = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Home window = new Home();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FTPException 
	 */
	public Home() throws SQLException, IOException, FTPException {
		
		initialize();
		
		/* Retrieve Cars in Database @ Start
		 * of application running */
		cars = data.retrieveCars();
		/* Use for loop to retrieve all features 
		 * and photos from all cars */
		for(int i = 0; i < cars.size();i++){
			int carID = cars.get(i).getCarID();
			ArrayList<String> loadFeatures = new ArrayList<String>();
			loadFeatures = data.retrieveFeatures(carID);
			for(int j = 0; j < loadFeatures.size(); j++){
				tempFeatures.add(new Feature(carID, loadFeatures.get(j)));
			}
			ArrayList<Photo> loadPhotos = new ArrayList<Photo>();
			loadPhotos = data.retrievePhotos(carID);
			for(int j = 0; j < loadPhotos.size(); j++){
				tempPhotos1.add(loadPhotos.get(j));
			}
		}
		System.out.println("There are a total of " + tempFeatures.size() + " features");
		System.out.println("There are a total of " + tempPhotos1.size() + " photos");
		
		// Set frame details
		frame.setTitle( "The Detail Shop of Hanover, LLC. Inventory" );
		frame.setSize( 800, 600 );
		frame.setBackground( Color.gray );
		frame.getContentPane().setLayout(null);
		
		// Create TabbedPane
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBounds(0, 0, 800, 578);
		frame.getContentPane().add(tabbedPane);
		
		// Create addPanel and add to tabbedPane
		addPanel = new JPanel(null);
		tabbedPane.addTab("Add", null, addPanel, null);
		
		// Create edit panel and ScrollPane
		editPanel = new JPanel(null);
		editScrollPane = new JScrollPane(editPanel);
		tabbedPane.addTab("Edit", null, editScrollPane, null);
		editScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		/* Create Remove Panel & ScrollPane */
		removePanel = new JPanel(null);
		removeScrollPane = new JScrollPane(removePanel);
		tabbedPane.addTab("Remove", null, removeScrollPane, null);
		removeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		/* Paint All 3 Panels (Add, Edit, Remove) */
		paintAddPanel();
		paintRemovePanel(cars);
		paintEditPanel(cars);
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(800, 600, 801, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 
	 * Paints Add Panel
	 * TODO: Need to Finish Button Handling
	 * @throws SQLException
	 * @throws IOException
	 */
	public void paintAddPanel() throws SQLException, IOException {
		addPanel.removeAll();
		
		// Create + Setup ScrollPane and JPanel to handle images
		imagePanel = new JPanel(null);
		imageScrollPane = new JScrollPane(imagePanel);
		imageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		imageScrollPane.setBounds(420, 20, 350, 500);
		addPanel.add(imageScrollPane);
		
		yearLabel = new JLabel( "Year:" );
		yearLabel.setBounds( 10, 15, 150, 20 );
		addPanel.add(yearLabel);

		yearField = new JTextField();
		yearField.setBounds( 10, 35, 150, 20 );
		addPanel.add( yearField );

		makeLabel = new JLabel( "Make:" );
		makeLabel.setBounds( 10, 60, 150, 20 );
		addPanel.add( makeLabel );

		makeField = new JTextField();
		makeField.setBounds( 10, 80, 150, 20 );
		addPanel.add( makeField );
		
		modelLabel = new JLabel( "Model:" );
		modelLabel.setBounds( 10, 105, 150, 20 );
		addPanel.add( modelLabel );

		modelField = new JTextField();
		modelField.setBounds( 10, 125, 150, 20 );
		addPanel.add( modelField );
		
		mileageLabel = new JLabel("Mileage: ");
		mileageLabel.setBounds(180, 30, 150, 20);
		addPanel.add(mileageLabel);
		
		mileageField = new JTextField();
		mileageField.setBounds(180, 50, 150, 20);
		addPanel.add(mileageField);
		
		priceLabel = new JLabel("Price:  ");
		priceLabel.setBounds(180, 90, 150, 20);
		addPanel.add(priceLabel);
		
		priceField = new JTextField();
		priceField.setBounds(180, 110, 150, 20);
		addPanel.add(priceField);
		
		featuresLabel = new JLabel("Features..seperate with comma and space (X, X)");
		featuresLabel.setBounds(10,150, 400, 20);
		addPanel.add(featuresLabel);
	
		featuresArea = new JTextArea();
		featuresArea.setLineWrap(true);
		featuresArea.setWrapStyleWord(true);
		featuresArea.setBounds(10, 176, 400, 200);
		addPanel.add(featuresArea);
		
		uploadButton = new JButton("Upload a Photo");
		uploadButton.setBounds(188, 425, 120, 40);
		addPanel.add(uploadButton);
		
		// Upload Button clickHandler - send to addPanelUploadButton
		uploadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					addPanelUploadButton();
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
			
		// Submit Button
		submitButton = new JButton("Submit");
		submitButton.setBounds(32, 425, 120, 40);
		addPanel.add(submitButton);	
		
		// Submit Button clickHandler - addPanelSubmitButton
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					
					addPanelSubmitButton();
				} catch (IOException | FTPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	
	}
	
	/**
	 * 
	 * TODO: Need to Finish Handling Removal of Cars
	 * @param cars
	 * @throws SQLException
	 * @throws IOException
	 */
	public void paintRemovePanel(ArrayList<Car> cars) throws SQLException, IOException {
		removePanel.removeAll();
		// Create ArrayList of cars and JButton and JLabel for displaying
		removeButton = new JButton[cars.size()];
		carNames = new JLabel[cars.size()];
		
		if(cars.size() > 0){		
			// Loop to add car names and buttons to removePanel
			for(int i = 0; i < cars.size(); i++){
				// Add removeButton for car
				removeButton[i] = new JButton("X");
				removeButton[i].setBounds(buttonX, startingY+(75*i), buttonXSize, buttonXSize);
				removePanel.add(removeButton[i]);
				// Add carName for car
				carNames[i] = new JLabel(cars.get(i).getCarYear() +" "+ cars.get(i).getCarMake() +" "+ cars.get(i).getCarModel());
				carNames[i].setBounds(labelX, startingY+(75*i), 400, 40);
				removePanel.add(carNames[i]);
				removePanel.setPreferredSize(new Dimension (700, startingY+(75*i)+50));
				removeButton[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
					// Retrieves the Index of the X clicked, subtract 25 for initial starting point, 
					// and Divide by 75 for spacing between cars
					// SHOULD BE CORRECT, NEED MORE TESTING
					int carIndex = ((e.getComponent().getY()-25)/75);
					System.out.println("X Clicked on Car " + carIndex + " which has an ID of " + cars.get(carIndex).getCarID());
					// 0 for ok, 2 for cancel
					delete = JOptionPane.showConfirmDialog(removePanel, "You are about to remove this car from the database, "
							+ "are you sure you want to continue? ",
							"Remove Car From Database", JOptionPane.OK_CANCEL_OPTION);
						// Remove the car from the database
						if(delete==0){
							String pathName = cars.get(carIndex).getCarYear()+cars.get(carIndex).getCarMake()+cars.get(carIndex).getCarModel();
							pathName = pathName.replaceAll("\\s", "");
							System.out.println("Should be removing directory "+ pathName);
							// Remove from server
							FTPDeleteDirectory ftpDelete = new FTPDeleteDirectory(HOST,PORT,USERNAME,PASSWORD, pathName);
							try {
								ftpDelete.removeCarDirectory();
								System.out.println("Deleted directory from server!");
							} catch (FTPException | IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							// Remove from the Database
							int carID = cars.get(carIndex).getCarID();
							try {
								DatabaseProvider.getInstance().deleteCar(carID);
								DatabaseProvider.getInstance().deletePhotos(carID);
								DatabaseProvider.getInstance().deleteFeatures(carID);
								cars.remove(carIndex);
							} catch (SQLException | IOException e2) {
								e2.printStackTrace();
							}
							/* Try to paint all of the panels??*/
							try {
								paintAddPanel();
							} catch (SQLException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								paintEditPanel(cars);
							} catch (SQLException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								paintRemovePanel(cars);
							} catch (SQLException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
			}
		}
	}	
	
	// Paints Edit Panel With Cars
	public void paintEditPanel(ArrayList<Car> cars) throws SQLException, IOException {
		editPanel.removeAll();
		/* Create Buttons & Labels for Cars*/
		editButton = new JButton[cars.size()];
		carNames = new JLabel[cars.size()];
		if(cars.size() > 0){		
			// Loop to add car names and buttons to removePanel
			for(int i = 0; i < cars.size(); i++){
				// Add removeButton for car
				editButton[i] = new JButton("EDIT");
				editButton[i].setBounds(buttonX, startingY+(75*i), 50, buttonXSize);
				editPanel.add(editButton[i]);
				// Add carName for car
				carNames[i] = new JLabel(cars.get(i).getCarYear() +" "+ cars.get(i).getCarMake() +" "+ cars.get(i).getCarModel());
				carNames[i].setBounds(labelX, startingY+(75*i), 400, 40);
				editPanel.add(carNames[i]);
				editPanel.setPreferredSize(new Dimension (700, startingY+(75*i)+50));
				editButton[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
					// Retrieves the Index of the X clicked, subtract 25 for initial starting point, 
					// and Divide by 75 for spacing between cars
					// SHOULD BE CORRECT, NEED MORE TESTING
					int carIndex = ((e.getComponent().getY()-25)/75);
					System.out.println("EDIT Clicked on Car " + carIndex);
					System.out.println("The ID of this car is " + cars.get(carIndex).getCarID());
						try {
							openEditFrame(carIndex);
						} catch (SQLException | IOException | FTPException e1) {
							e1.printStackTrace();
						} 
					
					}
				});
			}
		}
	}
	
	
	/**
	 * Handles when user clicks Upload Button
	 * TODO: 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void addPanelUploadButton() throws SQLException, IOException {
		
		
		int carID = DatabaseProvider.getInstance().returnAllCars().size();
		int photoID = 1;
		
		// Create JFileChooser - set to accept images
		fileChooser = new JFileChooser();
		fileChooser.setVisible(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpeg, jpg, png files", "jpeg", "jpg","png");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setBounds(500,20, 250, 300);
		addPanel.add(fileChooser);;
		int status = fileChooser.showOpenDialog(null);
		
		// If user selects allowable file
		if(status== JFileChooser.APPROVE_OPTION){
			f = fileChooser.getSelectedFile();	
			System.out.println(f.getAbsolutePath());
			// Create InputStream
			try {
				// Create temp photo + Add to ArrayList
				Photo tempPhoto = new Photo(photoID, carID, f.getAbsolutePath());
				//addPanelPhotos.add(tempPhoto);
				tempPhotos.add(tempPhoto);
				// Create bufferedImage to display
				inputStream = new FileInputStream(f);
				img = ImageIO.read(f);
				System.out.println("creating input stream for a photo");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			repaintAddPanelPhotos();
		} else {
			// No file was chosen
			System.out.println("NOT UPLOADING A PHOTO?");
		}
	}

	public void repaintAddPanelPhotos() throws IOException{
		frame.validate();
		System.out.println("Should be adding photos");
		System.out.println("RepaintAddPanelPhotos has a size of " + tempPhotos.size());
		for(int i = 0; i < tempPhotos.size(); i++){
			// Create imageLabel[i]
			imageLabel = new JLabel[tempPhotos.size()];
			imageLabel[i] = new JLabel("");
		    imageLabel[i].setBounds(imageX, (imageY*(i+1))+(imageSize*i), imageSize, imageSize);
           	imageLabel[i].setIcon(new ImageIcon(img.getScaledInstance(imageSize, imageSize, imageSize)));
		    imagePanel.setPreferredSize(new Dimension (325, tempPhotos.size()*220+20));
            imagePanel.add(imageLabel[i]);
            imagePanel.validate();
            // remove Image Buttons
            removeImageButton = new JButton[tempPhotos.size()];
            removeImageButton[i] = new JButton("X");
            removeImageButton[i].setBounds(removeImageStartX, (i*220)+100, buttonXSize, buttonXSize);
            imagePanel.add(removeImageButton[i]);
            frame.validate();
            // TODO: TEST TO MAKE SURE YOU CAN REMOVE AN IMAGE
	        removeImageButton[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// Retrieves the Index of the X clicked, subtract 25 for initial starting point, 
					// and Divide by 75 for spacing between cars
					int imageIndex = ((e.getComponent().getY()-100)/220);
					System.out.println("X Clicked on Photo " + imageIndex);
					// TODO Remove the image from the being send to database!
					tempPhotos.remove(imageIndex);
					frame.validate();
					System.out.println("After removing a photo, there are now " + addPanelPhotos.size()+ " left");
					try {
						imagePanel.removeAll();
						repaintAddPanelPhotos();
						frame.validate();
						addPanel.validate();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
	        });
		}
	}

	public void addPanelSubmitButton() throws Exception{
							
		// Get Data to create car
		carYear = Integer.parseInt(yearField.getText());
		carModel= modelField.getText();
		carMake = makeField.getText();
		carPrice = Integer.parseInt(priceField.getText());
		carMileage = Integer.parseInt(mileageField.getText());
		String needsParsed = featuresArea.getText();
		
		// Create directory string & remove all spaces
		String photoDirectoryName = carYear+carMake+carModel;
		photoDirectoryName = photoDirectoryName.replaceAll("\\s", "");
		
		// Create objects to be inserted into database
		submitCar = new Car(0, carYear, carMake, carModel, carPrice, carMileage);
		/* Returns the ID of the car that was just inserted */
		int carDBID = DatabaseProvider.getInstance().addCar(submitCar);
		
		/* Adds features to Database */
		ArrayList<String> features = new ArrayList<String>();
		features = returnFeatures(needsParsed);
		for(int i = 0; i < features.size(); i++){
			System.out.println("Adding feature to database: " + features.get(i));
			DatabaseProvider.getInstance().addFeatures(carDBID, features.get(i));
		}
		
		// Uploading of car to GoDaddy Server
		for(int i = 0; i < tempPhotos.size(); i++){
			// TODO WORKING, TEST MORE DIFFICULT CASES
			System.out.println("The id associated with the photo is: " + carDBID + " and the path to the photo is " + tempPhotos.get(i).getFilePath());
			
			// Creates a new file, uploads it to the server
			f = new File(tempPhotos.get(i).getFilePath());
			System.out.println("Path for photo before uploading to server is " + f.getPath());
			FTPTask completeFTP = new FTPTask(HOST,PORT,USERNAME,PASSWORD, photoDirectoryName,f);
			completeFTP.upload();
			System.out.println("Check directory now, upload should have worked");
			
			// Set carID after ID has been returned.  Set photopath so it can be found on the server
			// Upload Photo
			tempPhotos.get(i).setCarID(carDBID);
			String filePath = photoDirectoryName + "/" + f.getName();
			System.out.println("The filepath being placed into the database is: " + filePath);
			tempPhotos.get(i).setFilePath(filePath);
			DatabaseProvider.getInstance().addPhoto(tempPhotos.get(i));
		}
		submitCar.setCarID(carDBID);
		cars.add(submitCar);
		addPanel.removeAll();
		features.clear();
		tempPhotos.clear();
		System.out.println("Temp photos: "+tempPhotos.size());
		paintAddPanel();
		paintEditPanel(cars);
		paintRemovePanel(cars);
	}
		
	
	
	/**
	 *
	 * TODO: Add Progress Bar for Loading
	 * @param carIndex - carIndex to retrieve information from Database
	 * @throws SQLException
	 * @throws IOException
	 * @throws FTPException
	 */
	public void openEditFrame(int carIndex) throws SQLException, IOException, FTPException {
		
		// Frame basics
		editFrame = new JFrame("Edit Car");
		editFrame.setSize( 800, 600 );
		editFrame.setVisible(true);
		editFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		editFramePanel = new JPanel(null);
		// Create editPanel
		// Create + Setup ScrollPane and JPanel to handle images
		editImagesPanel = new JPanel(null);
		editImageScrollPane = new JScrollPane(editImagesPanel);
		editImageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		editImageScrollPane.setBounds(420, 20, 350, 500);
		editFramePanel.add(editImageScrollPane);
		// Add everything to edit Frame
		editFrame.getContentPane().add(editFramePanel);
		
		
		// Preparing Objects for EditPanel
		editCar = new Car(cars.get(carIndex).getCarID(), cars.get(carIndex).getCarYear(), cars.get(carIndex).getCarMake(), 
				cars.get(carIndex).getCarModel(), cars.get(carIndex).getPrice(), cars.get(carIndex).getMileage());
		editPanelPhotos = DatabaseProvider.getInstance().returnCarPhotos(editCar.getCarID());
		editPanelFeatures = DatabaseProvider.getInstance().returnCarFeatures(editCar.getCarID());
		// Add and Fill in all of the labels
		paintEditFrame(editCar, editPanelPhotos, editPanelFeatures);	
	}
	
	public void paintEditFrame(Car editCar, ArrayList<Photo> photos, ArrayList<String> features) throws SQLException, IOException, FTPException{
		yearLabel = new JLabel( "Year:" );
		yearLabel.setBounds( 10, 15, 150, 20 );
		editFramePanel.add(yearLabel);

		yearField = new JTextField();
		yearField.setBounds( 10, 35, 150, 20 );
		yearField.setText(String.valueOf(editCar.getCarYear()));
		editFramePanel.add( yearField );

		makeLabel = new JLabel( "Make:" );
		makeLabel.setBounds( 10, 60, 150, 20 );
		editFramePanel.add( makeLabel );

		makeField = new JTextField();
		makeField.setBounds( 10, 80, 150, 20 );
		makeField.setText(editCar.getCarMake());
		editFramePanel.add( makeField );
		
		modelLabel = new JLabel( "Model:" );
		modelLabel.setBounds( 10, 105, 150, 20 );
		editFramePanel.add( modelLabel );

		modelField = new JTextField();
		modelField.setBounds( 10, 125, 150, 20 );
		modelField.setText(editCar.getCarModel());
		editFramePanel.add( modelField );
		
		mileageLabel = new JLabel("Mileage: ");
		mileageLabel.setBounds(180, 30, 150, 20);
		editFramePanel.add(mileageLabel);
		
		mileageField = new JTextField();
		mileageField.setBounds(180, 50, 150, 20);
		mileageField.setText(String.valueOf(editCar.getMileage()));
		editFramePanel.add(mileageField);
		
		priceLabel = new JLabel("Price:  ");
		priceLabel.setBounds(180, 90, 150, 20);
		editFramePanel.add(priceLabel);
		
		priceField = new JTextField();
		priceField.setBounds(180, 110, 150, 20);
		priceField.setText(String.valueOf(editCar.getPrice()));
		editFramePanel.add(priceField);
		
		featuresLabel = new JLabel("Features..seperate with comma *NO SPACE* (feature,feature)");
		featuresLabel.setBounds(10,150, 400, 20);
		editFramePanel.add(featuresLabel);
		
		String reparsed = reparseFeatures(editPanelFeatures);
		featuresArea = new JTextArea(reparsed);
		featuresArea.setLineWrap(true);
		featuresArea.setWrapStyleWord(true);
		featuresArea.setBounds(10, 176, 400, 200);
		editFramePanel.add(featuresArea);
		
		/* Create car after painting*/
		Car edited = new Car(editCar.getCarID(), Integer.parseInt(yearField.getText()), modelField.getText(), 
				makeField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(mileageField.getText()));
		
		
		
		
		
		// Save + Submit Button
		submitChangesButton = new JButton("Save and Submit Changes");
		submitChangesButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				carYear = Integer.parseInt(yearField.getText());
				carModel= modelField.getText();
				carMake = makeField.getText();
				carPrice = Integer.parseInt(priceField.getText());
				carMileage = Integer.parseInt(mileageField.getText());
				String needsParsed = featuresArea.getText();
				Car changedCar = new Car(editCar.getCarID(), carYear, carMake, carModel, carPrice, carMileage );
				try {
					// Reopens Edit Frame
					editPanelSubmitButton(editCar, changedCar, reparsed, needsParsed);
					//paintEditFrame(changedCar);
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		submitChangesButton.setBounds(32, 425, 200, 40);
		editFramePanel.add(submitChangesButton);
		
		// Upload Button for Editing
		uploadButton = new JButton("Upload a Photo");
		// Upload Button clickHandler - send to addPanelUploadButton
		uploadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					editPanelUploadButton();
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		uploadButton.setBounds(252, 425, 120, 40);
		editFramePanel.add(uploadButton);

		editImageLabel = new JLabel[photos.size()];
		for(int i = 0; i < photos.size(); i++){			
			// Need to use FTP Client to download filepath
			String filePath = photos.get(i).getFilePath();
			// System.out.println("The photo path for the photo being downloaded is " + photos.get(i).getFilePath());
			FTPDownload ftpDownload = new FTPDownload(HOST, PORT, USERNAME, PASSWORD, filePath);
			//finish FTP Donwload
			File file = ftpDownload.downloadPhoto();
			files.add(file);
			try {		
				img = ImageIO.read(file);
				// System.out.println("creating input stream for a photo");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			editImageLabel[i] = new JLabel("");
			editImageLabel[i].setIcon(new ImageIcon(img.getScaledInstance(imageSize, imageSize, imageSize)));
			editImageLabel[i].setSize(imageSize, imageSize);
			editImageLabel[i].setBounds(imageX, (imageY*(i+1))+(imageSize*i), imageSize, imageSize);
		   	editImageLabel[i].setVisible(true);
		    editImagesPanel.setPreferredSize(new Dimension (325, photos.size()*220+20));
		   	editImagesPanel.add(editImageLabel[i]);
		    editImageLabel[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				// Retrieves the Index of the X clicked, subtract 25 for initial starting point, 
				// and Divide by 75 for spacing between cars
				// SHOULD BE CORRECT, NEED MORE TESTING
				int carIndex = ((e.getComponent().getY()-25)/75);
				System.out.println("Clicked on Image " + carIndex);
				System.out.println("Should open up image with actual size");
				// TODO: ADD CLICK HANDLER FOR IMAGE LABEL TO DISPLAY FULL SIZE IMAGE
				
				}
			});
		   
		    
		   	// removeImage Buttons
		    editRemoveImageButton = new JButton[photos.size()];
		    editRemoveImageButton[i] = new JButton("X");
		    editRemoveImageButton[i].setBounds(removeImageStartX, (i*220)+100, buttonXSize, buttonXSize);
		    editImagesPanel.add(editRemoveImageButton[i]);
		    // Remove Image Button ClickHandler
		    editRemoveImageButton[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				// Retrieve car index	
				int photoIndex = ((e.getComponent().getY()-100)/220);
				System.out.println("EDIT FRAME X Clicked on Photo " + photoIndex);
					// System.out.println("NEED TO REMOVE IMAGE"); 
					// System.out.println("Photo ID: is" + photos.get(photoIndex).getPhotoID());
					String removePath = "/public_html/images/" + photos.get(photoIndex).getFilePath();
					// Delete from server
					FTPDeleteFile delete = new FTPDeleteFile(HOST, PORT, USERNAME, PASSWORD, removePath);
					delete.removeFile();		
					try {
						// Delete from database
						System.out.println(photos.get(photoIndex).getPhotoID());
						DatabaseProvider.getInstance().removePhoto(photos.get(photoIndex).getPhotoID());
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
					editImagesPanel.removeAll();
					editImagesPanel.validate();
					try {
						repaintEditPanelPhotos();
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		    
		}
	} 
	
	public void editPanelUploadButton() throws Exception{
		// Create JFileChooser - set to accept images
		fileChooser = new JFileChooser();
		fileChooser.setVisible(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpeg, jpg, png files", "jpeg", "jpg","png");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setBounds(500,20, 250, 300);
		addPanel.add(fileChooser);;
		int status = fileChooser.showOpenDialog(null);
		String directoryName = editCar.getCarYear()+editCar.getCarMake()+editCar.getCarModel();
		// If user selects allowable file
		if(status== JFileChooser.APPROVE_OPTION){
			f = fileChooser.getSelectedFile();	
			// Create InputStream
			try {
				// Upload the photo
				// System.out.println("Path for photo before uploading to server is " + f.getPath());
				FTPTask completeFTP = new FTPTask(HOST,PORT,USERNAME,PASSWORD, directoryName ,f);
				completeFTP.upload();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// Upload photo to database
			Photo upload = new Photo(1, editCar.getCarID(), directoryName+"/"+f.getName());
			DatabaseProvider.getInstance().addPhoto(upload);
			// Need to repaintEditPanelPhotos
			repaintEditPanelPhotos();
		} else {
			// No file was chosen
			System.out.println("NOT UPLOADING A PHOTO?");
		}
	}
	
	public void repaintEditPanelPhotos() throws SQLException, IOException{
		editImagesPanel.validate();
		// Return Photos associated with car
		editPanelPhotos = DatabaseProvider.getInstance().returnCarPhotos(editCar.getCarID());
		// Paint Method
		System.out.println("After uploading a picture in edit panel, there are this many cars: " + editPanelPhotos.size());
		editImageLabel = new JLabel[editPanelPhotos.size()];
		System.out.println("Repainting editPanel with size of: " + editPanelPhotos.size());
		for(int i = 0; i < editPanelPhotos.size(); i++){			
			// Need to use FTP Client to download filepath
			String filePath = editPanelPhotos.get(i).getFilePath();
			// System.out.println("The photo path for the photo being downloaded is " + editPanelPhotos.get(i).getFilePath());
			FTPDownload ftpDownload = new FTPDownload(HOST, PORT, USERNAME, PASSWORD, filePath);
			//finish FTP Donwload
			File file = ftpDownload.downloadPhoto();
			files.add(file);
			try {		
				img = ImageIO.read(file);
				// System.out.println("creating input stream for a photo");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			editImageLabel[i] = new JLabel("");
			editImageLabel[i].setIcon(new ImageIcon(img.getScaledInstance(imageSize, imageSize, imageSize)));
			editImageLabel[i].setBounds(imageX, (imageY*(i+1))+(imageSize*i), imageSize, imageSize);
		   	editImageLabel[i].setVisible(true);
		    editImagesPanel.setPreferredSize(new Dimension (325, editPanelPhotos.size()*220+20));
		   	editImagesPanel.add(editImageLabel[i]);
		   	// removeImage Buttons
		    editRemoveImageButton = new JButton[editPanelPhotos.size()];
		    editRemoveImageButton[i] = new JButton("X");
		    editRemoveImageButton[i].setBounds(removeImageStartX, (i*220)+100, buttonXSize, buttonXSize);
		    editImagesPanel.add(editRemoveImageButton[i]);
		    // Remove Image Button ClickHandler
		    editRemoveImageButton[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				// Retrieve car index	
				int photoIndex = ((e.getComponent().getY()-100)/220);
				System.out.println("EDIT FRAME X Clicked on Photo " + photoIndex);
					// System.out.println("NEED TO REMOVE IMAGE"); 
					// System.out.println("Photo ID: is" + editPanelPhotos.get(photoIndex).getPhotoID());
					String removePath = "/public_html/images/" + editPanelPhotos.get(photoIndex).getFilePath();
					// Delete from server
					FTPDeleteFile delete = new FTPDeleteFile(HOST, PORT, USERNAME, PASSWORD, removePath);
					delete.removeFile();		
					try {
						// Delete from database
						System.out.println(photos.get(photoIndex).getPhotoID());
						DatabaseProvider.getInstance().removePhoto(photos.get(photoIndex).getPhotoID());
						
					} catch (SQLException | IOException e1) {			
						e1.printStackTrace();
					}
						editFrame.removeAll();
						editFrame.validate();	
					    try {
							repaintEditPanelPhotos();
						} catch (SQLException | IOException e1) {
							e1.printStackTrace();
						}
				}
			});
		
		}
	}

	
	public void editPanelSubmitButton(Car oldCar, Car newCar, String oldFeatures, String newFeatures) throws SQLException, IOException{
		// TODO Save changes to database
		/* Car Make */
		System.out.println("Old car: " + oldCar.getCarID() + oldCar.getCarYear() + oldCar.getCarMake() + oldCar.getCarModel() + oldCar.getPrice());
		System.out.println("New car: " + newCar.getCarID() + newCar.getCarYear() + newCar.getCarMake() + newCar.getCarModel() + newCar.getPrice());
 		if(oldCar.getCarMake().equals(newCar.getCarMake())){
			// DO NOTHING
		} else {
			// Change car make
			DatabaseProvider.getInstance().changeCarMake(editCar.getCarID(), newCar.getCarMake());
			System.out.println("Changed make");
		}
 		/* Car Year */
 		if(oldCar.getCarYear()==newCar.getCarYear()){
			// DO NOTHING
		} else {
			// Change car make
			DatabaseProvider.getInstance().changeCarYear(editCar.getCarID(), newCar.getCarYear());
			System.out.println("Changed Year");
		}
 		/* Car Model */
 		if(oldCar.getCarModel().equals(newCar.getCarModel())){
			// DO NOTHING
		} else {
			// Change car make
			DatabaseProvider.getInstance().changeCarModel(editCar.getCarID(), newCar.getCarModel());
			System.out.println("Changed model");
		}
 		/* Car Price */
 		if(oldCar.getPrice()==newCar.getPrice()){
			// DO NOTHING
		} else {
			// Change car make
			DatabaseProvider.getInstance().changeCarPrice(editCar.getCarID(), newCar.getPrice());
			System.out.println("Changed price");
		}
 		/* Car Mileage */
 		if(oldCar.getMileage()==newCar.getMileage()){
 			// DO NOTHING
		} else {
			// Change car make
			DatabaseProvider.getInstance().changeCarMileage(editCar.getCarID(), newCar.getMileage());
			System.out.println("Changed mileage");
		}
 		
 		// TODO: FEATURES!!!
 		/* If strings are equal, do nothing
 		 *  else, delete all features and add all features
 		 * */
 		if(oldFeatures.equals(newFeatures)){
 			System.out.println("Features are equal");
 		} else {
 			// Remove old features
 			DatabaseProvider.getInstance().deleteFeatures(editCar.getCarID());
 			// Convert new features to ArrayList
 			ArrayList<String> updateFeatures = new ArrayList<String>();
 			updateFeatures = returnFeatures(newFeatures);
 			// Add new Features
 			for(int i = 0; i < updateFeatures.size(); i++){
 				DatabaseProvider.getInstance().addFeatures(editCar.getCarID(), updateFeatures.get(i));
 			}
		}
	}
		
		/**
	 * 
	 * @param features - takes the features from a given car
	 * @return a string of text with the features associated with a car
	 */
	public String reparseFeatures (ArrayList<String> features){
		String reparsedText = null;
		if(features.size()==0){
			reparsedText = "NO FEATURES ADDED";
		} else if (features.size()==1){
			reparsedText = features.get(0);
		} else {
			reparsedText = "";
			for(int i = 0; i < features.size()-1; i++){
				reparsedText = reparsedText + features.get(i) + ", ";
			}
			reparsedText = reparsedText + features.get(features.size()-1);
		}
		
			return reparsedText;
	}
	
	/**
	 * 
	 * @param needsParsed - a string of features that needs to be parsed
	 * @return an arraylist of features associated with a car
	 */
	public ArrayList<String> returnFeatures (String needsParsed){
		ArrayList<String> parsedList = new ArrayList<String>();
		String delims = "[,]+[ ]";
		String[] tokens = needsParsed.split(delims);
		for(int i = 0; i < tokens.length; i++){
			parsedList.add(tokens[i]);
		}
		return parsedList;
	}
}
