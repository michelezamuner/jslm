package io.slc.jsm.slc_interpreter.stubs.plain;

import io.slc.jsm.slc_interpreter.Runtime;

public class PlainRuntime implements Runtime
{
    private final int exitStatus;

    PlainRuntime(final int exitStatus)
    {
        this.exitStatus = exitStatus;
    }

    public int getExitStatus()
    {
        return exitStatus;
    }
}
