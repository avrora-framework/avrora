package avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import avrora.avrora.test.InterruptTestHarness;
import avrora.cck.test.TestEngine;
import avrora.cck.util.ClassMap;

public class InterruptHarnessTest
{
    @Test
    public void testDisassemblerHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("interrupt", InterruptTestHarness.class);
        String[] filePaths = TestResources
                .testFileNames("/avrora/test/interrupts/", this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertTrue(1 <= filePaths.length);

        for (String tstFile : filePaths)
        {
            if (tstFile.endsWith(".tst"))
            {
                assertTrue(testSuite.runTests(new String[] { tstFile }));
                assertEquals(1, testSuite.successes.size());
            }
        }
        assertTrue(false);
    }
}
