package avrora.test.jintgen;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import avrora.cck.test.TestEngine;
import avrora.cck.util.ClassMap;
import avrora.jintgen.isdl.verifier.VerifierTestHarness;

public class VerifyerHarnessTest
{
    @Test
    public void testVerifyHarness_usingAllTestcases_expectNotExceptional()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("verifier", VerifierTestHarness.class);
        String[] filePaths = testFileNames().toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }


    private ArrayList<String> testFileNames()
    {
        File folder = new File(
                getClass().getResource("/avrora/test/jintgen/").getFile());
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
