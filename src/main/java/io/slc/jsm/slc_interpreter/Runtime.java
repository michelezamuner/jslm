package io.slc.jsm.slc_interpreter;

import java.util.List;

public interface Runtime
{
    ExecutionResult exec(final List<Integer> instruction)
        throws RuntimeExecutionException;
}