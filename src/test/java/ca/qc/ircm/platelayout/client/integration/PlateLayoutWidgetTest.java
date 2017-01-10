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

import static ca.qc.ircm.platelayout.client.PlateLayoutWidget.CLASSNAME;
import static ca.qc.ircm.platelayout.client.PlateLayoutWidget.COLUMN_HEADER_CLASSNAME;
import static ca.qc.ircm.platelayout.client.PlateLayoutWidget.HEADER_CLASSNAME;
import static ca.qc.ircm.platelayout.client.PlateLayoutWidget.ROW_HEADER_CLASSNAME;
import static ca.qc.ircm.platelayout.client.PlateLayoutWidget.WELL_CLASSNAME;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.COLUMNS_DEFAULT;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.COLUMN_CLICKED;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.ROWS_DEFAULT;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.ROW_CLICKED;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.WELL_CLICKED;
import static ca.qc.ircm.platelayout.test.config.TestPlateLayoutView.WELL_LABEL_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.qc.ircm.platelayout.test.config.TestBenchTestAnnotations;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elementsbase.AbstractElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TestBenchTestAnnotations
public class PlateLayoutWidgetTest extends PlateLayoutWidgetPageObject {
  private int columns = COLUMNS_DEFAULT + 1;
  private int rows = ROWS_DEFAULT + 1;

  private void validateCellStyles() {
    assertTrue(plateLayoutCell(0, 0).getAttribute("class").contains(HEADER_CLASSNAME));
    for (int column = 1; column < getPlateLayoutColumns(); column++) {
      AbstractElement cell = plateLayoutCell(column, 0);
      assertTrue(cell.getAttribute("class").contains(HEADER_CLASSNAME));
      assertTrue(cell.getAttribute("class").contains(COLUMN_HEADER_CLASSNAME));
      assertEquals(String.valueOf((char) ('A' + column - 1)), cell.getText());
    }
    for (int row = 1; row < getPlateLayoutRows(); row++) {
      AbstractElement cell = plateLayoutCell(0, row);
      assertTrue(cell.getAttribute("class").contains(HEADER_CLASSNAME));
      assertTrue(cell.getAttribute("class").contains(ROW_HEADER_CLASSNAME));
      assertEquals(String.valueOf((char) ('1' + row - 1)), cell.getText());
    }
    for (int column = 1; column < getPlateLayoutColumns(); column++) {
      for (int row = 1; row < getPlateLayoutRows(); row++) {
        AbstractElement cell = plateLayoutCell(column, row);
        assertTrue(cell.getAttribute("class").contains(WELL_CLASSNAME));
      }
    }
  }

  @Test
  public void init() throws Throwable {
    open();

    assertEquals(columns, getPlateLayoutColumns());
    assertEquals(rows, getPlateLayoutRows());
    assertEquals("0", plateLayout().getAttribute("cellpadding"));
    assertEquals("0", plateLayout().getAttribute("cellspacing"));
    assertTrue(plateLayout().getAttribute("class").contains(CLASSNAME));
    validateCellStyles();
    for (int column = 1; column < getPlateLayoutColumns(); column++) {
      for (int row = 1; row < getPlateLayoutRows(); row++) {
        AbstractElement cell = plateLayoutCell(column, row);
        assertEquals(String.format(WELL_LABEL_VALUE, column - 1, row - 1),
            cell.$(LabelElement.class).first().getText());
      }
    }
  }

  @Test
  public void resizeColumns_Increase() {
    open();
    int columns = this.columns + 2;

    setColumns(columns - 1);
    clickResize();

    assertEquals(columns, getPlateLayoutColumns());
    assertEquals(rows, getPlateLayoutRows());
    validateCellStyles();
  }

  @Test
  public void resizeColumns_Decrease() {
    open();
    int columns = this.columns - 1;

    setColumns(columns - 1);
    clickResize();

    assertEquals(columns, getPlateLayoutColumns());
    assertEquals(rows, getPlateLayoutRows());
    validateCellStyles();
  }

  @Test
  public void resizeRows_Increase() {
    open();
    int rows = this.rows + 2;

    setRows(rows - 1);
    clickResize();

    assertEquals(columns, getPlateLayoutColumns());
    assertEquals(rows, getPlateLayoutRows());
    validateCellStyles();
  }

  @Test
  public void resizeRows_Decrease() {
    open();
    int rows = this.rows - 1;

    setRows(rows - 1);
    clickResize();

    assertEquals(columns, getPlateLayoutColumns());
    assertEquals(rows, getPlateLayoutRows());
    validateCellStyles();
  }

  @Test
  public void clickUpperLeftCorner() {
    open();

    plateLayoutCell(0, 0).click();

    List<String> styles = Arrays.asList(plateLayout().getAttribute("class").split(" "));
    assertEquals(2, styles.size());
    assertTrue(styles.contains("v-widget"));
    assertTrue(styles.contains(CLASSNAME));
  }

  @Test
  public void clickColumnHeader() {
    open();

    plateLayoutCell(3, 0).click();

    assertTrue(plateLayout().getAttribute("class").contains(String.format(COLUMN_CLICKED, 2)));
  }

  @Test
  public void clickRowHeader() {
    open();

    plateLayoutCell(0, 2).click();

    assertTrue(plateLayout().getAttribute("class").contains(String.format(ROW_CLICKED, 1)));
  }

  @Test
  public void clickWell() {
    open();

    plateLayoutCell(3, 2).click();

    assertTrue(plateLayout().getAttribute("class").contains(String.format(WELL_CLICKED, 2, 1)));
  }

  @Test
  public void setStyle() {
    open();

    setStyle("test");
    clickStyle();

    for (int column = 1; column < getPlateLayoutColumns(); column++) {
      for (int row = 1; row < getPlateLayoutRows(); row++) {
        AbstractElement cell = plateLayoutCell(column, row);
        assertTrue(cell.getAttribute("class").contains(WELL_CLASSNAME));
        assertTrue(cell.getAttribute("class").contains("test"));
      }
    }

    setStyle("");
    clickStyle();

    for (int column = 1; column < getPlateLayoutColumns(); column++) {
      for (int row = 1; row < getPlateLayoutRows(); row++) {
        AbstractElement cell = plateLayoutCell(column, row);
        assertTrue(cell.getAttribute("class").contains(WELL_CLASSNAME));
        assertFalse(cell.getAttribute("class").contains("test"));
      }
    }
  }
}