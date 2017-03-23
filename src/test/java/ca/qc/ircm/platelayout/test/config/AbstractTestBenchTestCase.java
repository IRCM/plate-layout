/*
 * Copyright (c) 2016 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.qc.ircm.platelayout.test.config;

import com.vaadin.testbench.TestBenchTestCase;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Additional functions for TestBenchTestCase.
 */
public abstract class AbstractTestBenchTestCase extends TestBenchTestCase {
  private static final String DRIVER_SYSTEM_PROPERTY = "testbench.driver";
  private static final Logger logger = LoggerFactory.getLogger(AbstractTestBenchTestCase.class);
  @Value("http://localhost:${local.server.port}")
  protected String baseUrl;
  private boolean useScreenshotRule = false;

  /**
   * Creates selenium driver before test.
   */
  @Before
  public void createDriver() {
    WebDriver driver = driver();
    setDriver(driver);
    driver.manage().window().setSize(new Dimension(1280, 960));
  }

  /**
   * Quits selenium driver after test.
   */
  @After
  public void quitDriver() {
    getDriver().manage().deleteAllCookies();
    if (!useScreenshotRule) {
      getDriver().quit();
    }
  }

  protected WebDriver driver() {
    String driverClass = System.getProperty(DRIVER_SYSTEM_PROPERTY);
    if (driverClass == null) {
      driverClass = FirefoxDriver.class.getName();
      if (SystemUtils.IS_OS_MAC) {
        // For MacOS, use Chrome as recommended by Vaadin. See
        // https://vaadin.com/docs/-/part/testbench/testbench-known-issues.html
        driverClass = ChromeDriver.class.getName();
      }
    }
    try {
      return (WebDriver) Class.forName(driverClass).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      logger.error("Could not instantiate WebDriver class {}", driverClass);
      throw new IllegalStateException("Could not instantiate WebDriver class " + driverClass, e);
    }
  }

  protected String viewUrl(String view) {
    return baseUrl + "/#!" + view;
  }

  protected void openView(String view) {
    String url = viewUrl(view);
    if (url.equals(getDriver().getCurrentUrl())) {
      getDriver().navigate().refresh();
    } else {
      getDriver().get(url);
    }
  }

  public boolean isUseScreenshotRule() {
    return useScreenshotRule;
  }

  public void setUseScreenshotRule(boolean useScreenshotRule) {
    this.useScreenshotRule = useScreenshotRule;
  }
}
