package edu.ucla.cs.compilers.avrora.test;

import java.io.File;
import java.util.ArrayList;

public class TestResources
{

    public static ArrayList<String> testFileNames(String resourceFolder,
            Object instance)
    {
        ArrayList<String> files = new ArrayList<>();
        for (File fileEntry : testFiles(resourceFolder, instance))
        {
            if (fileEntry.isFile())
            {
                files.add(fileEntry.getAbsolutePath());
            }
        }
        return files;
    }


    public static ArrayList<File> testFiles(String resourceFolder,
            Object instance)
    {
        File folder = new File(
                instance.getClass().getResource(resourceFolder).getFile());
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : folder.listFiles())
        {
            if (fileEntry.isFile())
            {
                files.add(fileEntry);
            }
        }
        return files;
    }


    public static ArrayList<String> testFileNamesStartingWith(
            String resourceFolder, String head, Object instance)
    {
        ArrayList<String> matchingFiles = new ArrayList<>();
        for (File testFile : TestResources.testFiles(resourceFolder, instance))
        {
            if (testFile.getName().startsWith(head))
            {
                matchingFiles.add(testFile.getAbsolutePath());
            }
        }
        return matchingFiles;
    }


    public static ArrayList<String> testFileNamesEndingWith(
            String resourceFolder, String tail, Object instance)
    {
        ArrayList<String> matchingFiles = new ArrayList<>();
        for (String testFile : TestResources.testFileNames(resourceFolder,
                instance))
        {
            if (testFile.endsWith(tail))
            {
                matchingFiles.add(testFile);
            }
        }
        return matchingFiles;
    }
}
