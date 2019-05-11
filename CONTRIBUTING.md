# Contributing

This project became open source and is under very active development. We are working out bits and pieces that needs refactoring as well as implementing new technologies and functionality. Hopefully this document will help you understand the process for contributing and making proposals.

## Code of Conduct

Our team has adapted a Code of Conduct from the Contributor Covenant, version 1.4, available at <https://www.contributor-covenant.org/version/1/4/code-of-conduct.html>. Please read the full text so that you can understand what actions will and will not be tolerated.

## Branch Organization

All development on LL(1) Compiler happens directly on [GitHub][github] which means that we try to keep code in good shape. We recommend that you use the [latest stable version][release] of the project. There are two major branches.

* The [**master** branch][masterB] is the latest stable version that has been tested
* The [**develop** branch][devB] is the current under development build.

## Bugs

The best way to get a bug fixed is to fill an issue. You must provide enough details and explain a test case. We keep track of all issues and try to make it clear when we have a fix in progress. Before filling an issue, try to make sure your issue doesn't already exist.

## Proposals

If you wish to make a proposal, we recommend filing an issue. This lets us review your proposal and get back to you with thoughts and feedback. Whether it is a bug-fix or an addition to the functionality of the project, make sure to provide enough details on the matter.

## Pull Requests

If you want to send a pull request, please create a new branch on your forked repository and then do it against the [**develop**  branch][devB]. Your pull request will undergo review and if there are non-breaking changes, it will be merged for further testing. Otherwise we may request changes to it or close it with corresponding explanation. We will do our best to provide updates and feedback throughout the process.

## Versioning

We follow [semantic versioning][semver]. This means that incrementation of version follows this rule: MAJOR.MINOR.PATCH.

* *MAJOR* version when we make ground-breaking changes in codebase or when there are incompatibilities between two builds
* *MINOR* version when additional functionality is added
* *PATCH* version when bug-fixes take place

We may use additional labels for pre-release and beta versions.

[semver]: https://semver.org/
[masterB]: https://github.com/sotostzam/LL1_Compiler/tree/master
[devB]: https://github.com/sotostzam/LL1_Compiler/tree/develop
[github]: https://github.com/sotostzam/LL1_Compiler
[release]: https://github.com/sotostzam/LL1_Compiler/releases/