package io.slc.jslm.interpreter;

public class ProgramException extends RuntimeException
{
    private static final long serialVersionUID = 0x0001;
    
    public ProgramException(String message)
    {
        super(message);
    }
}