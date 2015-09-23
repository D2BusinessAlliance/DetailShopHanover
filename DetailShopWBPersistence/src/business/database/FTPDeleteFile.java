package business.database;

import java.io.IOException;

public class FTPDeleteFile {
	
	private int port;
	private String host;
	private String username;
	private String password;
	private String filePath;	
	
	public FTPDeleteFile(String host, int port, String username, String password, String filePath){
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		this.filePath = filePath;
	}
	
	public boolean removeFile(){
		FTPConnection ftpConnection = new FTPConnection(host, port, username, password);
		try {
			ftpConnection.connect();
			ftpConnection.removeImage(filePath);
		} catch (FTPException | IOException e) {
			e.printStackTrace();
		} finally {
			ftpConnection.disconnect();
		}
		return true;
	}
}

