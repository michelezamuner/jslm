package io.slc.jsm.defaultinterpreter;

public class RuntimeExecutionException extends Exception
{
    private static final long serialVersionUID = 1L;

    public RuntimeExecutionException(final String message)
    {
        super(message);
    }

    public RuntimeExecutionException(final String message, final Throwable err)
    {
        super(message, err);
    }

    public String getMessage()
    {
        String message = super.getMessage();
        
        return message != null ? message : "";
    }
}