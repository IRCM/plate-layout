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
 * Well click event.
 */
public class WellClickEvent extends Event {
  private static final long serialVersionUID = -7127891611808715599L;
  private final int column;
  private final int row;
  private final MouseEventDetails details;

  /**
   * Creates well click event.
   *
   * @param source
   *          source component
   * @param column
   *          column
   * @param row
   *          row
   */
  public WellClickEvent(Component source, int column, int row) {
    super(source);
    this.column = column;
    this.row = row;
    this.details = null;
  }

  /**
   * Creates well click event.
   *
   * @param source
   *          source component
   * @param column
   *          column
   * @param row
   *          row
   * @param details
   *          mouse details
   */
  public WellClickEvent(Component source, int column, int row, MouseEventDetails details) {
    super(source);
    this.column = column;
    this.row = row;
    this.details = details;
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }

  public MouseEventDetails getDetails() {
    return details;
  }
}
