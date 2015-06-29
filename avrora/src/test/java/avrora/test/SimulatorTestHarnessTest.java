package avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import avrora.avrora.test.SimplifierTestHarness;
import avrora.avrora.test.sim.SimTestHarness;
import avrora.cck.test.TestEngine;
import avrora.cck.text.Status;
import avrora.cck.text.Terminal;
import avrora.cck.util.ClassMap;

public class SimulatorTestHarnessTest
{
    @Test
    public void testSimulatorHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        Terminal.useColors = false;
        Status.ENABLED = true;
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("simulator", SimTestHarness.class);
        harnessMap.addClass("simplifier", SimplifierTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesEndingWith("/avrora/test/interpreter/", ".tst",
                        this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(310, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(310, testSuite.successes.size());
        assertTrue(false);
    }

}