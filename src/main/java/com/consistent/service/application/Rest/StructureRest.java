package com.consistent.service.application.Rest;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.consistent.service.application.domain.AssetTagsApi;
import com.consistent.service.application.domain.FileEntryApi;
import com.consistent.service.application.domain.JournalApi;
import com.consistent.service.application.domain.QueriesLiferayApi;
import com.consistent.service.application.domain.StructuresApi;
import com.consistent.service.application.domain.VocabularyApi;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import aQute.bnd.annotation.xml.XMLAttribute;


@Path("/structure")
public class StructureRest {
	private static final Log log = LogFactoryUtil.getLog(StructureRest.class);

	
	
	
	@GET
	@Path("/getCategories")
	@Produces("application/json")
	public String getVocabulary(@QueryParam("groupId") Long groupId) throws PortalException {
 		return new VocabularyApi().getVocabulariesByGroup(groupId).toJSONString();
	}

	@GET
	@Path("/getStructuresBygroupId")
	@Produces("application/json")
	public String getStructuresBygroupId(@QueryParam("groupId") Long groupId) throws PortalException {
    List<DDMStructure>  structures = new StructuresApi().getAllStructuresBySite(new Long(groupId));
	if(structures.size()>0){
	return structures.toString();
	}
	else{
	log.error("Not Found Structures");
	return "Not Found Structures";
	}
	}
	
	@GET
	@Path("/getFilesAndFolders")
	@Produces("application/json")
	public String getFiles(@QueryParam("groupId") Long groupId,
							  @QueryParam("brand") String brand,
							  @QueryParam("code_hotel") String code_hotel,
							  @QueryParam("mime_type") String mime_type) throws PortalException {
    log.info(groupId + brand+ mime_type+ code_hotel);
   
    return  new FileEntryApi().getFilesByName(groupId, brand,mime_type, code_hotel).toString();	
	}

	
	
	@POST
	@Path("/saveFile")
	@Produces("application/json")
	public String saveFile(@QueryParam("groupId") Long groupId,
							  @QueryParam("folderId") Long folderId,
							  @QueryParam("file") String image,
							  @QueryParam("changeLog") String mimeType,
							  @QueryParam("userId") Long userId,
							  @QueryParam("name") String name,
							  @QueryParam("description") String description) throws PortalException, FileNotFoundException {
    log.info(groupId +folderId+ mimeType);
    return new FileEntryApi().saveFile(groupId, userId, folderId, image,name, description, mimeType).toJSONString();	
	}
	
	//getFoldersAndFilesByName	
	
	@GET
	@Path("/getFilesAndFoldersByFolder")
	@Produces("application/json")
	public String getFilesAndFoldersByFolder(@QueryParam("groupId") Long groupId,
							  @QueryParam("code") Long currentFolder,
							  @QueryParam("name") String name
							   ) throws PortalException {
    log.info(groupId+currentFolder);
    return  new FileEntryApi().getFilesByCurrentFolderAndName(groupId, currentFolder, name).toJSONString();
	}
	
	


	
	/**
	 * @param groupId
	 * @param idStructure
	 * @return
	 * @throws PortalException
	 */
	@GET
	@Path("/getStructuresBygroupIdAndID")
	@Produces("application/json")
	public String getStructuresBygroupIdAndID(@QueryParam("groupId") Long groupId,
											 @QueryParam("idStructure") String idStructure,
											 @QueryParam("code") String code) throws PortalException {
   
		
	
	
   for (JournalArticle iterable_element : new JournalApi().searchWebContentByCodeHotelFirstLevel(groupId,idStructure, code)) {
	System.out.println(iterable_element);
}	
   return new JournalApi().searchWebContentByCodeHotelFirstLevel(groupId,idStructure, code).toString();
	//	return api.parseJsonToXML(idStructure);
   /* List<DDMStructure>  structures = _services.getStructureByID(new Long(idStructure),new Long(groupId));
	if(structures.size()>0){
	return structures.toString();
	}
	else{
	log.error("Not Found Structures");
	return"Not Found Structures";
	}
	}*/

	}
	
