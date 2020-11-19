package io.slc.jsm.slc_instruction_set.instructions;

import java.util.Arrays;
import java.util.List;

import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.Registers;
import io.slc.jsm.slc_runtime.RegistersException;
import io.slc.jsm.slc_runtime.SlcRuntime;

public class Movi implements SlcInstruction
{
    @Override
    public ExecutionResult exec(final SlcRuntime runtime, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final Registers registers = runtime.getRegisters();
        final int register = operands.get(0);
        final int firstByte = operands.get(1);
        final int secondByte = operands.get(2);

        try {
            registers.write(register, Arrays.asList(0, 0, firstByte, secondByte));
        } catch (RegistersException e) {
            throw new InstructionExecutionException(e.getMessage(), e);
        }

        return ExecutionResult.proceed();
    }
}
