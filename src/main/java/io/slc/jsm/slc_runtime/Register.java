package io.slc.jsm.slc_runtime;

public enum Register
{
    EAX(0x00),
    AX(0x01),
    AH(0x02),
    AL(0x03),
    EBX(0x04),
    BX(0x05),
    BH(0x06),
    BL(0x07),
    ECX(0x08),
    CX(0x09),
    CH(0x0a),
    CL(0x0b),
    EDX(0x0c),
    DX(0x0d),
    DH(0x0e),
    DL(0x0f);

    private final int address;

    Register(final int address)
    {
        this.address = address;
    }

    public int getAddress()
    {
        return address;
    }
}
