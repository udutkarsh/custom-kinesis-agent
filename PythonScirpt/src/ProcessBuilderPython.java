import java.lang.*;
import java.io.*;
import java.util.*;

class ProcessBuilderPython {
	static String n,type;
	
	public static void main(String[] args) throws Exception {
		
		List<String> command=null;
		  n = args[1];
		  type = args[0];
		pythonScript(command);      //Installing python packages and running python script
	
	}

	private static void pythonScript(List<String> command) throws IOException {
		ProcessBuilder pb;
		Process process;
		
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
		command.add(n);
		command.add("-o");
		command.add(type);
		pb = new ProcessBuilder(command); 
		process = pb.start();   // starting the process

		
		
		
		
	}

}
