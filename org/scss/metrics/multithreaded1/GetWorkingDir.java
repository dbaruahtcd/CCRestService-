package org.scss.metrics.multithreaded1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;

public class GetWorkingDir {
  public static void main(String[] args) {
       System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
  
  
  //fileInfo.add("FileName \" : \"" +getFileName(str)+"\"");
       
  String fname = "test";
          
  String demo = "\"Filename \"" + ":"+ "\"" + fname + "\"";
  System.out.println(demo);
  String name = "dan";
  
  System.out.println("\"" + name + "\"");
  
  /*String fileName = "\"Filename \"" + ":"+ "\"" + FileHandler.getFileName(str) + "\"";
  String comp = "\"Complexity \"" + ":"+ "\"" + complexityCountFile + "\"";
  String commits = "\"#Commits \"" + ":"+ "\"" + commitCount + "\"";*/
  
  
  
  
  System.out.println("time+++++++++++++++++++++++++");
  
  long start = System.currentTimeMillis();
  double i = 0;
  while(i < 100000000)
  {
	  i+=1;
  }
  
  long end = System.currentTimeMillis();
  
  double  minutes = (end - start) / 1000.0;
  
  System.out.println("time " + minutes);
  }
}


//Working Directory = C:\Users\Dan\EnterpriceEclipse\CyclomaticComplexityRestService