	/**
	 * @param groupId
	 * @param idStructure
	 * @return
	 * @throws PortalException
	 */
	@GET
	@Path("/webcontent")
	//@Produces(MediaType.APPLICATION_XML)
	@Produces("application/json")
	public String parseJson(@QueryParam("code") String code) throws PortalException {
	String  json="{ \"aviableLocales\": \"es_ES,en_US\", \"groupId\": 20142, \"ddmStructure\": \"200950\", \"description\": \"ddd\", \"title\": \"ttt\", \"userId\": 20155, \"folderId\": 0, \"tags\": [ \"mexico\", \"live\", \"aqua\" ], \"localeDefault\": \"es_ES\", \"ddmTemplate\": \"201004\", \"categories\": [], \"fields\": [{ \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Hotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"fiesta-americana-hermosillo\", \"es_ES\": \"fiesta-americana-hermosillo\" } ], \"name\": \"friendlyUrlHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"FAH\", \"es_ES\": \"FAH\" } ], \"name\": \"codeHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"98013\", \"es_ES\": \"98013\" } ], \"name\": \"codeTravelClickHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"98013\", \"es_ES\": \"98013\" } ], \"name\": \"codeLeonardoHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"Fiesta Americana Hermosillo\", \"es_ES\": \"Fiesta Americana Hermosillo\" } ], \"name\": \"nameHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"golf, meetings, conventions, weddings, vacation, family, business, 15, hermosillo, fa, fiesta americana hermosillo, fiesta americana, ciudad, city\", \"es_ES\": \"golf, meetings, conventions, weddings, vacation, family, business, 15, hermosillo, fa, fiesta americana hermosillo, fiesta americana, ciudad, city\" } ], \"name\": \"keywordsHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"$1,769\", \"es_ES\": \"$1,769\" } ], \"name\": \"startingFromHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"descriptionsHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\", \"es_ES\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\" } ], \"name\": \"descriptionHotel\", \"type\": \"text_area\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\", \"es_ES\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\" } ], \"name\": \"shortDescriptionHotel\", \"type\": \"text_area\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\", \"es_ES\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\" } ], \"name\": \"BEPDescription\", \"type\": \"text_area\", \"nestedFields\": [] }, { \"indexType\": \"text\", \"values\": [ { \"en_US\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\", \"es_ES\": \"La ubicación del Hotel Fiesta Americana Hermosillo es privilegiada, localizado en la Avenida principal de la Ciudad en la Zona Hotelera, rodeado por los principales restaurantes, museos, bancos, tiendas y boutiques, a sólo 5 minutos de centro de Gobierno, a 15 minutos del Parque Industrial donde se ubica la planta de FORD y sus principales proveedores y a tan sólo 15 minutos del Aeropuerto Internacional.\" } ], \"name\": \"corpoRateDescription\", \"type\": \"text_area\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"descriptionRoomsHotel\", \"type\": \"text_area\", \"nestedFields\": [] } ] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"highlightsHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"24 h\", \"es_ES\": \"24 h\" } ], \"name\": \"highLightValueHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"Room Service\", \"es_ES\": \"Room Service\" } ], \"name\": \"highLightTextHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"highlightsHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"55\", \"es_ES\": \"55\" } ], \"name\": \"highLightValueHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"TV\", \"es_ES\": \"TV\" } ], \"name\": \"highLightTextHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"highlightsHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"100%\", \"es_ES\": \"100%\" } ], \"name\": \"highLightValueHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"Toallas algodón\", \"es_ES\": \"Cotton pillows\" } ], \"name\": \"highLightTextHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"highlightsHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"250\", \"es_ES\": \"250\" } ], \"name\": \"highLightValueHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"Hilos Duvet\", \"es_ES\": \"Hilos Duvet\" } ], \"name\": \"highLightTextHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"amenitiesHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"wifiHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"guideDogsHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"gymHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"smokeFreeHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"outdoorParkingFreeHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"outdoorParkingWithCostHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"EstacionamientoCubiertoGratuito\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"indoorParkingWithCostHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"shuttleServiceFreeHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"shuttleServiceWithCostHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"roomServiceHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"outdoorPoolHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"indoorPoolHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"true\", \"es_ES\": \"true\" } ], \"name\": \"allInclusiveHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"otherHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"adultsOnlyHotel\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"mediaLinksHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"sequence\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\", \"es_ES\": \"/documents/20142/106322/Hotel_960x720_FAH.jpg/f26ed892-013c-f9b1-3e46-894358413bb0\" } ], \"name\": \"mediaLinkHotel\", \"type\": \"ddm-documentlibrary\", \"nestedFields\": [ { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"CarouselImageXL-Desktop\", \"es_ES\": \"CarouselImageXL-Desktop\" } ], \"name\": \"typeHotel\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"keyword\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Pie\", \"type\": \"text\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"Booleanofa4o\", \"type\": \"boolean\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"travelclick\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"leonardo\", \"type\": \"boolean\", \"nestedFields\": [] } , { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCaption\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"imageCategory\", \"type\": \"select\", \"multiple\": \"false\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"shortDescription\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"description\", \"type\": \"ddm-integer\", \"nestedFields\": [] } ] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"roomLinksHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127676\", \"es_ES\": \"127676\" } ], \"name\": \"roomLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"roomLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"roomLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"roomLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"roomLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"roomLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"facilityLinksHtotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127676\", \"es_ES\": \"127676\" } ], \"name\": \"facilityLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"facilityLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"facilityLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"facilityLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"facilityLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127683\", \"es_ES\": \"127683\" } ], \"name\": \"facilityLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"destinationLinksHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127676\", \"es_ES\": \"127676\" } ], \"name\": \"destinationLinkHotel\", \"type\": \"ddm-journal-article\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"127676\", \"es_ES\": \"127676\" } ], \"name\": \"contactHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"addressHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"Blvd. Eusebio Kino No. 369 Col. Lomas Pitic\", \"es_ES\": \"Blvd. Eusebio Kino No. 369 Col. Lomas Pitic\" } ], \"name\": \"addressDetailHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"countryHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"stateHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"cityHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"zipHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"latitudHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"longitudHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"referencesHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"México\", \"es_ES\": \"México\" } ], \"name\": \"directionsHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"telephoneHotel\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"+52 (662) 259 60 00\", \"es_ES\": \"+52 (662) 259 60 00\" } ], \"name\": \"telephoneNumberHotel\", \"type\": \"text\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"hotelesAlternos\", \"type\": \"ddm-separator\", \"nestedFields\": [ { \"indexType\": \"\", \"values\": [ { \"en_US\": \"FIH\", \"es_ES\": \"FIH\" } ], \"name\": \"hotelesAlternosLink\", \"type\": \"select\", \"multiple\": \"true\", \"nestedFields\": [] } ] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"floorsHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"roomsHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"elevatorsHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"restaurantsHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"barsHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"poolsHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"1\", \"es_ES\": \"1\" } ], \"name\": \"starRatingHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"openingDateHotels\", \"type\": \"ddm-date\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"closingDateHotels\", \"type\": \"ddm-date\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"avisosHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"fechaInicioAvisoHotels\", \"type\": \"ddm-date\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"fechaFinAvisoHotels\", \"type\": \"ddm-date\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"horaCheckinHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"horaCheckoutHotels\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"CheckoutTimea3qv\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"\", \"es_ES\": \"\" } ], \"name\": \"SelloTripadvisor\", \"type\": \"text_area\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"Las habitaciones están muy limpias | Yani Reyes | 4 Abril 2018\", \"es_ES\": \"Las habitaciones están muy limpias | Yani Reyes | 4 Abril 2018\" } ], \"name\": \"quotesHotel\", \"type\": \"text\", \"nestedFields\": [] }, { \"indexType\": \"\", \"values\": [ { \"en_US\": \"Las habitaciones están muy limpias | Yani Reyes | 4 Abril 2018\", \"es_ES\": \"Las habitaciones están muy limpias | Yani Reyes | 4 Abril 2018\" } ], \"name\": \"quotesHotel\", \"type\": \"text\", \"nestedFields\": [] } ] } ], \"brand\": \"AQUA-J\" }";
	//log.info(new JournalApi().saveWC(json).toString());
   // return new QueriesLiferayApi().createNewWC1(code);
    return new JournalApi().saveWC(json).toString();
	}
	
