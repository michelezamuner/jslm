package io.slc.jsm.slc_interpreter.stubs;

import io.slc.jsm.slc_interpreter.runtime.Loader;
import io.slc.jsm.slc_interpreter.runtime.Runtime;

public class StubLoader implements Loader
{
    private int instructionSize;

    public StubLoader(final int instructionSize)
    {
        this.instructionSize = instructionSize;
    }

    public Runtime load(final String... args)
    {
        return new StubRuntime(instructionSize, args);
    }
}
