package io.slc.jsm.defaultinterpreter;

import java.util.List;

public interface Runtime
{
    ExecutionResult exec(final List<Integer> instruction)
        throws RuntimeExecutionException;
}