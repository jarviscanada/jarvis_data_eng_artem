package ca.jrvs.apps.grep;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JavaGrepLambdaImp extends JavaGrepImp {
  public static void main(String[] args){

    if(args.length<3){
      throw new IllegalArgumentException("USAGE: regex rootPath outFile");
    }

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try{
      javaGrepLambdaImp.process();
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  @Override
  public List<String> readLines(File inputFile) {
    try {
      return Files.lines(Paths.get(inputFile.getPath())).collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<String>();
  }

  @Override
  public List<File> listFiles(String rootDir) {
    return Stream.of(new File(rootDir).listFiles()).filter(a -> a.isFile()).collect(Collectors.toList());
  }
}
