package io.slc.jsm.slc_interpreter;

import java.util.List;

public interface Instruction<R extends Runtime>
{
    ExecutionResult exec(final R runtime, final List<Integer> operands)
        throws InstructionExecutionException;
}
