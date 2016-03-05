package edu.ucla.cs.compilers.avrora.avrora.sim;

import edu.ucla.cs.compilers.avrora.avrora.core.Program;
import edu.ucla.cs.compilers.avrora.avrora.sim.Interpreter;
import edu.ucla.cs.compilers.avrora.avrora.sim.InterpreterFactory;
import edu.ucla.cs.compilers.avrora.avrora.sim.Simulator;
import edu.ucla.cs.compilers.avrora.avrora.sim.mcu.MCUProperties;

/**
 * Mock for the {@link InterpreterFactory} class.
 * 
 * @author Matthias Linder
 */
public class MockInterpreterFactory extends InterpreterFactory
{

    public Interpreter interpreter;


    @Override
    public Interpreter newInterpreter(Simulator s, Program p, MCUProperties pr)
    {
        return interpreter;
    }

}
