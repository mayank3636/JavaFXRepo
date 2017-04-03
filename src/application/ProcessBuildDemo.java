package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;


/**
 * @author M1030090,Mayank Upadhyaya
 *
 */
public class ProcessBuildDemo extends Thread{ 
	static Thread t1=new GreetingClient();
	 static String filePath;
	@Override
	public void run() {
		try {
		filePath=System.getenv("APPDATA");
		File f1= new File(filePath+"\\"+"ILP.cfg");
		Properties prop = new Properties();
		InputStream input= new FileInputStream(filePath+"\\"+"Application.properties");
		prop.load(input);
		String processInput=prop.getProperty("process");
		String processes[]=processInput.split(",");
		FileWriter fw1= new FileWriter(f1);

		BufferedWriter bw1= new BufferedWriter(fw1);
		for(String buildProcess:processes){
			bw1.write("\\process("+buildProcess+")\\% processor time");
			bw1.write('\n');
			bw1.write("\\.NET CLR Memory("+buildProcess+")\\# Bytes in all Heaps");
			bw1.write('\n');
			bw1.write("\\.NET CLR Memory("+buildProcess+")\\# Total committed Bytes");
			bw1.write('\n');
			bw1.write("\\.NET CLR Memory("+buildProcess+")\\% Time in GC");
			bw1.write('\n');
		}

		bw1.flush();
		bw1.close();
		File f= new File(filePath+"\\"+"ILP.bat");

		FileWriter fw= new FileWriter(f);
		BufferedWriter bw= new BufferedWriter (fw);
		bw.write("@echo off\r\n\r\n:: BatchGotAdmin\r\n:-------------------------------------\r\nREM  --> Check for permissions\r\n>nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"\r\n\r\nREM --> If error flag set, we do not have admin.\r\nif '%errorlevel%' NEQ '0' (\r\n    echo Requesting administrative privileges...\r\n    goto UACPrompt\r\n) else ( goto gotAdmin )\r\n\r\n:UACPrompt\r\n    echo Set UAC = CreateObject^(\"Shell.Application\"^) > \"%temp%\\getadmin.vbs\"\r\n    echo UAC.ShellExecute \"%~s0\", \"\", \"\", \"runas\", 1 >> \"%temp%\\getadmin.vbs\"\r\n\r\n    \"%temp%\\getadmin.vbs\"\r\n    exit /B\r\n\r\n:gotAdmin\r\n    if exist \"%temp%\\getadmin.vbs\" ( del \"%temp%\\getadmin.vbs\" )\r\n    pushd \"%CD%\"\r\n    CD /D \"%~dp0\"\r\n:--------------------------------------");
		bw.write('\n');
		bw.write("cd "+" "+filePath);
		bw.write('\n');
		bw.write("typeperf -cf"+" "+'"'+"ILP.cfg"+'"'+" "+"-f csv");
		bw.flush();
		bw.close();
		f= new File(filePath+"\\"+"logstash.conf");

		fw= new FileWriter(f);
		bw= new BufferedWriter (fw);
		String x="input {\r\n  file {\r\n    path => \""+filePath+"\\output.csv"+"\"\r\n    start_position => \"beginning\"\r\n   sincedb_path => \"/dev/null\"\r\n  }\r\n}\r\nfilter {\r\n  csv {\r\n      separator => \",\"  \r\n\t columns => [\"dateTime\",\"ILWSBClient % Processor Time\",\"ILPlatform % Processor Time\",\"ILPlatform # Bytes in all Heaps\",\"ILWSBClient # Bytes in all Heaps\",\"ILPlatform # Total committed Bytes\",\"ILWSBClient # Total committed Bytes\",\"ILPlatform % Time in GC\",\"ILWSBClient % Time in GC\"]\r\n  }\r\n   date {\r\nmatch => [ \"dateTime\", \"MM/dd/yyyy HH:mm:ss.SSS\", \"ISO8601\" ]\r\n  }\r\n  mutate {\r\nconvert => { \"ILWSBClient % Processor Time\" => \"integer\" }\r\nconvert => { \"ILPlatform % Processor Time\" => \"integer\" }\r\nconvert => { \"Total% Processor Time\" => \"integer\" }\r\nconvert => { \"ILWSBClient # Bytes in all Heaps\" => \"integer\" }\r\nconvert => { \"ILPlatform # Bytes in all Heaps\" => \"integer\" }\r\nconvert => { \"ILWSBClient % Time in GC\" => \"integer\" }\r\nconvert => { \"ILPlatform % Time in GC\" => \"integer\" }\r\nconvert => { \"ILWSBClient # Total committed Bytes\" => \"integer\" }\r\nconvert => { \"ILPlatform # Total committed Bytes\" => \"integer\" }\r\n}\r\n  }\r\noutput {\r\n   elasticsearch {\r\n     hosts => \"http://localhost:9200\"\r\n  }\r\nstdout {}\r\n}\r\n\r\n";	
		bw.write(x);
		bw.flush();
		bw.close();
		String[] command = {"CMD","/c",filePath+"\\"+"ILP.bat"};

		        ProcessBuilder probuilder = new ProcessBuilder( command );
		        //You can set up your work directory
		        probuilder.directory(new File(filePath));

		        Process process = probuilder.start();

		        //Read out dir output
		        InputStream is = process.getInputStream();
		        InputStreamReader isr = new InputStreamReader(is);
		        BufferedReader br = new BufferedReader(isr);
		        String line;
		        System.out.printf("Output of running %s is:\n",
		                Arrays.toString(command));
		        while ((line = br.readLine()) != null) {
		            System.out.println(line);
		        }
		        //Wait to get exit value
		        t1= new GreetingClient();   
		        while(true){
		        try{
		        	t1.run();
		        }catch (IllegalStateException e){
		        	System.out.println("More Data will be added in Next Cycle");
		        }
		       try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        }
		}catch (IOException e){
			e.printStackTrace();
		}
	}
 
}