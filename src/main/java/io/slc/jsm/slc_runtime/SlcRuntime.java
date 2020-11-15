package io.slc.jsm.slc_runtime;

import io.slc.jsm.slc_interpreter.Runtime;

public class SlcRuntime implements Runtime
{
    public Registers getRegisters()
    {
        throw new UnsupportedOperationException();
    }

    public int readRegister(final int register)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
