package io.slc.jsm.slc_instructions.syscall;

import java.util.List;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_runtime.Instruction;
import io.slc.jsm.slc_runtime.VirtualMachine;
import io.slc.jsm.slc_runtime.InstructionExecutionException;

public class Syscall implements Instruction
{
    private final SyscallMap map = new SyscallMap();

    public ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final Class<? extends Instruction> syscallClass = map.get(vm);

        try {
            final Instruction syscall = syscallClass.newInstance();

            return syscall.exec(vm, operands);
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Error instantiating instruction", e);
        }
    }
}
