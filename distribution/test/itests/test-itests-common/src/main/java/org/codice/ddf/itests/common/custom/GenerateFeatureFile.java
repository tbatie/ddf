package org.codice.ddf.itests.common.custom;

import static org.codice.ddf.itests.common.custom.GenerateFeatureFile.NewFeature.getUniqueFeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.features.BundleInfo;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.codice.ddf.itests.common.SampleServiceI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.ImmutableMap;

@Service
@Command(scope = "tracy", name = "gen-feature",
        description = "Creates feature files based off the ids of the given bundles")
public class GenerateFeatureFile implements SampleServiceI, Action {


    private BundleService bundleService;
    private FeaturesService featuresService;

    @Argument(index = 0, name = "ids", description = "Bundles to generate ids from", required = true, multiValued = true)
    List<Long> bundlesIds;

    @Option(name = "-t", aliases={"--createThirdPartyFeatures"}, description="Generates feature files for thirdparty bundles as well.", required = false, multiValued = false)
    boolean createThirdPartyFeatures = false;

    @Option(name = "-u", aliases={"--createUberFeature"}, description="Generates a single feature with all the bundles", required = false, multiValued = false)
    boolean createUberFeature = false;

    @Option(name = "-p", aliases={"--prettyPrint"}, description="A readable non xml format", required = false, multiValued = false)
    boolean prettyPrint = false;

    @Option(name = "-all", aliases={"--genAll"}, description="Generate features for all the bundles in the system running", required = false, multiValued = false)
    boolean genAll = false;

    Map<String, NewFeature> features = new HashMap<>();

    @Override
    public Object execute() throws Exception {
        bundleService = getService(BundleService.class);
        featuresService = getService(FeaturesService.class);

        List<Bundle> bundles = bundlesIds.stream()
                .map(this::getBundle)
                .collect(Collectors.toList());

        if(genAll) {
            bundles = Arrays.asList(getBundleContext().getBundles());
        }

        if(!createThirdPartyFeatures) {
            bundles = bundles.stream()
                    .filter(this::containsDdfName)
                    .collect(Collectors.toList());
        }

        String bundleNames = bundles.stream().map(Bundle::getSymbolicName).collect(Collectors.joining("\n "));

        System.out.println("\n------------------------------------------");
        System.out.println("               FEATURE GENERATOR            ");
        System.out.println("\nSettings: ");
        System.out.println("- createThirdPartyFeatures: " + createThirdPartyFeatures);
        System.out.println("- createUberFeature: " + createUberFeature);
        System.out.println("- Generating features for bundle: " + bundleNames);

        // TODO: tbatie - 8/3/17 - Only create third party features
        // TODO: tbatie - 8/3/17 - Don't include thirdparty bundles
        System.out.println();

        Collection<NewFeature> allAllFeatures = new ArrayList<>();
        for(Bundle bundle : bundles) {
            allAllFeatures.addAll(createFeature(new TreeShow().doExecute(bundle),createThirdPartyFeatures, createUberFeature).getAllCreatedFeatures());
        }

        for(NewFeature feature : getUniqueFeatures(allAllFeatures)) {
            if(prettyPrint) {
                feature.prettyPrint();
            } else {
                feature.print();
            }
        }

        return null;
    }

    public Bundle getBundle(long id) {
        return getBundleContext().getBundle(id);
    }

    public NewFeature createFeature(Node<Bundle> bundle, boolean createThirdPartyFeatures, boolean createUberFeature) {
        if(features.containsKey(NewFeature.createFeatureName(bundle.getValue()))) {
            return features.get(NewFeature.createFeatureName(bundle.getValue()));
        } else {
            NewFeature newFeature = new NewFeature(bundle.getValue());

            for(Node<Bundle> child : bundle.getChildren()) {
                if(!child.getValue().getLocation().contains("mvn")) {
                    System.out.println("weird dependency, skipping: " + child.getValue().getLocation());
                } else if(!createUberFeature && child.getChildren().size() > 0 && (createThirdPartyFeatures || containsDdfName(child.getValue()))) {
                    newFeature.addFeature(createFeature(child, createThirdPartyFeatures, createUberFeature));
                } else {
                    newFeature.addBundles(child.flatten());
                }
            }

            features.put(newFeature.getFeatureName(), newFeature);
            return newFeature;
        }
    }

