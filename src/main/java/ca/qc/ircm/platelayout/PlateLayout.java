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

import ca.qc.ircm.platelayout.client.PlateLayoutServerRpc;
import ca.qc.ircm.platelayout.client.PlateLayoutState;
import ca.qc.ircm.platelayout.client.PlateLayoutState.WellData;
import com.vaadin.shared.Connector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlateLayout extends com.vaadin.ui.AbstractComponentContainer {
  private static final long serialVersionUID = 1382449218052015699L;
  private int currentRow;
  private int currentColumn;
  private List<Component> children = new ArrayList<>();
  private PlateLayoutServerRpc rpc = new PlateLayoutServerRpc() {
    private static final long serialVersionUID = -3946732969142392399L;

    @Override
    public void columnHeaderClicked(int column, MouseEventDetails mouseDetails) {
      fireEvent(new ColumnHeaderClickEvent(PlateLayout.this, column, mouseDetails));
    }

    @Override
    public void rowHeaderClicked(int row, MouseEventDetails mouseDetails) {
      fireEvent(new RowHeaderClickEvent(PlateLayout.this, row, mouseDetails));
    }

    @Override
    public void wellClicked(int column, int row, MouseEventDetails mouseDetails) {
      fireEvent(new WellClickEvent(PlateLayout.this, column, row, mouseDetails));
    }
  };

  public PlateLayout() {
    registerRpc(rpc);
  }

  /**
   * Creates plate layout with specified number of columns and rows.
   *
   * @param columns
   *          number of columns
   * @param rows
   *          number of rows
   */
  public PlateLayout(int columns, int rows) {
    this();
    setColumns(columns);
    setRows(rows);
  }

  private void validatePosition(int column, int row) {
    if (column < 0 || column >= getColumns()) {
      throw new IndexOutOfBoundsException(
          "column " + column + " not between 0 and " + getColumns());
    }
    if (row < 0 || row >= getRows()) {
      throw new IndexOutOfBoundsException("row " + row + " not between 0 and " + getRows());
    }
  }

  private Optional<Connector> findPositionConnector(int column, int row) {
    return getState().wellData.entrySet().stream()
        .filter(e -> e.getValue().column == column && e.getValue().row == row).map(e -> e.getKey())
        .findAny();
  }

  private boolean containsPosition(int column, int row) {
    return findPositionConnector(column, row).isPresent();
  }

  private WellData createWellData(int column, int row) {
    WellData data = new WellData();
    data.column = column;
    data.row = row;
    return data;
  }

  /**
   * Adds component at specified coordinates.
   *
   * @param component
   *          component
   * @param column
   *          column
   * @param row
   *          row
   */
  public void addComponent(Component component, int column, int row) {
    if (component == null) {
      throw new NullPointerException("Component must not be null");
    }
    if (children.contains(component)) {
      throw new IllegalArgumentException("Component is already in the container");
    }
    validatePosition(column, row);
    if (containsPosition(column, row)) {
      throw new IllegalArgumentException(
          "A component already exists at position " + column + "-" + row);
    }

    children.add(component);
    getState().wellData.put(component, createWellData(column, row));
    currentColumn = column;
    currentRow = row;
    super.addComponent(component);
    markAsDirty();
  }

  @Override
  public void addComponent(Component component) {
    int column = currentColumn;
    int row = currentRow;
    while (containsPosition(column, row)) {
      if (column < getColumns() - 1) {
        column++;
      } else if (row < getRows() - 1) {
        column = 0;
        row++;
      } else {
        throw new IllegalStateException("Component cannot be added, container is full");
      }
    }
    addComponent(component, column, row);
  }

  @Override
  public void replaceComponent(Component oldComponent, Component newComponent) {
    WellData oldData = getState().wellData.get(oldComponent);
    WellData newData = getState().wellData.get(newComponent);
    if (oldData == null) {
      addComponent(newComponent);
    } else if (newData == null) {
      removeComponent(oldComponent);
      addComponent(newComponent, oldData.column, oldData.row);
    } else {
      getState().wellData.put(newComponent, oldData);
      getState().wellData.put(oldComponent, newData);
    }
  }

  @Override
  public void removeComponent(Component component) {
    if (component == null || !children.contains(component)) {
      return;
    }

    children.remove(component);
    getState().wellData.remove(component);
    super.removeComponent(component);
    markAsDirty();
  }

  /**
   * Removes component at specified coordinates.
   *
   * @param column
   *          column
   * @param row
   *          row
   */
  public void removeComponent(int column, int row) {
    validatePosition(column, row);
    if (!containsPosition(column, row)) {
      return;
    }

    Component component = (Component) findPositionConnector(column, row).orElse(null);
    removeComponent(component);
  }

  public void clickColumnHeader(int column) {
    validatePosition(column, 0);
    fireEvent(new ColumnHeaderClickEvent(PlateLayout.this, column));
  }

  public void clickRowHeader(int row) {
    validatePosition(0, row);
    fireEvent(new RowHeaderClickEvent(PlateLayout.this, row));
  }

  public void clickWell(int column, int row) {
    validatePosition(column, row);
    fireEvent(new WellClickEvent(PlateLayout.this, column, row));
  }

  public void addColumnHeaderClickListener(ColumnHeaderClickListener listener) {
    addListener(ColumnHeaderClickEvent.class, listener,
        ColumnHeaderClickListener.COLUMN_HEADER_CLICK_METHOD);
  }

  public void removeColumnHeaderClickListener(ColumnHeaderClickListener listener) {
    removeListener(ColumnHeaderClickEvent.class, listener);
  }

  public void addRowHeaderClickListener(RowHeaderClickListener listener) {
    addListener(RowHeaderClickEvent.class, listener,
        RowHeaderClickListener.ROW_HEADER_CLICK_METHOD);
  }

  public void removeRowHeaderClickListener(RowHeaderClickListener listener) {
    removeListener(RowHeaderClickEvent.class, listener);
  }

  public void addWellClickListener(WellClickListener listener) {
    addListener(WellClickEvent.class, listener, WellClickListener.WELL_CLICK_METHOD);
  }

  public void removeWellClickListener(WellClickListener listener) {
    removeListener(WellClickEvent.class, listener);
  }

  private List<String> wellStyles(int column, int row) {
    Component component = (Component) findPositionConnector(column, row).orElse(null);
    if (component != null) {
      WellData data = getState().wellData.get(component);
      if (data.styles == null) {
        data.styles = new ArrayList<>();
      }
      return data.styles;
    } else {
      return new ArrayList<>();
    }
  }

  public String getWellStyleName(int column, int row) {
    validatePosition(column, row);
    return wellStyles(column, row).stream().collect(Collectors.joining(" "));
  }

  /**
   * Adds style to well.
   *
   * @param column
   *          column
   * @param row
   *          row
   * @param styleName
   *          style to add
   */
  public void addWellStyleName(int column, int row, String styleName) {
    validatePosition(column, row);
    wellStyles(column, row).add(styleName);
    markAsDirty();
  }

  /**
   * Removes a style from well.
   *
   * @param column
   *          column
   * @param row
   *          row
   * @param styleName
   *          style to remove
   */
  public void removeWellStyleName(int column, int row, String styleName) {
    validatePosition(column, row);
    wellStyles(column, row).remove(styleName);
    markAsDirty();
  }

  /**
   * Removes all styles from well.
   *
   * @param column
   *          column
   * @param row
   *          row
   */
  public void clearWellStyleName(int column, int row) {
    validatePosition(column, row);
    wellStyles(column, row).clear();
    markAsDirty();
  }

  @Override
  public int getComponentCount() {
    return children.size();
  }

  @Override
  public Iterator<Component> iterator() {
    return children.iterator();
  }

  public int getColumns() {
    return getState().columns;
  }

  /**
   * Sets number of columns.
   *
   * @param columns
   *          number of columns
   */
  public void setColumns(int columns) {
    if (columns < 0) {
      throw new IllegalArgumentException("columns " + columns + " not above 0");
    }

    getState().columns = columns;
    markAsDirty();
  }

  public int getRows() {
    return getState().rows;
  }

  /**
   * Sets number of rows.
   *
   * @param rows
   *          number of rows
   */
  public void setRows(int rows) {
    if (rows < 0) {
      throw new IllegalArgumentException("rows " + rows + " not above 0");
    }

    getState().rows = rows;
    markAsDirty();
  }

  @Override
  public PlateLayoutState getState() {
    return (PlateLayoutState) super.getState();
  }
}
