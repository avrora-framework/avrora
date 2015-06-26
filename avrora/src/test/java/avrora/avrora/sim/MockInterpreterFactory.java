package avrora.avrora.sim;

import avrora.avrora.core.Program;
import avrora.avrora.sim.Interpreter;
import avrora.avrora.sim.InterpreterFactory;
import avrora.avrora.sim.Simulator;
import avrora.avrora.sim.mcu.MCUProperties;

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
