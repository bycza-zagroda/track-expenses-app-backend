name: Feature request
description: Suggest an idea for this project
labels:
- enhancement
- new feature

body:
- type: textarea
  id: feature-problem
  attributes:
  label: Is your feature request related to a problem?
  description: A clear and concise description of what the problem is. Example I'm always frustrated when [...]

- type: textarea
  id: feature-description
  attributes:
  label: Describe the solution you'd like
  description: A clear and concise description of what you want to happen.
  validations:
  required: true

- type: textarea
  id: feature-alternatives
  attributes:
  label: Describe alternatives you've considered
  description: A clear and concise description of any alternative solutions or features you've considered.

- type: textarea
  id: feature-additional-context
  attributes:
  label: Additional context
  description: Anything else that did not fit in the other categories.