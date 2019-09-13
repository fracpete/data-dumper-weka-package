/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * InMemory.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package weka.classifiers.meta;

import weka.core.Instances;
import weka.core.dump.AbstractDumper;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps the data in memory and notifies any registered listeners when data changes.
 * Not to be used from GUI, but from an API point of view.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class InMemory
  extends AbstractDumper {

  private static final long serialVersionUID = -5971382385452901618L;

  /** the data. */
  protected transient Instances m_Data = null;

  /** the listeners. */
  protected transient Set<ChangeListener> m_Listeners;

  /**
   * Returns a string describing this scheme.
   *
   * @return a description of the filter suitable for displaying in the
   *         explorer/experimenter gui
   */
  @Override
  public String globalInfo() {
    return "Keeps the data in memory and notifies any registered listeners when data changes.\n"
      + "Not to be used from GUI, but from an API point of view.";
  }

  /**
   * Returns the current data.
   *
   * @return		the data, null if none available (yet)
   */
  public Instances getData() {
    return m_Data;
  }

  /**
   * Adds the listener to its internal list to notify if data changes.
   *
   * @param l		the listener to add
   */
  public void addListener(ChangeListener l) {
    if (m_Listeners == null)
      m_Listeners = new HashSet<ChangeListener>();
    m_Listeners.add(l);
  }

  /**
   * Removes the listener from its internal list to notify if data changes.
   *
   * @param l		the listener to remove
   */
  public void removeListener(ChangeListener l) {
    if (m_Listeners == null)
      m_Listeners = new HashSet<ChangeListener>();
    m_Listeners.remove(l);
  }

  /**
   * Notifies all the listeners that the data has changed.
   */
  protected void notifyListeners() {
    ChangeEvent e;

    if (m_Listeners == null)
      return;

    e = new ChangeEvent(this);
    for (ChangeListener l: m_Listeners)
      l.stateChanged(e);
  }

  /**
   * Dumps the data.
   *
   * @param data	the data to dump
   * @throws Exception	if dumping fails
   */
  @Override
  public void dump(Instances data) throws Exception {
    m_Data = data;
    notifyListeners();
  }
}