	@GET
	@Path("/templates")
	//@Produces(MediaType.APPLICATION_XML)
	@Produces("application/json")
	public String templates() throws PortalException {
 
    return DDMTemplateLocalServiceUtil.getDDMTemplates(0, DDMTemplateLocalServiceUtil.getDDMTemplatesCount()).toString();
	}
	
	@GET
	@Path("/getCategories")
	@Produces("application/json")
	public String getVocabulary1(@QueryParam("groupId") Long groupId) throws PortalException {
		VocabularyApi _vocabulary = new VocabularyApi();
		return _vocabulary.getVocabulariesByGroup(groupId).toJSONString();
	}

	
	@GET
	@Path("/getTagsByname")
	@Produces("application/json")
	public String getTagsByname(@QueryParam("groupId") Long groupId,@QueryParam("name") String name) throws PortalException {
		for (AssetTag iterable_element : new AssetTagsApi().getTagsByname(groupId, name)) {
			System.out.println(iterable_element.getName());
		}
		return new AssetTagsApi().getTagsByname(groupId, name).toString();
		}
	
	@GET
	@Path("/getListJournalFolders")
	@Produces("application/json")
	public String getListJournalFolders(@QueryParam("groupId") Long groupId,
									    @QueryParam("brand") String brand,
									    @QueryParam("code_hotel") String code_hotel) throws PortalException {
    return  new JournalApi().getListJournalFolders(groupId, brand, code_hotel).toJSONString();
	}
	
	
	@GET
	@Path("/getListJournalFoldersByCode")
	public String getListJournalFoldersByCode(@QueryParam("groupId") Long groupId,
											  @QueryParam("codeBrand") String codeBrand,
											  @QueryParam("codeHotel") String codeHotel,
											  @QueryParam("idStructure") Long idStructure
											  
											  ) throws PortalException {
		System.out.println("Hola");
		return  new JournalApi().getWebcontentRecursiveByType(groupId, codeBrand, codeHotel, idStructure).toString();
	}
	
	@GET
	@Path("/getListByCode")
	public String getListByCode(@QueryParam("groupId") Long groupId,
			@QueryParam("structureId") Long structureId,
			@QueryParam("code") String code
											 ) throws PortalException {
		System.out.println("Hola");
		return  new JournalApi().getWCByCode(groupId, structureId, code).toString();
	}

}
