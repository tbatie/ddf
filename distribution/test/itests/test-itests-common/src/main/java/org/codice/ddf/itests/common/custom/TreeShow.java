package org.codice.ddf.itests.common.custom;

import static java.lang.String.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.felix.utils.manifest.Clause;
import org.apache.felix.utils.manifest.Parser;
import org.apache.felix.utils.version.VersionRange;
import org.apache.felix.utils.version.VersionTable;
import org.apache.karaf.bundle.core.BundleService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleRevisions;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

//Copied from the karaf tree show command
public class TreeShow {

    private boolean versions;

    private Tree<Bundle> tree;

    BundleService bundleService;

    public TreeShow() {
        bundleService = getBundleContext().getService(getBundleContext().getServiceReference(BundleService.class));
    }
    public Tree<Bundle> doExecute(Bundle bundle) throws Exception {
        tree = new Tree<>(bundle);
        createTree(bundle);
        return tree;
    }

    /**
     * Return a String representation of a bundle state
     */
    private String getState(Bundle bundle) {
        switch (bundle.getState()) {
        case Bundle.UNINSTALLED : return "UNINSTALLED";
        case Bundle.INSTALLED : return "INSTALLED";
        case Bundle.RESOLVED: return "RESOLVED";
        case Bundle.STARTING : return "STARTING";
        case Bundle.STOPPING : return "STOPPING";
        case Bundle.ACTIVE : return "ACTIVE";
        default : return "UNKNOWN";
        }
    }

    /*
     * Print the dependency tree
     */
    private void printTree(Tree<Bundle> tree) {
        tree.write(System.out, node -> {
            if (versions) {
                return format("%s / [%s] [%s]",
                        node.getValue().getSymbolicName(),
                        node.getValue().getVersion().toString(),
                        node.getValue().getBundleId());
            } else {
                return format("%s [%s]",
                        node.getValue().getSymbolicName(),
                        node.getValue().getBundleId());
            }
        });
    }

    /*
     * Check for bundles in the tree exporting the same package
     * as a possible cause for 'Unresolved constraint...' on a uses-conflict
     */
    private void printDuplicatePackages(Tree<Bundle> tree) {
        Set<Bundle> bundles = tree.flatten();
        Map<String, Set<Bundle>> exports = new HashMap<>();

        for (Bundle bundle : bundles) {
            for (BundleRevision revision : bundle.adapt(BundleRevisions.class).getRevisions()) {
                BundleWiring wiring = revision.getWiring();
                if (wiring != null) {
                    List<BundleWire> wires = wiring.getProvidedWires(BundleRevision.PACKAGE_NAMESPACE);
                    if (wires != null) {
                        for (BundleWire wire : wires) {
                            String name = wire.getCapability().getAttributes().get(BundleRevision.PACKAGE_NAMESPACE).toString();
                            exports.computeIfAbsent(name, k -> new HashSet<>()).add(bundle);
                        }
                    }
                }
            }
        }

        for (String pkg : exports.keySet()) {
            if (exports.get(pkg).size() > 1) {
                System.out.printf("%n");
                System.out.printf("WARNING: multiple bundles are exporting package %s%n", pkg);
                for (Bundle bundle : exports.get(pkg)) {
                    System.out.printf("- %s%n", bundle);
                }
            }
        }
    }

    /*
     * Creates the bundle tree
     */
    protected void createTree(Bundle bundle) {
        if (bundle.getState() >= Bundle.RESOLVED) {
            createNode(tree);
        } else {
            createNodesForImports(tree, bundle);
        }
    }

    /*
     * Creates nodes for the imports of the bundle (instead of reporting wiring information
     */
    private void createNodesForImports(Node<Bundle> node, Bundle bundle) {
        Clause[] imports = Parser.parseHeader(bundle.getHeaders().get("Import-Package"));
        Clause[] exports = Parser.parseHeader(bundle.getHeaders().get("Export-Package"));
        for (Clause i : imports) {
            boolean exported = false;
            for (Clause e : exports) {
                if (e.getName().equals(i.getName())) {
                    exported = true;
                    break;
                }
            }
            if (!exported) {
                createNodeForImport(node, bundle, i);
            }
        }
    }

    /*
     * Create a child node for a given import (by finding a matching export in the currently installed bundles)
     */
    private void createNodeForImport(Node<Bundle> node, Bundle bundle, Clause i) {
        VersionRange range = VersionRange.parseVersionRange(i.getAttribute(Constants.VERSION_ATTRIBUTE));
        boolean foundMatch = false;
        for (Bundle b : getBundleContext().getBundles()) {
            BundleWiring wiring = b.adapt(BundleWiring.class);
            if (wiring != null) {
                List<BundleCapability> caps = wiring.getCapabilities(BundleRevision.PACKAGE_NAMESPACE);
                if (caps != null) {
                    for (BundleCapability cap : caps) {
                        String n = getAttribute(cap, BundleRevision.PACKAGE_NAMESPACE);
                        String v = getAttribute(cap, Constants.VERSION_ATTRIBUTE);
                        if (i.getName().equals(n) && range.contains(VersionTable.getVersion(v))) {
                            boolean existing = tree.flatten().contains(b);
                            System.out.printf("- import %s: resolved using %s%n", i, b);
                            foundMatch = true;
                            if (!node.hasChild(b)) {
                                Node<Bundle> child = node.addChild(b);
                                if (!existing) {
                                    createNode(child);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!foundMatch) {
            System.out.printf("- import %s: WARNING - unable to find matching export%n", i);
        }
    }

    private String getAttribute(BundleCapability capability, String name) {
        Object o = capability.getAttributes().get(name);
        return o != null ? o.toString() : null;
    }

    /*
    * Creates a node in the bundle tree
    */
    private void createNode(Node<Bundle> node) {
        Bundle bundle = node.getValue();
        Collection<Bundle> exporters = new HashSet<>();
        exporters.addAll(bundleService.getWiredBundles(bundle).values());

        for (Bundle exporter : exporters) {
            if (node.hasAncestor(exporter)) {
            } else {
                boolean existing = tree.flatten().contains(exporter);
                Node<Bundle> child = node.addChild(exporter);
                if (existing) {
                } else {
                    createNode(child);
                }
            }
        }
    }

    public BundleContext getBundleContext() {
        return FrameworkUtil.getBundle(GenerateFeatureFile.class).getBundleContext();
    }
}
