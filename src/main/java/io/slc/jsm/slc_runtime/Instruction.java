package io.slc.jsm.slc_runtime;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;

public interface Instruction
{
    ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException;
}