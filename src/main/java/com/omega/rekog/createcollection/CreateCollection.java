package com.omega.rekog.createcollection;

import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import com.amazonaws.services.rekognition.model.ListCollectionsRequest;
import com.amazonaws.services.rekognition.model.ListCollectionsResult;

public class CreateCollection {

//CreateCollection Construction	
   public CreateCollection() {

      AWSCredentials credentials;
      try {
         credentials = new ProfileCredentialsProvider("adminuser").getCredentials();
      } catch (Exception e) {
         throw new AmazonClientException(
            "Cannot load the credentials from the credential profiles file. " +
            "Please make sure that your credentials file is at the correct " +
            "location (/Users/userid/.aws/credentials), and is in valid format.",
            e);
      }


      AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
         .standard()
         .withRegion(Regions.US_EAST_1)
         .withCredentials(new AWSStaticCredentialsProvider(credentials))
         .build();

      // 1. CreateCollection 1
      String collectionId = "exampleCollection";
      String collectionId2 = "exampleCollection2";
      System.out.println("Creating collections: " +
         collectionId +
         " & " + collectionId2);
      CreateCollectionResult createCollectionResult = callCreateCollection(
         collectionId, amazonRekognition);
      System.out.println("CollectionArn : " +
         createCollectionResult.getCollectionArn());
      System.out.println("Status code : " +
         createCollectionResult.getStatusCode().toString());

      //CreateCollection 2
      createCollectionResult = callCreateCollection(collectionId2,
         amazonRekognition);
      System.out.println("CollectionArn : " +
         createCollectionResult.getCollectionArn());
      System.out.println("Status code : " +
         createCollectionResult.getStatusCode().toString());


      // 3. Page through collections with ListCollections
      System.out.println("Listing collections");
      int limit = 1;
      ListCollectionsResult listCollectionsResult = null;
      String paginationToken = null;
      do {
         if (listCollectionsResult != null) {
            paginationToken = listCollectionsResult.getNextToken();
         }
         listCollectionsResult = callListCollections(paginationToken, limit,
            amazonRekognition);

         List < String > collectionIds = listCollectionsResult.getCollectionIds();
         for (String resultId: collectionIds) {
            System.out.println(resultId);
         }
      } while (listCollectionsResult != null && listCollectionsResult.getNextToken() !=
         null);

      // 4. Clean up collections with DeleteCollection

      System.out.println("Deleting collections");
      DeleteCollectionResult deleteCollectionResult = callDeleteCollection(
         collectionId, amazonRekognition);
      System.out.println(collectionId + ": " + deleteCollectionResult.getStatusCode()
         .toString());

      DeleteCollectionResult deleteCollectionResult2 = callDeleteCollection(
         collectionId2, amazonRekognition);
      System.out.println(collectionId2 + ": " + deleteCollectionResult2.getStatusCode()
         .toString());

   }

   private static CreateCollectionResult callCreateCollection(String collectionId,
      AmazonRekognition amazonRekognition) {
      CreateCollectionRequest request = new CreateCollectionRequest()
         .withCollectionId(collectionId);
      return amazonRekognition.createCollection(request);
   }

   private static DeleteCollectionResult callDeleteCollection(String collectionId,
      AmazonRekognition amazonRekognition) {
      DeleteCollectionRequest request = new DeleteCollectionRequest()
         .withCollectionId(collectionId);
      return amazonRekognition.deleteCollection(request);
   }

   private static ListCollectionsResult callListCollections(String paginationToken,
      int limit, AmazonRekognition amazonRekognition) {
      ListCollectionsRequest listCollectionsRequest = new ListCollectionsRequest()
         .withMaxResults(limit)
         .withNextToken(paginationToken);
      return amazonRekognition.listCollections(listCollectionsRequest);
   }

}
