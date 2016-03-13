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

package edu.ucla.cs.compilers.avrora.avrora.sim.platform;

import edu.ucla.cs.compilers.avrora.avrora.sim.FiniteStateMachine;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.avrora.sim.clock.Clock;
import edu.ucla.cs.compilers.avrora.avrora.sim.mcu.ATMegaFamily;
import edu.ucla.cs.compilers.avrora.avrora.sim.mcu.Microcontroller;
import edu.ucla.cs.compilers.avrora.avrora.sim.output.SimPrinter;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>PinWire</code> class is the interface for making wire connections
 * to other microcontrollers.
 *
 * @author Jacob Everist
 */

public class PinWire {

    // names of the states of this device
    private static final String[] modeName = {"low", "high"};
    // default mode of the device is off
    private static final int startMode = 0;
    protected final FiniteStateMachine state;
    protected final int colorNum;
    protected final String pinName;
    protected final ATMegaFamily atmel;
    // probe of the PinWire activity
    protected final PinWireProbe probe;
    // propagation delay in cycles
    protected final long propDelay;
    // input/output connectors
    public WireInput wireInput;
    public WireOutput wireOutput;
    protected Simulator sim;
    // whether the microcontroller can read this as input
    private boolean acceptsInput;
    // whether the microcontroller can write this as output
    private boolean acceptsOutput;
    private Map<FiniteStateMachine.Probe, Boolean> externalProbes = new HashMap<FiniteStateMachine.Probe, Boolean>();
    private boolean isConnectDisabled = true;

    protected PinWire(Simulator s, int colorNum, String pinName) {

        sim = s;
        Clock clock = sim.getClock();
        state = new FiniteStateMachine(clock, startMode, modeName, 0);
        wireOutput = new WireOutput();
        wireInput = new WireInput();

        acceptsInput = false;
        acceptsOutput = false;

        probe = new PinWireProbe();

        this.colorNum = colorNum;
        this.pinName = pinName;
        atmel = null;

        propDelay = clock.millisToCycles(0.0014);
    }

    protected PinWire(Simulator s, int colorNum, String pinName, Microcontroller mcu) {

        sim = s;
        Clock clock = sim.getClock();
        state = new FiniteStateMachine(clock, startMode, modeName, 0);
        wireOutput = new WireOutput();
        wireInput = new WireInput();

        acceptsInput = false;
        acceptsOutput = false;

        probe = new PinWireProbe();

        this.colorNum = colorNum;
        this.pinName = pinName;
        atmel = (ATMegaFamily) mcu;
        propDelay = clock.millisToCycles(0.0014);
    }

    public String readName() {
        return pinName;
    }

    public void enableConnect() {
        isConnectDisabled = false;

        state.insertProbe(probe);
        for (Map.Entry<FiniteStateMachine.Probe, Boolean> entry : externalProbes.entrySet()) {
            if (entry.getValue() == false) {
                state.insertProbe(entry.getKey());
                entry.setValue(true);
            }
        }
    }

    /**
     * Register a probe to this {@link PinWire}'s {@link FiniteStateMachine}.
     *
     * @param pinWireProbe the pin wire probe to register
     */
    public void insertProbe(FiniteStateMachine.Probe pinWireProbe) {
        if (externalProbes.containsKey(pinWireProbe)) return;

        if (!isConnectDisabled) {
            externalProbes.put(pinWireProbe, true);
            state.insertProbe(pinWireProbe);
        } else {
            externalProbes.put(pinWireProbe, false);
        }
    }

    /**
     * remove a probe from this {@link PinWire}'s {@link FiniteStateMachine}.
     *
     * @param pinWireProbe the pin wire probe
     */
    public void removeProbe(FiniteStateMachine.Probe pinWireProbe) {
        if (!externalProbes.containsKey(pinWireProbe)) return;

        state.removeProbe(pinWireProbe);
        externalProbes.remove(pinWireProbe);
    }

    public void disableConnect() {
        isConnectDisabled = true;

        state.removeProbe(probe);
        for (Map.Entry<FiniteStateMachine.Probe, Boolean> entry : externalProbes.entrySet()) {
            if (entry.getValue() == true) {
                state.removeProbe(entry.getKey());
                entry.setValue(false);
            }
        }
    }

    public boolean inputReady() {
        return acceptsInput;
    }

    public boolean outputReady() {
        return acceptsOutput;
    }

    /**
     * The <code>PinWireProbe</code> class implements a probe from the (tiny)
     * finite state machine that represents an PinWire's state. An PinWire can
     * be "off" or "on". This probe will simply display changes to the state.
     */
    class PinWireProbe implements FiniteStateMachine.Probe {
        final SimPrinter printer;

        PinWireProbe() {
            printer = sim.getPrinter();
        }

        @Override
        public void fireBeforeTransition(int beforeState, int afterState) {
            // do nothing
        }

        @Override
        public void fireAfterTransition(int beforeState, int afterState) {
            if (beforeState == afterState) return;

            // print the status of the PinWire
            StringBuffer buf = printer.getBuffer(20);
            Terminal.append(colorNum, buf, pinName);
            buf.append(": ");
            buf.append(modeName[afterState]);
            printer.printBuffer(buf);

            wireInput.onStateChange(afterState == 1);
        }
    }

    class WireInput extends Microcontroller.Pin.ListenableInput {

        /**
         * Constructor
         */
        protected WireInput() {
        }

        /**
         * The <code>enableInput()</code> method is called by the simulator when
         * the program changes the direction of the pin. The device connected to
         * this pin can then take action accordingly.
         */
        public void enableInput() {
            acceptsInput = true;

            // automatically disable output
            acceptsOutput = false;
        }

        /**
         * The <code>read()</code> method is called by the simulator when the
         * program attempts to read the level of the pin. The device can then
         * compute and return the current level of the pin.
         *
         * @return true if the level of the pin is high; false otherwise
         */
        @Override
        public boolean read() {
            // read the current state and return boolean value
            return state.getCurrentState() == 1;
        }

        /**
         * Called when the state of the pinwire has changed
         */
        public void onStateChange(boolean newState) {
            notifyListeners(newState);
        }
    }

    class WireOutput implements Microcontroller.Pin.Output {

        /**
         * Constructor
         */
        protected WireOutput() {
        }

        /**
         * The <code>enableOutput()</code> method is called by the simulator
         * when the program changes the direction of the pin. The device
         * connected to this pin can then take action accordingly.
         */
        public void enableOutput() {
            acceptsOutput = true;

            // automatically disable input
            acceptsInput = false;
        }

        /**
         * The <code>write()</code> method is called by the simulator when the
         * program writes a logical level to the pin. The device can then take
         * the appropriate action.
         *
         * @param level a boolean representing the logical level of the write
         */
        @Override
        public void write(boolean level) {

            // propagate signal after 1.4 uS =
            // sim.insertEvent(new WirePropagationEvent(level), propDelay);

            if (level) state.transition(1);
            else state.transition(0);
        }

        protected class WirePropagationEvent implements Simulator.Event {
            private boolean value;

            public WirePropagationEvent(boolean value) {
                this.value = value;
            }

            // propagate signal to the pin finally
            @Override
            public void fire() {
                if (value) state.transition(1);
                else state.transition(0);
            }
        }
    }
}
