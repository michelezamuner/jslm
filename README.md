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
