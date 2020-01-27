

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LogConverter {

	
	static BufferedReader reader;
    static FileReader read;
    static String line;
    static Properties p;
    static int id=0;
    static InetAddress ip;
    static ArrayList<String> array;
    static ArrayList<String> file_parameter; 
	
	 public static void logReader() {
	        
	        try {
	        	
	        	//setting parameters from log_file
	    		file_parameter = new ArrayList<String>();  
	            file_parameter.add("remoteIP");
	            file_parameter.add("time");
	            file_parameter.add("method");
	            file_parameter.add("request");
	            file_parameter.add("status");
	            file_parameter.add("port");
	            file_parameter.add("host");
	            file_parameter.add("userAgent"); 
	        	

	    		//adding common parameters
	        	 file_parameter.add("id");
	             file_parameter.add("ip");
	        	
	        	// 
	             read=new FileReader("path.properties");  
	             
	             p=new Properties();  
	             p.load(read);  
	             Files.list(Paths.get("."))
	             .forEach(System.out::println);
	             
	            reader = new BufferedReader(
	                    new FileReader(p.getProperty("access_file")));
	            line = reader.readLine();
				if(p.getProperty("conversion_type").equals("LogToJson"))
				{
					logToJson();
				}
				else if(p.getProperty("conversion_type").equals("CSVToJson"))
				{
					logToCSV();
				}
	            
	            
	           
	          //  
	          
	            
	        } catch (IOException e) { 
	            e.printStackTrace();
	        }
	    }
	 
	 
	 private static void logToCSV() throws IOException {	 
		
	
        
			
			 while (line != null) {
			       array = new ArrayList<String>();
			        String[] words = line.split(",");
			        for (int i = 0; i <= words.length - 1; i++) {
			        	array.add(words[i]);
			        }
			        logToJsonGenerator(array,file_parameter);
			        line = reader.readLine();
			 }
			
	 }
	 
	 
	private static void logToJson() throws IOException {
		  
    
		
				 while (line != null) {
		       array = new ArrayList<String>();
		        String[] words = line.split("\\s");
		        for (int i = 0; i <= words.length - 1; i++) {
		            if (i == 0 || i == 6 || i == 8 || i == 9)
		                array.add(words[i]);
		            else if (i == 3) {
		                array.add(words[i] + " " + words[i + 1]);
		            } else if (i == 5 || i == 11) {
		                array.add(words[i].substring(1));
		            } else if (i == 10) {
		                array.add(words[i].substring(1, words[i].length() - 2));
		            }
		        }
		        logToJsonGenerator(array,file_parameter);
		           line = reader.readLine();
				 }
		}
	    public static void logToJsonGenerator(ArrayList<String> array,ArrayList<String> file_parameter) throws UnknownHostException {
	       
	    	
	         //adding common values ip and count_id
	         ip=InetAddress.getLocalHost();
	         array.add(Integer.toString(id++));
	         array.add(ip.toString());
	               
	        String json = " { ";
	        int i;
	        for (i = 0; i < array.size() - 1; i++) {
	            json = json + "\"" + file_parameter.get(i) + "\" : \"" + array.get(i) + "\", ";
	        }
	        json = json + "\"" + file_parameter.get(i) + "\" : \"" + array.get(i) + "\" }";
	        
	        
	        System.out.println(json+" ");
	        
	        
	        try {
           KinesisFirehose.jsonSender(json);
	        } catch (Exception e) {
	          
	            e.printStackTrace();
	        }
	   


	}
}	
	
		

