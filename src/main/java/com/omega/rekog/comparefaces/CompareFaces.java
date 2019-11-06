package com.omega.rekog.comparefaces;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;


public class CompareFaces {
    public static final String S3_BUCKET = "rekognition-jess";
  //constructor
	public CompareFaces() {
        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("adminuser").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
        		.standard()
        		.withRegion(Regions.US_EAST_1)
        		.withCredentials(new AWSStaticCredentialsProvider(credentials))
        		.build();

        Image source = getImageUtil(S3_BUCKET, "DSC_2811U.jpg");
        Image target = getImageUtil(S3_BUCKET, "DSC_2743.jpg");
        Float similarityThreshold = 70F;
        CompareFacesResult compareFacesResult = callCompareFaces(source,
        		target,
        		similarityThreshold,
        		rekognitionClient);


        List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
        for (CompareFacesMatch match: faceDetails){
        	ComparedFace face= match.getFace();
        	BoundingBox position = face.getBoundingBox();
        	System.out.println("Face at " + position.getLeft().toString()
        			+ " " + position.getTop()
        			+ " matches with " + face.getConfidence().toString()
        			+ "% confidence.");
        }

   }

    public static CompareFacesResult callCompareFaces(Image sourceImage, Image targetImage,
            Float similarityThreshold, AmazonRekognition amazonRekognition) {

      CompareFacesRequest compareFacesRequest = new CompareFacesRequest()
         .withSourceImage(sourceImage)
         .withTargetImage(targetImage)
         .withSimilarityThreshold(similarityThreshold);
      return amazonRekognition.compareFaces(compareFacesRequest);
   }

    private static Image getImageUtil(String bucket, String key) {
      return new Image()
          .withS3Object(new S3Object()
                  .withBucket(bucket)
                  .withName(key));
    }
}

