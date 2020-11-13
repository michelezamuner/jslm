package io.slc.jsm.slc_instruction_set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.slc.jsm.slc_instruction_set.stubs.StubBuffer;
import io.slc.jsm.slc_instruction_set.stubs.StubLoader;
import io.slc.jsm.slc_interpreter.SlcInterpreter;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.vm.interpreter.ProgramException;

public class ITSlcInstructionSet
{
    @Disabled
    @Test
    public void programIsTerminatedWithSpecificExitStatus()
        throws ProgramException
    {
        final Integer expectedExitStatus = 192;
        final int[] bytes = {
            Mnemonic.MOVI, Register.EAX, 0x00, Mnemonic.SYSCALL_EXIT, // set "exit" syscall
            Mnemonic.MOVI, Register.EBX, 0x00, expectedExitStatus, // set exit status for syscall
            Mnemonic.MOVI, Register.ECX, 0x00, 0x03, // not needed, should do nothing
            Mnemonic.SYSCALL, 0x00, 0x00, 0x00, // execute syscall
            Mnemonic.MOVI, Register.EAX, 0x00, Mnemonic.SYSCALL_EXIT, // we should have already terminated here
            Mnemonic.MOVI, Register.EBX, 0x00, 0x04, // we should have already terminated here
            Mnemonic.SYSCALL, 0x00, 0x00, 0x00 // we should have already terminated here
        };

        final StubLoader loader = new StubLoader();
        final SlcInstructionSet instructionSet = new SlcInstructionSet();
        final SlcInterpreter<SlcRuntime> interpreter = new SlcInterpreter<>(loader, instructionSet);
        final StubBuffer program = new StubBuffer(bytes);

        final int exitStatus = interpreter.run(program, new String[]{});
        assertEquals(expectedExitStatus, exitStatus);
    }
}
