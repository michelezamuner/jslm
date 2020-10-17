package io.slc.jsm.slc_runtime.instruction_set;

public class InvalidInstructionException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidInstructionException(final String message)
    {
        super(message);
    }

    public InvalidInstructionException(final String message, final Throwable err)
    {
        super(message, err);
    }

    public String getMessage()
    {
        final String message = super.getMessage();

        return message != null ? message : "";
    }
}
