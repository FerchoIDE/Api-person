package com.consistent.service.application.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.consistent.service.application.IDomain.IJournalApi;
import com.consistent.service.application.models.Contants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
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


public class JournalApi extends QueriesLiferayApi implements IJournalApi  {
	  private static final Log _log = LogFactoryUtil.getLog(JournalApi.class);

	  
	    public List<JournalArticle> getWCByCode(Long groupId,Long structureId,Long code){
	    	return getWCByCode(groupId, structureId, code);
	    }

	    
		@Override
		public JSONArray getListJournalFoldersByBrand(Long groupId, Long brand)
				throws PortalException {
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
	        return getJournalFoldersJsonOneLevel(groupId, brand, filesArray);
	        
		}
		
		
		@Override
		public JSONArray getListJournalFoldersByBrand(Long groupId, String brand) throws PortalException {
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
			Long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			if(brand!=null){
				Long brandFolder= getFolderWC(groupId, brand, idBase);
				return  getJournalFoldersJsonOneLevel(groupId, brandFolder, filesArray);
			}
			else{
				 return  getJournalFoldersJsonOneLevel(groupId, idBase, filesArray);

			}
			
		}
	    @Override
		public JSONArray getListJournalFoldersBrand(Long groupId) throws PortalException {
			Long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
	        return  getJournalFoldersJsonOneLevel(groupId, idBase, filesArray);
		}
	    
	    public JSONArray getListJournalFolders(Long groupId,Long parent) throws PortalException {
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
	        return  getJournalFoldersJsonOneLevel(groupId, parent, filesArray);
		}
	  
	    @Override
	    public JSONArray getListJournalFolders(Long groupId,String brand,String codeHotel) throws PortalException{
	        JSONArray filesArray=JSONFactoryUtil.createJSONArray();
	        long idBase= getHotelFolderRootByConfigurationFolderWebcontent(groupId);
	        if(codeHotel!=null && brand!=null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
	            Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
	            filesArray= getJournalFoldersJson(groupId, hotelCode, filesArray);
	            if(JournalFolderLocalServiceUtil.getFolder(hotelCode)!=null){
	            JSONObject filesObject= JSONFactoryUtil.createJSONObject();
	 	        JournalFolder object = JournalFolderLocalServiceUtil.getFolder(hotelCode); 
	 	        filesObject.put("folderId", object.getFolderId());
	 	        filesObject.put("nameFolder", object.getName());
	 	        filesArray.put(filesObject);
	            }
	            return filesArray;
	        
	        }
	        else if(codeHotel==null && brand!=null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
	            filesArray= getJournalFoldersJson(groupId, brandFolder, filesArray);
	            if(JournalFolderLocalServiceUtil.getFolder(brandFolder)!=null){
	            JSONObject filesObject= JSONFactoryUtil.createJSONObject();
	 	        JournalFolder object = JournalFolderLocalServiceUtil.getFolder(brandFolder); 
	 	        filesObject.put("folderId", object.getFolderId());
	 	        filesObject.put("nameFolder", object.getName());
	 	        filesArray.put(filesObject);
	            }
	            return filesArray;
	        }
	        else if(brand==null && codeHotel!=null){
	        	   filesArray= getJournalFoldersJson(groupId, idBase, filesArray);
		            if(JournalFolderLocalServiceUtil.getFolder(idBase)!=null){
		            JSONObject filesObject= JSONFactoryUtil.createJSONObject();
		 	        JournalFolder object = JournalFolderLocalServiceUtil.getFolder(idBase); 
		 	        filesObject.put("folderId", object.getFolderId());
		 	        filesObject.put("nameFolder", object.getName());
		 	        filesArray.put(filesObject);
		            }
		            return filesArray;
	        }else{
	            return  getJournalFoldersJson(groupId, idBase, filesArray);
	        }

	    }

