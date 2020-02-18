

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;

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

	
	 BufferedReader reader;
     FileReader read;
     String line;
     Properties p;
     int id=0;
     InetAddress ip;
     ArrayList<String> array;
     ArrayList<String> file_parameter; 
     String app_name;
	
    

	public static void main(String[] arg) throws Exception {	
		 LogConverter logConverter =  new  LogConverter();
		 logConverter.logReader();
 	//Calling Logreader method for conversion
	}
    
    
	 public  void logReader() {
	        
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
	             file_parameter.add("application_name");
	        	
	             read=new FileReader("path.properties");  
	             
	             p=new Properties();  
	             p.load(read);  
	             app_name= p.getProperty("app_name");

	             
	            reader = new BufferedReader(
	                    new FileReader(p.getProperty("access_file")));
	            line = reader.readLine();
	            logToJson();
	            
	            
	          
	            
	        } catch (IOException e) { 
	            e.printStackTrace();
	        }
	    }
	 
	 
	 private void logToCSV() throws IOException {	 
		
	
        
			
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
	 
	 
	private  void logToJson() throws IOException {
		  
    
		
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
	    public void logToJsonGenerator(ArrayList<String> array,ArrayList<String> file_parameter) throws UnknownHostException {
	       
	    	
	         //adding common values ip and count_id
	         ip=InetAddress.getLocalHost();
	         array.add(Integer.toString(id++));
	         array.add(ip.toString());
	         array.add(app_name);
	               
	        StringBuilder json = new StringBuilder("{ ");
	        JSONObject messageJson = new JSONObject();
	        int i;
	        for (i = 0; i < array.size() ; i++) {
	        	
	        	 messageJson.put(file_parameter.get(i), array.get(i));
	          
	        } 
	        try {
	        	KinesisFirehose firehose= new KinesisFirehose();
	        	firehose.jsonSender(messageJson);
	        } catch (Exception e) {
	          
	            e.printStackTrace();
	        }
	   


	}
}	
	
		

