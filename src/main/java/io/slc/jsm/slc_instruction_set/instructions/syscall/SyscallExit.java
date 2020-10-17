package io.slc.jsm.slc_instruction_set.instructions.syscall;

import java.util.List;

import io.slc.jsm.slc_interpreter.runtime.ExecutionResult;
import io.slc.jsm.slc_runtime.instruction_set.Instruction;
import io.slc.jsm.slc_runtime.instruction_set.InstructionExecutionException;
import io.slc.jsm.slc_runtime.virtual_machine.VirtualMachine;
import io.slc.jsm.slc_runtime.virtual_machine.Register;

class SyscallExit implements Instruction
{
    public ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final int exitStatus = vm.readRegister(Register.EBX);

        return ExecutionResult.exit(exitStatus);
    }
}
