package io.slc.jsm.slc_instructions;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_runtime.Instruction;
import io.slc.jsm.slc_runtime.InstructionExecutionException;
import io.slc.jsm.slc_runtime.VirtualMachine;

class SyscallExit implements Instruction
{
    public ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}