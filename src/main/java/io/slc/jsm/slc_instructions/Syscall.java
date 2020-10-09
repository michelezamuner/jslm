package io.slc.jsm.slc_instructions;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_runtime.Instruction;
import io.slc.jsm.slc_runtime.InstructionExecutionException;
import io.slc.jsm.slc_runtime.VirtualMachine;
import io.slc.jsm.slc_runtime.Register;

public class Syscall implements Instruction
{
    @SuppressWarnings("serial")
    private static final Map<Integer, Class<? extends Instruction>> syscalls = new HashMap<Integer, Class<? extends Instruction>>() {{
        put(1, SyscallExit.class);
    }};

    public ExecutionResult exec(final VirtualMachine vm, final List<Integer> operands)
        throws InstructionExecutionException
    {
        final int syscallCode = vm.readRegister(Register.EAX);
        final Class<? extends Instruction> syscallClass = syscalls.get(syscallCode);
        if (syscallClass == null) {
            throw new InstructionExecutionException("Invalid syscall code " + syscallCode);
        }

        try {
            final Instruction syscall = syscallClass.newInstance();

            return syscall.exec(vm, operands);
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Error instantiating instruction", e);
        }
    }
}
