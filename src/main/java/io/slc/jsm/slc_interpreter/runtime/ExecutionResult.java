package io.slc.jsm.slc_interpreter.runtime;

public class ExecutionResult
{
    private boolean shouldExit;
    private int exitStatus;
    private boolean shouldJump;
    private int jumpAddress;

    public static ExecutionResult exit(final int exitStatus)
    {
        final ExecutionResult result = new ExecutionResult();
        result.shouldExit = true;
        result.exitStatus = exitStatus;

        return result;
    }

    public static ExecutionResult jump(final int jumpAddress)
    {
        final ExecutionResult result = new ExecutionResult();
        result.shouldJump = true;
        result.jumpAddress = jumpAddress;

        return result;
    }

    private ExecutionResult()
    {
    }

    public boolean shouldExit()
    {
        return shouldExit;
    }

    public int getExitStatus()
    {
        if (!shouldExit()) {
            throw new RuntimeException("No exit has been set");
        }
        return exitStatus;
    }

    public boolean shouldJump()
    {
        return shouldJump;
    }

    public int getJumpAddress()
    {
        if (!shouldJump()) {
            throw new RuntimeException("No jump has been set");
        }
        return jumpAddress;
    }
}
