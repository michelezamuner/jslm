package io.slc.jsm.slc_instruction_set.instructions;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_instruction_set.instructions.syscall.Selector;

public class Syscall implements SlcInstruction
{
    private final Selector selector = new Selector();

    @Override
    public ExecutionResult exec(final SlcRuntime runtime, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final Class<? extends SlcInstruction> syscallClass = selector.select(runtime);

        try {
            final SlcInstruction syscall = syscallClass.newInstance();

            return syscall.exec(runtime, operands);
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Error instantiating instruction", e);
        }
    }
}
