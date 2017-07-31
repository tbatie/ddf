package org.codice.ddf.itests.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.karaf.features.BundleInfo;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class SampleService implements SampleServiceI {


    Set<String> locations;

    public SampleService() throws Exception {
        System.out.println("sample service started");
        try {
            locations = getAllRunningBundlesCoordinates();
        } catch (Exception e) {
            System.out.println("oh no exceptions" );
            e.printStackTrace();
        }
    }


    public BundleContext getBundleContext() {
        return FrameworkUtil.getBundle(SampleService.class).getBundleContext();
    }

    public FeaturesService getFeaturesService() {
        ServiceReference<FeaturesService> featuresServiceRef = getBundleContext().getServiceReference(FeaturesService.class);
        return getBundleContext().getService(featuresServiceRef);
    }

    public List<String> getInstalledFeatureNames() throws Exception {
        return Arrays.asList(getFeaturesService().listInstalledFeatures())
                .stream()
                .map(Feature::getName)
                .collect(Collectors.toList());
    }

    public Set<String> getAllRunningBundlesCoordinates() throws Exception {
        Set<String> locations = new HashSet<>();
        List<Feature> installedFeatures = Arrays.asList(getFeaturesService().listInstalledFeatures());
        for(Feature feature: installedFeatures) {
            List<BundleInfo> bundles = feature.getBundles();
            for(BundleInfo bundleInfo : bundles) {
                locations.add(bundleInfo.getLocation());
            }
        }

        return locations;
    }
}
