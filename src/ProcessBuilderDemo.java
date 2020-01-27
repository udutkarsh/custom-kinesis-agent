import java.lang.*;
import java.io.*;
import java.util.*;

class ProcessBuilderDemo {

	
	public static void main(String[] arg) throws Exception {
		
		List<String> command=null;
		
		pythonScript(command);      //Installing python packages and running python script
		Thread.sleep(2000);
		LogConverter.logReader();  	//Calling LogConverter class for conversion
	}

	private static void pythonScript(List<String> command) throws IOException {
		ProcessBuilder pb;
		Process process;
		Properties p;
	    FileReader read;
		 read=new FileReader("path.properties");  
         p=new Properties();  
         p.load(read);
		
         
         
		//Command for install python packages from requirements file
		
		command = new ArrayList<String>();
		command.add("python");
		command.add("-m");
		command.add("pip");
		command.add("install");
		command.add("-r");
		command.add("requirements.txt");
		pb = new ProcessBuilder(command);
		process = pb.start();  // starting the process
		
		//command to run python script (apache-fake-log-gen.py) in terminal 

		command = new ArrayList<String>();
		command.add("python");
		command.add("apache-fake-log-gen.py");
		command.add("-n");
		command.add(p.getProperty("n"));
		command.add("-o");
		command.add(p.getProperty("type"));
		pb = new ProcessBuilder(command); 
		process = pb.start();   // starting the process

		
		
		
		
	}

}
