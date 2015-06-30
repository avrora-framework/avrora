package avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import avrora.avrora.test.InterruptTestHarness;
import avrora.cck.test.TestEngine;
import avrora.cck.text.Terminal;
import avrora.cck.util.ClassMap;

public class InterruptTestHarnessTest
{
    @Before
    public void disableColors()
    {
        Terminal.useColors = false;
    }


    @Test
    public void testInterruptHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("interrupt", InterruptTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesEndingWith("/avrora/test/interrupts/", ".tst",
                        this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertTrue(testSuite.runTests(filePaths));
        assertEquals(1, testSuite.successes.size());
        assertEquals(1, filePaths.length);
    }
}
