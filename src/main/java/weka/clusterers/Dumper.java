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
 * Dumper.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package weka.clusterers;

import weka.core.Drawable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.dump.AbstractDumper;
import weka.core.dump.Null;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Forwards the training data to the selected data dumper before training the base clusterer.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Dumper
  extends SingleClustererEnhancer
  implements Drawable {

  private static final long serialVersionUID = 3849909650437555245L;

  /** the dumper */
  protected AbstractDumper m_Dumper = new Null();

  /**
   * Returns a string describing this filter.
   *
   * @return a description of the filter suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String globalInfo() {
    return "Forwards the training data to the selected data dumper before training the base clusterer.";
  }

  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  @Override
  public Enumeration<Option> listOptions() {
    Vector<Option> result = new Vector<Option>();

    result.addElement(
      new Option(
	"\tThe data dumper to use.\n"
	  + "\t(default: " + Null.class.getName() + ")",
	"dumper", 1, "-dumper <classname + options>"));

    result.addAll(Collections.list(super.listOptions()));

    return result.elements();
  }

  /**
   * Parses a given list of options.
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    String 	tmpStr;
    String 	className;
    String[] 	classOptions;

    tmpStr = Utils.getOption("dumper", options);
    if (tmpStr.length() != 0) {
      classOptions    = Utils.splitOptions(tmpStr);
      className       = classOptions[0];
      classOptions[0] = "";
      setDumper((AbstractDumper) Utils.forName(AbstractDumper.class, className, classOptions));
    }
    else {
      setDumper(new Null());
    }

    super.setOptions(options);
  }

  /**
   * Gets the current settings of the filter.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  @Override
  public String[] getOptions() {
    List<String> result = new ArrayList<String>();

    result.add("-dumper");
    result.add(Utils.toCommandLine(getDumper()));

    Collections.addAll(result, super.getOptions());

    return result.toArray(new String[0]);
  }

  /**
   * Sets the data dumper to use.
   *
   * @param value the data dumper to use
   */
  public void setDumper(AbstractDumper value) {
    m_Dumper = value;
  }

  /**
   * Gets the current data dumper.
   *
   * @return the data dumper
   */
  public AbstractDumper getDumper() {
    return m_Dumper;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String dumperTipText() {
    return "The data dumper to use.";
  }

  /**
   * Generates a clusterer. Has to initialize all fields of the clusterer that
   * are not being set via options.
   *
   * @param data set of instances serving as training data
   * @exception Exception if the clusterer has not been generated successfully
   */
  @Override
  public void buildClusterer(Instances data) throws Exception {
    m_Dumper.dump(data);
    m_Clusterer.buildClusterer(data);
  }

  /**
   * Classifies a given instance after filtering.
   *
   * @param instance the instance to be classified
   * @return the class distribution for the given instance
   * @throws Exception if instance could not be classified successfully
   */
  @Override
  public double[] distributionForInstance(Instance instance) throws Exception {
    return m_Clusterer.distributionForInstance(instance);
  }

  /**
   * Returns the type of graph this clusterer represents.
   *
   * @return the graph type of this clusterer
   */
  public int graphType() {
    if (m_Clusterer instanceof Drawable)
      return ((Drawable) m_Clusterer).graphType();
    else
      return Drawable.NOT_DRAWABLE;
  }

  /**
   * Returns graph describing the clusterer (if possible).
   *
   * @return the graph of the clusterer in dotty format
   * @throws Exception if the clusterer cannot be graphed
   */
  public String graph() throws Exception {
    if (m_Clusterer instanceof Drawable)
      return ((Drawable) m_Clusterer).graph();
    else
      throw new Exception("Clusterer: " + getClustererSpec() + " cannot be graphed");
  }

  /**
   * Shows the model of the base classifier.
   *
   * @return		the model
   */
  @Override
  public String toString() {
    return m_Clusterer.toString();
  }

  /**
   * Returns the revision string.
   *
   * @return the revision
   */
  @Override
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 1 $");
  }

  /**
   * Main method for testing this class.
   *
   * @param args the commandline options, use "-h" for help
   */
  public static void main(String[] args) {
    runClusterer(new Dumper(), args);
  }
}
