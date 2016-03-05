package edu.ucla.cs.compilers.avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ucla.cs.compilers.avrora.avrora.test.sim.SimTestHarness;
import edu.ucla.cs.compilers.avrora.cck.test.TestEngine;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;
import edu.ucla.cs.compilers.avrora.cck.util.ClassMap;

public class TimersTestHarnessTest
{
    @Before
    public void disableColors()
    {
        Terminal.useColors = false;
    }


    @Test
    public void testTimers_usingAllTstFiles_expectAllPass() throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("simulator", SimTestHarness.class);
        String[] filePaths = TestResources.testFileNamesEndingWith(
                "/edu/ucla/cs/compilers/avrora/test/timers/", "tst", this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(4, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(4, testSuite.successes.size());
    }
}
