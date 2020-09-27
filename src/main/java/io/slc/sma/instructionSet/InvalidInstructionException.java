package io.slc.sma.instructionset;

import java.util.List;

public class InvalidInstructionException extends Exception
{
    private static final long serialVersionUID = 1L;
    private static final String message = "Invalid instruction";

    private List<Integer> instructionData;
    
    public InvalidInstructionException(final List<Integer> instructionData)
    {
        super(message);
        this.instructionData = instructionData;
    }

    public InvalidInstructionException(final List<Integer> instructionData, final Throwable err)
    {
        super(message, err);
        this.instructionData = instructionData;
    }

    public List<Integer> getInstructionData()
    {
        return this.instructionData;
    }

    public String getMessage()
    {
        return message;
    }
}
