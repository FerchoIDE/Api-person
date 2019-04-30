package com.consistent.service.application.domain;

import java.util.ArrayList;
import java.util.List;


import com.consistent.service.application.IDomain.IJournalApi;
import com.consistent.service.application.models.Contants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.model.impl.JournalFolderImpl;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;


public class JournalApi implements IJournalApi {
	private static final Log _log = LogFactoryUtil.getLog(JournalApi.class);
	private static QueriesLiferayApi _query = new QueriesLiferayApi();

	@Override
	public JSONArray getListJournalFolders(Long groupId,String brand,String codeHotel) throws PortalException{
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long idBase= getHotelFolderRootByConfigurationFolderWebcontent(groupId);
		if(codeHotel!=null && brand!=null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			Long hotelCode=_query.getFolderWC(groupId, codeHotel, brandFolder);
			return getJournalFoldersJson(groupId, hotelCode, filesArray);
		}
		else if(brand!=null && codeHotel==null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			return getJournalFoldersJson(groupId, brandFolder, filesArray);
		}
		else if(codeHotel!=null && brand==null){
			return getJournalFoldersJson(groupId, idBase, filesArray);
		}else{
			return getJournalFoldersJson(groupId, idBase, filesArray);
		}
			
	}
	
	@Override
	public JSONArray getListJournalFoldersByCode(Long groupId,Long codeBrand) throws PortalException{
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		if(getJournalFoldersJson(groupId, codeBrand, filesArray)!= null){
			return getJournalFoldersJson(groupId, codeBrand, filesArray);
		}
		return filesArray; 
	}
	
