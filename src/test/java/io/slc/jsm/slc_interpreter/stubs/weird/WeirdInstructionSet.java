package io.slc.jsm.slc_interpreter.stubs.weird;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.Instruction;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_interpreter.InstructionSet;

public class WeirdInstructionSet implements InstructionSet<WeirdRuntime>
{
    private static final int INSTRUCTION_SIZE = 2;

    public static class CounterInstruction implements Instruction<WeirdRuntime>
    {
        @Override
        public ExecutionResult exec(final WeirdRuntime runtime, final List<Integer> operands)
            throws InstructionExecutionException
        {
            runtime.incrementCounterInstructions();

            return ExecutionResult.proceed();
        }
    }

    public static class ActionInstruction implements Instruction<WeirdRuntime>
    {
        public ExecutionResult exec(final WeirdRuntime runtime, final List<Integer> operands)
            throws InstructionExecutionException
        {
            if (runtime.getCounterInstructions() == runtime.getInstructionsToExit()) {
                runtime.resetCounterInstructions();

                return ExecutionResult.exit(operands.get(0));
            }

            if (runtime.getCounterInstructions() == runtime.getInstructionsToJump()) {
                runtime.resetCounterInstructions();

                return ExecutionResult.jump(operands.get(0));
            }

            return ExecutionResult.proceed();
        }
    }

    @Override
    public int getInstructionSize()
    {
        return INSTRUCTION_SIZE;
    }

    @Override
    public Instruction<WeirdRuntime> get(final int opcode)
    {
        if (opcode == 0xff) {
            return new CounterInstruction();
        }

        return new ActionInstruction();
    }
}
