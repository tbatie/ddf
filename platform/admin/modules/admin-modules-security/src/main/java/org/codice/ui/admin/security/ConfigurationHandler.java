package org.codice.ui.admin.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codice.ui.admin.security.config.Configuration;

public class ConfigurationHandler {

    public static final String TEST_LDAP_CONNECTION = "testLdapConnection";

    public static final String TEST_LDAP_BIND = "testLdapBind";

    public static final String TEST_LDAP_DIRECTORY_STRUCT = "testLdapDirStruct";

    List<String> test(String testId, Configuration config) throws Exception {
        switch (testId) {
        case TEST_LDAP_CONNECTION:
            return testLdapConnection();
        case TEST_LDAP_BIND:
            return testLdapBind();
        case TEST_LDAP_DIRECTORY_STRUCT:
            return testLdapDirectoryStructure();
        }

        throw new Exception();
    }

    List<String> persist(Configuration config) {
        return new ArrayList<>();
    }

    List<String> getTestIds() {
        return Arrays.asList(TEST_LDAP_CONNECTION, TEST_LDAP_BIND, TEST_LDAP_DIRECTORY_STRUCT);
    }

    public List<String> testLdapConnection() {
        return new ArrayList<>();
    }

    public List<String> testLdapBind() {
        return new ArrayList<>();
    }

    public List<String> testLdapDirectoryStructure() {
        return new ArrayList<>();
    }

}
