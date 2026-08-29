# Contributing
First of all contributions of all kinds are welcome, regardless of experience level. Contributions aren't
limited to writing code, they can be:
- Bug reports
- Feature ideas
- Documentation improvements
- Examples
- Tests
- Code
- Performance improvements
- Typo fixes
- Helping other users in issues

All of these provide value and are greatly appreciated.

## Development setup
To get started on your first contribution the project will first need to be setup locally. Besides that,
make sure Docker is installed.
- Clone the repository
- Open the project
- Optionally you can run `mvn clean install -DskipTests`
- You can now run the tests from your IDE.

## Making a change
If you'd like to make a change to Beluga, feel free to work on an existing issue or open a new one if your idea
hasn't been discussed yet.

For larger changes, it's recommended to open an issue first so the proposed approach can be discussed before
starting the implementation. This isn't required for smaller changes such as documentation improvements, typo fixes,
or straightforward bug fixes.

Once you're ready to contribute, create a branch for your changes and make your changes there. 
Please keep changes focused on the issue or improvement you're addressing.

Before opening a pull request, make sure the project builds successfully 
and that the relevant tests pass. If your change affects the documented behaviour of Beluga,
please update the documentation where appropriate.

Once your changes are ready, open a pull request and describe what you 
changed and why. Don't worry if you're unsure whether your approach is the 
best one — discussion and iteration are part of the contribution process.

## Pull requests
When opening a pull request, 
please provide a brief description of what was changed and why.

Please keep pull requests focused on a single issue or improvement where possible. This makes changes easier to review and discuss.

Pull requests don't need to be perfect when they're first opened. Feedback and discussion 
are part of the review process, and changes can be made based on that feedback.

Before opening a pull request, please make sure that the project builds successfully and that the relevant tests pass.

## Code style
Beluga uses the Maven Checkstyle plugin to enforce the project's code style. 

Please make sure your changes comply with the configured Checkstyle rules. Checkstyle is run as part of the Maven
build, so style violations will be reported when building the project.

If you're using an IDE, you can also configure it to follow the project's Checkstyle configuration.

## Tests
When contributing code, please add or update tests where appropriate. Bug fixes should ideally 
include a test that prevents the issue from being reintroduced.

Tests should pass before opening a pull request. If you're unable to add a test for 
your change, don't hesitate to open the pull request anyway and explain why.

## Code of conduct
The code of conduct can be found [here](CODE_OF_CONDUCT.md).