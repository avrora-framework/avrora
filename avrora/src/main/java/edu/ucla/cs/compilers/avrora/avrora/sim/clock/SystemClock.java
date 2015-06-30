package edu.ucla.cs.compilers.avrora.avrora.sim.clock;

import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.cck.util.Util;

/**
 * The <code>SystemClock</code> class represents a wrapper around the system
 * clock that measures actual wall clock time passed in simulation. This
 * implementation encapsulates the <code>System.currentTimeMillis()</code>
 * method, and thus provides a precision of approximately 1000 cycles per
 * second.
 *
 * @author Ben L. Titzer
 */
public class SystemClock extends Clock
{

    private static final SystemClock instance = new SystemClock();


    private SystemClock()
    {
        super("system", 1000);
    }


    /**
     * The <code>get()</code> method retrieves the singleton instance of the
     * system clock.
     * 
     * @return an instance of the <code>SystemClock</code> class
     */
    public static SystemClock get()
    {
        return instance;
    }


    /**
     * The <code>getCount()</code> method returns the number of clock cycles
     * (ticks) that have elapsed for this clock.
     *
     * @return the number of elapsed time ticks in clock cycles
     */
    @Override
    public long getCount()
    {
        return System.currentTimeMillis();
    }


    /**
     * The <code>insertEvent()</code> method inserts an event into the event
     * queue of the clock with the specified delay in clock cycles. The event
     * will then be executed at the future time specified.
     *
     * @param e
     *            the event to be inserted
     * @param cycles
     *            the number of cycles in the future at which to fire
     */
    @Override
    public void insertEvent(Simulator.Event e, long cycles)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>removeEvent()</code> method removes an event from the event
     * queue of the clock. The comparison used is reference equality, not
     * <code>.equals()</code>.
     *
     * @param e
     *            the event to remove
     */
    @Override
    public void removeEvent(Simulator.Event e)
    {
        throw Util.unimplemented();
    }


    /**
     * The <code>getFirstEventDelta()</code> method returns the number of clock
     * cycles until the first event in the event queue will fire. This method
     * will return -1 if there are no events in the queue.
     * 
     * @return the delta in clock cycles of the first event in the queue; -1 if
     *         there are no events in the queue
     */
    public long getFirstEventDelta()
    {
        throw Util.unimplemented();
    }
}
