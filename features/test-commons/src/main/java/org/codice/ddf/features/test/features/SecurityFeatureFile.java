/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.features.test.features;

import static org.ops4j.pax.exam.CoreOptions.maven;

import org.codice.ddf.features.test.DependencyVersionResolver;
import org.codice.ddf.features.test.Feature;
import org.codice.ddf.features.test.FeatureFile;
import org.codice.ddf.features.test.FeatureImpl;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.ops4j.pax.exam.options.UrlReference;

public class SecurityFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("security")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private SecurityFeatureFile() {}

  private SecurityFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static FeatureFile featureFile() {
    return new SecurityFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new SecurityFeatureFile(filePath);
  }

  public static Feature securityLogger() {
    return new FeatureImpl(featureUrl, "security-logger");
  }

  public static Feature securityCoreApi() {
    return new FeatureImpl(featureUrl, "security-core-api");
  }

  public static Feature platformFilterResponse() {
    return new FeatureImpl(featureUrl, "platform-filter-response");
  }

  public static Feature platformFilterDelegate() {
    return new FeatureImpl(featureUrl, "platform-filter-delegate");
  }

  public static Feature securityEncryption() {
    return new FeatureImpl(featureUrl, "security-encryption");
  }

  public static Feature clientInfoFilter() {
    return new FeatureImpl(featureUrl, "clientinfo-filter");
  }

  public static Feature webContainer() {
    return new FeatureImpl(featureUrl, "web-container");
  }

  public static Feature platformHttpProxy() {
    return new FeatureImpl(featureUrl, "platform-http-proxy");
  }

  public static Feature platformPaxwebJettyconfig() {
    return new FeatureImpl(featureUrl, "platform-paxweb-jettyconfig");
  }

  // security-services-app
  public static Feature securityRestAuthentication() {
    return new FeatureImpl(featureUrl, "security-rest-authentication");
  }

  public static Feature securityHandlerApi() {
    return new FeatureImpl(featureUrl, "security-handler-api");
  }

  public static Feature securityCore() {
    return new FeatureImpl(featureUrl, "security-core");
  }

  public static Feature securityExpansion() {
    return new FeatureImpl(featureUrl, "security-expansion");
  }

  public static Feature securityCasClient() {
    return new FeatureImpl(featureUrl, "security-cas-client");
  }

  public static Feature securityCasTokenvalidator() {
    return new FeatureImpl(featureUrl, "security-cas-tokenvalidator");
  }

  public static Feature securityCasCxfServletfilter() {
    return new FeatureImpl(featureUrl, "security-cas-cxfservletfilter");
  }

  public static Feature securityPdpAuthz() {
    return new FeatureImpl(featureUrl, "security-pdp-authz");
  }

  public static Feature securityPepServiceauthz() {
    return new FeatureImpl(featureUrl, "security-pep-serviceauthz");
  }

  public static Feature securityExpansionUserAttributes() {
    return new FeatureImpl(featureUrl, "security-expansion-user-attributes");
  }

  public static Feature stsPrereqs() {
    return new FeatureImpl(featureUrl, "sts-prereqs");
  }

  public static Feature securityStsServer() {
    return new FeatureImpl(featureUrl, "security-sts-server");
  }

  public static Feature securityStsRealm() {
    return new FeatureImpl(featureUrl, "security-sts-realm");
  }

  public static Feature securityStsLdaplogin() {
    return new FeatureImpl(featureUrl, "security-sts-ldaplogin");
  }

  public static Feature securityStsLdapclaimshandler() {
    return new FeatureImpl(featureUrl, "security-sts-ldapclaimshandler");
  }

  public static Feature ldapEmbeddedDefaultConfigs() {
    return new FeatureImpl(featureUrl, "ldap-embedded-default-configs");
  }

  public static Feature ldapEmbeddedDefaultClaimshandlerConfig() {
    return new FeatureImpl(featureUrl, "ldap-embedded-default-claimshandler-config");
  }

  public static Feature ldapEmbeddedDefaultStsloginConfig() {
    return new FeatureImpl(featureUrl, "ldap-embedded-default-stslogin-config");
  }

  public static Feature securityHandlerBasic() {
    return new FeatureImpl(featureUrl, "security-handler-basic");
  }

  public static Feature securityFilterLogin() {
    return new FeatureImpl(featureUrl, "security-filter-login");
  }

  public static Feature securityFilterAuthz() {
    return new FeatureImpl(featureUrl, "security-filter-authz");
  }

  public static Feature securityHandlerSaml() {
    return new FeatureImpl(featureUrl, "security-handler-saml");
  }

  public static Feature securityHandlerGuest() {
    return new FeatureImpl(featureUrl, "security-handler-guest");
  }

  public static Feature securityFilterWebSso() {
    return new FeatureImpl(featureUrl, "security-filter-web-sso");
  }

  public static Feature securityPolicyContext() {
    return new FeatureImpl(featureUrl, "security-policy-context");
  }

  public static Feature securityStsGuestvalidator() {
    return new FeatureImpl(featureUrl, "security-sts-guestvalidator");
  }

  public static Feature securityStsGuestclaimshandler() {
    return new FeatureImpl(featureUrl, "security-sts-guestclaimshandler");
  }

  public static Feature securityStsCertificateclaimshandler() {
    return new FeatureImpl(featureUrl, "security-sts-certificateclaimshandler");
  }

  public static Feature securityStsPropertyclaimshandler() {
    return new FeatureImpl(featureUrl, "security-sts-propertyclaimshandler");
  }

  public static Feature securityInterceptorGuest() {
    return new FeatureImpl(featureUrl, "security-interceptor-guest");
  }

  public static Feature securityStsAttributequeryclaimshandler() {
    return new FeatureImpl(featureUrl, "security-sts-attributequeryclaimshandler");
  }

  public static Feature securityInterceptorGuestWrapper() {
    return new FeatureImpl(featureUrl, "security-interceptor-guest-wrapper");
  }

  public static Feature securityCertificate() {
    return new FeatureImpl(featureUrl, "security-certificate");
  }

  public static Feature securityGuest() {
    return new FeatureImpl(featureUrl, "security-guest");
  }

  public static Feature securityServletLogout() {
    return new FeatureImpl(featureUrl, "security-servlet-logout");
  }

  public static Feature securityServletSessionExpiry() {
    return new FeatureImpl(featureUrl, "security-servlet-session-expiry");
  }

  public static Feature securityServletWhoami() {
    return new FeatureImpl(featureUrl, "security-servlet-whoami");
  }

  public static Feature securityIdp() {
    return new FeatureImpl(featureUrl, "security-idp");
  }

  public static Feature securityCommandListener() {
    return new FeatureImpl(featureUrl, "security-command-listener");
  }

  public static Feature securityAll() {
    return new FeatureImpl(featureUrl, "security-all");
  }

  public static Feature securityWebSsoDefaults() {
    return new FeatureImpl(featureUrl, "security-web-sso-defaults");
  }

  public static Feature securityManagerCommand() {
    return new FeatureImpl(featureUrl, "session-manager-command");
  }

  public static Feature sessionInvalidator() {
    return new FeatureImpl(featureUrl, "session-invalidator");
  }
}
