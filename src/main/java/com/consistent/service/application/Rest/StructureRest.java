package com.consistent.service.application.Rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Reference;

import com.consistent.service.application.LiferayServices.JournalArticleServices;
import com.consistent.service.application.LiferayServices.QueriesLiferayApi;
import com.consistent.service.application.LiferayServices.VocabularyApi;
import com.consistent.service.application.models.Files;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.impl.JournalArticleImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;


@Path("/structure")
public class StructureRest {
	private static final Log log = LogFactoryUtil.getLog(StructureRest.class);

	static com.consistent.service.application.LiferayServices.JournalArticleServices _services = new JournalArticleServices();
	static QueriesLiferayApi _api = new QueriesLiferayApi();
	
	
	
	@GET
	@Path("/getCategories")
	@Produces("application/json")
	public String getVocabulary(@QueryParam("groupId") Long groupId) throws PortalException {
		VocabularyApi _vocabulary = new VocabularyApi();
		return _vocabulary.getVocabulariesByGroup(groupId).toJSONString();
	}

	@GET
	@Path("/getStructuresBygroupId")
	@Produces("application/json")
	public String getStructuresBygroupId(@QueryParam("groupId") Long groupId) throws PortalException {
    List<DDMStructure>  structures = _services.getAllStructuresBySite(new Long(groupId));
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
   
    return  _services.getFilesByName(groupId, brand,mime_type, code_hotel).toString();	
	}
	
	@GET
	@Path("/getFilesByLikeName")
	@Produces("application/json")
	public String getFilesByLikeName(@QueryParam("groupId") Long groupId,
							  @QueryParam("currentFolder") Long currentFolder,
							  @QueryParam("namefile") String namefile) throws PortalException {
    log.info(groupId +namefile+ currentFolder);
    JSONArray filesArray=JSONFactoryUtil.createJSONArray();
    return  _services.getFoldersAndFilesByName(groupId, currentFolder, namefile, filesArray).toJSONString();	
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
    return  _services.saveFile(groupId, userId, folderId, image,name, description, mimeType).toJSONString();	
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
    return  _services.getFilesByCurrentFolderAndName(groupId, currentFolder, name).toJSONString();
	}
	
	

		
	@GET
	@Path("/getStructuresBygroupIdAndName")
	@Produces("application/json")
	public String getStructuresBygroupIdAndName(@QueryParam("groupId") Long groupId,
											   @QueryParam("name") String name) {
    List<DDMStructure>  structures = _services.getStructureByName("%>"+name+"<%" ,new Long(groupId));
	if(structures.size()>0){
	return structures.toString();
	}
	else{
	log.error("Not Found Structures");
	return"Not Found Structures";
	}
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
   
		
	
	
   for (JournalArticle iterable_element :  _services.searchWebContentByCodeHotelFirstLevel(groupId,idStructure, code)) {
	System.out.println(iterable_element);
}	
   return _services.searchWebContentByCodeHotelFirstLevel(groupId,idStructure, code).toString();
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
    return  _services.getListJournalFolders(groupId, brand, code_hotel).toJSONString();
	}
	
	
	@GET
	@Path("/getListJournalFoldersByCode")
	@Produces("application/json")
	public String getListJournalFoldersByCode(@QueryParam("groupId") Long groupId,
											  @QueryParam("codeBrand") Long codeBrand) throws PortalException {
    return  _services.getListJournalFoldersByCode(groupId, codeBrand).toJSONString();
	}
	
}
