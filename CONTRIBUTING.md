# Contributing guidelines

So you want to help with the development of Trébol? Thanks so much! 
Please follow guidance as described in this file. These are not mandatory rules, but if you
want your contributions to be accepted eagerly, it is better to follow them as good as you can.

__If you have any questions, don't hesitate to contact @bglamadrid (me) for it.__


## What to help with?
- See the _Issues_ section, first of all.
- Improve existing documentation. It's mostly JavaDoc for now, and even that needs some polishing. The wiki is currently empty, that'd be a good place to start.
- You can help me write better contribution guidelines, too!
- Find code worth improving, performance-wise or clarity-wise.
- Add unit tests for the application. [Check this issue](https://github.com/trebol-ecommerce/spring-boot-backend/issues/22).
- Submit a security review coming from the [OWASP Top Ten](https://owasp.org/www-project-top-ten/) (talk to me if this interests you).


## Submitting issues
- Before submitting an issue, make sure it is not already open and present in there.
- Your issues must include at least one of the following tags: `bug`, `feature`, `enhancement`, `tests`, `refactor` or `documentation`.
- Try to divide your issue into steps whenever possible.
- Details are always appreciated.
- Depending on which tags you chose:
    - `bug` - explain the expected behavior vs the resulting behavior; include a description of the procedure you used to replicate the problem. If you know the solution, include it as well.
    - `feature` - explain the functionality that you wish to add. If you know how it would be implemented, explain that as well.
    - `enhancement` - explain the functionality you wish to improve; and the focus of the improvement itself. If you know or have an idea of the outcome and/or the implementation procedure, include those as well.
    - `tests` - if you're willing to write unit tests, try to submit one (1) issue per test. 
    - `refactor` or `documentation` - briefly explain the changes you want to add.
- During October, you can also add the `hacktoberfest` tag to your issues! And unless stated otherwise, any issues raised this way are completely yours to take care of!


## Working on issues
- If you want to to work on an issue, request that it is assigned to you beforehand.
- If for some reason you are blocked, can't or don't want to go on with something, please let me know so that I free you of your assignment. It's okay to stop when things are not going the way you wish.


## Submitting pull requests
- Make sure your changes don't get in the way of compilation. This repo doesn't have a CI/CD automation process yet.
- Make sure you also describe your changes in the `CHANGELOG.md` file
  - Try to keep it simple and concise.
  - __Do not__ add excessively technical info about the changes, like filenames or line numbers.
  - If you need to do that, you can add comments within the GitHub web interface.
  - You can give yourself authorship of your changes.