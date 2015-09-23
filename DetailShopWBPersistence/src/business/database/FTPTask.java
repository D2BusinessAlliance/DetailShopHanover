package business.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FTPTask {
	private static final int BUFFER = 16384;
	
	private int port;
	private String host;
	private String username;
	private String password;
	
	private String destDirectory;
	private File file;
	
	FileOutputStream fos = null;
	
	public FTPTask(String host, int port, String username, String password, String destDirectory, File file){
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		this.destDirectory=destDirectory;
		this.file=file;
	}
	
	public Void upload() throws Exception {
		// create FTPConnection
		FTPConnection ftpConnection = new FTPConnection(host, port, username, password);	
		// Try to upload file
		try{
			ftpConnection.connect();	
			ftpConnection.uploadFile(file, destDirectory);
			
			System.out.println("The file being uploaded to: " + destDirectory + " is " + file.getName());
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[BUFFER];
			int bytesRead = -1;
			long totalBytes = 0;
			int percentCompleted = 0;
			long fileSize = file.length();
			
			while ((bytesRead = inputStream.read(buffer)) != -1) {
	            ftpConnection.writeFileToArray(buffer, 0, bytesRead);
	            totalBytes += bytesRead;
	            percentCompleted = (int) (totalBytes * 100 / fileSize);
	            if(percentCompleted==50){
	            	System.out.println("Half way done");
	            }
	        }
				
			inputStream.close();
			ftpConnection.finishUpload();
			} catch(FTPException e){
				e.printStackTrace();
			} finally {
				ftpConnection.disconnect();
			}
		return null;
	}
	
	
}
	/*
	public FileOutputStream downloadFile(String filePath){
		// create FTPConnection
				FTPConnection ftpConnection = new FTPConnection(host, port, username, password);	
				// Try to upload file
				try{
					ftpConnection.connect();
					
					//fos = new FileOutputStream(ftpConnection.ftpClient.retrieveFileStream(filePath));
					
				} finally {
					fos.close();
				}
	}
}
*/


