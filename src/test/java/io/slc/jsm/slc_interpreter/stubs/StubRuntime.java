package io.slc.jsm.slc_interpreter.stubs;

import java.util.List;

import io.slc.jsm.slc_interpreter.runtime.Runtime;
import io.slc.jsm.slc_interpreter.runtime.ExecutionResult;
import io.slc.jsm.slc_interpreter.runtime.RuntimeExecutionException;

public class StubRuntime implements Runtime
{
    private int instructionSize;
    private int exitStatus;

    public StubRuntime(final int instructionSize, final String... args)
    {
        this.instructionSize = instructionSize;
        this.exitStatus = Integer.parseInt(args[0]);
    }

    public ExecutionResult exec(final List<Integer> instruction)
        throws RuntimeExecutionException
    {
        final int opcode = instruction.get(0);

        if (opcode == 1) {
            return ExecutionResult.jump(instruction.get(3));
        }

        if (opcode == 0xff) {
            return ExecutionResult.exit(exitStatus);
        }

        return ExecutionResult.proceed();
    }
}