    public boolean containsDdfName(Bundle bundle) {
        return bundle.getLocation().contains("codice") || bundle.getLocation().contains("ddf");
    }

    public static class NewFeature {

        private String featureName;
        private Bundle createdFromBundle;
        private List<Object> content;

        public NewFeature(Bundle createdFromBundle) {
            this.featureName = createFeatureName(createdFromBundle);
            this.content = new ArrayList<>();
            content.add(createdFromBundle);
            this.createdFromBundle = createdFromBundle;
        }

        public void addBundle(Bundle bundleCoordinate) {
            sortContent();
            content.add(bundleCoordinate);
        }

        public void addBundles(Set<Bundle> bundleCoordinate) {
            sortContent();
            content.addAll(bundleCoordinate);
        }

        public void addFeature(NewFeature feature) {
            sortContent();
            content.add(feature);
        }

        public static String createFeatureName(Bundle bundle) {
            return bundle.getSymbolicName() + "-dependencies";
        }

        public String getFeatureName() {
            return featureName;
        }

        public void sortContent() {
            Collections.sort(content, (o1, o2) -> {
                String obj1 = o1 instanceof NewFeature ? ((NewFeature) o1).getFeatureName() : ((Bundle) o1).getLocation();
                String obj2 = o2 instanceof NewFeature ? ((NewFeature) o2).getFeatureName() : ((Bundle) o2).getLocation();

                return obj1.compareTo(obj2);
            });
        }
        public Collection<NewFeature> getAllCreatedFeatures() {
            List<NewFeature> allCreatedFeatures = new ArrayList<>();
            for(Object obj : content) {
                if(obj instanceof NewFeature) {
                    allCreatedFeatures.add((NewFeature) obj);
                    allCreatedFeatures.addAll(((NewFeature)obj).getAllCreatedFeatures());
                }
            }

            allCreatedFeatures.add(this);
            return allCreatedFeatures;
        }

        public static Collection<NewFeature> getUniqueFeatures(Collection<NewFeature> features) {
            Map<String, NewFeature> uniqueFeatures = new HashMap<>();

            for(NewFeature feature : features) {
                uniqueFeatures.put(feature.featureName, feature);
            }

            List<NewFeature> sortFeature = new ArrayList<>();
            sortFeature.addAll(uniqueFeatures.values());

            Collections.sort(sortFeature, Comparator.comparing(NewFeature::getFeatureName));
            return sortFeature;
        }

        public void prettyPrint() {
            printHeader();
            for (Object object : content) {
                if (object instanceof NewFeature) {
                    System.out.println("- feature: " + ((NewFeature) object).featureName);
                } else {
                    System.out.println("- bundle: " + ((Bundle) object).getLocation());
                }

            }


            System.out.println("- bundle: " + createdFromBundle.getLocation());
            System.out.println();
        }

        public void print() {
            printHeader();
            System.out.println(newFeatureToFeatureFile(this));
            System.out.println();
        }

        public void printHeader() {
            System.out.println("<!-- ============================================ -->");
            System.out.println("<!-- Feature: " + featureName + " ( " + createdFromBundle.getLocation() + " ) -->");
            System.out.println("<!-- ============================================ -->");
        }

        public static final String VARIABLE_DELIMETER = "#";

        public static final String NEW_FEATURE_NAME_SYMBOL = "new.feature.name";
        public static final String BUNDLE_LOCATION_SYMBOL = "bundle.location";
        public static final String FEATURE_NAME_SYMBOL = "feature.name";

        public static final String FEATURE_FILE_HEADER_TEMPLATE = "<feature name=\"" + VARIABLE_DELIMETER + NEW_FEATURE_NAME_SYMBOL + VARIABLE_DELIMETER + "\" install=\"manual\" version=\"${project.version}\" description=\"Auto generated by FEATURE GENERATOR.\">";
        public static final String BUNDLE_TEMPLATE = "\t<bundle>" + VARIABLE_DELIMETER + BUNDLE_LOCATION_SYMBOL + VARIABLE_DELIMETER + "</bundle>";
        public static final String FEATURE_TEMPLATE = "\t<feature>"  + VARIABLE_DELIMETER + FEATURE_NAME_SYMBOL + VARIABLE_DELIMETER + "</feature>";
        public static final String FEATURE_FILE_FOOTER_TEMPLATE = "</feature>";

