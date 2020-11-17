package io.slc.jsm.slc_instruction_set.instructions.syscall;

import java.util.List;

import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.RegistersException;

class SyscallExit implements SlcInstruction
{
    @Override
    public ExecutionResult exec(final SlcRuntime runtime, final List<Integer> operands)
        throws InstructionExecutionException
    {
        return ExecutionResult.exit(getExitStatus(runtime, Register.EBX));
    }

    private int getExitStatus(final SlcRuntime runtime, final int register)
        throws InstructionExecutionException
    {
        try {
            final List<Integer> data = runtime.getRegisters().read(register);

            return data.get(3);
        } catch (RegistersException e) {
            throw new InstructionExecutionException(e.getMessage(), e);
        }
    }
}
