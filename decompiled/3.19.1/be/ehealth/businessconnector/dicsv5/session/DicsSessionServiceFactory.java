package be.ehealth.businessconnector.dicsv5.session;

import be.ehealth.businessconnector.dicsv5.session.impl.DicsImplementationClassFactory;
import be.ehealth.businessconnector.dicsv5.session.impl.DicsSessionServiceImpl;
import be.ehealth.technicalconnector.exception.ConnectorException;
import be.ehealth.technicalconnector.session.AbstractSessionServiceFactory;

public class DicsSessionServiceFactory extends AbstractSessionServiceFactory {
   private DicsSessionServiceFactory() {
   }

   public static DicsSessionService getDicsSession() throws ConnectorException {
      return (DicsSessionService)getService(DicsSessionServiceImpl.class, new DicsImplementationClassFactory(), new String[0]);
   }
}
