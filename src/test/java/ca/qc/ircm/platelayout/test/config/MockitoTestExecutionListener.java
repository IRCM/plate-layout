package ca.qc.ircm.platelayout.test.config;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Ensure Mockito is used correctly.
 */
public class MockitoTestExecutionListener extends AbstractTestExecutionListener {
  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    MockitoAnnotations.initMocks(testContext.getTestInstance());
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    Mockito.validateMockitoUsage();
  }
}
