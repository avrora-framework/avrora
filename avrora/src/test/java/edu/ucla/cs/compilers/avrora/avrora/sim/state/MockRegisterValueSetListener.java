package edu.ucla.cs.compilers.avrora.avrora.sim.state;

import edu.ucla.cs.compilers.avrora.avrora.sim.state.RegisterView;

/**
 * Mocked RegisterValueSetListener
 * 
 * @author Matthias Linder
 */
public class MockRegisterValueSetListener
        implements RegisterView.RegisterValueSetListener
{

    public int called;
    public int oldValue;
    public int newValue;


    @Override
    public void onValueSet(RegisterView view, int oldValue, int newValue)
    {
        called++;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
