package io.slc.jsm.slc_interpreter;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ExecutionResult
{
    private final boolean shouldExit;
    private final @Nullable Integer exitStatus;
    private final boolean shouldJump;
    private final @Nullable Integer jumpAddress;

    public static ExecutionResult proceed()
    {
        return new ExecutionResult(null, null);
    }

    public static ExecutionResult exit(final int exitStatus)
    {
        return new ExecutionResult(exitStatus, null);
    }

    public static ExecutionResult jump(final int jumpAddress)
    {
        return new ExecutionResult(null, jumpAddress);
    }

    private ExecutionResult(final @Nullable Integer exitStatus, final @Nullable Integer jumpAddress)
    {
        this.shouldExit = exitStatus != null;
        this.exitStatus = exitStatus;
        this.shouldJump = jumpAddress != null;
        this.jumpAddress = jumpAddress;
    }

    public boolean shouldExit()
    {
        return shouldExit;
    }

    public int getExitStatus()
    {
        if (exitStatus == null) {
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
        if (jumpAddress == null) {
            throw new RuntimeException("No jump has been set");
        }
        return jumpAddress;
    }
}
