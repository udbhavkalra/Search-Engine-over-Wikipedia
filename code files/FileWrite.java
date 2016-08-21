package wikipackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileWrite{

   public void writeToFile(StringBuilder text)throws IOException{
//
//      File file = new File("index.txt");

      Path filePath = Paths.get("index.txt");
      if (!Files.exists(filePath)) {
    	    Files.createFile(filePath);
    	}
    	Files.write(filePath, text.toString().getBytes(), StandardOpenOption.APPEND);
   }
}
