package org.codice.ddf.features.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FeatureFileUtils {

  public static final String FEATURE_NAME_XPATH =  "/features/feature[@name='*']/name";

  public static List<String> getFeaturesFromFeaturesFile(String featureFilePath) {
    XPath xPath = XPathFactory.newInstance().newXPath();
    File featureFile = new File(featureFilePath);

    try {
      NodeList features =  (NodeList) xPath.compile(FEATURE_NAME_XPATH).evaluate(new InputSource(new FileInputStream(featureFile)),
              XPathConstants.NODESET);
      for(int i =0; i < features.getLength(); i++) {
        Node feature = features.item(i);
      }
      return new ArrayList<>();
    } catch (XPathExpressionException e) {
      throw new RuntimeException("Unable to find features names in feature file", e);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Feature file not found: " + featureFile.getAbsolutePath(), e);
    }
  }

  public static List<Object[]> featureFileToFeatureParameters(URL featureFile) {
    return getFeaturesFromFeaturesFile(Paths.get(featureFile.getPath()).toAbsolutePath().toString())
            .stream()
            .map(feat -> new Object[] {feat})
            .collect(Collectors.toList());
  }

  public static List<Object[]> featureFileToFeatureParameters(String featureFile) {
    return getFeaturesFromFeaturesFile(featureFile)
            .stream()
            .map(feat -> new Object[] {feat})
            .collect(Collectors.toList());
  }
}
