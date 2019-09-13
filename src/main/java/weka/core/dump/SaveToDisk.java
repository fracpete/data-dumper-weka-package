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
 * SaveToDisk.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package weka.core.dump;

import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSink;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Saves the data to the specified file.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SaveToDisk
  extends AbstractDumper {

  private static final long serialVersionUID = -5971382385452901618L;

  /** the format for the timestamp. */
  public final static String TIMESTAMP_FORMAT = "yyyyMMdd_mmhhss_SSS";
  
  /** the output file. */
  protected File m_OutputFile = new File(".");

  /** whether to append a timestamp to the file (eg "file.arff" -> "file-20190919_164637_234.arff"). */
  protected boolean m_AppendTimestamp = false;

  /** the timetamp format. */
  protected transient SimpleDateFormat m_Formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);

  /**
   * Returns a string describing this scheme.
   *
   * @return a description of the filter suitable for displaying in the
   *         explorer/experimenter gui
   */
  @Override
  public String globalInfo() {
    return "Saves the data to the specified file.\n"
      + "It is possible to append a timestamp, to allow the dumping also work "
      + "within cross-validation (format: " + TIMESTAMP_FORMAT + ").";
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
	"\tThe file to save the data to.\n"
	  + "\t(default: .)",
	"output-file", 1, "-output-file <file>"));

    result.addElement(
      new Option(
	"\tWhether to append a timestamp to the file name (eg when used in cross-validation).\n"
	+ "\tExample: 'output.arff' -> 'output-20190919_164637_234.arff'\n"
	  + "\t(default: no)",
	"append-timestamp", 0, "-append-timestamp"));

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

    tmpStr = Utils.getOption("output-file", options);
    if (tmpStr.length() != 0)
      setOutputFile(new File(tmpStr));
    else
      setOutputFile(new File("."));

    setAppendTimestamp(Utils.getFlag("append-timestamp", options));

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

    result.add("-output-file");
    result.add("" + getOutputFile());

    if (getAppendTimestamp())
      result.add("-append-timestamp");

    Collections.addAll(result, super.getOptions());

    return result.toArray(new String[0]);
  }

  /**
   * Sets the output file.
   *
   * @param value the output file to use
   */
  public void setOutputFile(File value) {
    m_OutputFile = value;
  }

  /**
   * Gets the output file.
   *
   * @return the output file
   */
  public File getOutputFile() {
    return m_OutputFile;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String outputFileTipText() {
    return "The file to save the data to.";
  }

  /**
   * Sets whether to append a timestamp to the filename.
   *
   * @param value true if to append
   */
  public void setAppendTimestamp(boolean value) {
    m_AppendTimestamp = value;
  }

  /**
   * Returns whether to append a timestamp to the filename.
   *
   * @return true if counter appended
   */
  public boolean getAppendTimestamp() {
    return m_AppendTimestamp;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String appendTimetsampTipText() {
    return "If enabled, a timestamp is appended to the file name (format: " + TIMESTAMP_FORMAT + ").";
  }

  /**
   * Dumps the data.
   *
   * @param data	the data to dump
   * @throws Exception	if dumping fails
   */
  @Override
  public void dump(Instances data) throws Exception {
    File	outputFile;
    String	timestamp;

    if (!m_OutputFile.isDirectory()) {
      if (m_AppendTimestamp) {
        if (m_Formatter == null)
          m_Formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        timestamp = m_Formatter.format(new Date());
        if (m_OutputFile.getName().contains(".")) {
	  outputFile = new File(m_OutputFile.getParentFile().getAbsolutePath()
	    + File.separator
	    + m_OutputFile.getName().substring(0, m_OutputFile.getName().lastIndexOf('.'))
	    + "-"
	    + timestamp
	    + m_OutputFile.getName().substring(m_OutputFile.getName().lastIndexOf('.')));
	}
	else {
          outputFile = new File(m_OutputFile.getAbsolutePath() + "-" + timestamp);
	}
      }
      else {
        outputFile = m_OutputFile;
      }
      DataSink.write(outputFile.getAbsolutePath(), data);
    }
  }
}
