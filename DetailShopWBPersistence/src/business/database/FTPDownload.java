package business.database;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import business.modelclasses.Photo;

public class FTPDownload {
	private static final int BUFFER = 16384;
	private int port;
	private String host;
	private String username;
	private String password;
	private String filePath;
	
	private BufferedOutputStream output;
	
	public FTPDownload(String host, int port, String username, String password, String filePath){
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		this.filePath = filePath;
	}
	
	public File downloadPhoto() throws SocketException, IOException{
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(this.host, this.port);
		File temp = File.createTempFile("image", ".jpg");
		System.out.println(temp.getPath());
		try {
			if(ftpClient.isConnected()){
				// Try to login
				boolean success = false;
				success = ftpClient.login(this.username, this.password);
				if(!success){
					System.out.println("Could not validate with credentials");
					System.exit(1);
				}
			}
			// We are now logged in, time to download!!
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// create temp file
			
			output = new BufferedOutputStream(new FileOutputStream(temp));
			ftpClient.retrieveFile("/public_html/images/"+this.filePath, output);
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if(output != null){
				output.close();
				ftpClient.disconnect();
			}
		}
		
		return temp;	
	}
	
	public File download() throws FTPException, IOException{
	FTPConnection ftpConnection = new FTPConnection(host, port, username, password);
	File file = null;
		try {
			ftpConnection.connect();
			file = File.createTempFile("image", ".jpg");
			String remoteFile = "/public_html/images/" + this.filePath;
			// output = new FileOutputStream(file);
			ftpConnection.ftpClient.retrieveFile(remoteFile, output);
			
		} finally {
			System.out.println("Trying to disconnection");
		ftpConnection.disconnect();
		}
		System.out.println("Returning file");
		return file;
		
		}
}
