package org.codice.ddf.features.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FeatureFileUtils {

  public static final String FEATURE_NAME_XPATH = "//*[local-name() = 'feature']/@name";

  public static List<String> getFeaturesFromFeaturesFile(String featureFile) {
    XPath xPath = XPathFactory.newInstance().newXPath();
    List<String> featureNames = new ArrayList<>();
    try {
      NodeList features =
          (NodeList)
              xPath
                  .compile(FEATURE_NAME_XPATH)
                  .evaluate(
                      new InputSource(new StringReader(featureFile)), XPathConstants.NODESET);
      for (int i = 0; i < features.getLength(); i++) {
        featureNames.add(features.item(i).getNodeValue());
      }
    } catch (XPathExpressionException e) {
      throw new RuntimeException("Unable to find features names in feature file", e);
    }
    return featureNames;
  }

  public static List<Object[]> featureFileToFeatureParameters(String featureFile) {
    return getFeaturesFromFeaturesFile(featureFile)
            .stream()
            .map(feat -> new Object[] {feat})
            .collect(Collectors.toList());
  }
}
