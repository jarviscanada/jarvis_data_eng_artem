package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepImp implements JavaGrep {

  private String rootPath;
  private String regex;
  private String outFile;

  public static void main(String[] args){

    if(args.length<3){
      throw new IllegalArgumentException("USAGE: regex rootPath outFile");
    }

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try{
      javaGrepImp.process();
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<String>();

    for(File file : listFiles(rootPath)) {
      for(String line : readLines(file)){
        if(containsPattern(line)){
          matchedLines.add(line);
        }
      }
    }

    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File folder = new File(rootDir);
    List<File> listOfFiles = new ArrayList<File>();

    for(File file : folder.listFiles()){
      if(file.isFile()){
        listOfFiles.add(file);
      }
    }

    return listOfFiles;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<String>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      String line = reader.readLine();

      while(line!=null){
        lines.add(line);
        line = reader.readLine();
      }

      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return line.contains(regex);
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    File outputFile = new File(outFile);
    if(!outputFile.exists()){ outputFile.createNewFile();}
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

    for(String line : lines){
      writer.write(line+"\n");
    }
    writer.close();
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
