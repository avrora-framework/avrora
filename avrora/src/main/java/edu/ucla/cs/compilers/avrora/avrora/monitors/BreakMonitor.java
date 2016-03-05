/**
 * Copyright (c) 2004-2005, Regents of the University of California All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * <p>
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * <p>
 * Neither the name of the University of California, Los Angeles nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.ucla.cs.compilers.avrora.avrora.monitors;

import edu.ucla.cs.compilers.avrora.avrora.arch.AbstractInstr;
import edu.ucla.cs.compilers.avrora.avrora.arch.legacy.LegacyInstr;
import edu.ucla.cs.compilers.avrora.avrora.arch.legacy.LegacyRegister;
import edu.ucla.cs.compilers.avrora.avrora.arch.legacy.LegacyState;
import edu.ucla.cs.compilers.avrora.avrora.core.Program;
import edu.ucla.cs.compilers.avrora.avrora.core.SourceMapping;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.avrora.sim.State;
import edu.ucla.cs.compilers.avrora.avrora.sim.output.SimPrinter;
import edu.ucla.cs.compilers.avrora.cck.text.StringUtil;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;

/**
 * The <code>Break</code> monitor prints a stack trace when the program being simulated executes a break instruction.
 * Breaks are are good implementation for assertion failures and similar.
 *
 * @author Reet D. Dhakal
 * @author John Regehr
 * @author Ben L. Titzer
 */
public class BreakMonitor extends MonitorFactory {

    public BreakMonitor() {
        super("The \"break\" monitor watches for execution of an AVR break " + "instruction, which can be used to " +
                "implement things like assertion " + "failures.  When a break is executed the simulator prints a " +
                "stack " + "trace.");
    }

    @Override
    public Monitor newMonitor(Simulator s) {
        return new Mon(s);
    }

    public static class Mon implements Monitor {
        public final Simulator simulator;
        public final SimPrinter printer;
        public final CallTrace trace;
        public final CallStack stack;
        private final SourceMapping sourceMap;

        Mon(Simulator s) {
            simulator = s;
            printer = s.getPrinter();
            trace = new CallTrace(s);
            stack = new CallStack();
            trace.attachMonitor(stack);

            Program p = s.getProgram();
            sourceMap = p.getSourceMapping();
            for (int pc = 0; pc < p.program_end; pc = p.getNextPC(pc)) {
                AbstractInstr i = p.readInstr(pc);
                if (i != null && i instanceof LegacyInstr.BREAK)
                    s.insertProbe(new BreakProbe(simulator, stack, sourceMap), pc);
            }
        }

        @Override
        public void report() {
            // do nothing
        }

        public static class BreakProbe extends Simulator.Probe.Empty {

            private final Simulator simulator;
            private final CallStack stack;
            private final SourceMapping sourceMap;

            public BreakProbe(Simulator sim, CallStack stack, SourceMapping sourceMap) {
                simulator = sim;
                this.stack = stack;
                this.sourceMap = sourceMap;
            }

            @Override
            public void fireBefore(State state, int pc) {
                LegacyState s = (LegacyState) simulator.getState();

                StringBuffer buf = simulator.getPrinter().getBuffer();
                buf.append("break instruction @ ");
                Terminal.append(Terminal.COLOR_CYAN, buf, StringUtil.addrToString(pc));
                buf.append(", r30:r31 = ");
                int v = s.getRegisterWord(LegacyRegister.getRegisterByNumber(30));
                Terminal.append(Terminal.COLOR_GREEN, buf, StringUtil.to0xHex(v, 4));
                simulator.getPrinter().printBuffer(buf);

                stack.printStack(simulator.getPrinter(), sourceMap);
            }
        }
    }
}
