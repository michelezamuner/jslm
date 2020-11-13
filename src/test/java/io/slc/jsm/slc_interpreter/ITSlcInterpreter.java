package io.slc.jsm.slc_interpreter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.vm.interpreter.ProgramException;

import io.slc.jsm.slc_interpreter.stubs.StubBuffer;
import io.slc.jsm.slc_interpreter.stubs.plain.PlainInstructionSet;
import io.slc.jsm.slc_interpreter.stubs.plain.PlainLoader;
import io.slc.jsm.slc_interpreter.stubs.plain.PlainRuntime;
import io.slc.jsm.slc_interpreter.stubs.weird.WeirdInstructionSet;
import io.slc.jsm.slc_interpreter.stubs.weird.WeirdLoader;
import io.slc.jsm.slc_interpreter.stubs.weird.WeirdRuntime;

public class ITSlcInterpreter
{
    @Test
    public void programIsRunWithQuasiDefaultRuntime()
        throws ProgramException
    {
        final Integer expectedExitStatus = 192;
        final int[] bytes = {
            0x00, 0x00, 0x00, 0x00, // no-op
            0x01, 0x00, 0x00, 0x0c, // jump to instruction
            0x00, 0x00, 0x00, 0x00, // skipped
            0xff, 0x00, 0x00, 0x00, // exit with exit status passed from the execution args
            0x00, 0x00, 0x00, 0x00 // never reached
        };

        final PlainLoader loader = new PlainLoader();
        final PlainInstructionSet instructionSet = new PlainInstructionSet();
        final SlcInterpreter<PlainRuntime> interpreter = new SlcInterpreter<>(loader, instructionSet);

        final Buffer program = new StubBuffer(bytes);
        final String[] args = { expectedExitStatus.toString() };

        final int exitStatus = interpreter.run(program, args);
        assertEquals(expectedExitStatus, exitStatus);
    }

    @Test
    public void programIsRunWithDifferentRuntime()
        throws ProgramException
    {
        final int expectedExitStatus = 192;
        final Integer instructionsToJump = 2;
        final Integer instructionsToExit = 3;
        final int[] bytes = {
            0x00, 0x00, // no-op
            0xff, 0x00,
            0xff, 0x00,
            0x00, 0x0a, // two 0xff in a row = jump
            0x00, 0x00, // skipped
            0xff, 0x00,
            0xff, 0x00,
            0xff, 0x00,
            0x00, expectedExitStatus, // three 0xff in a row = exit
            0x00, 0x00, // never reached
        };

        final WeirdLoader loader = new WeirdLoader();
        final WeirdInstructionSet instructionSet = new WeirdInstructionSet();
        final SlcInterpreter<WeirdRuntime> interpreter = new SlcInterpreter<>(loader, instructionSet);

        final Buffer program = new StubBuffer(bytes);
        final String[] args = new String[]{ instructionsToJump.toString(), instructionsToExit.toString() };

        final int exitStatus = interpreter.run(program, args);
        assertEquals(expectedExitStatus, exitStatus);
    }
}
