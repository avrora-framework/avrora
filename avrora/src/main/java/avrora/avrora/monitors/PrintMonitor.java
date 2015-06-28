/**
 * Copyright (c) 2004-2005, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the University of California, Los Angeles nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package avrora.avrora.monitors;

import avrora.avrora.core.SourceMapping;
import avrora.avrora.sim.Simulator;
import avrora.avrora.sim.util.MemPrint;
import avrora.cck.text.Printer;
import avrora.cck.text.Verbose;
import avrora.cck.util.Option;
import avrora.cck.util.Util;

/**
 * The <code>PrintMonitor</code> gives apps a simple way to tell the simulator
 * to print a string or int to the screen
 *
 * @author John Regehr
 * @author Rodolfo de Paz
 */
public class PrintMonitor extends MonitorFactory
{

    protected final Option.Str VARIABLENAME = newOption("VariableName",
            "debugbuf1",
            "This option specifies the name of the variable marking the base address of "
                    + "the memory region to watch.");
    protected final Option.Str BASEADDR = newOption("base", "",
            "This option specifies the starting address in SRAM of the memory region to "
                    + "watch for instructions. (If specified, it takes precedence over VariableName).");
    protected final Option.Str MAX = newOption("max", "30",
            "This option specifies the maximum length of the data to print. It should not "
                    + "be larger than the DEBUGBUF_SIZE used in AvroraPrint.h");
    protected final Option.Str LOG = newOption("printlogfile", "",
            "If this option is specified, then each node's print statements will be "
                    + "written to <option>.#, where '#' represents the node ID.");

    static final Printer verbosePrinter = Verbose.getVerbosePrinter("c-print");

    public class Monitor implements avrora.avrora.monitors.Monitor
    {

        Monitor(Simulator s)
        {
            final int max = Integer.parseInt(MAX.get());
            int base = -1;

            if (!BASEADDR.isBlank())
            {
                // The address is given directly, so we do not need to look-up
                // the variable.
                base = Integer.parseInt(BASEADDR.get());
            } else
            {
                // Look for the label that equals the desired variable name
                // inside the map file.
                final SourceMapping map = s.getProgram().getSourceMapping();
                final SourceMapping.Location location = map
                        .getLocation(VARIABLENAME.get());
                if (location != null)
                {
                    // Strip any memory-region markers from the address.
                    base = location.vma_addr & 0xffff;
                } else
                {
                    Util.userError("c-print monitor could not find variable \""
                            + VARIABLENAME.get() + "\"");
                }
            }

            String fileName;
            if (!LOG.isBlank())
            {
                fileName = LOG.get() + s.getID();
            } else
            {
                fileName = "";
            }

            if (base != -1)
            {
                verbosePrinter.println("c-print monitor monitoring SRAM at "
                        + base + "; maximum length " + max);
                MemPrint memPrint = new MemPrint(base, max, fileName);
                s.insertWatch(memPrint, base);
            } else
            {
                verbosePrinter.println(
                        "c-print monitor not monitoring any memory region");
            }
        }


        @Override
        public void report()
        {
            // do nothing.
        }
    }


    public PrintMonitor()
    {
        super("The \"c-print\" monitor watches a dedicated range of SRAM for instructions "
                + "to print a string or int to the screen. Since it expects a special format "
                + "of this memory location use together with AvroraPrint.h");
    }


    @Override
    public avrora.avrora.monitors.Monitor newMonitor(Simulator s)
    {
        return new Monitor(s);
    }
}
