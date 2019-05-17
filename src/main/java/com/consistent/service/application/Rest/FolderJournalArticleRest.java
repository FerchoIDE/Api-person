package com.consistent.service.application.Rest;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.consistent.service.application.domain.JournalApi;
import com.consistent.service.application.domain.StructuresApi;
import com.consistent.service.application.domain.VocabularyApi;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Path("/folder")
public class FolderJournalArticleRest {
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(FolderJournalArticleRest.class);

	@GET
	@Path("/getArticleById")
	public String getArticleById(@QueryParam("groupId") Long groupId) throws PortalException {
	 Map<JournalArticle, DDMStructure> map = new JournalApi().getWCandStructureById(groupId);
 			for (Map.Entry<JournalArticle, DDMStructure> entry : map.entrySet()) {
 			    System.out.println(entry.getKey().getTitle() + "/" + entry.getValue().getName());
 			}
	return "JAJAJAJAJAJa";
 		
	}
	
	@GET
	@Path("/getListJournalFoldersBrand")
	@Produces("application/json")
	public String getListJournalFoldersBrand(@QueryParam("groupId") Long groupId) throws PortalException {
 		return new JournalApi().getListJournalFoldersBrand(groupId).toJSONString();
	}
	
	@GET
	@Path("/getListJournalFolders")
	@Produces("application/json")
	public String getListJournalFolders(@QueryParam("groupId") Long groupId,
								@QueryParam("parentFolderId") Long parentFolderId) throws PortalException {
 	//	return new JournalApi().getListJournalFolders(groupId, brand, codeHotel).toString();
		return new JournalApi().getListJournalFolders(groupId,parentFolderId).toJSONString();
	}
	
	@GET
	@Path("/getListJournalFoldersByBrand")
	@Produces("application/json")
	public String getListJournalFoldersByBrand(@QueryParam("groupId") Long groupId,
								@QueryParam("brand") String brand,
								@QueryParam("codeHotel") String codeHotel) throws PortalException {
 		return new JournalApi().getListJournalFoldersByBrand(groupId, brand).toString();
	}
	
	
	@GET
	@Path("/getListJournalFoldersByBrandId")
	@Produces("application/json")
	public String getListJournalFoldersByBrandId(
								@QueryParam("groupId") Long groupId,
								@QueryParam("brand") Long brand) throws PortalException {
 		return new JournalApi().getListJournalFoldersByBrand(groupId, brand).toString();
	}
	
	@GET
	@Path("/getListJournalFoldersByCode")
	@Produces("application/json")
	public String getListJournalFoldersByCode(@QueryParam("groupId") Long groupId,
								@QueryParam("brand") Long brand) throws PortalException {
 		return new JournalApi().getListJournalFoldersByCode(groupId, brand).toString();
	}
	
	@GET
	@Path("/getWCFoldersByCode")
	@Produces("application/json")
	public String getWCAndJournalFolder(@QueryParam("groupId") Long groupId,
								@QueryParam("brand") String brand,
								@QueryParam("codeHotel") String codeHotel) throws PortalException {
 		
		for (JournalArticle iterable_element : new JournalApi().getWCAndJournalFolder(groupId, brand, codeHotel)) {
			System.out.println(iterable_element.getArticleId());
		}
		return "hola";
	}

	@GET
	@Path("/getWCAndJournalFolderByName")
	@Produces("application/json")
	public String getWCAndJournalFolderByName(@QueryParam("groupId") Long groupId,
								@QueryParam("brand") String brand,
								@QueryParam("codeHotel") String codeHotel,
								@QueryParam("name") String name) throws PortalException {
 		return new JournalApi().getWCAndJournalFolderByName(groupId, brand, codeHotel,name).toString();
	}
	
	@GET
	@Path("/getWCAndJournalFolderByNameSI")
	@Produces("application/json")
	public String getWCAndJournalFolderByName(@QueryParam("groupId") Long groupId,
								@QueryParam("brand") String brand,
								@QueryParam("codeHotel") String codeHotel,
								@QueryParam("name") String name,
								@QueryParam("structureId") Long structureId) throws PortalException {
 		return new JournalApi().getWCAndJournalFolderByName(groupId, brand, codeHotel,name,structureId).toString();
	}
	
	@GET
	@Path("/getALLStructures")
	@Produces("application/json")
	public String getALLStructures(@QueryParam("groupId") Long groupId) throws PortalException {
 		return new StructuresApi().getAllStructuresBySite(groupId).toString();
	}
	
	
	
	
	
}
