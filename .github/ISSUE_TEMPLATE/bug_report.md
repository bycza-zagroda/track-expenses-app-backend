name: Bug Report
description: Create a report to help us improve
labels:
- bug
  body:
- type: textarea
  id: bug-description
  attributes:
  label: Describe the bug
  description: A clear and concise description of what the bug is
  validations:
  required: true

- type: textarea
  id: bug-reproduce
  attributes:
  label: To Reproduce
  description: |
  Describe how to reproduce the bug.

      Please provide a [minimal reproducible example](https://stackoverflow.com/help/minimal-reproducible-example).

      Your code examples should be…
      >   …Minimal – Use as little code as possible that still produces the same problem
      >   …Complete – Provide all parts someone else needs to reproduce your problem in the issue itself
      >   …Reproducible – Test the code you're about to provide to make sure it reproduces the problem

      Ideally, you can provide the code directly here, alternatively, you can post a link to an example project on Github.
  validations:
  required: true

- type: textarea
  id: bug-expected-behavior
  attributes:
  label: Expected behavior
  description: A clear and concise description of what you expected to happen.
  validations:
  required: true

- type: textarea
  id: bug-actual-behavior
  attributes:
  label: Actual behavior
  description: A clear and concise description of what happened instead.
  validations:
  required: true

- type: textarea
  id: bug-java-version
  attributes:
  label: Java version
  description: Which Java version are you using `java -version` ?
  validations:
  required: true

- type: textarea
  id: bug-buildtool-version
  attributes:
  label: Buildtool version
  description: Which Buildtool version are you using `java -version` ?
  placeholder: Paste the output of `gradlew --version` or  `mvn --version` here.
  validations:
  required: true

- type: dropdown
  id: bug-os
  attributes:
  label: What operating system are you using
  options:
  - Windows
  - Linux
  - Mac
  - Other
  validations:
  required: true

- type: textarea
  id: bug-dependencies
  attributes:
  label: Dependencies
  description: |
  Which Dependencies are you using?

      Either manually list the relevant dependencies below,
      or use one of these commands to create a dependencies-report.txt and attach it
      * `./gradlew -q dependencies --configuration testRuntimeClasspath > dependencies-report.txt`
      * `mvn dependency:tree -DoutputFile=dependency-report.txt`

      Or, if your build is public you can use a [build scan](https://scans.gradle.com/) link.
  validations:
  required: true

- type: textarea
  id: bug-additional-context
  attributes:
  label: Additional context
  description: Anything else that did not fit in the other categories.