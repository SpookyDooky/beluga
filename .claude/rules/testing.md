# Testing rules

## 1. Match the existing tests. This rule overrides everything below.
Before you write or change a test, open 2–3 existing tests in the same package or
module and copy what they do:
- class and method naming (e.g. `shouldX_whenY` vs `givenX_whenY_thenZ`)
- test layout (`// given / when / then` comments, `@Nested`, `@DisplayName`)
- setup style (`@ExtendWith(MockitoExtension.class)` vs `MockitoAnnotations.openMocks`)
- stubbing style (`when(...).thenReturn` vs BDDMockito `given(...).willReturn`)
- assertion library (AssertJ / JUnit / Hamcrest): use whichever the project uses
- shared fixtures, base classes, Instancio models and helpers that already exist

Do NOT:
- add a test library, assertion style or base class the project doesn't already use
- write a new helper or fixture when an existing one does the job
- "improve" or reformat existing tests that aren't part of the task
- change production code just to make it easier to test, unless asked

If the existing tests disagree with this file, or with each other, follow the
closest existing test and say so. If you're not sure, ask before going your own way.

## 2. Instancio for test data
- Build domain objects, DTOs and entities with Instancio, not by hand with
  setters or constructors.
- Set only the fields the test actually depends on, so the test's intent is visible:
  ```java
  var order = Instancio.of(Order.class)
          .set(field(Order::getStatus), OrderStatus.PAID)
          .create();
  ```
- Collections: `Instancio.ofList(Order.class).size(3).create()`.
- If an Instancio `Model` already exists for a type, use it instead of
  defining a new one.
- Never assert against a random value you didn't set. Assert against the value
  on the generated object (`order.getId()`), not a hardcoded guess.
- Use `@Seed`, `InstancioExtension` or settings overrides only if the project
  already does.

## 3. Mockito for collaborators
- Mock collaborators (repositories, clients, other services). Never mock the
  class under test, and never mock value objects or DTOs: create those with Instancio.
- Use `@Mock` / `@InjectMocks` (or constructor injection) the same way the
  surrounding tests do.
- Stub only what the test needs. Don't add `lenient()` to get rid of
  `UnnecessaryStubbingException`; delete the unused stub instead.
- Match on real values when they matter; use `any()` only when the argument
  really doesn't matter to the test.
- Use `ArgumentCaptor` to check what was passed to a collaborator.
- `verify` only the interactions that are the point of the test. Don't verify
  calls that are already proven by a stubbed return value.
- Use `spy`, `mockStatic` or `mockConstruction` only if the project already
  uses them for the same kind of case.

## 4. What makes a good test here
- One behaviour per test; the method name says which.
- Cover the happy path, the edge cases and the error paths (exceptions:
  `assertThatThrownBy` / `assertThrows`, whichever the project uses).
- Every bug fix gets a test that fails before the fix.
- Don't delete, `@Disabled` or weaken a failing test to make it pass. Fix the code
  or ask.
- Run the tests you touched (and the module's test suite) before saying you're done.