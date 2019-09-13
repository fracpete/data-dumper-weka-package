# data-dumper-weka-package
Weka package that allows listening in on data as it passes through filter 
pipelines (aka `weka.filters.MultiFilter`), classifiers or clusterers.


## Available schemes

* Dumper schemes

  * `weka.core.dump.Null` -- dummy, does nothing
  * `weka.core.dump.InMemory` -- not for GUI use, but API use, as it notifies
    registered listeners whenever the data changes.
  * `weka.core.dump.SaveToDisk` -- stores the data in the specified file on disk 
    (auto-detects file format based on extension); allows appending a timestamp
    to output separate files during cross-validation.

* Integration

  * `weka.classifiers.meta.Dumper`
  * `weka.clusterers.Dumper`
  * `weka.filters.Dumper`


## Example usage:

When used in conjunction with the `weka.filters.MultiFilter`, it is possible to output
the data at any given stage in the filter pipeline by inserting `weka.filters.Dumper`
instances where required. Here is an example layout:

```
- weka.filters.MultiFilter
  |
  + weka.filters.Dumper -dumper "weka.core.dump.SaveToDisk -output-file ./1-initial.arff"
  |
  + weka.filters.unsupervised.attribute.AddNoise"
  |
  + weka.filters.Dumper -dumper "weka.core.dump.SaveToDisk -output-file ./2-with_noise.arff"
  |
  + weka.filters.unsupervised.attribute.Normalize"
  |
  + weka.filters.Dumper -dumper "weka.core.dump.SaveToDisk -output-file ./3-normalized.arff"
```

## Releases

* [2019.9.13](https://github.com/fracpete/data-dumper-weka-package/releases/download/v2019.9.13/data-dumper-2019.9.13.zip)


## How to use packages

For more information on how to install the package, see:

https://waikato.github.io/weka-wiki/packages/manager/


## Maven

Use the following dependency in your `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>data-dumper-weka-package</artifactId>
      <version>2019.9.13</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```
