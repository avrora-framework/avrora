package edu.ucla.cs.compilers.avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ucla.cs.compilers.avrora.avrora.test.SimplifierTestHarness;
import edu.ucla.cs.compilers.avrora.avrora.test.sim.SimTestHarness;
import edu.ucla.cs.compilers.avrora.cck.test.TestEngine;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;
import edu.ucla.cs.compilers.avrora.cck.util.ClassMap;

public class SimulatorTestHarnessTest
{
    @Before
    public void disableColors()
    {
        Terminal.useColors = false;
    }


    @Test
    public void testSimulatorHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("simulator", SimTestHarness.class);
        harnessMap.addClass("simplifier", SimplifierTestHarness.class);
        TestEngine testSuite = new TestEngine(harnessMap);

        String[] filePaths = getFilesExpectToPass();
        assertEquals(309, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(309, testSuite.successes.size());
    }


    @Test
    public void testSimulatorHarness_usingAllTstFiles_expectAllFail()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("simulator", SimTestHarness.class);
        harnessMap.addClass("simplifier", SimplifierTestHarness.class);
        TestEngine testSuite = new TestEngine(harnessMap);

        String[] filePaths = getFilesExpectedToFail();
        assertEquals(1, filePaths.length);
        assertFalse(testSuite.runTests(filePaths));
        assertEquals(0, testSuite.successes.size());
    }


    private String[] getFilesExpectedToFail()
    {
        String[] filePaths = TestResources
                .testFileNamesEndingWith("/edu/ucla/cs/compilers/avrora/test/interpreter/", ".tst",
                        this)
                .toArray(new String[0]);

        for (String testFile : filePaths)
        {
            if (testFile.endsWith("include02.tst"))
            {
                return new String[] { testFile };
            }
        }
        return null;
    }


    private String[] getFilesExpectToPass()
    {
        ArrayList<String> testFiles = new ArrayList<>();

        for (String testFile : TestResources.testFileNamesEndingWith(
                "/edu/ucla/cs/compilers/avrora/test/interpreter/", ".tst", this))
        {
            if (false == testFile.endsWith("include02.tst"))
            {
                testFiles.add(testFile);
            }
        }
        return testFiles.toArray(new String[testFiles.size()]);
    }

}
