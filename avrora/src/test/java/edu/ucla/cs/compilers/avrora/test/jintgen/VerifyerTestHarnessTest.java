package edu.ucla.cs.compilers.avrora.test.jintgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import edu.ucla.cs.compilers.avrora.cck.test.TestEngine;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;
import edu.ucla.cs.compilers.avrora.cck.util.ClassMap;
import edu.ucla.cs.compilers.avrora.jintgen.isdl.verifier.VerifierTestHarness;
import edu.ucla.cs.compilers.avrora.test.TestResources;

public class VerifyerTestHarnessTest
{
    @Before
    public void disableColors()
    {
        Terminal.useColors = false;
    }


    @Test
    public void testVerifyHarness_usingAllTstFilesExceptUunresolvedEnumAndUnrType_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("verifier", VerifierTestHarness.class);

        ArrayList<String> files = TestResources
                .testFileNamesEndingWith("/edu/ucla/cs/compilers/avrora/test/jintgen/", ".tst", this);

        // skip unresolved_eunum_0x files
        Iterator<String> iterator = files.iterator();
        while (iterator.hasNext())
        {
            String filename = iterator.next();
            if (filename.contains("unr_enum_") || filename.contains("unr_ot_"))
            {
                iterator.remove();
            }
        }
        String[] filePaths = files.toArray(new String[files.size()]);

        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(111, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }


    @Test
    public void testVerifyHarness_usingEnumTstFiles_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("verifier", VerifierTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesStartingWith("/edu/ucla/cs/compilers/avrora/test/jintgen/", "unr_enum_",
                        this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(2, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }


    @Test
    public void testVerifyHarness_usingUnrOtTstFiles_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("verifier", VerifierTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesStartingWith("/edu/ucla/cs/compilers/avrora/test/jintgen/", "unr_ot_",
                        this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(6, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }

}
