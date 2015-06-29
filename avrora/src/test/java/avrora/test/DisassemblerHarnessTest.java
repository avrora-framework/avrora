package avrora.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import avrora.avrora.test.DisassemblerTestHarness;
import avrora.cck.test.TestEngine;
import avrora.cck.text.Terminal;
import avrora.cck.util.ClassMap;

public class DisassemblerHarnessTest
{

    @Test
    public void testDisassemblerHarness_usingAllTstFiles_expectAllPass()
            throws Exception
    {
        Terminal.useColors = false;
        ClassMap harnessMap = new ClassMap("Test Harness",
                TestEngine.Harness.class);
        harnessMap.addClass("disassembler", DisassemblerTestHarness.class);
        String[] filePaths = TestResources
                .testFileNamesEndingWith("/avrora/test/disassembler/", "tst", this)
                .toArray(new String[0]);
        TestEngine testSuite = new TestEngine(harnessMap);

        assertEquals(120, filePaths.length);
        assertTrue(testSuite.runTests(filePaths));
        assertEquals(filePaths.length, testSuite.successes.size());
    }

}