	@Override
	public List<JournalArticle> getWCByFolder(Long groupId,Long folderId) throws PortalException{
		List<JournalArticle> journalArray= new ArrayList<>();
		if(folderId!=null && groupId!=null){
			journalArray=getJournalFoldersAndWC(groupId, folderId, journalArray);
			if(_query.getWCByJournalFolder(groupId, folderId)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, folderId)) {
					journalArray.add(journal);
			}
			}
			return journalArray;
			
		}
		return new ArrayList<>();
		}
	@Override
	public JSONArray createFolderNestedFolderId(Long userId,Long groupId,Long parentFolderId,String name) throws PortalException{
		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setScopeGroupId(groupId);
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		JSONObject filesObject =JSONFactoryUtil.createJSONObject();
		JournalFolder object = JournalFolderLocalServiceUtil.addFolder(userId, groupId, parentFolderId, name, name, serviceContext);			
		filesObject.put("folderId", object.getFolderId());
		filesObject.put("nameFolder", object.getName());
		filesArray.put(filesObject);
		return filesArray; 
	}
	@Override
	public List<JournalArticle> getWCByName(Long groupId,String brand,String codeHotel,String name) throws PortalException{
		List<JournalArticle> journalArray= new ArrayList<>();
		long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
		if(codeHotel!=null && brand!=null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			Long hotelCode=_query.getFolderWC(groupId, codeHotel, brandFolder);
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolder(groupId, hotelCode)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, hotelCode)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
			
		}
		else if(brand!=null && codeHotel==null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolderAndName(groupId, idBase,name)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolderAndName(groupId, brandFolder,name)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}
		else if(codeHotel!=null && brand==null){
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolderAndName(groupId, idBase,name)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolderAndName(groupId, idBase,name)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}else{
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolderAndName(groupId, idBase,name)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolderAndName(groupId, idBase,name)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}

	}
	@Override
	public List<JournalArticle> getWCAndJournalFolder(Long groupId,String brand,String codeHotel) throws PortalException{
		List<JournalArticle> journalArray= new ArrayList<>();
		long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
		if(codeHotel!=null && brand!=null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			Long hotelCode=_query.getFolderWC(groupId, codeHotel, brandFolder);
			journalArray=getJournalFoldersAndWC(groupId, hotelCode, journalArray);
			if(_query.getWCByJournalFolder(groupId, hotelCode)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, hotelCode)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
			
		}
		else if(brand!=null && codeHotel==null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			journalArray=getJournalFoldersAndWC(groupId, brandFolder, journalArray);
			if(_query.getWCByJournalFolder(groupId, brandFolder)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, brandFolder)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}
		else if(codeHotel!=null && brand==null){
			journalArray=getJournalFoldersAndWC(groupId, idBase, journalArray);
			if(_query.getWCByJournalFolder(groupId, idBase)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, idBase)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}else{
			journalArray=getJournalFoldersAndWC(groupId, idBase, journalArray);
			if(_query.getWCByJournalFolder(groupId, idBase)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, idBase)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}
		
		
	}
	//getJournalFoldersAndWCByType
	@Override
	public List<JournalArticle> getWCByJournalFolderAndStructureType(Long groupId,String brand,String codeHotel,String nameStructure) throws PortalException{
		if(_query.getStructureByName(nameStructure, groupId)!=null && !_query.getStructureByName(nameStructure, groupId).isEmpty()){
			List<DDMStructure> ddm = _query.getStructureByName(nameStructure, groupId);
			List<JournalArticle> journalArray= new ArrayList<>();
			long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			if(brand!=null && codeHotel!=null){//Búsqueda por Marca y Codigo de Hotlel
				Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
				Long hotelCode=_query.getFolderWC(groupId, codeHotel, brandFolder);
				journalArray=getJournalFoldersAndWCByType(groupId, hotelCode,ddm.get(0).getStructureKey(), journalArray);
				if(_query.getWCByJournalFolderAndTypeStructure(groupId, hotelCode,ddm.get(0).getStructureKey())!=null){
					for (JournalArticle journal : _query.getWCByJournalFolderAndTypeStructure(groupId, hotelCode,ddm.get(0).getStructureKey())) {
						if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
				    		 journalArray.add(journal);
				    	 }else{
				    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
				    	 }
					}
					}
				return journalArray;
			}
			else if(brand!=null && codeHotel==null){//Búsqueda por marca 
				Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
				journalArray=getJournalFoldersAndWCByType(groupId, brandFolder,ddm.get(0).getStructureKey(), journalArray);
				if(_query.getWCByJournalFolderAndTypeStructure(groupId, brandFolder,ddm.get(0).getStructureKey())!=null){
					for (JournalArticle journal : _query.getWCByJournalFolderAndTypeStructure(groupId, brandFolder,ddm.get(0).getStructureKey())) {
						if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
				    		 journalArray.add(journal);
				    	 }else{
				    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
				    	 }
					}
					}
				return journalArray;
			}
			else{//Si no se cumplen estos casos B´squeda desde la raiz 
				journalArray=getJournalFoldersAndWCByType(groupId, idBase,ddm.get(0).getStructureKey(), journalArray);
				if(_query.getWCByJournalFolderAndTypeStructure(groupId, idBase,ddm.get(0).getStructureKey())!=null){
					for (JournalArticle journal : _query.getWCByJournalFolderAndTypeStructure(groupId, idBase,ddm.get(0).getStructureKey())) {
						if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
				    		 journalArray.add(journal);
				    	 }else{
				    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
				    	 }
					}
					}
				return journalArray;
			}
		}
		else{
			return new ArrayList<>();
		}
		
		}
	@Override
	public List<JournalArticle> getWCAndJournalFolderByName(Long groupId,String brand,String codeHotel,String name,String types) throws PortalException{
		List<JournalArticle> journalArray= new ArrayList<>();
		long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
		if(codeHotel!=null && brand!=null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			Long hotelCode=_query.getFolderWC(groupId, codeHotel, brandFolder);
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolder(groupId, hotelCode)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolder(groupId, hotelCode)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
			
		}
		else if(brand!=null && codeHotel==null){
			Long brandFolder=_query.getFolderWC(groupId, brand, idBase);
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolderAndName(groupId, idBase,name)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolderAndName(groupId, brandFolder,name)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}
		else if(codeHotel!=null && brand==null){
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolderAndName(groupId, idBase,name)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolderAndName(groupId, idBase,name)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}else{
			journalArray=_query.getFoldersWCByName(groupId, idBase,name, journalArray);
			if(_query.getWCByJournalFolderAndName(groupId, idBase,name)!=null){
			for (JournalArticle journal : _query.getWCByJournalFolderAndName(groupId, idBase,name)) {
				if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		    		 journalArray.add(journal);
		    	 }else{
		    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(journal.getResourcePrimKey()));
		    	 }
			}
			}
			return journalArray;
		}
		
		
	}
	@Override
	public List<JournalArticle> searchWebContentByCodeHotelFirstLevel(Long groupId,String structureName,String code) throws PortalException{
		if(_query.getFoldersWCByCode(groupId, structureName, code)!=null){
			return _query.getFoldersWCByCode(groupId, structureName, code);
		}else{
			return new ArrayList<>();
		}	
	}

	
	/***Métodos privados****/
	
	private long getHotelFolderRootByConfigurationFolderWebcontent(Long groupId) throws PortalException{
		JournalFolderImpl idFolder = null;
		for (String baseFileEntry : Contants.JOURNAL_HOTEL) {
			if(idFolder!=null){
				 idFolder= _query.getJournalFolderByName(groupId, idFolder.getFolderId(), baseFileEntry);
				}
				else{
					idFolder= _query.getJournalFolderByName(groupId, new Long(0), baseFileEntry);
				}
				_log.info("Folder webcontent:"+idFolder.getName()+"id:"+idFolder.getFolderId());
				}
			return idFolder.getFolderId();
	 }
	private JSONArray getJournalFoldersJson(Long groupId,Long parent,JSONArray filesArray) throws PortalException{
		List<JournalFolder> listFolders =JournalFolderLocalServiceUtil.getFolders(groupId, new Long(parent));
		if(listFolders != null && listFolders.size() > 0){
			JSONObject filesObject=null;
			for (JournalFolder object : listFolders) {
				filesObject=JSONFactoryUtil.createJSONObject();
				filesObject.put("folderId", object.getFolderId());
				filesObject.put("nameFolder", object.getName());
				filesArray.put(filesObject);
				if(getJournalFoldersJson(groupId,object.getFolderId(),filesArray)!= null && !getJournalFoldersJson(groupId,object.getFolderId(),filesArray).isNull(0)){
					getJournalFoldersJson(groupId,object.getFolderId(),filesArray);
				}
				
			}
		}
		return filesArray;
	}	
	
		
		private List<JournalArticle> getJournalFoldersAndWC(Long groupId,Long parent,List<JournalArticle> journalArray) throws PortalException{
			List<JournalFolder> listFolders = _query.getSubFolderByJournalFolderParent(groupId, new Long(parent));		
			if(listFolders != null && listFolders.size() > 0){
				for (JournalFolder object : listFolders) {
					if(_query.getWCByJournalFolder(groupId, object.getFolderId())!= null && !_query.getWCByJournalFolder(groupId, object.getFolderId()).isEmpty()){
						for (JournalArticle ja : _query.getWCByJournalFolder(groupId, object.getFolderId())) {
					    	 if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, ja.getArticleId(), ja.getVersion())){
					    		 journalArray.add(ja);
					    	 }else{
					    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(ja.getResourcePrimKey()));
					    	 }
							}
						}
					if(getJournalFoldersAndWC(groupId,object.getFolderId(),journalArray)!= null && !getJournalFoldersAndWC(groupId,object.getFolderId(),journalArray).isEmpty()){
						getJournalFoldersAndWC(groupId,object.getFolderId(),journalArray);
					}
				}
			}
			return journalArray;
		}
		private List<JournalArticle> getJournalFoldersAndWCByType(Long groupId,Long parent,String type,List<JournalArticle> journalArray) throws PortalException{
			List<JournalFolder> listFolders = _query.getSubFolderByJournalFolderParent(groupId, new Long(parent));		
			if(listFolders != null && listFolders.size() > 0){
				for (JournalFolder object : listFolders) {
					if(_query.getWCByJournalFolderAndTypeStructure(groupId, object.getFolderId(),type)!= null && !_query.getWCByJournalFolderAndTypeStructure(groupId, object.getFolderId(),type).isEmpty()){
						for (JournalArticle ja : _query.getWCByJournalFolderAndTypeStructure(groupId, object.getFolderId(),type)) {
					    	 if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, ja.getArticleId(), ja.getVersion())){
					    		 journalArray.add(ja);
					    	 }else{
					    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(ja.getResourcePrimKey()));
					    	 }
							}
						}
					if(getJournalFoldersAndWCByType(groupId,object.getFolderId(),type,journalArray)!= null && !getJournalFoldersAndWCByType(groupId,object.getFolderId(),type,journalArray).isEmpty()){
						getJournalFoldersAndWCByType(groupId,object.getFolderId(),type,journalArray);
					}
				}
			}
			return journalArray;
		}



	}

