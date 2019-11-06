package com.omega.rekog.detectlables.localbytes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.ModerationLabel;
import com.amazonaws.util.IOUtils;
import java.net.URL;

public class DetectLabelsBytes {
    public DetectLabelsBytes() {
    	String photo2="/Users/jessegrimes/Downloads/alg-nude-adam-levine-jpg.jpg";
    	String photo="http://porngirlworld.com/wp-content/uploads/2015/04/Nude-at-Ranch07.jpg" ;

        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("adminuser").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Usersuserid.aws/credentials), and is in a valid format.", e);
        }
        ByteBuffer imageBytes1 = null;
        ByteBuffer imageBytes2 = null;
        
        try (InputStream inputStream1 = new FileInputStream(new File(photo2))) {
            imageBytes1 = ByteBuffer.wrap(IOUtils.toByteArray(inputStream1));
        }
        catch(Exception ex)
        {
        	
        }
        
              
        try (InputStream inputStream2 = new URL(photo).openStream()) {
            imageBytes2 = ByteBuffer.wrap(IOUtils.toByteArray(inputStream2));
        }
        catch(Exception ex)
        {
        	
        }


        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
          		.standard()
          		.withRegion(Regions.US_EAST_1)
        		.withCredentials(new AWSStaticCredentialsProvider(credentials))
        		.build();

       

        try {
        	
        	 if(imageBytes2 != null)
             {	
             DetectLabelsRequest request = new DetectLabelsRequest()
                     .withImage(new Image()
                             .withBytes(imageBytes2))
                     .withMaxLabels(10)
                     .withMinConfidence(30F);
             
             DetectModerationLabelsRequest ModReq = new DetectModerationLabelsRequest()
            		 .withImage(new Image()
            				 .withBytes(imageBytes2))
            		 .withMinConfidence(50F);
            		 
            
           
             DetectLabelsResult result = rekognitionClient.detectLabels(request);
             List <Label> labels = result.getLabels();

             System.out.println("Detected labels for " + photo);
             for (Label label: labels) {
            	 System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }
             
             DetectModerationLabelsResult resultMod =  rekognitionClient.detectModerationLabels(ModReq);
            
    		 
             List <ModerationLabel> labels1 = resultMod.getModerationLabels();
             
             System.out.println("Detected ModerationLable for " + photo);
             for (ModerationLabel label: labels1) {
            	 System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }
             
             }

        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }

    }
}
