package io.slc.jsm.slc_runtime.instruction_set;

import java.util.List;

import io.slc.jsm.slc_interpreter.runtime.ExecutionResult;
import io.slc.jsm.slc_runtime.virtual_machine.VirtualMachine;

public interface Instruction
{
    ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException;
}
