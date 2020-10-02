package io.slc.jsm.defaultinterpreter;

public interface Runtime
{
    boolean shouldExit();
    
    int getExitStatus();
    
    boolean shouldJump();
    
    int getJumpAddress();
}