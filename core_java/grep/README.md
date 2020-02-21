# Introduction
The Grep application is used to find text patterns in all files of a directory and write lines that contain a given pattern into a file. 
The application receives the regex (text pattern), the path to a directory and the output file as arguments. This can easily help the user 
find specific information in his files. There are two versions of the Grep application. The first version uses a simple approach to process data and is not very efficient with large files. The second one is improved in terms of efficiency and is more suitable for larger files. It uses more efficient approaches to process the information - data pipelines (by means of streams and lambda expressions available in Java 8). The project was built using Intellij IDEA. 

# Usage
As mentioned above, the program accepts three arguments: regex, a path to a directory (where the files will be checked) and a path to an output file. Firstly, the program will go into the directory and find the list of files there. Then, from each file, all of the lines will be extracted into the list of lines. Then each line will be examined for containing the regex. If the line contains the regex, it will be stored in another list of lines. The contents of the list will be written to the output file after each file in the given directory is processed.\
Example:

``` 
java grep.java abc /home/user/directory /home/user/ouput.txt 
```

Assuming there is only one file in a given directory which contains: 
```
abc
adefg
abhigk
abcdefghi
```
Contents of the output file after running the Grep application: 
```
abc 
abcdefghi
```

# Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

# Performance issue
The performance issue of grep app is that the lines from each file are stored in the list of Strings which has a limited capacity defined by the JVM. This means that for files with a large number of lines the program would not work, because it will run out of memory. To solve this problem streams can be used. Due to the fact that they provide pipelines, the improved program can process each line from file one by one and check if it contains the regex. If so it will be stored, otherwise dismissed. This means that the program will not store every line from the file but only the needed lines and would not run out of memory.

# Improvements
1. Extend the search scope. At this point, Grep looks for files in the provided directory and ignores files in subdirectories. An improved version would search for files in all subdirectories. 
2. Create a second output file which will include the paths and line numbers of the lines that contained the regex and were added to the first output file. Or include this additional information into the original output file.
3. Make a program work with multiple regexes. This will make a user run the program once for multiple regexes, instead of running the program multiple times of each regex.
