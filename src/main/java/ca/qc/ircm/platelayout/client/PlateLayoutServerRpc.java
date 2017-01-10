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

package ca.qc.ircm.platelayout.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

/**
 * Server RPC for plate layout.
 */
public interface PlateLayoutServerRpc extends ServerRpc {
  /**
   * Column header was clicked.
   *
   * @param column
   *          column index
   * @param mouseDetails
   *          mouse details
   */
  public void columnHeaderClicked(int column, MouseEventDetails mouseDetails);

  /**
   * Row header was clicked.
   *
   * @param row
   *          row index
   * @param mouseDetails
   *          mouse details
   */
  public void rowHeaderClicked(int row, MouseEventDetails mouseDetails);

  /**
   * Well was clicked.
   *
   * @param column
   *          column index
   * @param row
   *          row index
   * @param mouseDetails
   *          mouse details
   */
  public void wellClicked(int column, int row, MouseEventDetails mouseDetails);
}
