package edu.ucla.cs.compilers.avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ucla.cs.compilers.avrora.avrora.test.DisassemblerTestHarness;
import edu.ucla.cs.compilers.avrora.cck.test.TestEngine;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;
import edu.ucla.cs.compilers.avrora.cck.util.ClassMap;

public class DisassemblerTestHarnessTest
{
    @Before
    public void disableColors()
    {
        Terminal.useColors = false;
    }


    @Test
    public void testDisassemblerHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("disassembler", DisassemblerTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesEndingWith("/edu/ucla/cs/compilers/avrora/test/disassembler/", "tst",
                        this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(120, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }

}
