# AbnishAutomation

A hybrid test automation framework for web UI and REST API testing, built on
**Selenium WebDriver**, **Cucumber BDD**, and **TestNG**, with **Extent Reports**
for reporting and **GitHub Actions** for continuous integration.

The suite demonstrates a production-shaped structure: page objects, externalised
configuration, driver lifecycle management via hooks, screenshot capture on
failure, and API tests using request/response POJOs.

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Build | Maven |
| Web automation | Selenium WebDriver + WebDriverManager |
| API testing | REST Assured |
| BDD | Cucumber (Gherkin feature files, step definitions) |
| Test runner | TestNG (`AbstractTestNGCucumberTests`) |
| Dependency injection | Guice (`cucumber-picocontainer`-style scenario scoping) |
| Reporting | Extent Reports (Spark), ReportNG, Cucumber timeline |
| Logging | Log4j |
| CI | GitHub Actions |

---

## Project structure

```
src/test/java/
├── api/               # REST Assured API tests (serialization, OAuth, JSON validation)
├── ApiPojo/           # Request/response POJOs for API payloads
├── pages/             # Page Object Model classes
├── stepDefnition/     # Cucumber step definitions (web + API)
├── setup/             # DriverFactory, ConfigReader, ApplicationHooks
├── testrunners/       # MyTestRunner - Cucumber + TestNG entry point
└── util/              # ElementUtil, SoftAssert, Constants, helpers

src/test/resources/
├── features/          # Gherkin .feature files
├── extent-config.xml  # Extent report styling
└── extent.properties  # Extent report configuration

config.properties      # Base URLs, browser selection
ObjectRepository.json  # Externalised locators
testng.xml             # Suite definition (parallel = classes, 3 threads)
```

---

## Design notes

**Driver lifecycle.** `DriverFactory` builds the WebDriver instance from
`config.properties` (browser name and view mode) and hands it to
`ApplicationHooks`, which starts the browser before each scenario and quits it
after. Drivers are held per-thread so scenarios stay isolated under parallel
execution.

**Configuration.** No URLs, browsers, or locators are hardcoded in test logic.
`ConfigReader` loads `config.properties` and `ObjectRepository.json`, so
switching environment or browser requires no code change.

**Failure evidence.** `ApplicationHooks` captures a Base64 screenshot on every
failed step and embeds it directly in the Extent report, so a red test comes
with the screen that produced it.

**API layer.** API tests are POJO-driven rather than string-concatenated JSON:
`ApiPojo` classes serialize into request bodies and deserialize responses, which
keeps payload changes in one place. Covers status-code assertions, nested JSON
path validation, and an OAuth access-token flow.

---

## Prerequisites

- JDK 17+
- Maven 3.8+
- Chrome (default browser; WebDriverManager resolves the driver binary)

---

## Running the tests

Full suite via the TestNG suite file:

```bash
mvn clean test
```

Run a specific runner:

```bash
mvn test -Dtest=MyTestRunner
```

Override the browser at runtime:

```bash
mvn clean test -Dbrowser=firefox
```

---

## Reports

After a run, reports are written to:

| Report | Location |
|---|---|
| Extent Spark report | `test-output/SparkReport/` |
| Cucumber timeline | `test-output-thread/index.html` |
| TestNG / ReportNG output | `test-output/` |

Open the Extent HTML file in any browser. In CI, all reports are uploaded as a
build artifact named `test-reports`.

---

## Continuous integration

`.github/workflows/ci.yml` runs the full suite on:

- every push to `main`
- every pull request targeting `main`
- manual dispatch (`workflow_dispatch`)

The job provisions Temurin JDK 17, restores the Maven cache, runs `mvn -B clean
test`, and uploads test reports as an artifact — including on failure, so a red
build is still debuggable.

---

## Writing a new test

1. Add a scenario to a `.feature` file under `src/test/resources/features/`.
2. Implement the missing steps in `stepDefnition/` (run once to have Cucumber
   print the snippets).
3. Add locators to `ObjectRepository.json` and a page class under `pages/`.
4. Run `mvn clean test` and check the Extent report.

---

## Roadmap

- [ ] Move credentials out of feature files into environment variables
- [ ] Add cross-browser execution matrix in CI
- [ ] Introduce tagged suites (`@smoke`, `@regression`) for selective runs
- [ ] Enable parallel scenario execution in the Cucumber data provider
