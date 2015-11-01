package presenter;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ManagerProperties {
	
	public ManagerProperties() {
		// TODO Auto-generated constructor stub
	}

	public Properties loadFile(String filePath) throws FileNotFoundException{
		XMLDecoder xmlFile;
		
			xmlFile = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)));
			Properties p = (Properties) xmlFile.readObject();
			xmlFile.close();
			return p ;
		
	
	}
	public void saveFile(String fileName,Properties p) throws FileNotFoundException
	{
		//create file
		//writing  to  xml file
		XMLEncoder xmlFile;
			xmlFile = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName+".xml")));
			xmlFile.writeObject(p);
			xmlFile.flush();
			xmlFile.close();
		
		
	}
	
	
}
