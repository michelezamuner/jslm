package io.slc.jsm.interpreter;

public class ProgramException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ProgramException(final String message)
    {
        super(message);
    }

    public ProgramException(final String message, final Throwable err)
    {
        super(message, err);
    }
}
