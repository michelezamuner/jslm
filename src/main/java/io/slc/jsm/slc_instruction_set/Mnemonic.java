package io.slc.jsm.slc_instruction_set;

import io.slc.jsm.slc_instruction_set.instructions.syscall.Type;

public class Mnemonic {
    public static final int MOVI = 0x01;
    public static final int SYSCALL = 0xf0;
    public static final int SYSCALL_EXIT = Type.EXIT;
}
