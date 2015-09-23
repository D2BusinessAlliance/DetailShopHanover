package business.database;

import java.io.IOException;

public class FTPDeleteDirectory {
	private int port;
	private String host;
	private String username;
	private String password;
	private String directory;	
	
	public FTPDeleteDirectory(String host, int port, String username, String password, String destDirectory){
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		this.directory=destDirectory;
	}
	
	public boolean removeCarDirectory() throws FTPException, IOException{
		FTPConnection ftpConnection = new FTPConnection(host, port, username, password);	
		try{
			ftpConnection.connect();
			ftpConnection.removeDirectory(directory);
			// remove directory
		} catch (FTPException | IOException e) {
			e.printStackTrace();
		} finally {
			ftpConnection.disconnect();
		}
		return true;
	}
}
	
	
