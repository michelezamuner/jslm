package io.slc.jsm.slc_instruction_set.stubs;

import io.slc.jsm.slc_interpreter.Loader;
import io.slc.jsm.slc_runtime.SlcRuntime;

public class StubLoader implements Loader<SlcRuntime>
{
    @Override
    public SlcRuntime load(final String... args)
    {
        return new SlcRuntime();
    }
}
