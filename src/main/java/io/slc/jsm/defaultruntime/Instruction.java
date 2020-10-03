package io.slc.jsm.defaultruntime;

import java.util.List;

import io.slc.jsm.defaultinterpreter.ExecutionResult;

public interface Instruction
{
    ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException;
}