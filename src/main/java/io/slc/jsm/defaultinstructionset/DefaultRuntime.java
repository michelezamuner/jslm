package io.slc.jsm.defaultinstructionset;

import io.slc.jsm.defaultinterpreter.Runtime;

public class DefaultRuntime implements Runtime
{
    public boolean shouldExit()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getExitStatus()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean shouldJump()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getJumpAddress()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
