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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import ca.qc.ircm.platelayout.client.PlateLayoutState.WellData;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;

import java.util.List;

@Connect(ca.qc.ircm.platelayout.PlateLayout.class)
public class PlateLayoutConnector extends AbstractComponentContainerConnector {
  private static final long serialVersionUID = 1494962942010670902L;
  PlateLayoutServerRpc rpc = RpcProxy.create(PlateLayoutServerRpc.class, this);

  @Override
  protected Widget createWidget() {
    Widget widget = GWT.create(PlateLayoutWidget.class);
    widget.addDomHandler(new PlateClickHandler(), ClickEvent.getType());
    return widget;
  }

  @Override
  public PlateLayoutWidget getWidget() {
    return (PlateLayoutWidget) super.getWidget();
  }

  @Override
  public PlateLayoutState getState() {
    return (PlateLayoutState) super.getState();
  }

  @Override
  public void onStateChanged(StateChangeEvent stateChangeEvent) {
    super.onStateChanged(stateChangeEvent);

    resizeWidget();
    setCellStyles();
  }

  @Override
  public void
      onConnectorHierarchyChange(ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
    resizeWidget();
    List<ComponentConnector> children = getChildComponents();
    PlateLayoutWidget widget = getWidget();
    widget.clearWells();
    for (ComponentConnector connector : children) {
      WellData data = getState().wellData.get(connector);
      widget.setWidget(data.row + 1, data.column + 1, connector.getWidget());
    }
    setCellStyles();
  }

  @Override
  public void updateCaption(ComponentConnector connector) {
  }

  private void resizeWidget() {
    PlateLayoutState state = getState();
    int columns = state.columns + 1;
    int rows = state.rows + 1;
    PlateLayoutWidget widget = getWidget();
    widget.resizeColumns(columns);
    widget.resizeRows(rows);
  }

  private void setCellStyles() {
    PlateLayoutWidget widget = getWidget();
    for (int row = 0; row < widget.getRowCount(); row++) {
      for (int column = 0; column < widget.getColumnCount(); column++) {
        widget.setCellStyles(row, column, null);
      }
    }
    List<ComponentConnector> children = getChildComponents();
    for (ComponentConnector connector : children) {
      WellData data = getState().wellData.get(connector);
      widget.setCellStyles(data.row + 1, data.column + 1, data.styles);
    }
  }

  private class PlateClickHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      final MouseEventDetails mouseDetails = MouseEventDetailsBuilder
          .buildMouseEventDetails(event.getNativeEvent(), getWidget().getElement());
      int column = getWidget().getCellForEvent(event).getCellIndex();
      int row = getWidget().getCellForEvent(event).getRowIndex();
      if (row == 0 && column == 0) {
        // Ignore upper left corner.
      } else if (row == 0) {
        rpc.columnHeaderClicked(column - 1, mouseDetails);
      } else if (column == 0) {
        rpc.rowHeaderClicked(row - 1, mouseDetails);
      } else {
        rpc.wellClicked(column - 1, row - 1, mouseDetails);
      }
    }
  }
}
