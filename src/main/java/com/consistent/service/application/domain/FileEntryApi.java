package com.consistent.service.application.domain;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.consistent.service.application.models.Contants;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Base64;

public class FileEntryApi implements com.consistent.service.application.IDomain.IFileEntryApi {
	private static final Log _log = LogFactoryUtil.getLog(FileEntryApi.class);
	private static QueriesLiferayApi _query = new QueriesLiferayApi();

	@Override
	public JSONArray saveFile(Long groupId,Long userId,Long folderId,String image,String name,String description,String mimeType) throws FileNotFoundException{
		
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		try {
		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setScopeGroupId(groupId);
		byte[] imageByte=Base64.decode(image);
		
		if(_query.getFilesByName(groupId, folderId, name)!=null && _query.getFilesByName(groupId, folderId, name).size()>0){
			JSONObject filesObject=null;
			filesObject=JSONFactoryUtil.createJSONObject();
			filesObject.put("message","No se a podido guardar");
			filesObject.put("causa", "El nombre del archivo ya existe");
			filesArray.put(filesObject);
			return filesArray;
		}
		else{
		JSONObject filesObject=null;
		FileEntry file = DLAppLocalServiceUtil.addFileEntry(userId, groupId, folderId, name, mimeType, imageByte, serviceContext);
		filesObject=JSONFactoryUtil.createJSONObject();
		filesObject.put("idFile", file.getFileEntryId());
		filesObject.put("filename", file.getFileName());
		String url="/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+System.currentTimeMillis();;
		filesObject.put("fullPath",url.replace(" ", "%20") );
		filesObject.put("imageThumbnail",url.replace(" ", "%20")+"&imageThumbnail=1");
		filesArray.put(filesObject);
		return filesArray;
		
		}
		} catch (PortalException e) {
		e.printStackTrace();		
		}
	return filesArray;
	
}

	@Override
	public JSONArray getFilesByCurrentFolderAndName(Long groupId,Long currentFolder,String name) throws PortalException{
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		filesArray=getFoldersAndFilesByName(groupId, currentFolder, name, filesArray);
		if(_query.getFilesByName(groupId, currentFolder, name)!=null){
		for (DLFileEntry file : _query.getFilesByName(groupId, currentFolder, name)) {
			filesArray.put(mapping(file));

		}
		}
		return filesArray;
	}

