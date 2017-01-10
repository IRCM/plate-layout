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

import com.vaadin.shared.Connector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plate layout state.
 */
public class PlateLayoutState extends com.vaadin.shared.AbstractComponentState {
  private static final long serialVersionUID = -1122609654126841136L;
  public int columns;
  public int rows;
  public Map<Connector, WellData> wellData = new HashMap<>();

  public static class WellData implements Serializable {
    private static final long serialVersionUID = -1900301599932252390L;
    public int column;
    public int row;
    public List<String> styles = null;
  }
}
