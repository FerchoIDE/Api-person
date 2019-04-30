package com.consistent.service.application.Rest;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.consistent.service.application.domain.FileEntryApi;
import com.consistent.service.application.domain.JournalApi;
import com.consistent.service.application.domain.StructuresApi;
import com.consistent.service.application.domain.VocabularyApi;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


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
	@Path("/parseJson")
	@Produces("application/json")
	public String parseJson(@QueryParam("groupId") Long groupId,
											 @QueryParam("idStructure") Long idStructure,
											 @QueryParam("code") String code) throws PortalException {
   
	return "";//_api.parseJsonToXML(code);
	}
	
	
	@GET
	@Path("/getCategories")
	@Produces("application/json")
	public String getVocabulary1(@QueryParam("groupId") Long groupId) throws PortalException {
		VocabularyApi _vocabulary = new VocabularyApi();
		return _vocabulary.getVocabulariesByGroup(groupId).toJSONString();
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
	@Produces("application/json")
	public String getListJournalFoldersByCode(@QueryParam("groupId") Long groupId,
											  @QueryParam("codeBrand") String codeBrand,
											  @QueryParam("codeHotel") String codeHotel,
											  @QueryParam("nameStructure") String nameStructure
											  
											  ) throws PortalException {
   
		return  new JournalApi().getWCByJournalFolderAndStructureType(groupId, codeBrand, codeHotel, nameStructure).toString();
	}

}
