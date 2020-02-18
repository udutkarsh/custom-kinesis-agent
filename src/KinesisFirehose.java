

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;

public class KinesisFirehose {
	private AmazonKinesisFirehose firehoseClient;
	
	private final static Logger LOGGER =  
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	@SuppressWarnings("deprecation")
	private  void init() throws Exception {
		
		AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
   

		firehoseClient = AmazonKinesisFirehoseClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();
	}

	public  void jsonSender(JSONObject messageJson) throws Exception {

		try {
			
			init();
			FileReader read=new FileReader("path.properties");  
		    Properties p=new Properties();  
		    p.load(read);  
	
		 
		    
				Record record = new Record();
				
				record.withData(ByteBuffer.wrap(messageJson.toString().getBytes()));
				
				
				PutRecordRequest putRecordRequest = new PutRecordRequest()
						.withDeliveryStreamName(p.getProperty("stream_name"));
				
				putRecordRequest.setRecord(record);
				
				PutRecordResult result = firehoseClient.putRecord(putRecordRequest);
				
				LOGGER.info("Message to Firehose: " + messageJson.toString());
				LOGGER.info("Result Inserted with ID: " + result.getRecordId());
				
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
