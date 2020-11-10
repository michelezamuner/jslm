package io.slc.jsm.slc_interpreter.stubs.plain;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.Instruction;
import io.slc.jsm.slc_interpreter.InstructionSet;
import io.slc.jsm.slc_interpreter.InvalidInstructionException;

public class PlainInstructionSet implements InstructionSet<PlainRuntime>
{
    private static final int INSTRUCTION_SIZE = 4;

    public static class NoopInstruction implements Instruction<PlainRuntime>
    {
        public ExecutionResult exec(final PlainRuntime runtime, final List<Integer> operands)
        {
            return ExecutionResult.proceed();
        }
    }

    public static class JumpInstruction implements Instruction<PlainRuntime>
    {
        public ExecutionResult exec(final PlainRuntime runtime, final List<Integer> operands)
        {
            return ExecutionResult.jump(operands.get(2));
        }
    }

    public static class ExitInstruction implements Instruction<PlainRuntime>
    {
        public ExecutionResult exec(final PlainRuntime runtime, final List<Integer> operands)
        {
            return ExecutionResult.exit(runtime.getExitStatus());
        }
    }

    public int getInstructionSize()
    {
        return INSTRUCTION_SIZE;
    }

    public Instruction<PlainRuntime> get(final int opcode)
        throws InvalidInstructionException
    {
        if (opcode == 0x01) {
            return new JumpInstruction();
        }

        if (opcode == 0xff) {
            return new ExitInstruction();
        }

        return new NoopInstruction();
    }
}
