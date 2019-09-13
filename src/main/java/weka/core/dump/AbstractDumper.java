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
 * AbstractDumper.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package weka.core.dump;

import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Ancestor for dumper schemes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractDumper
  implements Serializable, OptionHandler {

  private static final long serialVersionUID = 6424471603637234937L;

  /**
   * Returns a string describing this scheme.
   *
   * @return a description of the filter suitable for displaying in the
   *         explorer/experimenter gui
   */
  public abstract String globalInfo();

  /**
   * Returns an enumeration of all the available options..
   *
   * @return an enumeration of all available options.
   */
  @Override
  public Enumeration<Option> listOptions() {
    return new Vector<Option>().elements();
  }

  /**
   * Sets the options to parse.
   *
   * @param options	the options
   * @throws Exception	if parsing fails
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    Utils.checkForRemainingOptions(options);
  }

  /**
   * Returns the current setup.
   *
   * @return		the options
   */
  @Override
  public String[] getOptions() {
    return new String[0];
  }

  /**
   * Dumps the data.
   *
   * @param data	the data to dump
   * @throws Exception	if dumping fails
   */
  public abstract void dump(Instances data) throws Exception;
}
