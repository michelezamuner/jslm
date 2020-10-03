package io.slc.jsm.defaultruntime;

public class InstructionExecutionException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InstructionExecutionException(final String message)
    {
        super(message);
    }

    public InstructionExecutionException(final String message, final Throwable err)
    {
        super(message, err);
    }

    public String getMessage()
    {
        final String message = super.getMessage();

        return message != null ? message : "";
    }
}