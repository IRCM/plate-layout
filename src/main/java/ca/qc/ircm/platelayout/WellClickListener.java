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

import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;

/**
 * Listener for well click events.
 */
public interface WellClickListener {
  public static final Method WELL_CLICK_METHOD =
      ReflectTools.findMethod(WellClickListener.class, "wellClick", WellClickEvent.class);

  public void wellClick(WellClickEvent event);
}
