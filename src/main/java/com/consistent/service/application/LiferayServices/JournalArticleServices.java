package com.consistent.service.application.LiferayServices;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import com.consistent.service.application.models.Contants;
import com.consistent.service.application.models.Files;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.model.impl.JournalArticleImpl;
import com.liferay.journal.model.impl.JournalFolderImpl;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
//import com.liferay.portal.kernel.dao.orm.QueryDefinition;
//import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
//import com.liferay.portal.kernel.workflow.WorkflowConstants;





@Component(configurationPid = "com.consistent.service.application.config.ConfigPersonalizacionApi")
public class JournalArticleServices {
	private static final Log log = LogFactoryUtil.getLog(JournalArticleServices.class);
	private static QueriesLiferayApi query = new QueriesLiferayApi();
	/*************************Inicia la sección de llamados***************************/
	
	
	
	
	 /**
     * Método getFilesAndFolder
     * @param groupId Id del sítio
     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
     * @param type tipo de archivo a buscar(No se filtra)
     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
     * @return <ul>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  </ul>
     */
	public JSONArray getFilesAndFolder(Long groupId,String brand,String type,String code_hotel) throws PortalException{
		
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long id_base=getRootFolderByConfiguration(groupId);
		if(code_hotel!=null && brand!=null){
			Long brandFolder=query.getFolder(groupId, brand, id_base);
			Long hc=query.getFolder(groupId, code_hotel, brandFolder);
			filesArray=getFoldersAndFilesByfolderJson(groupId, hc, type, filesArray);
			if(getFilesByFolder(groupId, hc)!=null){
				for (DLFileEntry file : getFilesByFolder(groupId, hc)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(brand!=null && code_hotel==null){
			Long brandFolder=query.getFolder(groupId, brand, id_base);
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
	
	
	
	
	
	 /**
     * Método getFolders
     * @param groupId Id del sítio
     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
     * @return <ul>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  </ul>
     */
	public JSONArray getListFolders(Long groupId,String brand,String code_hotel) throws PortalException{
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long id_base=getRootFolderByConfiguration(groupId);
		if(code_hotel!=null && brand!=null){
			Long brandFolder=query.getFolder(groupId, brand, id_base);
			Long hc=query.getFolder(groupId, code_hotel, brandFolder);
			return getFoldersJson(groupId, hc, filesArray);
		}
		else if(brand!=null && code_hotel==null){
			Long brandFolder=query.getFolder(groupId, brand, id_base);

			return getFoldersJson(groupId, brandFolder, filesArray);
		}
		else if(code_hotel!=null && brand==null){
			return getFoldersJson(groupId, id_base, filesArray);
		}else{
			return getFoldersJson(groupId, id_base, filesArray);
		}
		
	}
	
	
	
	//getFilesByName((portletGroupId,"AQUA","","AQC","junior");//busqueda recursiva de folder y archivos x M, CH y NH
	
	 /**
     * Método getFilesByName
     * @param groupId Id del sítio
     * @param name filrado por este campo
     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
     * @return <ul>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  </ul>
     */
	public JSONArray getFilesByName(Long groupId,String brand,String name,String code_hotel) throws PortalException{
		
	
		JSONArray filesArray=JSONFactoryUtil.createJSONArray();
		long id_base=getRootFolderByConfiguration(groupId);
		if(code_hotel!=null && brand!=null){
			Long brandFolder=query.getFolder(groupId, brand, id_base);
			Long hc=query.getFolder(groupId, code_hotel, brandFolder);
			filesArray=getFoldersAndFilesByName(groupId, hc, name, filesArray);
			if(query.getFilesByName(groupId, hc, name)!=null){
				for (DLFileEntry file : query.getFilesByName(groupId, hc, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(brand!=null && code_hotel==null){
			Long brandFolder=query.getFolder(groupId, brand, id_base);
			filesArray=getFoldersAndFilesByName(groupId, brandFolder, name, filesArray);
			if(query.getFilesByName(groupId, brandFolder, name)!=null){
				for (DLFileEntry file : query.getFilesByName(groupId, brandFolder, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}
		else if(code_hotel!=null && brand==null){
			filesArray=getFoldersAndFilesByName(groupId, id_base, name, filesArray);
			if(query.getFilesByName(groupId, id_base, name)!=null){
				for (DLFileEntry file : query.getFilesByName(groupId, id_base, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}else{
			filesArray=getFoldersAndFilesByName(groupId, id_base, name, filesArray);
			if(query.getFilesByName(groupId, id_base, name)!=null){
				for (DLFileEntry file : query.getFilesByName(groupId, id_base, name)) {
					filesArray.put(mapping(file));
				}
				}
			return filesArray;
		}

	}
	
	
	 /**
     * Método getFilesByCurrentFolderAndName
     * @param groupId Id del sítio
     * @param name filrado por este campo
     * @param currentFolder Nombre de la carpeta marca donde se hace la búsqueda
     * @return <ul>
     *  <li>JSONArray </li>
     *  </ul>
     */
	public JSONArray getFilesByCurrentFolderAndName(Long groupId,Long currentFolder,String name) throws PortalException{
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
			filesArray=getFoldersAndFilesByName(groupId, currentFolder, name, filesArray);
			if(query.getFilesByName(groupId, currentFolder, name)!=null){
			for (DLFileEntry file : query.getFilesByName(groupId, currentFolder, name)) {
				filesArray.put(mapping(file));

			}
			}
			return filesArray;
		}
		
		/*Recupera la informacion de los folders*/
		public JSONArray getFoldersJson(Long groupId,Long parent,JSONArray filesArray) throws PortalException{
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

		/*Permite salvar un archivo a través de base64*/
		public JSONArray saveFile(Long groupId,Long userId,Long folderId,String image,String name,String description,String mimeType) throws FileNotFoundException{
			
						JSONArray filesArray=JSONFactoryUtil.createJSONArray();
						try {
						ServiceContext serviceContext = new ServiceContext();
						serviceContext.setScopeGroupId(groupId);
						byte[] imageByte=Base64.decode(image);
						
						if(query.getFilesByName(groupId, folderId, name)!=null && query.getFilesByName(groupId, folderId, name).size()>0){
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
	
		
		
		/*****************Manejo de webcontents******************/
		/**
	     * Método getListJournalFolders
	     * @param groupId Id del sítio
	     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
	     * @param type tipo de archivo a buscar(No se filtra)
	     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
	     * @return <ul>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  </ul>
	     */
		public JSONArray getListJournalFolders(Long groupId,String brand,String code_hotel) throws PortalException{
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
			long id_base= getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			if(code_hotel!=null && brand!=null){
				Long brandFolder=query.getFolderWC(groupId, brand, id_base);
				Long hc=query.getFolderWC(groupId, code_hotel, brandFolder);
				return getJournalFoldersJson(groupId, hc, filesArray);
			}
			else if(brand!=null && code_hotel==null){
				Long brandFolder=query.getFolderWC(groupId, brand, id_base);
				return getJournalFoldersJson(groupId, brandFolder, filesArray);
			}
			else if(code_hotel!=null && brand==null){
				return getJournalFoldersJson(groupId, id_base, filesArray);
			}else{
				return getJournalFoldersJson(groupId, id_base, filesArray);
			}
				
		}
		
		/**
	     * Método getWCByName
	     * @param groupId Id del sítio
	     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
	     * @param type tipo de archivo a buscar(No se filtra)
	     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
	     * @return <ul>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  </ul>
	     */
		public JSONArray getWCByName(Long groupId,String brand,String name,String code_hotel) throws PortalException{
			JSONArray filesArray=JSONFactoryUtil.createJSONArray();
			long id_base=getRootFolderByConfiguration(groupId);
			if(code_hotel!=null && brand!=null){
				Long brandFolder=query.getFolder(groupId, brand, id_base);
				Long hc=query.getFolder(groupId, code_hotel, brandFolder);
				filesArray=getFoldersAndFilesByName(groupId, hc, name, filesArray);
				if(query.getFilesByName(groupId, hc, name)!=null){
					for (DLFileEntry file : query.getFilesByName(groupId, hc, name)) {
						filesArray.put(mapping(file));
					}
					}
				return filesArray;
			}
			else if(brand!=null && code_hotel==null){
				Long brandFolder=query.getFolder(groupId, brand, id_base);
				filesArray=getFoldersAndFilesByName(groupId, brandFolder, name, filesArray);
				if(query.getFilesByName(groupId, brandFolder, name)!=null){
					for (DLFileEntry file : query.getFilesByName(groupId, brandFolder, name)) {
						filesArray.put(mapping(file));
					}
					}
				return filesArray;
			}
			else if(code_hotel!=null && brand==null){
				filesArray=getFoldersAndFilesByName(groupId, id_base, name, filesArray);
				if(query.getFilesByName(groupId, id_base, name)!=null){
					for (DLFileEntry file : query.getFilesByName(groupId, id_base, name)) {
						filesArray.put(mapping(file));
					}
					}
				return filesArray;
			}else{
				filesArray=getFoldersAndFilesByName(groupId, id_base, name, filesArray);
				if(query.getFilesByName(groupId, id_base, name)!=null){
					for (DLFileEntry file : query.getFilesByName(groupId, id_base, name)) {
						filesArray.put(mapping(file));
					}
					}
				return filesArray;
			}

		}
		
		//busqueda recursiva de webcontents x M y CH getAllWCAndJournalFolder((portletGroupId,"AQUA","","AQC");
		/**
	     * Método getWCByName
	     * @param groupId Id del sítio
	     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
	     * @param type tipo de archivo a buscar(No se filtra)
	     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
	     * @return <ul>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  </ul>
	     */
		public List<JournalArticle> getWCAndJournalFolder(Long groupId,String brand,String code_hotel) throws PortalException{
			List<JournalArticle> journalArray= new ArrayList<>();
			long id_base=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
			if(code_hotel!=null && brand!=null){
				Long brandFolder=query.getFolderWC(groupId, brand, id_base);
				Long hc=query.getFolderWC(groupId, code_hotel, brandFolder);
				journalArray=getJournalFoldersAndWC(groupId, hc, journalArray);
				if(query.getWCByJournalFolder(groupId, hc)!=null){
				for (JournalArticle journal : query.getWCByJournalFolder(groupId, hc)) {
						journalArray.add(journal);
				}
				}
				return journalArray;
				
			}
			else if(brand!=null && code_hotel==null){
				Long brandFolder=query.getFolderWC(groupId, brand, id_base);
				journalArray=getJournalFoldersAndWC(groupId, brandFolder, journalArray);
				if(query.getWCByJournalFolder(groupId, brandFolder)!=null){
				for (JournalArticle journal : query.getWCByJournalFolder(groupId, brandFolder)) {
						journalArray.add(journal);
				}
				}
				return journalArray;
			}
			else if(code_hotel!=null && brand==null){
				journalArray=getJournalFoldersAndWC(groupId, id_base, journalArray);
				if(query.getWCByJournalFolder(groupId, id_base)!=null){
				for (JournalArticle journal : query.getWCByJournalFolder(groupId, id_base)) {
						journalArray.add(journal);
				}
				}
				return journalArray;
			}else{
				journalArray=getJournalFoldersAndWC(groupId, id_base, journalArray);
				if(query.getWCByJournalFolder(groupId, id_base)!=null){
				for (JournalArticle journal : query.getWCByJournalFolder(groupId, id_base)) {
						journalArray.add(journal);
				}
				}
				return journalArray;
			}
			
			
		}
		
		//busqueda recursiva de webcontents x M, CH y tipo getWCAndJournalFolderType((portletGroupId,"AQUA","","AQC");
		/**
	     * Método getWCByName
	     * @param groupId Id del sítio
	     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
	     * @param type tipo de archivo a buscar(No se filtra)
	     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
	     * @return <ul>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  </ul>
	     */
		public List<JournalArticle> getWCAndJournalFolderType(Long groupId,String brand,String code_hotel,String type) throws PortalException{
				if(getStructureByName(type, groupId)!=null && !getStructureByName(type, groupId).isEmpty()){
					List<DDMStructure> ddm = getStructureByName(type, groupId);
					List<JournalArticle> journalArray= new ArrayList<>();
					long id_base=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
					Long brandFolder=query.getFolderWC(groupId, brand, id_base);
					Long hc=query.getFolderWC(groupId, code_hotel, brandFolder);
					journalArray=getJournalFoldersAndWCByType(groupId, hc,ddm.get(0).getStructureKey(), journalArray);
					if(query.getWCByJournalFolderAndType(groupId, hc,ddm.get(0).getStructureKey())!=null){
						for (JournalArticle journal : query.getWCByJournalFolderAndType(groupId, hc,ddm.get(0).getStructureKey())) {
							journalArray.add(journal);
						}
						}
					return journalArray;
				}
				return new ArrayList<>();
				}
		/**
	     * Método getWCByName
	     * @param groupId Id del sítio
	     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
	     * @param type tipo de archivo a buscar(No se filtra)
	     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
	     * @return <ul>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
	     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
	     *  </ul>
	     */
			public List<JournalArticle> getWCAndJournalFolderByName(Long groupId,String brand,String code_hotel,String name,String type) throws PortalException{
				List<JournalArticle> journalArray= new ArrayList<>();
				long id_base=getHotelFolderRootByConfigurationFolderWebcontent(groupId);
				if(code_hotel!=null && brand!=null){
					Long brandFolder=query.getFolderWC(groupId, brand, id_base);
					Long hc=query.getFolderWC(groupId, code_hotel, brandFolder);
					journalArray=query.getFoldersWCByName(groupId, id_base,name, journalArray);
					if(query.getWCByJournalFolder(groupId, hc)!=null){
					for (JournalArticle journal : query.getWCByJournalFolder(groupId, hc)) {
							journalArray.add(journal);
					}
					}
					return journalArray;
					
				}
				else if(brand!=null && code_hotel==null){
					Long brandFolder=query.getFolderWC(groupId, brand, id_base);
					journalArray=query.getFoldersWCByName(groupId, id_base,name, journalArray);
					if(query.getWCByJournalFolderAndName(groupId, id_base,name)!=null){
					for (JournalArticle journal : query.getWCByJournalFolderAndName(groupId, brandFolder,name)) {
							journalArray.add(journal);
					}
					}
					return journalArray;
				}
				else if(code_hotel!=null && brand==null){
					journalArray=query.getFoldersWCByName(groupId, id_base,name, journalArray);
					if(query.getWCByJournalFolderAndName(groupId, id_base,name)!=null){
					for (JournalArticle journal : query.getWCByJournalFolderAndName(groupId, id_base,name)) {
							journalArray.add(journal);
					}
					}
					return journalArray;
				}else{
					journalArray=query.getFoldersWCByName(groupId, id_base,name, journalArray);
					if(query.getWCByJournalFolderAndName(groupId, id_base,name)!=null){
					for (JournalArticle journal : query.getWCByJournalFolderAndName(groupId, id_base,name)) {
							journalArray.add(journal);
					}
					}
					return journalArray;
				}
				
				
			}
	/*****************Manejo de webcontents******************/
			/**
		     * Método getWCByName
		     * @param groupId Id del sítio
		     * @param brand Nombre de la carpeta marca donde se hace la búsqueda
		     * @param type tipo de archivo a buscar(No se filtra)
		     * @param code_hotel Nombre de la carpeta códifgo de hotel para la búsqueda
		     * @return <ul>
		     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
		     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
		     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
		     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
		     *  </ul>
		     */
	public List<DDMStructure> getAllStructuresBySite(Long groupId) throws PortalException {
	DynamicQuery query = DDMStructureLocalServiceUtil.dynamicQuery()
	.add(PropertyFactoryUtil.forName("groupId").eq(groupId));
	List<DDMStructure> structures = DDMStructureLocalServiceUtil.dynamicQuery(query);
	List<DDMStructure> valid_structures = new ArrayList<>();
	for (DDMStructure ddmStructure : structures) {
		 if(validStructure(ddmStructure.getStructureId())==true){
		 valid_structures.add(ddmStructure);
		 }
		}
		if (valid_structures.size() > 0) {
		log.info("Estrucruras Validas:"+valid_structures.size() );
		return valid_structures;
		}
		return new ArrayList<>();
		    }
			
/*************************Termina la sección de llamados***************************/
		
			/*Mapeo entidad DLFILEENTRY*/
			public JSONObject mapping(DLFileEntry file) throws PortalException{
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
			
			
			
		/*Recupera la informacion de los JournalFolder*/
			public JSONArray getJournalFoldersJson(Long groupId,Long parent,JSONArray filesArray) throws PortalException{
				List<JournalFolder> listFolders =JournalFolderLocalServiceUtil.getFolders(groupId, new Long(parent));
				if(listFolders != null && listFolders.size() > 0){
					JSONObject filesObject=null;
					for (JournalFolder object : listFolders) {
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
	
	
	
	
	
	public JSONArray getFoldersAndFilesByName(Long groupId,Long parent,String namefile,JSONArray filesArray) throws PortalException{
		List<DLFolder> listFolders = getSubFolderByFolderParent(groupId, new Long(parent));
		if(listFolders != null && listFolders.size() > 0){
			JSONObject filesObject=null;
			for (DLFolder object : listFolders) {
				System.out.println("folder:"+ object.getFolderId());
				if(query.getFilesByName(groupId, object.getFolderId(),namefile)!= null && !query.getFilesByName(groupId, object.getFolderId(),namefile).isEmpty()){
					for (DLFileEntry file : query.getFilesByName(groupId, object.getFolderId(),namefile)) {
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
  
	public JSONArray ReadJSON(JSONArray folders,JSONArray filesArray) throws PortalException{
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
	
	public JSONArray getFilesAndFolders(Long groupId,String brand,String type,String code_hotel) throws PortalException{
		long id_base=getRootFolderByConfiguration(groupId);
		Long brandFolder=query.getFolder(groupId, brand, id_base);
		getFilesByFolder(groupId, brandFolder);
		Long hc=query.getFolder(groupId, code_hotel, brandFolder);
		getFilesByFolder(groupId, hc);
        return getFolders(groupId,hc,type);
	}
	

	
	public List<DDMStructure> getAllStructuresBySite1(Long siteID) {
        DynamicQuery query = DDMStructureLocalServiceUtil.dynamicQuery()
                 .add(PropertyFactoryUtil.forName("groupId").eq(siteID));
        List<DDMStructure> structures = DDMStructureLocalServiceUtil.dynamicQuery(query);
        List<DDMStructure> valid_structures = new ArrayList<>();
        for (DDMStructure ddmStructure : structures) {
        	if(validStructure(ddmStructure.getStructureId())==true){
        		valid_structures.add(ddmStructure);
        	}
		}
        if (valid_structures.size() > 0) {
        	log.info("Estrucruras Validas:"+valid_structures.size() );
            return valid_structures;
        }
        return new ArrayList<>();
    }
	
	
	public List<DDMStructure> getStructureByID(Long structureId,Long siteID) {
        DynamicQuery query = DDMStructureLocalServiceUtil.dynamicQuery()
                 .add(PropertyFactoryUtil.forName("structureId").eq(structureId))
                 .add(PropertyFactoryUtil.forName("groupId").eq(new Long(siteID)));
        List<DDMStructure> structures = DDMStructureLocalServiceUtil.dynamicQuery(query);
        List<DDMStructure> valid_structures = new ArrayList<>();
        for (DDMStructure ddmStructure : structures) {
        	if(validStructure(ddmStructure.getStructureId())){
        		valid_structures.add(ddmStructure);
        	}
		}
        if (valid_structures.size() > 0) {
        	log.info("Estrucruras Validas:"+valid_structures.size() );
            return valid_structures;
        }
        return new ArrayList<>();
    }
	
	public boolean validStructure(Long id){
		for (String structureID : Contants.STRUCTURE_IDS) {
			log.info("structure id uno: "+structureID);
			log.info("structure id dos: "+id);
			if(structureID.equals(String.valueOf(id))) return true;
		}
		return false;
	}
	
	
	
	public List<DDMStructure> getStructureByName(String nameStructure,Long siteID) {
		DynamicQuery query = DDMStructureLocalServiceUtil.dynamicQuery()
                 .add(PropertyFactoryUtil.forName("name").like("%>"+nameStructure+"<%"))
                 .add(PropertyFactoryUtil.forName("groupId").eq(new Long(siteID)));
        List<DDMStructure> structures = DDMStructureLocalServiceUtil.dynamicQuery(query);
        List<DDMStructure> valid_structures = new ArrayList<DDMStructure>();
        for (DDMStructure ddmStructure : structures) {
        log.info(ddmStructure);
        	if(validStructure(ddmStructure.getStructureId())){
        		valid_structures.add(ddmStructure);
        	}
		}
        if (valid_structures.size() > 0) {
        	log.info("Estrucruras Validas:"+valid_structures.size() );
            return valid_structures;
        }
        return new ArrayList<>();
    }
	

    public DLFolder getFolderByName(Long groupId,String name) throws PortalException{
    Long base_folder = getRootFolderByConfiguration(groupId);
    return	DLFolderLocalServiceUtil.getFolder(groupId, base_folder, name);
    }
    
    public List<DLFolder> getSubFolderByFolderParent(Long groupId,Long idFolder){
    	List<DLFolder> dlFolders = DLFolderLocalServiceUtil.getFolders(groupId, idFolder);
    	if(dlFolders.size()>0){
    		return dlFolders;
    	}
      return new ArrayList<>();
    }
    
    public List<DLFileEntry> getFilesByFolder(Long siteID,Long idFolder) throws PortalException{
    	List<DLFileEntry> results = DLFileEntryLocalServiceUtil.getFileEntries(siteID,idFolder);
    	if(results.size()>0){
    		return results;
    	}
    	return new ArrayList<>();
    }
    
    
    public List<Files> getFilesByParams(Long siteID,ArrayList<Long> nameList) throws PortalException{
    	List<com.consistent.service.application.models.Files> ficheros = new ArrayList<>();
    	com.consistent.service.application.models.Files fichero=null;
    	for (Long idFolder : nameList) {
    		List<DLFileEntry> results = DLFileEntryLocalServiceUtil.getFileEntries(siteID,idFolder);
    		if(results.size()>0){
    			for (DLFileEntry file : results) {
    				fichero = new Files();
					fichero.setName(file.getFileName());
					fichero.setPath(file.getFolder().getPath());
					fichero.setPk(file.getFileEntryId());
					String url="/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+System.currentTimeMillis();
					fichero.setUrl(url.replace(" ", "%20"));
					ficheros.add(fichero);
				}
    			}
    	}
       	if(ficheros.size()>0){
    		return ficheros;
    	}
    	return new ArrayList<>();
    }
    /*Marca codigo_hotel tipo*/
	public long getRootFolderByConfiguration(Long groupId) throws PortalException{
		DLFolder idFolder = null;
		for (String baseFileEntry : Contants.DLFILEENTRY_BASE) {
			if(idFolder==null){
			 idFolder= DLFolderLocalServiceUtil.getFolder(groupId, 0, baseFileEntry);
			}
			else{
			 idFolder= DLFolderLocalServiceUtil.getFolder(groupId, idFolder.getFolderId(), baseFileEntry);
			}
			log.info(idFolder.getName()+"id:"+idFolder.getFolderId());
			}
		return idFolder.getFolderId();
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
	
	public List<Files> getFoldersAndFilesList(Long groupId,Long parent,String type,List<Files> ficheros) throws PortalException{
		List<DLFolder> listFolders = getSubFolderByFolderParent(groupId, parent);
		com.consistent.service.application.models.Files fichero =null;
		if(listFolders != null && listFolders.size() > 0){
			for (DLFolder object : listFolders) {
				if(getFilesByFolder(groupId, object.getFolderId())!= null && !getFilesByFolder(groupId, object.getFolderId()).isEmpty()){
					for (DLFileEntry file : getFilesByFolder(groupId, object.getFolderId())) {
						fichero = new Files();
						fichero.setName(file.getFileName());
						fichero.setPath(file.getFolder().getPath());
						fichero.setPk(file.getFileEntryId());
						String url="/documents/"+file.getGroupId()+"/"+file.getFolderId()+"/"+file.getFileName()+"/"+file.getUuid()+"?t="+System.currentTimeMillis();
						fichero.setUrl(url.replace(" ", "%20"));
						fichero.setShourtCut(url.replace(" ", "%20")+"&imageThumbnail=1");
					}
					ficheros.add(fichero);
				}
				if(getFoldersAndFilesList(groupId,object.getFolderId(),type,ficheros)!= null && !getFoldersAndFilesList(groupId,object.getFolderId(),type,ficheros).isEmpty()){
					getFoldersAndFilesList(groupId,object.getFolderId(),type,ficheros);
				}

			}
		}
		return ficheros;
	}



public long getHotelFolderRootByConfigurationFolderWebcontent(Long groupId) throws PortalException{
JournalFolderImpl idFolder = null;
for (String baseFileEntry : Contants.JOURNAL_HOTEL) {
	if(idFolder!=null){
		 idFolder= query.getJournalFolderByName(groupId, idFolder.getFolderId(), baseFileEntry);
		}
		else{
			idFolder= query.getJournalFolderByName(groupId, new Long(0), baseFileEntry);
		}
		log.info("Folder webcontent:"+idFolder.getName()+"id:"+idFolder.getFolderId());
		}
	return idFolder.getFolderId();
}



public List<JournalArticle> getJournalFoldersAndWC(Long groupId,Long parent,List<JournalArticle> journalArray) throws PortalException{
	List<JournalFolder> listFolders = query.getSubFolderByJournalFolderParent(groupId, new Long(parent));		
	if(listFolders != null && listFolders.size() > 0){
		for (JournalFolder object : listFolders) {
			System.out.println(object.getName());
			if(query.getWCByJournalFolder(groupId, object.getFolderId())!= null && !query.getWCByJournalFolder(groupId, object.getFolderId()).isEmpty()){
				for (JournalArticle ja : query.getWCByJournalFolder(groupId, object.getFolderId())) {
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


public List<JournalArticle> getJournalFoldersAndWCByType(Long groupId,Long parent,String type,List<JournalArticle> journalArray) throws PortalException{
	List<JournalFolder> listFolders = query.getSubFolderByJournalFolderParent(groupId, new Long(parent));		
	if(listFolders != null && listFolders.size() > 0){
		for (JournalFolder object : listFolders) {
			System.out.println(object.getName());
			if(query.getWCByJournalFolderAndType(groupId, object.getFolderId(),type)!= null && !query.getWCByJournalFolderAndType(groupId, object.getFolderId(),type).isEmpty()){
				for (JournalArticle ja : query.getWCByJournalFolderAndType(groupId, object.getFolderId(),type)) {
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

/*Sección de journal folders*/

	@Activate
	@Modified
	public void activate(Map<String, Object> properties) {
	
		System.out.println("Configuración actualizada  " + new Date().toString());
		
		_configuration = ConfigurableUtil.createConfigurable(com.consistent.service.application.config.ConfigPersonalizacionApi.class, properties);
		
		if (_configuration != null) {
			Contants.STRUCTURE_IDS=_configuration.StructureId();
			Contants.DLFILEENTRY_BASE=_configuration.DLFileEntryFolderBase();
			Contants.JOURNAL_HOTEL=_configuration.JournalFolderHotelBase();
			for (String iterable_element : Contants.STRUCTURE_IDS) {
				System.out.println("Registro actual en la configuración, info="+iterable_element);

			}
			for (String iterable_element : Contants.DLFILEENTRY_BASE) {
				System.out.println("Registro actual en la configuración, info="+iterable_element);

			}
			for (String iterable_element : Contants.JOURNAL_HOTEL) {
				System.out.println("Registro actual en la configuración, info="+iterable_element);

			}
			
		} else {
			System.out.println("No hay datos en la configuración inicial");
		}
	}

    private volatile com.consistent.service.application.config.ConfigPersonalizacionApi _configuration;
 
    }
