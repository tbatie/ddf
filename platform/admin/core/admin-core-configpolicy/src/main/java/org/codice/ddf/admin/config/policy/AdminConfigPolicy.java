/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.admin.config.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.Permission;

import com.google.common.collect.Sets;

import ddf.security.permission.CollectionPermission;
import ddf.security.permission.KeyValueCollectionPermission;
import ddf.security.permission.KeyValuePermission;
import ddf.security.policy.extension.PolicyExtension;

public class AdminConfigPolicy implements PolicyExtension {

    public static String FEATURE_NAME = "feature.name";

    public static String SERVICE_PID = "service.pid";

    public static String VIEW_FEATURE_ACTION = "view-" + FEATURE_NAME;

    public static String VIEW_SERVICE_ACTION = "view-" + SERVICE_PID;

    Map<String, List<KeyValueCollectionPermission>> featurePolicyPermissions = new HashMap<>();

    Map<String, List<KeyValueCollectionPermission>> servicePolicyPermissions = new HashMap<>();

    @Override
    public KeyValueCollectionPermission isPermittedMatchAll(
            CollectionPermission subjectAllCollection,
            KeyValueCollectionPermission matchAllCollection) {
        return isPermittedMatchOne(subjectAllCollection, matchAllCollection);
    }

    @Override
    public KeyValueCollectionPermission isPermittedMatchOne(
            CollectionPermission subjectAllCollection,
            KeyValueCollectionPermission matchOneCollection) {

        if (!(matchOneCollection.getAction() != null && (matchOneCollection.getAction()
                .equals(VIEW_FEATURE_ACTION) || matchOneCollection.getAction()
                .equals(VIEW_SERVICE_ACTION)))) {
            return matchOneCollection;
        }

        KeyValueCollectionPermission newMatchCollection = new KeyValueCollectionPermission();

        for (Permission permission : matchOneCollection.getPermissionList()) {
            if (permission instanceof KeyValuePermission) {
                String matchPermissionName = ((KeyValuePermission) permission).getKey();

                Map<String, List<KeyValueCollectionPermission>> policyPermissions;

                if (matchPermissionName.equals(FEATURE_NAME)) {
                    policyPermissions = featurePolicyPermissions;
                } else if (matchPermissionName.equals(SERVICE_PID)) {
                    policyPermissions = servicePolicyPermissions;
                } else {
                    //Not a listed policy attribute name so it is white listed
                    continue;
                }

                String matchPermissionValue = ((KeyValuePermission) permission).getValues()
                        .iterator()
                        .next();

                List<KeyValueCollectionPermission> matchOneAttributes = policyPermissions.get(
                        matchPermissionValue);

                //Not a listed attribute, the feature or service is white listed
                if (matchOneAttributes == null) {
                    continue;
                }

                boolean permitted = false;

                //Only need to match one attribute permission to be permitted
                for (KeyValueCollectionPermission attributePermissions : matchOneAttributes) {
                    if (subjectAllCollection.implies(attributePermissions)) {
                        permitted = true;
                    }
                }

                if (!permitted) {
                    newMatchCollection.addAll(Collections.singleton(permission));
                }

            }
        }

        return newMatchCollection;
    }

    public void setFeaturePolicies(List<String> featurePolicies) {
        featurePolicyPermissions.clear();
        featurePolicyPermissions.putAll(parsePermissions(featurePolicies));
    }

    public void setServicePolicies(List<String> servicePolicies) {
        servicePolicyPermissions.clear();
        servicePolicyPermissions.putAll(parsePermissions(servicePolicies));
    }

    public Map<String, List<KeyValueCollectionPermission>> parsePermissions(
            List<String> policies) {

        Map<String, List<KeyValueCollectionPermission>> newPolicyPermissions =
                new HashMap<>();

        for (String policy : policies) {
            //Ex: featurename = "attributeName=attributeValue","attributeName2 = attributeValue2"
            String policyTrimmed = policy.replaceAll("\\s+","");
            String permissionName = policyTrimmed.split("=", 2)[0];
            String policyAttributes = policyTrimmed.split("=", 2)[1];

            List<KeyValueCollectionPermission> permissionAttributeMap =
                    new ArrayList<>();

            for (String policyAttribute : policyAttributes.split(",")) {
                policyAttribute = policyAttribute.replace("\"", "");
                String attributeName = policyAttribute.split("=")[0];
                String attributeValue = policyAttribute.split("=")[1];

                KeyValueCollectionPermission newPermission = new KeyValueCollectionPermission(null,
                        new KeyValuePermission(attributeName, Sets.newHashSet(attributeValue)));
                permissionAttributeMap.add(newPermission);
            }

            // TODO: tbatie - 5/26/16 - Check if entry already exists, log error
            if(newPolicyPermissions.containsKey(permissionName)) {

            }
            newPolicyPermissions.put(permissionName, permissionAttributeMap);
        }

        return newPolicyPermissions;
    }


}
