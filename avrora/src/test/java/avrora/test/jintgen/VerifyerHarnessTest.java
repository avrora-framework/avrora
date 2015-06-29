package avrora.test.jintgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import avrora.cck.test.TestEngine;
import avrora.cck.text.Terminal;
import avrora.cck.util.ClassMap;
import avrora.jintgen.isdl.verifier.VerifierTestHarness;
import avrora.test.TestResources;

public class VerifyerHarnessTest
{
    @Test
    public void testVerifyHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        Terminal.useColors = false;
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("verifier", VerifierTestHarness.class);
        String[] filePaths = TestResources
                .testFileNames("/avrora/test/jintgen/", this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(120, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }

}
