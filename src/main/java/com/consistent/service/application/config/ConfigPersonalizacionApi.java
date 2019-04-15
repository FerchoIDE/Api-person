package com.consistent.service.application.config;


import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(category = "Posadas", scope = ExtendedObjectClassDefinition.Scope.SYSTEM)
@Meta.OCD( localization = "content/Language",id = "com.consistent.service.application.config.ConfigPersonalizacionApi",name = "Liferay api personalization")
public interface ConfigPersonalizacionApi {
	/*Restaurant,Meeting room,Gym,Bar,Spa,Fiesta Kids Club,Generic,Brand,Destination,Rate,Hotels,Room,Facility*/
	@Meta.AD(deflt = "119201|119204|121330|121324|119207|121327|121333|121321|121312|121309|121318|121315|119198|32880", required = false, description = "Get id Base Structures Liferay")
    public String[] StructureId();
	
	@Meta.AD(deflt = "POSADAS|HOTEL", required = false, description = "Get path Base Folders FileSystem")
    public String[] DLFileEntryFolderBase();

	@Meta.AD(deflt = "POSADAS|HOTEL", required = false, description = "Get path Base Folders Hotel Webcontent")
    public String[] JournalFolderHotelBase();
    
	@Meta.AD(deflt = "POSADAS|RATES", required = false, description = "Get path Base Folders Rates Webcontent")
    public String[] JournalFolderRateBase();
  
}

