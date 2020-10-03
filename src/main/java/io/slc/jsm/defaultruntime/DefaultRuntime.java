package io.slc.jsm.defaultruntime;

import java.util.List;

import io.slc.jsm.defaultinterpreter.Runtime;
import io.slc.jsm.defaultinterpreter.ExecutionResult;
import io.slc.jsm.defaultinterpreter.RuntimeExecutionException;

class DefaultRuntime implements Runtime
{
    private final InstructionSet instructionSet;
    private final VirtualMachine vm;

    public DefaultRuntime(final InstructionSet instructionSet, final VirtualMachine vm)
    {
        this.instructionSet = instructionSet;
        this.vm = vm;
    }

    public ExecutionResult exec(final List<Integer> instructionData)
        throws RuntimeExecutionException
    {
        final int opcode = instructionData.get(0);
        final List<Integer> operands = instructionData.subList(1, instructionData.size());
        final Instruction instruction = getInstruction(opcode);

        return exec(instruction, operands);
    }

    private Instruction getInstruction(final int opcode)
        throws RuntimeExecutionException
    {
        try {
            return instructionSet.get(opcode);
        } catch (InvalidInstructionException e) {
            throw new RuntimeExecutionException(e.getMessage(), e);
        }
    }

    private ExecutionResult exec(final Instruction instruction, final List<Integer> operands)
        throws RuntimeExecutionException
    {
        try {
            return instruction.exec(vm, operands);
        } catch (InstructionExecutionException e) {
            throw new RuntimeExecutionException(e.getMessage(), e);
        }
    }
}
