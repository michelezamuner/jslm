package io.slc.jsm.slc_instruction_set.instructions.syscall;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.RegistersException;
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
        final int syscallCode = getSyscallCode(runtime, Register.EAX);
        final Class<? extends SlcInstruction> syscallClass = syscalls.get(syscallCode);
        if (syscallClass == null) {
            throw new InstructionExecutionException("Invalid syscall code: " + syscallCode);
        }

        return syscallClass;
    }

    private int getSyscallCode(final SlcRuntime runtime, final int register)
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
