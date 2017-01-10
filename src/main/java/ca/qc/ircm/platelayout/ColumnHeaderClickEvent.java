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

package ca.qc.ircm.platelayout;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

/**
 * Column header click event.
 */
public class ColumnHeaderClickEvent extends Event {
  private static final long serialVersionUID = -7127891611808715599L;
  private final int column;
  private final MouseEventDetails details;

  /**
   * Creates column header click event.
   *
   * @param source
   *          source component
   * @param column
   *          column
   */
  public ColumnHeaderClickEvent(Component source, int column) {
    super(source);
    this.column = column;
    this.details = null;
  }

  /**
   * Creates column header click event.
   *
   * @param source
   *          source component
   * @param column
   *          column
   * @param details
   *          mouse details
   */
  public ColumnHeaderClickEvent(Component source, int column, MouseEventDetails details) {
    super(source);
    this.column = column;
    this.details = details;
  }

  public int getColumn() {
    return column;
  }

  public MouseEventDetails getDetails() {
    return details;
  }
}
