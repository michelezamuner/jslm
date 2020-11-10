package io.slc.jsm.slc_interpreter;

public class InvalidInstructionException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidInstructionException(final String message)
    {
        super(message);
    }

    public InvalidInstructionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public String getMessage()
    {
        final String message = super.getMessage();
        
        return message != null ? message : "";
    }
}
