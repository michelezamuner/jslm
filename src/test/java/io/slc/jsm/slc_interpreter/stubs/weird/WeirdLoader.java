package io.slc.jsm.slc_interpreter.stubs.weird;

import io.slc.jsm.slc_interpreter.Loader;

public class WeirdLoader implements Loader<WeirdRuntime>
{
    @Override
    public WeirdRuntime load(final String... args)
    {
        final int instructionsToJump = Integer.parseInt(args[0]);
        final int instructionsToExit = Integer.parseInt(args[1]);

        return new WeirdRuntime(instructionsToJump, instructionsToExit);
    }
}
