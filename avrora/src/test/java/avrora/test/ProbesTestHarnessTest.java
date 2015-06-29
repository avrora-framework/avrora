package avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import avrora.avrora.test.ProbeTestHarness;
import avrora.cck.test.TestEngine;
import avrora.cck.text.Status;
import avrora.cck.text.Terminal;
import avrora.cck.util.ClassMap;

public class ProbesTestHarnessTest
{
    @Test
    public void testProbesHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        Terminal.useColors = false;
        Status.ENABLED = true;
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("probes", ProbeTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesEndingWith("/avrora/test/probes/", "tst", this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(12, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(12, testSuite.successes.size());
    }
}
