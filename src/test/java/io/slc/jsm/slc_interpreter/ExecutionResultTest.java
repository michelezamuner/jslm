package io.slc.jsm.slc_interpreter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("initialization")
public class ExecutionResultTest
{
    @Test
    public void instructsToProceed()
    {
        final ExecutionResult result = ExecutionResult.proceed();

        assertFalse(result.shouldExit());
        assertFalse(result.shouldJump());

        RuntimeException exception = null;

        exception = assertThrows(RuntimeException.class, () -> {
            result.getExitStatus();
        });
        assertEquals("No exit has been set", exception.getMessage());

        exception = assertThrows(RuntimeException.class, () -> {
            result.getJumpAddress();
        });
        assertEquals("No jump has been set", exception.getMessage());
    }

    @Test
    public void instructsToExit()
    {
        final int exitStatus = 192;
        final ExecutionResult result = ExecutionResult.exit(exitStatus);

        assertTrue(result.shouldExit());
        assertEquals(exitStatus, result.getExitStatus());
        assertFalse(result.shouldJump());

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            result.getJumpAddress();
        });
        assertEquals("No jump has been set", exception.getMessage());
    }

    @Test
    public void instructsToJump()
    {
        final int jumpAddress = 0xFF;
        final ExecutionResult result = ExecutionResult.jump(jumpAddress);

        assertTrue(result.shouldJump());
        assertEquals(jumpAddress, result.getJumpAddress());
        assertFalse(result.shouldExit());

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            result.getExitStatus();
        });
        assertEquals("No exit has been set", exception.getMessage());
    }
}
