package io.slc.jsm.slc_instruction_set.instructions.syscall;

import java.util.Map;
import java.util.HashMap;

import io.slc.jsm.slc_runtime.instruction_set.Instruction;
import io.slc.jsm.slc_runtime.instruction_set.InstructionExecutionException;
import io.slc.jsm.slc_runtime.virtual_machine.VirtualMachine;
import io.slc.jsm.slc_runtime.virtual_machine.Register;

public class SyscallMap
{
    static final int SYSCALL_EXIT_CODE = 1;

    @SuppressWarnings("serial")
    private static final Map<Integer, Class<? extends Instruction>> syscalls = new HashMap<Integer, Class<? extends Instruction>>() {{
        put(SYSCALL_EXIT_CODE, SyscallExit.class);
    }};

    public Class<? extends Instruction> get(final VirtualMachine vm)
        throws InstructionExecutionException
    {
        final int syscallCode = vm.readRegister(Register.EAX);
        final Class<? extends Instruction> syscallClass = syscalls.get(syscallCode);
        if (syscallClass == null) {
            throw new InstructionExecutionException("Invalid syscall code: " + syscallCode);
        }

        return syscallClass;
    }
}
