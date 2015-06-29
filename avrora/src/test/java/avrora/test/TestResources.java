package avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

public class TestResources
{

    public static ArrayList<String> testFileNames(String resourceFolder,
            Object instance)
    {
        File folder = new File(
                instance.getClass().getResource(resourceFolder).getFile());
        ArrayList<String> files = new ArrayList<>();
        for (File fileEntry : folder.listFiles())
        {
            if (fileEntry.isFile())
            {
                files.add(fileEntry.getAbsolutePath());
            }
        }
        return files;
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
