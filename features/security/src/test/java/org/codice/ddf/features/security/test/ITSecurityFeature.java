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
package org.codice.ddf.features.security.test;

import static org.codice.ddf.features.test.config.DebugOptions.defaultDebuggingOptions;
import static org.codice.ddf.features.test.config.DistributionOptions.includeDependencyPropertiesFile;
import static org.codice.ddf.features.test.config.DistributionOptions.kernelDistributionOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addBootFeatureOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addFeaturesToFeatureRepo;
import static org.codice.ddf.features.test.config.PortOptions.defaultPortsOptions;
import static org.codice.ddf.features.test.config.VmOptions.defaultVmOptions;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;
import org.codice.ddf.features.test.FeatureInstallException;
import org.codice.ddf.features.test.FeatureServiceWrapper;
import org.codice.ddf.features.test.FeatureUninstallException;
import org.codice.ddf.features.test.features.SecurityFeatureFile;
import org.codice.ddf.features.test.features.TestUtilitiesFeatureFile;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ITSecurityFeature {

  private static final URL SECURITY_FEATURE =
      ITSecurityFeature.class.getResource(File.separator + "features.xml");

  @Configuration
  public Option[] examConfiguration() {
    return options(
        kernelDistributionOption(),
        defaultDebuggingOptions(),
        defaultVmOptions(),
        defaultPortsOptions(),
        includeDependencyPropertiesFile(),
        addFeaturesToFeatureRepo(SecurityFeatureFile.featureFile(SECURITY_FEATURE.getPath())),
        addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Inject FeatureServiceWrapper featuresService;

  @Test
  public void installSecurityLogger() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityLogger());
  }

  @Test
  public void installSecurityCoreApi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCoreApi());
  }

  @Test
  public void installPlatformFilterResponse()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.platformFilterResponse());
  }

  @Test
  public void installPlatformFilterDelegate()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.platformFilterDelegate());
  }

  @Test
  public void installSecurityEncryption()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityEncryption());
  }

  @Test
  public void installClientInfoFilter() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.clientInfoFilter());
  }

  @Test
  public void installWebContainer() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.webContainer());
  }

  @Test
  public void installPlatformHttpProxy() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.platformHttpProxy());
  }

  @Test
  public void installPlatformPaxwebJettyconfig()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.platformPaxwebJettyconfig());
  }

  // security-service-app features
  @Test
  public void installSecurityRestAuthentication()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityRestAuthentication());
  }

  @Test
  public void installSecurityHandlerApi()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityHandlerApi());
  }

  @Test
  public void installSecurityCore() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCore());
  }

  @Test
  public void installSecurityExpansion() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityExpansion());
  }

  // TODO: tbatie - 1/18/18 - No bundles in DDF exporting this package through a normal install.
  // [caused by: Unable to resolve org.codice.thirdparty.cas-client-core/3.1.10.1: missing
  // requirement [org.codice.thirdparty.cas-client-core/3.1.10.1] osgi.wiring.package;
  // filter:="(osgi.wiring.package=org.opensaml)"]]]
  @Test
  @Ignore
  public void installSecurityCasClient() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCasClient());
  }

  // TODO: tbatie - 1/18/18 - No bundles in DDF exporting this version through a normal install.
  // missing requirement [security-cas-tokenvalidator/2.12.0.SNAPSHOT] osgi.wiring.package;
  // filter:="(&(osgi.wiring.package=javax.servlet)(version>=2.5.0)(!(version>=3.0.0)))"]]
  @Test
  @Ignore
  public void installSecurityCasTokenvalidator()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCasTokenvalidator());
  }

  // TODO: tbatie - 1/18/18 - No bundles in DDF exporting this package through a normal install.
  // [caused by: Unable to resolve org.codice.thirdparty.cas-client-core/3.1.10.1: missing
  // requirement [org.codice.thirdparty.cas-client-core/3.1.10.1] osgi.wiring.package;
  // filter:="(osgi.wiring.package=org.opensaml)"]]]
  @Test
  @Ignore
  public void installSecurityCasCxfServletfilter()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCasCxfServletfilter());
  }

  @Test
  public void installSecurityPdpAuthz() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityPdpAuthz());
  }

  @Test
  public void installSecurityPepServiceauthz()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityPepServiceauthz());
  }

  @Test
  public void installSecurityExpansionUserAttributes()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.securityExpansionUserAttributes());
  }

  @Test
  public void installStsPrereqs() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.stsPrereqs());
  }

  @Test
  public void installSecurityStsServer() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityStsServer());
  }

  @Test
  public void installSecurityStsRealm() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityStsRealm());
  }

  @Test
  public void installSecurityStsLdaplogin()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityStsLdaplogin());
  }

  @Test
  public void installSecurityStsLdapclaimshandler()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityStsLdapclaimshandler());
  }

  @Test
  public void installLdapEmbeddedDefaultConfigs()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.ldapEmbeddedDefaultConfigs());
  }

  @Test
  public void installLdapEmbeddedDefaultClaimshandlerConfig()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.ldapEmbeddedDefaultClaimshandlerConfig());
  }

  @Test
  public void installLdapEmbeddedDefaultStsloginConfig()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.ldapEmbeddedDefaultStsloginConfig());
  }

  @Test
  public void installSecurityHandlerBasic()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityHandlerBasic());
  }

  @Test
  public void installSecurityFilterLogin()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityFilterLogin());
  }

  @Test
  public void installSecurityFilterAuthz()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityFilterAuthz());
  }

  @Test
  public void installSecurityHandlerSaml()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityHandlerSaml());
  }

  @Test
  public void installSecurityHandlerGuest()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityHandlerGuest());
  }

  @Test
  public void installSecurityFilterWebSso()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityFilterWebSso());
  }

  @Test
  public void installSecurityPolicyContext()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityPolicyContext());
  }

  @Test
  public void installSecurityStsGuestvalidator()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityStsGuestvalidator());
  }

  @Test
  public void installSecurityStsGuestclaimshandler()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityStsGuestclaimshandler());
  }

  @Test
  public void installSecurityStsCertificateclaimshandler()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.securityStsCertificateclaimshandler());
  }

  @Test
  public void installSecurityStsPropertyclaimshandler()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.securityStsPropertyclaimshandler());
  }

  @Test
  public void installSecurityInterceptorGuest()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityInterceptorGuest());
  }

  @Test
  public void installSecurityStsAttributequeryclaimshandler()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.securityStsAttributequeryclaimshandler());
  }

  @Test
  public void installSecurityInterceptorGuestWrapper()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(
        SecurityFeatureFile.securityInterceptorGuestWrapper());
  }

  @Test
  public void installSecurityCertificate()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCertificate());
  }

  @Test
  public void installSecurityGuest() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityGuest());
  }

  @Test
  public void installSecurityServletLogout()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityServletLogout());
  }

  @Test
  public void installSecurityServletSessionExpiry()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityServletSessionExpiry());
  }

  @Test
  public void installSecurityServletWhoami()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityServletWhoami());
  }

  @Test
  public void installSecurityIdp() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityIdp());
  }

  @Test
  public void installSecurityCommandListener()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityCommandListener());
  }

  @Test
  public void installSecurityAll() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityAll());
  }

  @Test
  public void installSecurityWebSsoDefaults()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityWebSsoDefaults());
  }

  @Test
  public void installSecurityManagerCommand()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.securityManagerCommand());
  }

  @Test
  public void installSessionInvalidator()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SecurityFeatureFile.sessionInvalidator());
  }
}
