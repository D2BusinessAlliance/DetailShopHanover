package business.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;



 public class FTPConnection {
	 // Create new FTPClient
	 FTPClient ftpClient = new FTPClient();
	
	 // Globals
	 private String host;
	 private int port;
	 private String username;
	 private String password;
	 private int replyCode;
	 private InputStream inputStream;
	 private OutputStream outputStream;

	/**
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public FTPConnection(String host, int port, String username, String password){
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
	}
	
	/**
	 * 
	 * Creates an FTP Connection to the server!
	 * 
	 */
	public void connect() throws FTPException {
		try{
			ftpClient.connect(host, port);
			replyCode = ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(replyCode)){
				throw new FTPException("Could not connection to FTP Server");
			}
			boolean login = ftpClient.login(username, password);
			if(!login){
				ftpClient.disconnect();
				System.out.println("Could not authenticate");
			}
			ftpClient.enterLocalPassiveMode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean prepareDirectory(String directory) throws IOException{
		boolean aFlag = false;
		System.out.println("Trying to get to directory: " + directory);
		ftpClient.changeWorkingDirectory("public_html/images");
		System.out.println(ftpClient.printWorkingDirectory() + " is the current directory");
		if(ftpClient.changeWorkingDirectory(directory)){
			System.out.println("Directory is already created");
			aFlag =  true;
		} else {
			System.out.println("Making directory " + directory + " in current directory" + ftpClient.printWorkingDirectory());
			ftpClient.makeDirectory(directory);
			System.out.println(ftpClient.printWorkingDirectory());
			aFlag = true;
		}
		return aFlag;
	}
	
	/**
	 * @param file - file that needs to be uploaded
	 * @param directory - destination for the file
	 * @throws IOException 
	 */
	public void uploadFile(File file, String directory) throws FTPException, IOException {
	    	prepareDirectory(directory);
	    	System.out.println("Should be no more problems");
            try {
		    	boolean success = ftpClient.changeWorkingDirectory(directory);
	            if (!success) {
	                System.out.println("Could not make it to directory in FTPConnection uploadFile");
	                System.out.println(ftpClient.printWorkingDirectory());
	            }
	            success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);         
	            if (!success) {
	                System.out.println("Could not change file Type");
	            }
	            outputStream = ftpClient.storeFileStream(file.getName()); 
	            System.out.println("Output Stream created");
            } catch (IOException e) {
	           System.out.println("Error uploading file: " + e.getMessage());
        }
    }

	/**
	 * @param bytes - the array to write data to
	 * @param offset - offset of the stream
	 * @param length - length of the stream
	 * @throws IOException
	 */
	public void writeFileToArray(byte[] bytes, int offset, int length)
			throws IOException{
		outputStream.write(bytes, offset, length);
	}

	/**
	 * @throws IOException
	 */
	public void finishUpload() throws IOException {
		outputStream.close();
		ftpClient.completePendingCommand();
		ftpClient.changeToParentDirectory();
	}
	
	public InputStream downloadFile(String filePath) throws FTPException, IOException {
    	System.out.println("Should be no more problems");
        try {
        	boolean success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);         
            if (!success) {
                System.out.println("Could not change file Type");
            }
            // enter local active mode - prevent timeout?
            ftpClient.enterLocalActiveMode();
            //System.out.println("FTP CONNECTION FILEPATH IS " + filePath);
        	//success = ftpClient.retrieveFile(host+"public_html/images/"+filePath, outputStream);
            //if (!success) {
              //  System.out.println("Could not retrieve the file with the path specificed");
            //}
            
        } catch (IOException e) {
           System.out.println("Error uploading file: " + e.getMessage());
        }
        //System.out.println(ftpClient.retrieveFileStream(host+filePath));
        inputStream = ftpClient.retrieveFileStream(filePath);
        return inputStream;
        
	}
	
	public void closeStreams() throws IOException{
		inputStream.close();
		outputStream.close();
	}
	
	/**
	 * Trying to disconnect from the server
	 */
	public void disconnect() {
		if(ftpClient.isConnected()){
			try {
				if(!ftpClient.logout()){
					System.out.println("Could not log out from goDaddy");
				}
				ftpClient.disconnect();
			} catch (IOException e){
				e.printStackTrace();
			}
		}	
	}

	public void removeDirectory(String carName) throws IOException {
		String parentDir = "public_html/images";
		String currentDir = carName;
		String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
 
        FTPFile[] subFiles = ftpClient.listFiles(dirToList);
        System.out.println("Files in directory: " + subFiles.length);
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                System.out.println("CurrentFileName: " + aFile.getName());
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }
 
                // delete the file
                boolean deleted = ftpClient.deleteFile(filePath);
                if (deleted) {
                    System.out.println("DELETED the file: " + filePath);
                } else {
                    System.out.println("CANNOT delete the file: "
                                + filePath);
                }
            }
        }
 
        // finally, remove the directory itself
        boolean removed = ftpClient.removeDirectory(dirToList);
        if (removed) {
            System.out.println("REMOVED the directory: " + dirToList);
        } else {
            System.out.println("CANNOT remove the directory: " + dirToList);
        }
    }
		
	
	
	
	public void removeImage(String imagePath) throws IOException{
		ftpClient.deleteFile(imagePath);
		System.out.println("File with path of: " +imagePath+ " was deleted");
	}
 
 }
	