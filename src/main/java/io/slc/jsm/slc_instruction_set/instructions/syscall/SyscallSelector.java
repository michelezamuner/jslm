package io.slc.jsm.slc_instruction_set.instructions.syscall;

import java.util.Map;
import java.util.HashMap;

import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_instruction_set.SlcInstruction;

public class SyscallSelector
{
    @SuppressWarnings("serial")
    private static final Map<Integer, Class<? extends SlcInstruction>> syscalls = new HashMap<Integer, Class<? extends SlcInstruction>>() {{
        put(Syscall.EXIT, SyscallExit.class);
    }};

    public Class<? extends SlcInstruction> select(final SlcRuntime runtime)
        throws InstructionExecutionException
    {
        final int syscallCode = runtime.getRegisters().read(Register.EAX).get(3);
        final Class<? extends SlcInstruction> syscallClass = syscalls.get(syscallCode);
        if (syscallClass == null) {
            throw new InstructionExecutionException("Invalid syscall code: " + syscallCode);
        }

        return syscallClass;
    }
}
