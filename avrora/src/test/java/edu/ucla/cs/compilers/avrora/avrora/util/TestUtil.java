package edu.ucla.cs.compilers.avrora.avrora.util;

import edu.ucla.cs.compilers.avrora.avrora.arch.avr.AVRArchitecture;
import edu.ucla.cs.compilers.avrora.avrora.core.Program;
import edu.ucla.cs.compilers.avrora.avrora.sim.MockInterpreterFactory;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.avrora.sim.mcu.ATMega128;
import edu.ucla.cs.compilers.avrora.avrora.sim.mcu.Microcontroller;
import edu.ucla.cs.compilers.avrora.avrora.sim.platform.MicaZ;
import edu.ucla.cs.compilers.avrora.avrora.sim.types.SingleSimulation;

/**
 * Test Utility class
 * 
 * @author Matthias Linder
 */
public final class TestUtil
{

    private TestUtil()
    {
    }


    /**
     * Creates a default {@link Simulator} object
     * 
     * @return
     */
    public static Simulator createSimulator(Microcontroller mcu)
    {
        return new SingleSimulation().createSimulator(0,
                new MockInterpreterFactory(), mcu, null);
    }


    /**
     * Creates a program.
     * 
     * @return
     */
    public static Program createProgram()
    {
        return new Program(new AVRArchitecture(), 0, 128);
    }


    /**
     * Creates the MicaZ platform.
     * 
     * @return
     */
    public static MicaZ createMicaZ()
    {
        return (MicaZ) new MicaZ.Factory().newPlatform(0,
                new SingleSimulation(), createProgram());
    }


    /**
     * Creates an ATMega128 mcu
     * 
     * @return
     */
    public static ATMega128 createATMega128()
    {
        return (ATMega128) createMicaZ().getMicrocontroller();
    }
}
