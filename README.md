# jsm

fetch cycle (fixed instruction size)
    fetch instruction
    update instruction pointer

fetch cycle (variable instruction size)
    opcodes have fixed size
    fetch opcode
    operands size from opcode
    fetch operands
    update instruction pointer

## interpreter

interpreter
	architecture specific, for example SMA (Simple sloth Machine Architecture)
	exposes binary code interface, meaning that can be targeted by any assembly code
	exposes generic programming interface, so that clients can be independent from interpreters
	does not expose any user interface

### interpreter usage
```java
Program program = programFileReader.read(filename);
// All details about how the program is handled are implementation-specific
ExitStatus exitStatus = interpreter.run(program);
exit(exitStatus.toInt());
```

### interpreter interface
```java
public interface ExitStatus
{
    public int toInt();
}
```

A `Program` needs to be fully stored in memory, to support at least jumps, and maybe also self-programming. Thus it makes no sense to provide a stream-like interface.
```java
public interface Program
{
    public Byte read(Address address);
    public Byte[] read(Address address, Size size);
    public Buffer read();
}
```

```java
public interface Interpreter
{
    public ExitStatus run(Program program, String[] args);
}
```

### SMA interpreter

The SMA interpreter implementation would support:
- fixed size instructions
- instructions directly read from the original `Program` (no need to copy it to the memory model)
- basic registers: data, flags
- fetch cycle managed through registers (ip for jumps, exit flag to terminate)
- exit instruction must be present
- static memory, stack and heap
- runtime arguments loaded onto the stack
- dedicated module to communicate with the system (except for process and thread handling)

```java
public class Fetcher
{
    public Instruction fetch(Program program, Address address)
    {
        Byte[] instructionData = program.read(address, instructionSize);
        return instructionSet.get(instructionData);
    }
}
```
```java
public ExitStatus run(Program program, String[] args)
{
    Byte ip = 0x00;
    // resets registers and memory, stores program data to the static memory, stores args to the stack
    Runtime runtime = loader.load(program, args);

    while(true) {
        Instruction instruction = fetcher.fetch(program, ip);
        RuntimeStatus status = instruction.exec(runtime);
        if (status.getExitStatus()) {
            return status.getExitStatus();
        }
        ip = ipUpdater.update(ip, status);
    }

    return status.getExitStatus();
}
```


## other

vm client
	exposes user interface, for example command line or network API
	does not know the specific interpreter, which is passed through configuration

vm = vm client + interpreter

asm core
	targets a specific assembly language, which targets a specific architecture, for example BASM -> SMA
	exposes generic programming interface, so that clients can be independent from cores
	does not expose any user interface

asm client
	exposes user interface, for example command line or network API
	the same client can be reused for multiple cores, if their interface is abstract enough

assembler = asm client + asm core

compiler backend (CHECK THIS)
	targets a specific assembly language, for example BASM
	exposes generic programming interface, so that frontends can be independent from backends
	does not expose any user interface

compiler frontend (CHECK THIS)
	targets a specific programming language, for example Sloth
	exposes user interface, for example command line or network API
	the same frontend can be reused for multiple backends, if their interface is asbtract enough

compiler = compiler frontend + compiler backend
