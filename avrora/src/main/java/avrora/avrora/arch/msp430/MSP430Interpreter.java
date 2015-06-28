/**
 * Copyright (c) 2005, Regents of the University of California
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
 *
 * Creation date: Nov 11, 2005
 */

package avrora.avrora.arch.msp430;

import avrora.avrora.core.Program;
import avrora.avrora.sim.Interpreter;
import avrora.avrora.sim.InterpreterFactory;
import avrora.avrora.sim.InterruptTable;
import avrora.avrora.sim.Simulator;
import avrora.avrora.sim.State;
import avrora.avrora.sim.mcu.MCUProperties;
import avrora.avrora.sim.mcu.RegisterSet;
import avrora.avrora.sim.util.MulticastProbe;
import avrora.avrora.sim.util.SimUtil;
import avrora.cck.text.StringUtil;
import avrora.cck.util.Util;

/**
 * @author Ben L. Titzer
 */
public class MSP430Interpreter extends MSP430InstrInterpreter
{

    public static Factory FACTORY = new Factory();

    public static class Factory extends InterpreterFactory
    {
        /**
         * The <code>newInterpreter()</code> method creates a new interpreter
         * given the simulator, the program, and the properties of the
         * microcontroller.
         * 
         * @param s
         *            the simulator for which the interpreter is being created
         * @param p
         *            the program to load into the interpreter
         * @param pr
         *            the properties of the microcontroller
         * @return a new instance of the <code>BaseInterpreter</code> class for
         *         the program
         */
        @Override
        public Interpreter newInterpreter(Simulator s, Program p,
                MCUProperties pr)
        {
            return new MSP430Interpreter(s, p, (MSP430Properties) pr);
        }
    }

    protected final RegisterSet registers;
    protected final MSP430Instr[] shared_instr;
    protected final STOP_instr STOP;
    protected boolean shouldRun;
    protected boolean sleeping;

    protected MulticastProbe globalProbe;


    /**
     * The constructor for the <code>AVRInterpreter</code> class creates a new
     * interpreter and initializes all of the state. This includes allocating
     * memory and segments to represent the SRAM, flash, interrupt table, IO
     * registers, etc.
     */
    public MSP430Interpreter(Simulator simulator, Program p,
            MSP430Properties pr)
    {
        super(simulator);
        // this class and its methods are performance critical
        // observed speedup with this call on Hotspot
        Compiler.compileClass(this.getClass());

        // if program will not fit onto hardware, error
        if (p.program_end > MSP430DataSegment.DATA_SIZE)
            throw Util.failure("program will not fit into "
                    + MSP430DataSegment.DATA_SIZE + " bytes");

        // allocate register file
        regs = new char[NUM_REGS];

        // initialize IO registers to default values
        registers = simulator.getMicrocontroller().getRegisterSet();

        // allocate SRAM
        data = new MSP430DataSegment(pr.sram_size, pr.code_start,
                registers.share(), this);

        // initialize the interrupt table
        interrupts = new InterruptTable(this, pr.num_interrupts);

        // share the instruction array
        shared_instr = data.shareInstr();

        // load the program into the data segment
        data.loadProgram(p);

        // set up the initial program counter
        pc = pr.code_start;

        globalProbe = new MulticastProbe();

        STOP = new STOP_instr();
    }


    protected void runLoop()
    {
        while (shouldRun)
        {
            if (globalProbe.isEmpty())
                fastLoop();
            else
                instrumentedLoop();
        }
    }


    private void fastLoop()
    {
        innerLoop = true;
        while (innerLoop)
        {
            int curpc = pc;
            execute(fetch(curpc));
        }
    }


    private void instrumentedLoop()
    {
        innerLoop = true;
        while (innerLoop)
        {
            int curpc = pc;
            globalProbe.fireBefore(this, curpc);
            execute(fetch(curpc));
            globalProbe.fireAfter(this, curpc);
        }
    }


    private MSP430Instr fetch(int curpc)
    {
        MSP430Instr i = shared_instr[curpc];
        if (i == null)
        {
            SimUtil.warning(simulator, StringUtil.to0xHex(curpc, 4),
                    "invalid instruction");
            i = STOP;
        }
        regs[PC_REG] = (char) (curpc + 2);
        nextpc = curpc + i.getSize();
        return i;
    }


    private void execute(MSP430Instr i)
    {
        i.accept(this);
        pc = regs[PC_REG] = (char) nextpc;
        clock.advance(1);
    }


    @Override
    protected void bumpPC()
    {
        regs[PC_REG] += 2;
    }


    @Override
    protected int bit(boolean b)
    {
        return b ? 1 : 0;
    }


    @Override
    protected int popByte()
    {
        int sp = getSP();
        byte b = data.read(sp);
        regs[SP_REG] = (char) (sp + 1);
        return b;
    }


    @Override
    protected void pushByte(int b)
    {
        int sp = getSP();
        int nsp = sp - 1;
        data.write(nsp, (byte) b);
        regs[SP_REG] = (char) nsp;
    }


