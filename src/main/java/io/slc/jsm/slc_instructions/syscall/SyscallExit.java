package io.slc.jsm.slc_instructions.syscall;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_runtime.Instruction;
import io.slc.jsm.slc_runtime.InstructionExecutionException;
import io.slc.jsm.slc_runtime.VirtualMachine;
import io.slc.jsm.slc_runtime.Register;

class SyscallExit implements Instruction
{
    public ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final int exitStatus = vm.readRegister(Register.EBX);

        return ExecutionResult.exit(exitStatus);
    }
}
