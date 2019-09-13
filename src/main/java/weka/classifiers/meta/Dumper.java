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

package weka.classifiers.meta;

import weka.classifiers.SingleClassifierEnhancer;
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
 * Forwards the training data to the selected data dumper before training the base classifier.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Dumper
  extends SingleClassifierEnhancer {

  /** the dumper */
  protected AbstractDumper m_Dumper = new Null();

  /**
   * Returns a string describing this filter.
   *
   * @return a description of the filter suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String globalInfo() {
    return "Forwards the training data to the selected data dumper before training the base classifier.";
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
   * Builds the classifier.
   *
   * @param data	the training data
   * @throws Exception	if build fails
   */
  @Override
  public void buildClassifier(Instances data) throws Exception {
    m_Dumper.dump(data);
    m_Classifier.buildClassifier(data);
  }

  /**
   * Generates a class distribution for the instance.
   *
   * @param instance	the instance to predict
   * @return		the class distribution
   * @throws Exception	if prediction failed
   */
  @Override
  public double[] distributionForInstance(Instance instance) throws Exception {
    return m_Classifier.distributionForInstance(instance);
  }

  /**
   * Generates a prediction for the instance (0-based label index or regression value).
   *
   * @param instance	the instance to predict
   * @return		the prediction
   * @throws Exception	if prediction failed
   */
  @Override
  public double classifyInstance(Instance instance) throws Exception {
    return m_Classifier.classifyInstance(instance);
  }

  /**
   * Shows the model of the base classifier.
   *
   * @return		the model
   */
  @Override
  public String toString() {
    return m_Classifier.toString();
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
    runClassifier(new Dumper(), args);
  }
}
