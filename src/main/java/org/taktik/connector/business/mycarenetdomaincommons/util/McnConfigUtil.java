package org.taktik.connector.business.mycarenetdomaincommons.util;

import org.taktik.connector.business.mycarenetdomaincommons.domain.McnPackageInfo;
import org.taktik.connector.technical.config.ConfigFactory;
import org.taktik.connector.technical.config.ConfigValidator;

public final class McnConfigUtil {
   private static final String PACKAGE_LICENSE_USERNAME = "package.license.username";
   private static final String PACKAGE_LICENSE_PASSWORD = "package.license.password";
   private static final String PACKAGE_LICENSE_NAME = "package.name";

   private static final ConfigValidator configValidator = ConfigFactory.getConfigValidator();

   private McnConfigUtil() {
      throw new UnsupportedOperationException();
   }

   public static McnPackageInfo retrievePackageInfo(String componentName, String licenseUsername, String licensePassword) {
      return retrievePackageInfo(componentName, licenseUsername,licensePassword);
   }

   public static McnPackageInfo retrievePackageInfo(String componentName, String professionName, String licenseUsername, String licensePassword) {
      String userName = licenseUsername != null ? licenseUsername : configValidator.getProperty(componentName + "." + PACKAGE_LICENSE_USERNAME + (professionName != null ? "." + professionName : ""), "${mycarenet.license.username" + (professionName != null ? "." + professionName : "") + "}");
      String password = licensePassword != null ? licensePassword : configValidator.getProperty(componentName + "." + PACKAGE_LICENSE_PASSWORD + (professionName != null ? "." + professionName : ""), "${mycarenet.license.password" + (professionName != null ? "." + professionName : "") + "}");
      //if property not found fallback to default without profession
      userName = ("${mycarenet.license.username" + (professionName != null ? "." + professionName : "") + "}").equals(userName) ? configValidator.getProperty(componentName + "." + PACKAGE_LICENSE_USERNAME, "${mycarenet.license.username" + "}") : userName;
      password = ("${mycarenet.license.password" + (professionName != null ? "." + professionName : "") + "}").equals(password) ? configValidator.getProperty(componentName + "." + PACKAGE_LICENSE_PASSWORD, "${mycarenet.license.password" + "}") : password;

      String name = configValidator.getProperty(componentName + "." + PACKAGE_LICENSE_NAME, "${mycarenet.package.name}");
      return new McnPackageInfo(userName, password, name);
   }
}
