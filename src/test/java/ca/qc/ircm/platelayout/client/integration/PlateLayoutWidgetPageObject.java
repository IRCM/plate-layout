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

package ca.qc.ircm.platelayout.client.integration;

import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.COLUMNS;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.RESIZE;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.ROWS;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.STYLE;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.STYLE_BUTTON;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;

import ca.qc.ircm.platelayout.client.PlateLayoutWidget;
import ca.qc.ircm.platelayout.test.config.AbstractTestBenchTestCase;
import ca.qc.ircm.platelayout.test.config.TestPlateLayoutView;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elementsbase.AbstractElement;

public abstract class PlateLayoutWidgetPageObject extends AbstractTestBenchTestCase {
  protected void open() {
    openView(TestPlateLayoutView.VIEW_NAME);
  }

  protected AbstractElement plateLayout() {
    return wrap(AbstractElement.class, findElement(className(PlateLayoutWidget.CLASSNAME)));
  }

  protected int getPlateLayoutColumns() {
    return plateLayout().findElement(tagName("tr")).findElements(tagName("td")).size();
  }

  protected int getPlateLayoutRows() {
    return plateLayout().findElements(tagName("tr")).size();
  }

  protected AbstractElement plateLayoutCell(int column, int row) {
    return wrap(AbstractElement.class,
        plateLayout().findElements(tagName("tr")).get(row).findElements(tagName("td")).get(column));
  }

  protected LabelElement plateLayoutCellLabel(int column, int row) {
    return wrap(AbstractElement.class,
        plateLayout().findElements(tagName("tr")).get(row).findElements(tagName("td")).get(column))
            .$(LabelElement.class).first();
  }

  protected TextFieldElement columnsField() {
    return $(TextFieldElement.class).id(COLUMNS);
  }

  protected void setColumns(int columns) {
    columnsField().setValue(String.valueOf(columns));
  }

  protected TextFieldElement rowsField() {
    return $(TextFieldElement.class).id(ROWS);
  }

  protected void setRows(int rows) {
    rowsField().setValue(String.valueOf(rows));
  }

  protected ButtonElement resizeButton() {
    return $(ButtonElement.class).id(RESIZE);
  }

  protected void clickResize() {
    resizeButton().click();
  }

  protected TextFieldElement styleField() {
    return $(TextFieldElement.class).id(STYLE);
  }

  protected void setStyle(String style) {
    styleField().setValue(style);
  }

  protected ButtonElement styleButton() {
    return $(ButtonElement.class).id(STYLE_BUTTON);
  }

  protected void clickStyle() {
    styleButton().click();
  }
}
