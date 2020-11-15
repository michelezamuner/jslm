package io.slc.jsm.slc_runtime;

public class RegistersException extends Exception
{
    private static final long serialVersionUID = 1L;

    public RegistersException(final String message)
    {
        super(message);
    }

    public RegistersException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public String getMessage()
    {
        return super.getMessage() != null ? super.getMessage() : "";
    }
}
