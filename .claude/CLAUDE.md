# Project guide for agents

## Architecture

`@./architecture.md` is the map of this codebase. It covers:

- **Overview**: what the system does and how the main pieces fit together
- **Folder layout**: what lives in each directory
- **Key modules**: what each module is responsible for
- **Data flow**: how a request or event moves through the system
- **Decisions & constraints**: deliberate choices that look odd but shouldn't be "fixed"

Read it before:
- adding a new file or module (so it goes in the right place)
- changing how two modules talk to each other
- refactoring anything that crosses folder boundaries

If your change alters the structure (a new module, a moved folder, a new dependency between parts), update `architecture.md` in the same change.