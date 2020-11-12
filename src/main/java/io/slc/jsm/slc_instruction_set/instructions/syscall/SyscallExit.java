package io.slc.jsm.slc_instruction_set.instructions.syscall;

import java.util.List;

import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_runtime.Register;

class SyscallExit implements SlcInstruction
{
    @Override
    public ExecutionResult exec(final SlcRuntime runtime, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final int exitStatus = runtime.readRegister(Register.EBX);

        return ExecutionResult.exit(exitStatus);
    }
}
