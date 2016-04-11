/**
 * Copyright (c) 2004-2005, Regents of the University of California All rights reserved.
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
 */

package edu.ucla.cs.compilers.avrora.avrora.sim.clock;

import edu.ucla.cs.compilers.avrora.avrora.actions.SimAction;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulation;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.cck.text.StringUtil;
import edu.ucla.cs.compilers.avrora.cck.text.Terminal;
import edu.ucla.cs.compilers.avrora.cck.util.Util;

/**
 * The <code>StepSynchronizer</code> class is an implementation of simulator synchronization that steps each
 * node one cycle at a time using the <code>Simulator.step()</code> method of each simulator.
 *
 * @author Ben L. Titzer
 */
public class StepSynchronizer extends Synchronizer {

    protected final Simulator.Event action;
    protected Simulator[] threads;
    protected int numThreads;
    protected boolean shouldRun;
    protected boolean innerLoop;
    protected RunThread thread;

    /**
     * The constructor for the <code>StepSynchronizer</code> class creates a new instance of this
     * synchronizer. The event passed as a parameter will be fired after each iteration; i.e. when all nodes
     * have been stepped one clock cycle.
     *
     * @param e the event to fire after each step of the simulation
     */
    public StepSynchronizer(Simulator.Event e) {
        action = e;
        threads = new Simulator[8];
    }

    /**
     * The <code>addNode()</code> method adds a node to this synchronization group. This method should only be
     * called before the <code>start()</code> method is called.
     *
     * @param n the simulator representing the node to add to this group
     */
    @Override
    public void addNode(Simulation.Node n) {
        int nn = numThreads++;
        if (nn >= threads.length) {
            Simulator[] nthreads = new Simulator[threads.length * 2];
            System.arraycopy(threads, 0, nthreads, 0, threads.length);
            threads = nthreads;
        }
        threads[nn] = n.getSimulator();
    }

    /**
     * The <code>removeNode()</code> method removes a node from this synchronization group, and wakes any
     * nodes that might be waiting on it.
     *
     * @param n the simulator thread to remove from this synchronization group
     */
    @Override
    public void removeNode(Simulation.Node n) {
        removeSimulator(n.getSimulator());
    }

    /**
     * The <code>waitForNeighbors()</code> method is called from within the execution of a node when that node
     * needs to wait for its neighbors to catch up to it in execution time. The node will be blocked until the
     * other nodes in other threads catch up in global time.
     */
    @Override
    public void waitForNeighbors(long time) {
        throw Util.unimplemented();
    }

    /**
     * The <code>start()</code> method starts the threads executing, and the synchronizer will add whatever
     * synchronization to their execution that is necessary to preserve the global timing properties of
     * simulation.
     */
    @Override
    public void start() {
        thread = new RunThread();
        thread.start();
    }

    /**
     * The <code>join()</code> method will block the caller until all of the threads in this synchronization
     * interval have terminated, either through <code>stop()</code> being called, or terminating normally such
     * as through a timeout.
     */
    @Override
    public void join() throws InterruptedException {
        if (thread != null) thread.join();
    }

    /**
     * The <code>pause()</code> method temporarily pauses the simulation. The nodes are not guaranteed to stop
     * at the same global time. This method will return when all threads in the simulation have been paused
     * and will no longer make progress until the <code>start()</code> method is called again.
     */
    @Override
    public void pause() {
        if (thread != null) thread.pause();
    }

    /**
     * The <code>stop()</code> method will terminate all the simulation threads. It is not guaranteed to stop
     * all the simulation threads at the same global time.
     */
    @Override
    public void stop() {
        if (thread == null) return;
        shouldRun = false;
        innerLoop = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // do nothing.
        }
    }

    void reportExit(Simulator s, Throwable t) {
        throw Util.unimplemented();
    }

    /**
     * report an exception to command line
     */
    private void reportException(Throwable thrown) {
        try {
            if (thrown != null) throw thrown;
        } catch (SimAction.TimeoutException e) {
            Terminal.printYellow("Simulation terminated");
            Terminal.println(": timeout reached at pc = " + StringUtil.addrToString(e.address) + ", time = " +
                    "" + e.state.getCycles());
        } catch (Throwable t) {
            Terminal.printRed("Simulation terminated with unexpected exception");
            Terminal.print(": ");
            t.printStackTrace();
        }
    }

    void removeSimulator(Simulator s) {
//        throw Util.unimplemented();
        s.stop();
    }

    /**
     * The <code>synch()</code> method will pause all of the nodes at the same global time. This method can
     * only be called when the simulation is paused. It will run all threads forward until the global time
     * specified and pause them.
     *
     * @param globalTime the global time in clock cycles to run all threads ahead to
     */
    @Override
    public void synch(long globalTime) {
        throw Util.unimplemented();
    }

    /**
     * The <code>RunThread</code> class implements a thread that runs the simulation, to preserve the model
     * that the thread interacting with the synchronizer through calls to <code>start()</code>,
     * <code>stop()</code>, etc. is different than any of the threads running actual simulator code.
     */
    protected class RunThread extends Thread {
        @Override
        public void run() {
            shouldRun = true;
            runLoop();
        }

        protected void runLoop() {
            int[] cycles = new int[numThreads];
            while (shouldRun) {
                fastLoop(cycles);
            }
        }

        private void fastLoop(int[] cycles) {
            innerLoop = true;
            while (innerLoop) step(cycles, threads);
        }

        protected void step(int[] cycles, Simulator[] threads) {
            for (int cntr = 0; cntr < numThreads; cntr++) {
                int left = --cycles[cntr];
                if (left <= 0) {
                    Simulator sim = threads[cntr];
                    try {
                        cycles[cntr] = sim.step();
                    } catch (Throwable t) {
                        reportException(t);
                        removeSimulator(threads[cntr]);
                        if (!(t instanceof SimAction.TimeoutException)) {
                            reportExit(sim, t);
                        }
                        stop();
                    }
                }
            }

            // Execute the event if there is one.
            if (action != null) action.fire();
        }

        public void pause() {
            throw Util.unimplemented();
        }
    }
}
