package io.slc.jsm.slc_runtime;

import io.slc.jsm.slc_interpreter.Runtime;

public class SlcRuntime implements Runtime
{
    private final Registers registers;

    public SlcRuntime(final Registers registers)
    {
        this.registers = registers;
    }

    public Registers getRegisters()
    {
        return registers;
    }
}
