package ca.qc.ircm.platelayout.test.config;

import static org.junit.Assume.assumeTrue;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBenchTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

/**
 * Rule for integration tests using Vaadin's test bench.
 */
@Order(TestBenchTestExecutionListener.ORDER)
public class TestBenchTestExecutionListener extends AbstractTestExecutionListener {
  public static final int ORDER = 0;
  private static final String LICENSE_ERROR_MESSAGE =
      "License for Vaadin TestBench not found. Skipping test class {0} .";
  private static final String[] LICENSE_PATHS =
      new String[] { "vaadin.testbench.developer.license", ".vaadin.testbench.developer.license" };
  private static final String LICENSE_SYSTEM_PROPERTY = "vaadin.testbench.developer.license";
  private static final String SKIP_TESTS_ERROR_MESSAGE = "TestBench tests are skipped";
  private static final String SKIP_TESTS_SYSTEM_PROPERTY = "testbench.skip";
  private static final String RETRIES_SYSTEM_PROPERTY = "testbench.retries";
  private static final Logger logger =
      LoggerFactory.getLogger(TestBenchTestExecutionListener.class);

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    if (isTestBenchTest(testContext)) {
      if (isSkipTestBenchTests()) {
        assumeTrue(SKIP_TESTS_ERROR_MESSAGE, false);
      }

      boolean licenseFileExists = false;
      for (String licencePath : LICENSE_PATHS) {
        licenseFileExists |=
            Files.exists(Paths.get(System.getProperty("user.home")).resolve(licencePath));
      }
      if (!licenseFileExists && System.getProperty(LICENSE_SYSTEM_PROPERTY) == null) {
        String message =
            MessageFormat.format(LICENSE_ERROR_MESSAGE, testContext.getTestClass().getName());
        logger.info(message);
        assumeTrue(message, false);
      }
      setRetries();
    }
  }

  private boolean isSkipTestBenchTests() {
    return Boolean.valueOf(System.getProperty(SKIP_TESTS_SYSTEM_PROPERTY));
  }

  private boolean isTestBenchTest(TestContext testContext) {
    return TestBenchTestCase.class.isAssignableFrom(testContext.getTestClass());
  }

  private void setRetries() {
    if (System.getProperty(RETRIES_SYSTEM_PROPERTY) != null) {
      Parameters.setMaxAttempts(Integer.parseInt(System.getProperty(RETRIES_SYSTEM_PROPERTY)));
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
