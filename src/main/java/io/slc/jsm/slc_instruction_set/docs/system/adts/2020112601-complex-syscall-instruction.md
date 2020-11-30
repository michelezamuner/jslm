# Complex Syscall instruction

## Context

The syscall instruction has a complex behavior: according to the contents of the EAX register, the actual behavior of the instruction is performed. We want to avoid implementing all different behaviors in the same class, also to ease the process to add and remove different behaviors.

## Decision

We will put the definitions of each different syscall into its own file, and decouple instruction creation from instruction selection and execution. So we'll have a repository of syscall instructions that knows how to choose the correct instruction class according to the given runtime, a factory that takes care of the lower level details for providing the actual instruction instance, and another element that performs the actual execution on the given instance.

## Status

Accepted

## Consequences

The repository and executor will contain all the business logic, and will be unit tested, while the factory will just contain the lower lever instantiation details, and as such won't need to be unit tested.

The repository will change with a higher rate, since each time a new syscall needs to be defined, it'll need to be added there.
