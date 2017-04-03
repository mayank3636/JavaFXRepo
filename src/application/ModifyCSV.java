package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifyCSV {

	
	static String hostname ="";
	
	public  void modifyCSV(String FilePath) {
		try{
		ClientName name= new ClientName();
		hostname=name.setHostName();	
		File f=new File (FilePath+"\\output.csv");
		FileReader fr= new FileReader(f);
		BufferedReader br= new BufferedReader(fr);
		File f2= new File(FilePath+"\\"+hostname+InitilizeElement.ILTestName+"Send.csv");
		
		FileWriter fw= new FileWriter (f2);
		BufferedWriter bw= new BufferedWriter(fw);
		
		String line ="";
		while((line=br.readLine())!=null){
			System.out.println(line);
			line=line+","+'"'+"\\\\"+hostname+"\\"+'"'+'\n';
			bw.write(line);
			System.out.println('\n');
		}
		bw.flush();
		bw.close();
		br.close();

		}catch(IOException e){
			e.printStackTrace();
		}
		}
}
