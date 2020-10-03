package io.slc.jsm.defaultruntime.instructions;

import java.util.List;

import io.slc.jsm.defaultinterpreter.ExecutionResult;
import io.slc.jsm.defaultruntime.Instruction;
import io.slc.jsm.defaultruntime.InstructionExecutionException;
import io.slc.jsm.defaultruntime.VirtualMachine;

class SyscallExit implements Instruction
{
    public ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}