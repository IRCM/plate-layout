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
import com.vaadin.testbench.elements.NotificationElement;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Additional functions for TestBenchTestCase.
 */
public abstract class AbstractTestBenchTestCase extends TestBenchTestCase {
  private static final long MAX_WAIT = 500;
  private static final long SINGLE_WAIT = 100;
  @Value("http://localhost:${local.server.port}")
  protected String baseUrl;

  protected String homeUrl() {
    return baseUrl + "/";
  }

  protected String viewUrl(String view) {
    return baseUrl + "/#!" + view;
  }

  /**
   * This method loads a page and fixes the issue with get method returning too quickly.
   *
   * @param view
   *          view to load
   */
  public void openView(String view) {
    openView(view, null);
  }

  /**
   * This method loads a page and fixes the issue with get method returning too quickly.
   *
   * @param view
   *          view to load
   * @param parameters
   *          view parameters
   */
  public void openView(String view, String parameters) {
    String url = viewUrl(view);
    if (parameters != null && !parameters.isEmpty()) {
      url += "/" + parameters;
    }
    if (url.equals(getDriver().getCurrentUrl())) {
      getDriver().navigate().refresh();
    } else {
      getDriver().get(url);
    }
    waitForPageLoad();
  }

  public void waitForPageLoad() {
    findElement(By.className("v-loading-indicator"));
  }

  public void waitForNotificationCaption(NotificationElement notification) {
    waitFor(() -> !notification.getCaption().isEmpty());
  }

  private void waitFor(Supplier<Boolean> condition) {
    long totalWait = 0;
    try {
      while (!condition.get() && totalWait < MAX_WAIT) {
        Thread.sleep(SINGLE_WAIT);
        totalWait += SINGLE_WAIT;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  protected <T> Optional<T> optional(Supplier<T> supplier) {
    try {
      return Optional.of(supplier.get());
    } catch (Throwable e) {
      return Optional.empty();
    }
  }
}