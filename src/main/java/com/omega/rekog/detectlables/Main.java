package com.omega.rekog.detectlables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		ArrayList<String> J = new ArrayList<String>();
    	String[] list = {"a", "b", "jill", "abc"};
      for(String j: list)
      {
    	  J.add(j);
      }
      
     Collections.sort(J);
       list.equals(J);
       
       Random r = new Random();
   	char[] random_3_Char = new char[5];
   	//(char)(48+ r.nextInt(47));
   //	System.out.print(random_3_Char);
   	int n;
   
   	String a = "abcdefghilmnopqrstuvwxyz";
    int N = a.length();	
   	for(n = 0; n <10 ; n++)
   	{
   		int z;
   		//write three character string
   		
   		for(z = 0; z< 3; z++)
   		{
    			random_3_Char[z] = a.charAt(r.nextInt(N));       
   		}
   		
   		random_3_Char[3] = '/';
   		random_3_Char[4] = 'r';
   		String b = new String(random_3_Char);
   		
   		J.add(b);
   		
   		//System.out.print(random_3_Char);
   	//	System.out.print("\r");
   	}
   	   J.size();
   	
	}
   
	
	

}
