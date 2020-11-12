package io.slc.jsm.slc_interpreter.stubs.plain;

import io.slc.jsm.slc_interpreter.Loader;

public class PlainLoader implements Loader<PlainRuntime>
{
    @Override
    public PlainRuntime load(final String... args)
    {
        return new PlainRuntime(Integer.parseInt(args[0]));
    }
}
