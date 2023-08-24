# Swagger Petstore Technical Assignment
Job candidate: Piotr Szczepaniak

## General notes

1. I wrote the test ideas **_before_** any hands-on exercises with the API and as such they are based on my understanding of the documentation and other assumptions, mostly experience based. I have retro-actively added more comments and observations as well as fixed most of the wrong assumptions there.
2. Suggested functional test cases are included in a separate file: [FUNCTIONAL TEST CASES.md](FUNCTIONAL%20TEST%20CASES.md)
3. I have also proposed some end-to-end test flows here: [END TO END TEST FLOWS.md](END%20TO%20END%20TEST%20FLOWS.md)
4. This README file will discuss technicalities behind my software solution for the test automation.

## Test framework choices

I have decided to use `TestNG` instead of `JUnit` simply because I am more experienced and accustomed to working with it. At its  core both testing frameworks offer similar features and I can quickly adapt / switch to using JUnit if needed. I tried to demonstrate some of those features, like using a simple `DataProvider` for a small, data-driven test case or ordering test cases for specific order of execution and adding explicit dependencies between test methods.

## No Cucumber

I didn't include *Cucumber* because I haven't personally worked with it. `RestAssured` already encourages BDD-style of writing API tests and it also includes Hamcrest matchers for more natural and expressive assertion statements which is good enough for me :) I will be happy to discuss and learn benefits of layering Cucumber on top RestAssured!

## Room for improvement

The solution has potential for more improvement. There's some code duplication (I was thinking about moving some shared items like the BASE URI to a config class what would be shared by all test cases). Another option would be to use RequestSpec builder and then add / modify elements of the request depending on the test case. I am also using one helper method to load Json file - that should ideally be moved to a shared "utils" class I guess. I also ended up with some tight coupling between api requests and expected data in the assertions.

All in all I decided to finish at this point and share the solution. I hope that I have demonstrated enough practical knowledge of Java and RestAssured / TestNG.