    @Override
    protected void disableInterrupts()
    {

    }


    @Override
    protected void enableInterrupts()
    {

    }


    @Override
    protected int popWord()
    {
        byte b1 = (byte) popByte();
        byte b2 = (byte) popByte();
        return uword(b1, b2);
    }


    @Override
    protected void pushWord(int b)
    {
        int sp = getSP();
        int nsp = sp - 2;
        data.write(nsp, (byte) b);
        data.write(nsp + 1, (byte) (b >> 8));
        regs[SP_REG] = (char) nsp;
    }


    @Override
    public State getState()
    {
        return this;
    }


    @Override
    public void start()
    {
        shouldRun = true;
        runLoop();
    }


    @Override
    public int step()
    {
        throw Util.unimplemented();
    }


    @Override
    public void stop()
    {
        shouldRun = false;
        innerLoop = false;
    }


    /**
     * The <code>insertProbe()</code> method is used internally to insert a
     * probe on a particular instruction.
     * 
     * @param p
     *            the probe to insert on an instruction
     * @param addr
     *            the address of the instruction on which to insert the probe
     */
    @Override
    protected void insertProbe(Simulator.Probe p, int addr)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>insertExceptionWatch()</code> method registers an </code>
     * ExceptionWatch</code> to listen for exceptional conditions in the
     * machine.
     *
     * @param watch
     *            The <code>ExceptionWatch</code> instance to add.
     */
    @Override
    protected void insertErrorWatch(Simulator.Watch watch)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>insertProbe()</code> method allows a probe to be inserted that
     * is executed before and after every instruction that is executed by the
     * simulator
     *
     * @param p
     *            the probe to insert
     */
    @Override
    protected void insertProbe(Simulator.Probe p)
    {
        globalProbe.add(p);
        innerLoop = false;
    }


    /**
     * The <code>removeProbe()</code> method is used internally to remove a
     * probe from a particular instruction.
     * 
     * @param p
     *            the probe to remove from an instruction
     * @param addr
     *            the address of the instruction from which to remove the probe
     */
    @Override
    protected void removeProbe(Simulator.Probe p, int addr)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>removeProbe()</code> method removes a probe from the global
     * probe table (the probes executed before and after every instruction). The
     * comparison used is reference equality, not <code>.equals()</code>.
     *
     * @param b
     *            the probe to remove
     */
    @Override
    public void removeProbe(Simulator.Probe b)
    {
        globalProbe.remove(b);
    }


    /**
     * The <code>insertWatch()</code> method is used internally to insert a
     * watch on a particular memory location.
     * 
     * @param p
     *            the watch to insert on a memory location
     * @param data_addr
     *            the address of the memory location on which to insert the
     *            watch
     */
    @Override
    protected void insertWatch(Simulator.Watch p, int data_addr)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>removeWatch()</code> method is used internally to remove a
     * watch from a particular memory location.
     * 
     * @param p
     *            the watch to remove from the memory location
     * @param data_addr
     *            the address of the memory location from which to remove the
     *            watch
     */
    @Override
    protected void removeWatch(Simulator.Watch p, int data_addr)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>delay()</code> method is used to add some delay cycles before
     * the next instruction is executed. This is necessary because some devices
     * such as the EEPROM actually delay execution of instructions while they
     * are working
     * 
     * @param cycles
     *            the number of cycles to delay the execution
     */
    @Override
    protected void delay(long cycles)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>setRegister()</code> method reads a general purpose register's
     * current value as a byte.
     *
     * @param reg
     *            the register to read
     * @param val
     *            the character value to write to the register
     */
    public void setRegister(MSP430Symbol.GPR reg, char val)
    {
        regs[reg.value] = val;
    }


    /**
     * The <code>setData()</code> method sets the value of the data segment at
     * the specified address.
     *
     * @param address
     *            the address at which to write the memory
     * @param val
     *            the character value to write to the register
     */
    public void setData(int address, char val)
    {
        data.set(address, (byte) val);
        data.set(address + 1, (byte) (val >> 8));
    }

    class STOP_instr extends MSP430Instr
    {

        STOP_instr()
        {
            super("stop", 0);
        }


        /**
         * The <code>accept()</code> method accepts an instruction visitor and
         * calls the appropriate <code>visit()</code> method for this
         * instruction.
         * 
         * @param v
         *            the instruction visitor to accept
         */
        @Override
        public void accept(MSP430InstrVisitor v)
        {
            stop();
        }


        /**
         * The <code>accept()</code> method accepts an addressing mode visitor
         * and calls the appropriate <code>visit_*()</code> method for this
         * instruction's addressing mode.
         * 
         * @param v
         *            the addressing mode visitor to accept
         */
        @Override
        public void accept(MSP430AddrModeVisitor v)
        {
            // the default implementation of accept() is empty
        }

    }
}
