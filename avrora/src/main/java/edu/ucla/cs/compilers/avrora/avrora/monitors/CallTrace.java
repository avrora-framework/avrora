/**
 * Copyright (c) 2005, Regents of the University of California All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the
 * following disclaimer.
 * <p>
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * <p>
 * Neither the name of the University of California, Los Angeles nor the names of its contributors may be used
 * to endorse or promote products derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * Created Apr 30, 2006
 */
package edu.ucla.cs.compilers.avrora.avrora.monitors;

import edu.ucla.cs.compilers.avrora.avrora.arch.legacy.LegacyInstr;
import edu.ucla.cs.compilers.avrora.avrora.arch.legacy.LegacyRegister;
import edu.ucla.cs.compilers.avrora.avrora.arch.legacy.LegacyState;
import edu.ucla.cs.compilers.avrora.avrora.core.Program;
import edu.ucla.cs.compilers.avrora.avrora.sim.InterruptTable;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.avrora.sim.State;

/**
 * The <code>CallTrace</code> class represents a trace of the call/return and interrupt behavior of the
 * program. A call trace represents a sequence of events that represent the calls, returns, interrupts, and
 * interrupt returns of the program.
 *
 * @author Ben L. Titzer
 */
public class CallTrace {

    protected final Simulator simulator;
    protected Monitor monitor;

    /**
     * The constructor for the <code>CallTrace</code> creates a new call trace generator for the specified
     * program.
     *
     * @param sim the simulation
     */
    public CallTrace(Simulator sim) {
        simulator = sim;

        // attach probes to all appropriate instructions
        attachInstructionProbes(sim);
        // attach probes to interrupt invocation
        attachInterruptProbes(sim);
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public void attachMonitor(Monitor m) {
        monitor = m;
    }

    private void attachInterruptProbes(Simulator sim) {
        InterruptTable table = sim.getInterpreter().getInterruptTable();
        table.insertProbe(new Probe_interrupt());
    }

    private void attachInstructionProbes(Simulator sim) {
        Program p = sim.getProgram();
        for (int pc = 0; pc < p.program_end; pc = p.getNextPC(pc)) {
            LegacyInstr i = (LegacyInstr) p.readInstr(pc);

            if (i != null) {
                if (i instanceof LegacyInstr.CALL) {
                    sim.insertProbe(new Probe_call(targetOfCall(i)), pc);
                } else if (i instanceof LegacyInstr.RCALL) {
                    // skip "RCALL .+0" since this is used for compiler optimization
                    if (i.getOperands().compareTo("0") != 0) {
                        sim.insertProbe(new Probe_call(targetOfRCall(i, pc)), pc);
                    }
                } else if (i instanceof LegacyInstr.ICALL) {
                    sim.insertProbe(new Probe_icall(), pc);
                } else if (i instanceof LegacyInstr.RET) {
                    sim.insertProbe(new Probe_ret(), pc);
                } else if (i instanceof LegacyInstr.RETI) {
                    sim.insertProbe(new Probe_iret(), pc);
                }
            }
        }
    }

    private int targetOfCall(LegacyInstr i) {
        return ((LegacyInstr.CALL) i).imm1 * 2;
    }

    private int targetOfRCall(LegacyInstr i, int pc) {
        return ((LegacyInstr.RCALL) i).imm1 * 2 + pc + 2;
    }

    public interface Monitor {
        void fireBeforeCall(long time, int pc, int target);

        void fireAfterReturn(long time, int pc, int retaddr);

        void fireBeforeInterrupt(long time, int pc, int inum);

        void fireAfterInterruptReturn(long time, int pc, int retaddr);
    }

    protected class Probe_call extends Simulator.Probe.Empty {
        protected final int target;

        protected Probe_call(int tar) {
            target = tar;
        }

        @Override
        public void fireBefore(State state, int pc) {
            // generate call event
            if (monitor != null) monitor.fireBeforeCall(state.getCycles(), pc, target);
        }
    }

    protected class Probe_icall extends Simulator.Probe.Empty {
        @Override
        public void fireBefore(State state, int pc) {
            // calculate target and generate call event
            if (monitor != null) {
                LegacyState lstate = ((LegacyState) state);
                int target = 2 * lstate.getRegisterWord(LegacyRegister.Z);
                monitor.fireBeforeCall(state.getCycles(), pc, target);
            }
        }
    }

    protected class Probe_ret extends Simulator.Probe.Empty {
        @Override
        public void fireAfter(State state, int pc) {
            // generate return event
            if (monitor != null) monitor.fireAfterReturn(state.getCycles(), pc, state.getPC());
        }
    }

    protected class Probe_iret extends Simulator.Probe.Empty {
        @Override
        public void fireBefore(State state, int pc) {
            // generate interrupt return event
            if (monitor != null) monitor.fireAfterInterruptReturn(state.getCycles(), pc, state.getPC());
        }
    }

    protected class Probe_interrupt extends Simulator.InterruptProbe.Empty {
        @Override
        public void fireBeforeInvoke(State s, int inum) {
            // do nothing.
            if (monitor != null) monitor.fireBeforeInterrupt(s.getCycles(), s.getPC(), inum);
        }
    }
}