	    @Override
	    public JSONArray getListJournalFoldersByCode(Long groupId,Long codeBrand) throws PortalException{
	        JSONArray filesArray=JSONFactoryUtil.createJSONArray();
	        if(getJournalFoldersJson(groupId, codeBrand, filesArray)!= null){
	            return  getJournalFoldersJson(groupId, codeBrand, filesArray);
	        }
	        return filesArray;
	    }
	  
	    @Override
	    public List<JournalArticle> getWCByFolder(Long groupId,Long folderId) throws PortalException{
	        List<JournalArticle> journalArray= new ArrayList<>();
	        if(folderId!=null && groupId!=null){
	            journalArray= getJournalFoldersAndWC(groupId, folderId, journalArray);
	            if( getWCByJournalFolder(groupId, folderId)!=null){
	                for (JournalArticle journal :  getWCByJournalFolder(groupId, folderId)) {
	                    journalArray.add(journal);
	                }
	            }
	            return journalArray;

	        }
	        return new ArrayList<>();
	    }
	   
	    @Override
	    public List<JournalArticle> getWCByName(Long groupId,String brand,String codeHotel,String name) throws PortalException{
	        List<JournalArticle> journalArray= new ArrayList<>();
	        long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
	        if(codeHotel!=null && brand!=null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
	            Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
	            journalArray= getFoldersWCByName(groupId, idBase,name, journalArray);
	            if( getWCByJournalFolder(groupId, hotelCode)!=null){
	                for (JournalArticle journal :  getWCByJournalFolder(groupId, hotelCode)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;

	        }
	        else if(brand!=null && codeHotel==null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
	            journalArray= getFoldersWCByName(groupId, idBase,name, journalArray);
	            if( getWCByJournalFolderAndName(groupId, idBase,name)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndName(groupId, brandFolder,name)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;
	        }
	        else if(codeHotel!=null && brand==null){
	            journalArray= getFoldersWCByName(groupId, idBase,name, journalArray);
	            if( getWCByJournalFolderAndName(groupId, idBase,name)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndName(groupId, idBase,name)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;
	        }else{
	            journalArray= getFoldersWCByName(groupId, idBase,name, journalArray);
	            if( getWCByJournalFolderAndName(groupId, idBase,name)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndName(groupId, idBase,name)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
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
	           System.out.println("case1");
	        	Long brandFolder= getFolderWC(groupId, brand, idBase);
	            Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
	            journalArray= getJournalFoldersAndWC(groupId, hotelCode, journalArray);
	            if( getWCByJournalFolder(groupId, hotelCode)!=null){
	                for (JournalArticle journal :  getWCByJournalFolder(groupId, hotelCode)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;

	        }
	        else if(brand!=null && codeHotel==null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
	            journalArray= getJournalFoldersAndWC(groupId, brandFolder, journalArray);
	            if( getWCByJournalFolder(groupId, brandFolder)!=null){
	                for (JournalArticle journal :  getWCByJournalFolder(groupId, brandFolder)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;
	        }
	        else if(codeHotel!=null && brand==null){
	            journalArray= getJournalFoldersAndWC(groupId, idBase, journalArray);
	            if( getWCByJournalFolder(groupId, idBase)!=null){
	                for (JournalArticle journal :  getWCByJournalFolder(groupId, idBase)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;
	        }else{
	            journalArray= getJournalFoldersAndWC(groupId, idBase, journalArray);
	            if( getWCByJournalFolder(groupId, idBase)!=null){
	                for (JournalArticle journal :  getWCByJournalFolder(groupId, idBase)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;
	        }


	    }
	    //getJournalFoldersAndWCByType
	    @Override
	    public List<JournalArticle> getWCByJournalFolderAndStructureType(Long groupId,String brand,String codeHotel,Long structureId) throws PortalException{
	       
	            DDMStructure ddm =  DDMStructureLocalServiceUtil.getDDMStructure(structureId);
	            List<JournalArticle> journalArray= new ArrayList<>();
	            long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
	            if(brand!=null && codeHotel!=null){//B�squeda por Marca y Codigo de Hotlel
	                Long brandFolder= getFolderWC(groupId, brand, idBase);
	                Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
	                journalArray =  getJournalFoldersAndWCByType(groupId, hotelCode,ddm.getStructureKey(), journalArray);
	                if( getWCByJournalFolderAndTypeStructure(groupId, hotelCode,ddm.getStructureKey())!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructure(groupId, idBase,ddm.getStructureKey())) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;
	                
	              
	            }
	            else if(brand!=null && codeHotel==null){//B�squeda por marca
	                Long brandFolder= getFolderWC(groupId, brand, idBase);
	                journalArray =  getJournalFoldersAndWCByType(groupId, brandFolder,ddm.getStructureKey(), journalArray);
	                if( getWCByJournalFolderAndTypeStructure(groupId, brandFolder,ddm.getStructureKey())!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructure(groupId, brandFolder,ddm.getStructureKey())) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;

	            }
	            else{//Si no se cumplen estos casos B�squeda desde la raiz
	            	 journalArray =  getJournalFoldersAndWCByType(groupId, idBase,ddm.getStructureKey(), journalArray);
		                if( getWCByJournalFolderAndTypeStructure(groupId, idBase,ddm.getStructureKey())!=null){
			                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructure(groupId, idBase,ddm.getStructureKey())) {
			                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
			                        journalArray.add(journal);
			                    }
			                }
			            }
		                return journalArray;
	            }
	       
	       	    }
	    @Override
	    public List<JournalArticle> getWCAndJournalFolderByName(Long groupId,String brand,String codeHotel,String name) throws PortalException{
	        List<JournalArticle> journalArray= new ArrayList<>();
	        long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
	        
	        if(codeHotel!=null && brand!=null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
	            Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
	            journalArray =   getFoldersWCByName(groupId, hotelCode,name, journalArray);
                if( getWCByJournalFolderAndName(groupId, hotelCode,name)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndName(groupId, brandFolder,name)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
                return journalArray;

	        }
	        else if(brand!=null && codeHotel==null){
	            Long brandFolder= getFolderWC(groupId, brand, idBase);
                journalArray =   getFoldersWCByName(groupId, brandFolder,name, journalArray);
                if( getWCByJournalFolderAndName(groupId, brandFolder,name)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndName(groupId, brandFolder,name)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
                return journalArray;
	            
	        }else{
	        	 journalArray =   getFoldersWCByName(groupId, idBase,name, journalArray);
	                if( getWCByJournalFolderAndName(groupId, idBase,name)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndName(groupId, idBase,name)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;
	        }


	    }
	    @Override
		public List<JournalArticle> getWCAndJournalFolderByName(Long groupId, String brand, String codeHotel, String name,
				Long structureId) throws PortalException {
	    	  List<JournalArticle> journalArray= new ArrayList<>();
	          long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
	          if(codeHotel!=null && brand!=null){
	              Long brandFolder= getFolderWC(groupId, brand, idBase);
	              Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
	              journalArray =  getFoldersWCByNameSI(groupId, hotelCode,name,structureId, journalArray);
	                if( getWCByJournalFolderAndNameSI(groupId, hotelCode,name,structureId)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndNameSI(groupId, hotelCode,name,structureId)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;
	          
	          }
	          else if(brand!=null && codeHotel==null){
	              Long brandFolder= getFolderWC(groupId, brand, idBase);
	              journalArray =  getFoldersWCByNameSI(groupId, brandFolder,name,structureId, journalArray);
	                if( getWCByJournalFolderAndNameSI(groupId, brandFolder,name,structureId)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndNameSI(groupId, brandFolder,name,structureId)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;
	          }
	          else if(codeHotel!=null && brand==null){
	        	  journalArray =  getFoldersWCByNameSI(groupId, idBase,name,structureId, journalArray);
	                if( getWCByJournalFolderAndNameSI(groupId, idBase,name,structureId)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndNameSI(groupId, idBase,name,structureId)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;
	          }else{
	        	  journalArray =  getFoldersWCByNameSI(groupId, idBase,name,structureId, journalArray);
	                if( getWCByJournalFolderAndNameSI(groupId, idBase,name,structureId)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndNameSI(groupId, idBase,name,structureId)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	                return journalArray;
	          }
		}
	    
	    
	    /*@Override
	    public List<JournalArticle> searchWebContentByCodeHotelFirstLevel(Long groupId,String structureName,String code) throws PortalException{
	        if( getFoldersWCByCode(groupId, structureName, code)!=null){
	            return  getFoldersWCByCode(groupId, structureName, code);
	        }else{
	            return new ArrayList<>();
	        }
	    }
	*/    @Override
		public List<JournalArticle> searchWebContentByCodeHotelFirstLevelByFolder(Long groupId, String structureName,
				String code,Long folderId) throws PortalException {
	    	if( getFoldersWCByCodeFolderId(groupId, structureName, code,folderId)!=null){
	            return  getFoldersWCByCodeFolderId(groupId, structureName, code,folderId);
	        }else{
	            return new ArrayList<>();
	        }
		}
	    

	    /***M�todos privados****/

	    private long getHotelFolderRootByConfigurationFolderWebcontent(Long groupId) throws PortalException{
	        JournalFolderImpl idFolder = null;
	        for (String baseFileEntry : Contants.JOURNAL_HOTEL) {
	            if(idFolder!=null){
	                idFolder=  getJournalFolderByName(groupId, idFolder.getFolderId(), baseFileEntry);
	            }
	            else{
	                idFolder=  getJournalFolderByName(groupId, new Long(0), baseFileEntry);
	            }
	            _log.info("Folder webcontent:"+idFolder.getName()+"id:"+idFolder.getFolderId());
	        }
	        return idFolder.getFolderId();
	    }
	  
	 @Override
	    public JournalArticle saveWC(String json) throws PortalException {
	        return  createNewWC(json);
	    }

	    @Override
	    public JournalArticle updateWC(String json) throws PortalException {
	        return  UpdateWC(json);
	    }



		@Override
		public List<JournalArticle> getWebcontentRecursiveByType(Long groupId, Long folderId, Long structureId) throws PortalException {
			List<JournalArticle> journalArray= new ArrayList<JournalArticle>();
			return  getJournalFoldersAndWCByTypeId(groupId, folderId, structureId, journalArray);	
		}
		
		
		@Override
		public List<JournalArticle> getWebcontentRecursiveByType(
				Long groupId, 
				String brand, 
				String codeHotel,
				Long structureId) throws PortalException {
			List<JournalArticle> journalArray= new ArrayList<JournalArticle>();
			Long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			if(codeHotel!=null && brand!=null){
			Long brandFolder= getFolderWC(groupId, brand, idBase);
			Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
			journalArray =  getJournalFoldersAndWCByTypeId(groupId, hotelCode, structureId, journalArray);
			if(getWCByJournalFolderAndTypeStructureId(groupId, hotelCode, structureId)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureId(groupId, hotelCode, structureId)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
              return journalArray;	
			}
			else if(codeHotel==null && brand!=null){
				Long brandFolder= getFolderWC(groupId, brand, idBase);
				journalArray =  getJournalFoldersAndWCByTypeId(groupId, brandFolder, structureId, journalArray);
				if( getWCByJournalFolderAndTypeStructureId(groupId, brandFolder, structureId)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureId(groupId, brandFolder, structureId)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	              return journalArray;	}
			else if(brand==null){
				journalArray =  getJournalFoldersAndWCByTypeId(groupId, idBase, structureId, journalArray);
				if( getWCByJournalFolderAndTypeStructureId(groupId, idBase, structureId)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureId(groupId, idBase, structureId)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	              return journalArray;			}
			return journalArray;
		}

		@Override
		public List<JournalArticle> getWebcontentRecursiveByTitle(Long groupId, Long folderId, String title,Long structureId) throws PortalException {
			List<JournalArticle> journalArray= new ArrayList<JournalArticle>();
			return  getJournalFoldersAndWCByTypeTitleS(groupId, folderId,title,structureId, journalArray);
			
		}
		
		@Override
		public List<JournalArticle> getWebcontentRecursiveByTitle(Long groupId, String brand, String codeHotel, String title,
				Long structureId) throws PortalException {
			List<JournalArticle> journalArray= new ArrayList<JournalArticle>();
			Long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			if(codeHotel!=null && brand!=null){
			Long brandFolder= getFolderWC(groupId, brand, idBase);
			Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
			journalArray =  getJournalFoldersAndWCByTypeTitleS(groupId, hotelCode,title,structureId, journalArray);
			if( getWCByJournalFolderAndTypeStructureTitleStructureId(groupId, hotelCode,structureId,title)!=null){
	                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureTitleStructureId(groupId, hotelCode,structureId,title)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
              return journalArray;
			}
			else if(codeHotel==null && brand!=null){
				Long brandFolder= getFolderWC(groupId, brand, idBase);
				journalArray =  getJournalFoldersAndWCByTypeTitleS(groupId, brandFolder,title,structureId, journalArray);
				if( getWCByJournalFolderAndTypeStructureTitleStructureId(groupId, brandFolder,structureId,title)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureTitleStructureId(groupId, brandFolder,structureId,title)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	              return journalArray;			}
			else if(brand==null){
				journalArray =  getJournalFoldersAndWCByTypeTitleS(groupId, idBase,title,structureId, journalArray);
				if( getWCByJournalFolderAndTypeStructureTitleStructureId(groupId, idBase,structureId,title)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureTitleStructureId(groupId, idBase,structureId,title)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	              return journalArray;		
	         }
			return journalArray;
			
		}
		
		


		

		@Override
		public List<JournalArticle> getWCByFolderId(Long groupId,Long folderId) throws PortalException{
	        List<JournalArticle> journalArray= new ArrayList<>();   
	            if(getWCByJournalFolders(groupId, folderId)!=null){
	                for (JournalArticle journal :  getWCByJournalFolders(groupId, folderId)) {
	                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
	                        journalArray.add(journal);
	                    }
	                }
	            }
	            return journalArray;

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
			public Map<JournalArticle, DDMStructure> getWCandStructureById(Long articleId) throws PortalException {
				Map<JournalArticle, DDMStructure> map = new HashMap<JournalArticle, DDMStructure>();
				JournalArticle curentArticle = JournalArticleLocalServiceUtil.getJournalArticle(articleId);
				if(curentArticle!=null){
					DDMStructure ddmStructure = DDMStructureLocalServiceUtil.getDDMStructure(curentArticle.getDDMStructure().getStructureId());
					map.put(curentArticle, ddmStructure);
				}
				return map;
			}


		@Override
		public List<JournalArticle> searchWebContentByCodeHotelFirstLevel(Long groupId, String structureName,
				String code) throws PortalException {
			// TODO Auto-generated method stub
			return null;
		}
		 
		 
			/*Deprecate @Override
			public List<JournalArticle> getWebcontentRecursiveByType(Long groupId, String brand, String codeHotel, String TypeContent) throws PortalException {
				List<JournalArticle> journalArray= new ArrayList<JournalArticle>();
				Long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
				List<DDMStructure> ddmSt =  getStructureByName(TypeContent, groupId);
				if(codeHotel!=null && brand!=null){
				Long brandFolder= getFolderWC(groupId, brand, idBase);
				Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
				journalArray =  getJournalFoldersAndWCByType(groupId, hotelCode, ddmSt.get(0).getStructureKey(), journalArray);
				if( getWCByJournalFolderAndTypeStructure(groupId, hotelCode, ddmSt.get(0).getStructureKey())!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructure(groupId, hotelCode, ddmSt.get(0).getStructureKey())) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	              return journalArray;	
				}
				else if(codeHotel==null && brand!=null){
					Long brandFolder= getFolderWC(groupId, brand, idBase);
					journalArray =  getJournalFoldersAndWCByType(groupId, brandFolder, ddmSt.get(0).getStructureKey(), journalArray);
					if( getWCByJournalFolderAndTypeStructure(groupId, brandFolder, ddmSt.get(0).getStructureKey())!=null){
			                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructure(groupId, brandFolder, ddmSt.get(0).getStructureKey())) {
			                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
			                        journalArray.add(journal);
			                    }
			                }
			            }
		              return journalArray;	
		              }
				else if(brand==null){
					journalArray =  getJournalFoldersAndWCByType(groupId, idBase, ddmSt.get(0).getStructureKey(), journalArray);
					if( getWCByJournalFolderAndTypeStructure(groupId, idBase, ddmSt.get(0).getStructureKey())!=null){
			                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructure(groupId, idBase, ddmSt.get(0).getStructureKey())) {
			                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
			                        journalArray.add(journal);
			                    }
			                }
			            }
		              return journalArray;
				}
				return journalArray;
				
			}*/

			/*@Override
			public List<JournalArticle> getWebcontentRecursiveByTitle(Long groupId, String brand, String codeHotel, String title,String TypeContent) throws PortalException {
				List<JournalArticle> journalArray= new ArrayList<JournalArticle>();
				Long idBase=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
				List<DDMStructure> ddmSt =  getStructureByName(TypeContent, groupId);
				if(codeHotel!=null && brand!=null){
				Long brandFolder= getFolderWC(groupId, brand, idBase);
				Long hotelCode= getFolderWC(groupId, codeHotel, brandFolder);
				journalArray =  getJournalFoldersAndWCByTypeTitle(groupId, hotelCode, ddmSt.get(0).getStructureKey(),title, journalArray);
				if( getWCByJournalFolderAndTypeStructureTitle(groupId, hotelCode, ddmSt.get(0).getStructureKey(),title)!=null){
		                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureTitle(groupId, hotelCode, ddmSt.get(0).getStructureKey(),title)) {
		                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
		                        journalArray.add(journal);
		                    }
		                }
		            }
	              return journalArray;
				}
				else if(codeHotel==null && brand!=null){
					Long brandFolder= getFolderWC(groupId, brand, idBase);
					journalArray =  getJournalFoldersAndWCByTypeTitle(groupId, brandFolder, ddmSt.get(0).getStructureKey(),title, journalArray);
					if( getWCByJournalFolderAndTypeStructureTitle(groupId, brandFolder, ddmSt.get(0).getStructureKey(),title)!=null){
			                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureTitle(groupId, brandFolder, ddmSt.get(0).getStructureKey(),title)) {
			                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
			                        journalArray.add(journal);
			                    }
			                }
			            }
		              return journalArray;		
		              }
				else if(brand==null){
					journalArray =  getJournalFoldersAndWCByTypeTitle(groupId, idBase, ddmSt.get(0).getStructureKey(),title, journalArray);
					if( getWCByJournalFolderAndTypeStructureTitle(groupId, idBase, ddmSt.get(0).getStructureKey(),title)!=null){
			                for (JournalArticle journal :  getWCByJournalFolderAndTypeStructureTitle(groupId, idBase, ddmSt.get(0).getStructureKey(),title)) {
			                    if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, journal.getArticleId(), journal.getVersion())){
			                        journalArray.add(journal);
			                    }
			                }
			            }
		              return journalArray;				}
				return journalArray;
				
			}

			*/
}

