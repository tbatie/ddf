package org.codice.ui.admin.security;

import static org.codice.ui.admin.security.stage.sample.CustomStage.CUSTOM_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.ExtendedBindHostSettingsStage.EXTENDED_LDAP_BIND_HOST_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID;

import org.codice.ui.admin.security.stage.Composer;

public class DiffLdapWizard extends LdapWizard {

    public static final String DIFF_LDAP_WIZARD_CONTEXT_PATH = "/admin/wizard/diffldap";

    @Override
    public String getContextPath() {
        return DIFF_LDAP_WIZARD_CONTEXT_PATH;
    }

    @Override
    public Composer getStageComposer() {
        Composer ldapWizard = super.getStageComposer();
        ldapWizard.setStageLink(LDAP_NETWORK_SETTINGS_STAGE_ID, EXTENDED_LDAP_BIND_HOST_SETTINGS_STAGE_ID);
        ldapWizard.setStageLink(EXTENDED_LDAP_BIND_HOST_SETTINGS_STAGE_ID, CUSTOM_STAGE_ID);
        ldapWizard.setStageLink(CUSTOM_STAGE_ID, LDAP_DIRECTORY_SETTINGS_STAGE_ID);
        return ldapWizard;
    }
}
