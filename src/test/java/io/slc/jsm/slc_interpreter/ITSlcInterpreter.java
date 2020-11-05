package io.slc.jsm.slc_interpreter;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.vm.interpreter.ProgramException;
import io.slc.jsm.slc_interpreter.runtime.Loader;

import io.slc.jsm.slc_interpreter.stubs.StubLoader;
import io.slc.jsm.slc_interpreter.stubs.StubBuffer;

public class ITSlcInterpreter
{
    @Test
    public void runProgramWithJumpsAndExits()
        throws ProgramException
    {
        final int expectedExitStatus = 192;
        final int instructionSize = 4;
        final int[] bytes = {
            0x00, 0x00, 0x00, 0x00, // no-op
            0x01, 0x00, 0x00, 0x0c, // jump to instruction
            0x00, 0x00, 0x00, 0x00, // no-op, skipped
            0xff, 0x00, 0x00, 0x00, // exit with exit status passed from the execution args
            0x00, 0x00, 0x00, 0x00 // no-op, never reached
        };

        final Configuration configuration = new Configuration(instructionSize);
        final Loader loader = new StubLoader(instructionSize);
        final Buffer program = new StubBuffer(bytes);
        final String[] args = { new Integer(expectedExitStatus).toString() };

        final SlcInterpreter interpreter = new SlcInterpreter(loader, configuration);

        final int exitStatus = interpreter.run(program, args);

        assertEquals(expectedExitStatus, exitStatus);
    }

    @Disabled
    @Test
    public void runProgramWithDifferentInstructionSize()
        throws ProgramException
    {

    }
}
