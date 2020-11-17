package io.slc.jsm.slc_runtime;

import java.util.List;

public class Registers
{
    public void write(final int register, final int... bytes)
        throws RegistersException
    {
        throw new UnsupportedOperationException(getClass() + " - write");
    }

    public List<Integer> read(final int register)
        throws RegistersException
    {
        throw new UnsupportedOperationException(getClass() + " - read");
    }
}
