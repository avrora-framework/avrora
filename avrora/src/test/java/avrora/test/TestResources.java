package avrora.test;

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
}
