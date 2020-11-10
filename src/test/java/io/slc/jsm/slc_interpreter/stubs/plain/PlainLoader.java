package io.slc.jsm.slc_interpreter.stubs.plain;

import io.slc.jsm.slc_interpreter.Loader;

public class PlainLoader implements Loader<PlainRuntime>
{
    public PlainRuntime load(final String... args)
    {
        final PlainRuntime runtime = new PlainRuntime();
        runtime.setExitStatus(Integer.parseInt(args[0]));
        
        return runtime;
    }
}
