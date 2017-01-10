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

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Main Vaadin UI.
 */
@Theme("plate-layout-test")
@SpringUI
@Push(value = PushMode.AUTOMATIC, transport = Transport.LONG_POLLING)
@Widgetset("ca.qc.ircm.platelayout.PlateLayoutWidgetset")
public class MainUi extends UI {
  private static final long serialVersionUID = 5623532890650543834L;
  @Autowired
  private SpringNavigator navigator;

  /**
   * Initialize navigator.
   */
  @PostConstruct
  public void initialize() {
    navigator.init(this, this);
  }

  @Override
  protected void init(VaadinRequest vaadinRequest) {
  }
}
