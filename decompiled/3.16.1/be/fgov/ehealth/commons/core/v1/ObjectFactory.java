package be.fgov.ehealth.commons.core.v1;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
   public IdentifierType createIdentifierType() {
      return new IdentifierType();
   }

   public PeriodType createPeriodType() {
      return new PeriodType();
   }

   public LocalisedString createLocalisedString() {
      return new LocalisedString();
   }

   public StatusType createStatusType() {
      return new StatusType();
   }

   public AddressType createAddressType() {
      return new AddressType();
   }

   public StreetType createStreetType() {
      return new StreetType();
   }

   public MunicipalityType createMunicipalityType() {
      return new MunicipalityType();
   }

   public CountryType createCountryType() {
      return new CountryType();
   }
}
