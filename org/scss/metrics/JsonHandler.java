package org.scss.metrics;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHandler {
  
    JSONObject obj = new JSONObject();
    FileWriter file ;
    
    
    
public void writeJson(JSONObject obj) throws IOException
{
    
    file = new FileWriter("C:/Users/Dan/Desktop/JsonOutput.json");
    
    try{
        file.write(obj.toJSONString());
        
    }
    catch(IOException e)
    {
        e.printStackTrace();
    }
    finally
    {
        file.flush();
        file.close();
    }
    
}

public void createJsonObj()
{
  //  obj.put("Project Name :", value);
    //obj.put("Total commit :", value);
    
    JSONArray commitPerFile = new JSONArray();
   // commitPerFile.add()
}
    
}
