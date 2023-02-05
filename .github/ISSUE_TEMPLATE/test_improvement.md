---
name: Test improvement
about: Suggest corrections or additional testing for the application
title: ''
labels: tests, new
assignees: ''

---

<!-- THESE COMMENTS ARE MEANT ONLY FOR YOU TO SEE. PLEASE REMOVE THESE BEFORE SUBMITTING YOUR ISSUE -->

## Summary

<!--

A clear description of the testing cases that needs to be added or improved. You can refer to things such as:

- The target Java class or method (or several of them if it applies)
- Technical context or simple human explanation of the use cases/application flows or events that are involved

If you have several classes, contexts or cases to report, you should try to take advantage of Markdown formatting and separate them into sections accordingly.
In such situations, an ideal summary would look like this:

```md

### `org.trebol.apices.CheckoutServiceImpl`

#### Improve edge cases for method `requestTransactionStart(SellPojo transaction)`

- Behavior not covered by current tests
  - When a request for inmediate checkout of a new order, is sent without providing the detail items for that order, the request should fail because the service doesn't know the contents of the order
  - When the same request type is sent without a billing type, the request should fail because the service doesn't know how to bill the customer/user
- Redundant stubbing
  - Calls to JPA repositories such as `.findById()` will not be called unless the order is processed first, no need to stub before it happens.
- Incorrectly tested behavior
  - A mock service in `org.trebol.apices` was confused with a service in `org.trebol.jpa.services` due to their similar method names and signatures. The correct service type must be used.

```

-->
