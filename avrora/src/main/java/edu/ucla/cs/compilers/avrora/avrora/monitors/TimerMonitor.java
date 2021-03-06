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

package edu.ucla.cs.compilers.avrora.avrora.monitors;

import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.avrora.sim.util.MemTimer;

/**
 * The <code>TimerMonitor</code> gives apps access to a simple start/stop timer
 * interface
 *
 * @author John Regehr
 */
public class TimerMonitor extends MonitorFactory
{

    public class Monitor implements edu.ucla.cs.compilers.avrora.avrora.monitors.Monitor
    {
        public final MemTimer memprofile;
        private static final int BASE = 2999;


        Monitor(Simulator s)
        {
            memprofile = new MemTimer(BASE);
            s.insertWatch(memprofile, BASE);
        }


        @Override
        public void report()
        {
            // do nothing.
        }
    }


    public TimerMonitor()
    {
        super("The \"timer\" monitor watches a dedicated SRAM location for instructions "
                + "from the simulated program telling it to start or stop a timer.  Be sure to "
                + "set BASE to an address not otherwise used by your program.");
    }


    @Override
    public edu.ucla.cs.compilers.avrora.avrora.monitors.Monitor newMonitor(Simulator s)
    {
        return new Monitor(s);
    }
}