	@Override
	public JSONArray getFilesByName(Long groupId,String brand,String name,String code_hotel) throws PortalException{
		
		
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long id_base=getRootFolderByConfiguration(groupId);
		if(code_hotel!=null && brand!=null){
			Long brandFolder=_query.getFolder(groupId, brand, id_base);
			Long hc=_query.getFolder(groupId, code_hotel, brandFolder);
			filesArray=getFoldersAndFilesByName(groupId, hc, name, filesArray);
			if(_query.getFilesByName(groupId, hc, name)!=null){
				for (DLFileEntry file : _query.getFilesByName(groupId, hc, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(brand!=null && code_hotel==null){
			Long brandFolder=_query.getFolder(groupId, brand, id_base);
			filesArray=getFoldersAndFilesByName(groupId, brandFolder, name, filesArray);
			if(_query.getFilesByName(groupId, brandFolder, name)!=null){
				for (DLFileEntry file : _query.getFilesByName(groupId, brandFolder, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(code_hotel!=null && brand==null){
			filesArray=getFoldersAndFilesByName(groupId, id_base, name, filesArray);
			if(_query.getFilesByName(groupId, id_base, name)!=null){
				for (DLFileEntry file : _query.getFilesByName(groupId, id_base, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}else{
			filesArray=getFoldersAndFilesByName(groupId, id_base, name, filesArray);
			if(_query.getFilesByName(groupId, id_base, name)!=null){
				for (DLFileEntry file : _query.getFilesByName(groupId, id_base, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}

	}

	@Override
	public JSONArray getListFolders(Long groupId,String brand,String code_hotel) throws PortalException{
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long id_base=getRootFolderByConfiguration(groupId);
		if(code_hotel!=null && brand!=null){
			Long brandFolder=_query.getFolder(groupId, brand, id_base);
			Long hc=_query.getFolder(groupId, code_hotel, brandFolder);
			return getFoldersJson(groupId, hc, filesArray);
		}
		else if(brand!=null && code_hotel==null){
			Long brandFolder=_query.getFolder(groupId, brand, id_base);

			return getFoldersJson(groupId, brandFolder, filesArray);
		}
		else if(code_hotel!=null && brand==null){
			return getFoldersJson(groupId, id_base, filesArray);
		}else{
			return getFoldersJson(groupId, id_base, filesArray);
		}
		
	}

	@Override
public JSONArray getFilesAndFolder(Long groupId,String brand,String type,String code_hotel) throws PortalException{
		
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long id_base=getRootFolderByConfiguration(groupId);
		if(code_hotel!=null && brand!=null){
			Long brandFolder=_query.getFolder(groupId, brand, id_base);
			Long hc=_query.getFolder(groupId, code_hotel, brandFolder);
			filesArray=getFoldersAndFilesByfolderJson(groupId, hc, type, filesArray);
			if(getFilesByFolder(groupId, hc)!=null){
				for (DLFileEntry file : getFilesByFolder(groupId, hc)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(brand!=null && code_hotel==null){
			Long brandFolder=_query.getFolder(groupId, brand, id_base);
			filesArray=getFoldersAndFilesByfolderJson(groupId, brandFolder, type, filesArray);
			if(getFilesByFolder(groupId, brandFolder)!=null){
				for (DLFileEntry file : getFilesByFolder(groupId, brandFolder)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(code_hotel!=null && brand==null){
			if(getFilesByFolder(groupId, id_base)!=null){
				for (DLFileEntry file : getFilesByFolder(groupId, id_base)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}else{
			if(getFilesByFolder(groupId, id_base)!=null){
				for (DLFileEntry file : getFilesByFolder(groupId, id_base)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		
	}
	
	
	
	
	
	
	private JSONArray getFoldersAndFilesByName(Long groupId,Long parent,String namefile,JSONArray filesArray) throws PortalException{
		List<DLFolder> listFolders = getSubFolderByFolderParent(groupId, new Long(parent));
		if(listFolders != null && listFolders.size() > 0){
			JSONObject filesObject=null;
			for (DLFolder object : listFolders) {
				System.out.println("folder:"+ object.getFolderId());
				if(_query.getFilesByName(groupId, object.getFolderId(),namefile)!= null && !_query.getFilesByName(groupId, object.getFolderId(),namefile).isEmpty()){
					for (DLFileEntry file : _query.getFilesByName(groupId, object.getFolderId(),namefile)) {
						filesObject=JSONFactoryUtil.createJSONObject();
						filesObject.put("idFile", file.getFileEntryId());
						filesObject.put("filename", file.getFileName());
						filesObject.put("path", file.getFolder().getPath());
						String url="/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+System.currentTimeMillis();;
						filesObject.put("fullPath",url.replace(" ", "%20") );
						filesObject.put("imageThumbnail",url.replace(" ", "%20")+"&imageThumbnail=1");
						filesArray.put(filesObject);
					}	
				}
				if(getFoldersAndFilesByName(groupId,object.getFolderId(),namefile,filesArray)!= null && !getFoldersAndFilesByName(groupId,object.getFolderId(),namefile,filesArray).isNull(0)){
					getFoldersAndFilesByName(groupId,object.getFolderId(),namefile,filesArray);
				}
			}
		}
		return filesArray;
	}
	
	 private List<DLFolder> getSubFolderByFolderParent(Long groupId,Long idFolder){
	    	List<DLFolder> dlFolders = DLFolderLocalServiceUtil.getFolders(groupId, idFolder);
	    	if(dlFolders.size()>0){
	    		return dlFolders;
	    	}
	      return new ArrayList<>();
	    }
	
	/*Mapeo entidad DLFILEENTRY*/
	private JSONObject mapping(DLFileEntry file) throws PortalException{
			JSONObject filesObject=null;
			filesObject=JSONFactoryUtil.createJSONObject();
			filesObject.put("idFile", file.getFileEntryId());
			filesObject.put("filename", file.getFileName());
			filesObject.put("path", file.getFolder().getPath());
			String url="/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+System.currentTimeMillis();;
			filesObject.put("fullPath",url.replace(" ", "%20") );
			filesObject.put("imageThumbnail",url.replace(" ", "%20")+"&imageThumbnail=1");
			return filesObject;

	}
	
	 /*Marca codigo_hotel tipo*/
		private long getRootFolderByConfiguration(Long groupId) throws PortalException{
			DLFolder idFolder = null;
			for (String baseFileEntry : Contants.DLFILEENTRY_BASE) {
				if(idFolder==null){
				 idFolder= DLFolderLocalServiceUtil.getFolder(groupId, 0, baseFileEntry);
				}
				else{
				 idFolder= DLFolderLocalServiceUtil.getFolder(groupId, idFolder.getFolderId(), baseFileEntry);
				}
				_log.info(idFolder.getName()+"id:"+idFolder.getFolderId());
				}
			return idFolder.getFolderId();
		}
		
		/*Recupera la informacion de los folders*/
		private JSONArray getFoldersJson(Long groupId,Long parent,JSONArray filesArray) throws PortalException{
			List<DLFolder> listFolders = getSubFolderByFolderParent(groupId, new Long(parent));
			if(listFolders != null && listFolders.size() > 0){
				JSONObject filesObject=null;
				for (DLFolder object : listFolders) {
					filesObject=JSONFactoryUtil.createJSONObject();
					filesObject.put("folderId", object.getFolderId());
					filesObject.put("nameFolder", object.getName());
					filesArray.put(filesObject);
					if(getFoldersJson(groupId,object.getFolderId(),filesArray)!= null && !getFoldersJson(groupId,object.getFolderId(),filesArray).isNull(0)){
					 getFoldersJson(groupId,object.getFolderId(),filesArray);
					}
					
				}
			}
			return filesArray;
		}
	    private List<DLFileEntry> getFilesByFolder(Long siteID,Long idFolder) throws PortalException{
	    	List<DLFileEntry> results = DLFileEntryLocalServiceUtil.getFileEntries(siteID,idFolder);
	    	if(results.size()>0){
	    		return results;
	    	}
	    	return new ArrayList<>();
	    }
	    
	    public JSONArray getFolders(Long groupId,Long parent,String type) throws PortalException{
			List<DLFolder> listFolders = getSubFolderByFolderParent(groupId, parent);
			JSONArray filesArray,folderArray=JSONFactoryUtil.createJSONArray();
		    JSONObject folderObject=null,filesObject=null; 
		    long currentDateTime = System.currentTimeMillis();
			if(listFolders != null && listFolders.size() > 0){
				for (DLFolder object : listFolders) {
					folderObject=JSONFactoryUtil.createJSONObject();
					folderObject.put("name", object.getName());
					folderObject.put("code", object.getFolderId());
					filesArray = JSONFactoryUtil.createJSONArray();
					if(getFilesByFolder(groupId, object.getFolderId())!= null && !getFilesByFolder(groupId, object.getFolderId()).isEmpty()){
						for (DLFileEntry file : getFilesByFolder(groupId, object.getFolderId())) {
							filesObject=JSONFactoryUtil.createJSONObject();
							filesObject.put("idFile", file.getFileEntryId());
							filesObject.put("filename", file.getFileName());
							filesObject.put("path", file.getFolder().getPath());
							filesObject.put("fullPath", "/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+currentDateTime);
							filesArray.put(filesObject);
						}
						folderObject.put("files", filesArray);
					}
					if(getFolders(groupId,object.getFolderId(),type)!= null && !getFolders(groupId,object.getFolderId(),type).isNull(0)){
						folderObject.put("child", getFolders(groupId,object.getFolderId(),type));
					}
					folderArray.put(folderObject);
				}
			}
			return folderArray;
		}
	    
	    
	    public JSONArray getFoldersAndFilesByfolderJson(Long groupId,Long parent,String type,JSONArray filesArray) throws PortalException{
			List<DLFolder> listFolders = getSubFolderByFolderParent(groupId, new Long(parent));		
			if(listFolders != null && listFolders.size() > 0){
				JSONObject filesObject=null;
				for (DLFolder object : listFolders) {
					System.out.println(object.getName());
					if(getFilesByFolder(groupId, object.getFolderId())!= null && !getFilesByFolder(groupId, object.getFolderId()).isEmpty()){
						for (DLFileEntry file : getFilesByFolder(groupId, object.getFolderId())) {
							filesObject=JSONFactoryUtil.createJSONObject();
						
							filesObject.put("idFile", file.getFileEntryId());
							filesObject.put("filename", file.getFileName());
							filesObject.put("path", file.getFolder().getPath());
							String url="/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+System.currentTimeMillis();;
							filesObject.put("fullPath",url.replace(" ", "%20") );
							filesObject.put("imageThumbnail",url.replace(" ", "%20")+"&imageThumbnail=1");
							filesArray.put(filesObject);
						}
						
					}
					if(getFoldersAndFilesByfolderJson(groupId,object.getFolderId(),type,filesArray)!= null && !getFoldersAndFilesByfolderJson(groupId,object.getFolderId(),type,filesArray).isNull(0)){
						getFoldersAndFilesByfolderJson(groupId,object.getFolderId(),type,filesArray);
					}
				}
			}
			return filesArray;
		}
	  
		@SuppressWarnings("unused")
		private JSONArray ReadJSON(JSONArray folders,JSONArray filesArray) throws PortalException{
			JSONObject folderObject=null;
			for (int index = 0;index < folders.length(); index++) {
				    folderObject = JSONFactoryUtil.createJSONObject();
			        JSONObject jsonObject = folders.getJSONObject(index);
			        if(!jsonObject.getString("filename").isEmpty()){
			        	folderObject.put("path", jsonObject.getString("path"));
			        	folderObject.put("filename", jsonObject.getString("filename"));
			        	folderObject.put("idFile", jsonObject.getString("idFile"));
			        	folderObject.put("fullPath", jsonObject.getString("fullPath"));
			      
			        }
			        filesArray.put(folderObject);
			        if(jsonObject.getJSONArray("child")!=null){
			        	JSONArray childsNodesJsonObject = jsonObject.getJSONArray("child");
			        	ReadJSON(childsNodesJsonObject,filesArray);
			      
			        }
			        else if(jsonObject.getJSONArray("files")!=null){
			        	JSONArray childsNodesJsonObject = jsonObject.getJSONArray("files");
			         	ReadJSON(childsNodesJsonObject,filesArray);
			       }
			        
			      }
			return filesArray;
		}
		
		 public DLFolder getFolderByName(Long groupId,String name) throws PortalException{
			    Long base_folder = getRootFolderByConfiguration(groupId);
			    return	DLFolderLocalServiceUtil.getFolder(groupId, base_folder, name);
		}
}
