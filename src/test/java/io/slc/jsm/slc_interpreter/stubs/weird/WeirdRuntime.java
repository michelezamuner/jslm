package io.slc.jsm.slc_interpreter.stubs.weird;

import io.slc.jsm.slc_interpreter.Runtime;

public class WeirdRuntime implements Runtime
{
    private final int instructionsToJump;
    private final int instructionsToExit;
    private int counterInstructions;

    public WeirdRuntime(final int instructionsToJump, final int instructionsToExit)
    {
        this.instructionsToJump = instructionsToJump;
        this.instructionsToExit = instructionsToExit;
        this.counterInstructions = 0;
    }

    public int getInstructionsToJump()
    {
        return instructionsToJump;
    }

    public int getInstructionsToExit()
    {
        return instructionsToExit;
    }

    public void incrementCounterInstructions()
    {
        counterInstructions++;
    }

    public void resetCounterInstructions()
    {
        counterInstructions = 0;
    }

    public int getCounterInstructions()
    {
        return counterInstructions;
    }
}
