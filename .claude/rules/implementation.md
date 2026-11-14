# Implementation rules

## 1. Find out how it's already done, then copy that
Before writing new code, find 1–2 places in the codebase that already do
something similar (a similar endpoint, service, repository, mapper, config or
job) and follow them:
- where the code goes (package / module / layer)
- how it's structured (class layout, constructor injection, naming)
- how errors are handled (which exceptions, where they're caught, how they're logged)
- how validation, mapping, logging and transactions are done
- which existing utilities, base classes and helpers it uses

If you couldn't find anything similar, say so and describe what you plan to do
before writing a lot of code.

## 2. No abstractions that aren't needed yet
Write the simplest code that solves the task the way the project already does it.
Do NOT add any of these unless the task asks for it or the project already uses
that pattern in the same kind of situation:
- an interface that has only one implementation
- a new base class, generic type, factory, builder, strategy or "manager"
- a new layer (e.g. a facade or adapter between the existing service and repository)
- config options, flags or extension points "for later"
- a utility class for something that's only used once

Duplicating two or three lines is fine. Only extract shared code when it
already appears in several places *and* it reads more clearly afterwards.

## 3. Reuse before you add
- Search for an existing method, util, mapper, constant or exception before
  creating a new one.
- Don't add a dependency, or a second library that does the same job as an
  existing one, without asking.
- Don't add a new way of doing something the project already does one way
  (e.g. a second mapping approach, a second HTTP client, a second logging style).

## 4. Stay inside the task
- Change only what the task needs. No drive-by refactors, renames,
  reformatting or "cleanups" in code you happened to open.
- Don't change public APIs, method signatures or database schemas unless the
  task needs it. If it does, say so explicitly.
- If you spot something that should be fixed but isn't part of the task, mention
  it in your summary instead of fixing it.

## 5. When to stop and ask
Ask before going ahead if:
- the existing code does the same thing in several different ways and it's not
  clear which one to follow
- following the existing pattern looks wrong for this case
- the task seems to need a new abstraction, layer or dependency
- the change grows well beyond what the task described

## 6. When you're done
Say in your summary which existing code you used as the model and anything
you did differently, with the reason.