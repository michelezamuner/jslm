package io.slc.jsm.slc_interpreter;

import java.util.List;

import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.vm.interpreter.Interpreter;
import io.slc.jsm.vm.interpreter.ProgramException;

public class SlcInterpreter<R extends Runtime> implements Interpreter
{
    private final Loader<R> loader;
    private final InstructionSet<R> instructionSet;

    public SlcInterpreter(final Loader<R> loader, final InstructionSet<R> instructionSet)
    {
        this.loader = loader;
        this.instructionSet = instructionSet;
    }

    public int run(final Buffer program, final String... args)
        throws ProgramException
    {
        final R runtime = loader.load(args);
        final int instructionSize = instructionSet.getInstructionSize();
        final int maximumAddress = program.getSize() - instructionSize;

        int ip = 0;
        int exitStatus = 0;
        while (true) {
            final List<Integer> instructionData = program.read(ip, instructionSize);
            final int opcode = instructionData.get(0);
            final List<Integer> operands = instructionData.subList(1, instructionSize);
            final Instruction<R> instruction = getInstruction(opcode);
            final ExecutionResult result = exec(instruction, runtime, operands);

            if (result.shouldJump()) {
                ip = result.getJumpAddress();
                if (ip > maximumAddress) {
                    throw new ProgramException(String.format("Invalid jump to address %d", ip));
                }

                continue;
            }

            if (result.shouldExit()) {
                exitStatus = result.getExitStatus();

                break;
            }

            ip += instructionSize;
            if (ip > maximumAddress) {
                break;
            }
        }

        return exitStatus;
    }

    private Instruction<R> getInstruction(final int opcode)
        throws ProgramException
    {
        try {
            return instructionSet.get(opcode);
        } catch (InvalidInstructionException e) {
            throw new ProgramException(e.getMessage(), e);
        }
    }

    private ExecutionResult exec(final Instruction<R> instruction, final R runtime, final List<Integer> operands)
        throws ProgramException
    {
        try {
            return instruction.exec(runtime, operands);
        } catch (InstructionExecutionException e) {
            throw new ProgramException(e.getMessage(), e);
        }
    }
}