        public static String newFeatureToFeatureFile(NewFeature newFeature) {
            String featureFile = featureFileHeader(newFeature);
            for(Object child : newFeature.content) {
                if(child instanceof NewFeature) {
                    featureFile = featureFile + "\n" + bundleFeatureFileContent((NewFeature) child);
                } else {
                    featureFile = featureFile + "\n" + bundleFeatureFileContent((Bundle) child);
                }
            }

            return featureFile + "\n" + FEATURE_FILE_FOOTER_TEMPLATE;
        }
        public static String featureFileHeader(NewFeature newFeature) {
            StrSubstitutor strSubstitutor = new StrSubstitutor(ImmutableMap.of(NEW_FEATURE_NAME_SYMBOL, newFeature.getFeatureName()));
            strSubstitutor.setVariablePrefix(VARIABLE_DELIMETER);
            strSubstitutor.setVariableSuffix(VARIABLE_DELIMETER);
            return strSubstitutor.replace(FEATURE_FILE_HEADER_TEMPLATE);
        }

        public static String bundleFeatureFileContent(Bundle bundle) {
            StrSubstitutor strSubstitutor = new StrSubstitutor(ImmutableMap.of(BUNDLE_LOCATION_SYMBOL, bundle.getLocation()));
            strSubstitutor.setVariablePrefix(VARIABLE_DELIMETER);
            strSubstitutor.setVariableSuffix(VARIABLE_DELIMETER);
            return strSubstitutor.replace(BUNDLE_TEMPLATE);
        }

        public static String bundleFeatureFileContent(NewFeature feature) {
            ImmutableMap params = ImmutableMap.of(FEATURE_NAME_SYMBOL, feature.getFeatureName());
            StrSubstitutor strSubstitutor = new StrSubstitutor(params);
            strSubstitutor.setVariablePrefix(VARIABLE_DELIMETER);
            strSubstitutor.setVariableSuffix(VARIABLE_DELIMETER);
            return strSubstitutor.replace(FEATURE_TEMPLATE);
        }
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
        return getAllRunningBundleInfosFromFeature().stream()
                .map(BundleInfo::getLocation)
                .collect(Collectors.toSet());
    }

    public Set<BundleInfo> getAllRunningBundleInfosFromFeature() throws Exception {
        Set<BundleInfo> bundles = new HashSet<>();
        List<Feature> installedFeatures =
                Arrays.asList(getFeaturesService().listInstalledFeatures());
        for (Feature feature : installedFeatures) {
            bundles.addAll(feature.getBundles());
        }
        return bundles;
    }

    public List<Bundle> getBundleWithNameContainingStr(String snippet) {
        List<Bundle> bundles = new ArrayList<>();

        for(Bundle bundle : getBundleContext().getBundles()) {
            String bundleName = bundle.getHeaders().get("Bundle-Name");
            if(bundleName != null && StringUtils.containsIgnoreCase(bundleName, snippet)) {
                bundles.add(bundle);
            }
        }

        return bundles;
    }

    public List<Bundle> getBundleWithArtifactIdContainingStr(String snippet) {
        List<Bundle> bundles = new ArrayList<>();

        for(Bundle bundle : getBundleContext().getBundles()) {
            String location = bundle.getLocation();
            if(location != null && StringUtils.containsIgnoreCase(location, snippet)) {
                bundles.add(bundle);
            }
        }

        return bundles;
    }

    public Bundle getFirstBundleWithNameContainingStr(String snippet) {
        List<Bundle> bundles = new ArrayList<>();

        for(Bundle bundle : getBundleContext().getBundles()) {
            String bundleName = bundle.getHeaders().get("Bundle-Name");
            if(bundleName != null && StringUtils.containsIgnoreCase(bundleName, snippet)) {
                return bundle;
            }
        }

        return null;
    }

    public <S> S getService(Class<S> clazz) {
        return getBundleContext().getService(getBundleContext().getServiceReference(clazz));
    }

    public BundleContext getBundleContext() {
        return FrameworkUtil.getBundle(GenerateFeatureFile.class).getBundleContext();
    }

}
