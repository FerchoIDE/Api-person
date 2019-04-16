package com.consistent.service.application.LiferayServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;

import com.consistent.service.application.models.Contants;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
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
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

public class QueriesLiferayApi {
	private static final Log log = LogFactoryUtil.getLog(QueriesLiferayApi.class);

	
	
	public List<JournalArticle> getFoldersWCByNameAndType(Long groupId,Long parent,String namefile,String type,List<JournalArticle> journalArray) throws PortalException{
		List<JournalFolder> listFolders = getSubFolderByJournalFolderParent(groupId, new Long(parent));
		if(listFolders != null && listFolders.size() > 0){
			for (JournalFolder object : listFolders) {
				System.out.println("folder:"+ object.getFolderId());
				if(getWCByJournalFolderAndTypeAndName(groupId, object.getFolderId(),type,namefile)!= null && !getWCByJournalFolderAndTypeAndName(groupId, object.getFolderId(),type,namefile).isEmpty()){
					for (JournalArticle ja : getWCByJournalFolderAndTypeAndName(groupId, object.getFolderId(),type,namefile)) {
							 if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, ja.getArticleId(), ja.getVersion())){
					    		 journalArray.add(ja);
					    	 }else{
					    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(ja.getResourcePrimKey()));
					    	 }
					}	
				}
				if(getFoldersWCByNameAndType(groupId,object.getFolderId(),namefile,type,journalArray)!= null && !getFoldersWCByNameAndType(groupId,object.getFolderId(),namefile,type,journalArray).isEmpty()){
					getFoldersWCByNameAndType(groupId,object.getFolderId(),namefile,type,journalArray);
				}
			}
		}
		return journalArray;
	}
	
	public List<JournalArticle> getFoldersWCByName(Long groupId,Long parent,String namefile,List<JournalArticle> journalArray) throws PortalException{
		List<JournalFolder> listFolders = getSubFolderByJournalFolderParent(groupId, new Long(parent));
		if(listFolders != null && listFolders.size() > 0){
			for (JournalFolder object : listFolders) {
				System.out.println("folder:"+ object.getFolderId());
				if(getWCByJournalFolderAndName(groupId, object.getFolderId(),namefile)!= null && !getWCByJournalFolderAndName(groupId, object.getFolderId(),namefile).isEmpty()){
					for (JournalArticle ja : getWCByJournalFolderAndName(groupId, object.getFolderId(),namefile)) {
							 if(JournalArticleLocalServiceUtil.isLatestVersion(groupId, ja.getArticleId(), ja.getVersion())){
					    		 journalArray.add(ja);
					    	 }else{
					    		 journalArray.add(JournalArticleLocalServiceUtil.getLatestArticle(ja.getResourcePrimKey()));
					    	 }
					}	
				}
				if(getFoldersWCByName(groupId,object.getFolderId(),namefile,journalArray)!= null && !getFoldersWCByName(groupId,object.getFolderId(),namefile,journalArray).isEmpty()){
					getFoldersWCByName(groupId,object.getFolderId(),namefile,journalArray);
				}
			}
		}
		return journalArray;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<com.consistent.service.application.models.Files> getFoldersAndFiles(Long groupId,Long parent,String type) throws PortalException{
		QueryDefinition queryDefinition = new QueryDefinition(WorkflowConstants.STATUS_ANY, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
		List<Object> foldersAndFileEntriesAndFileShortcuts= DLFolderLocalServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(groupId, parent, null, true, queryDefinition);
		for (Object folderAndFileEntryAndFileShortcut: foldersAndFileEntriesAndFileShortcuts) {
			if (folderAndFileEntryAndFileShortcut instanceof DLFileEntry) {
		    	DLFileEntry fileEntry = (DLFileEntry)folderAndFileEntryAndFileShortcut;	
		      
		    }
			else if (folderAndFileEntryAndFileShortcut instanceof DLFolder) {
		    	DLFolder subFolder = (DLFolder) folderAndFileEntryAndFileShortcut;
	
		        if(subFolder!=null){
		        	getFoldersAndFiles(groupId, subFolder.getFolderId(), type);
		        }
		    } 
	     }
		 return new ArrayList<>();
	}
	
	  public List<JournalFolderImpl> getRateFolder1(long parentFolder,String nameFolder,Long siteID){
		DynamicQuery query_journal_folder = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalFolderImpl.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
		query_journal_folder.add(RestrictionsFactoryUtil.eq("name", nameFolder));
		query_journal_folder.add(RestrictionsFactoryUtil.eq("parentFolderId", new Long(parentFolder)));
		query_journal_folder.add(RestrictionsFactoryUtil.eq("groupId",new Long(siteID)));
		List<com.liferay.journal.model.impl.JournalFolderImpl> ja_1 = JournalArticleResourceLocalServiceUtil.dynamicQuery(query_journal_folder);
		return ja_1;
		}

	/*Sección de journal folders*/	
	
	public Long getFolderWC(Long siteID,String nameFolder,Long parentFolder){
		List<Long> listIdFolders = new ArrayList<>();
		DynamicQuery queryJournalFolder = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalFolderImpl.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
		queryJournalFolder.add(RestrictionsFactoryUtil.eq("name", nameFolder));
		queryJournalFolder.add( RestrictionsFactoryUtil.eq("parentFolderId", new Long(parentFolder)));
		queryJournalFolder.add( RestrictionsFactoryUtil.eq("groupId",new Long(siteID)));
		List<com.liferay.journal.model.impl.JournalFolderImpl> journalfolderResults = JournalArticleResourceLocalServiceUtil.dynamicQuery(queryJournalFolder);		
		for (com.liferay.journal.model.impl.JournalFolderImpl dlFolder : journalfolderResults) {
			listIdFolders.add(dlFolder.getFolderId());
		}
		return listIdFolders.get(0);
	}
	
public List<JournalFolder> getSubFolderByJournalFolderParent(Long groupId,Long idFolder){
	    List<JournalFolder> dlFolders = JournalFolderLocalServiceUtil.getFolders(groupId, idFolder);
	    	if(dlFolders.size()>0){
	    		return dlFolders;
	    	}
	      return new ArrayList<>();
}  
public JournalFolderImpl getJournalFolderByName(Long siteID,Long parentFolder,String nameFolder){
	DynamicQuery queryJournalFolder = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalFolderImpl.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
	queryJournalFolder.add(RestrictionsFactoryUtil.eq("name", nameFolder));
	queryJournalFolder.add( RestrictionsFactoryUtil.eq("parentFolderId", new Long(parentFolder)));
	queryJournalFolder.add( RestrictionsFactoryUtil.eq("groupId",new Long(siteID)));
	List<com.liferay.journal.model.impl.JournalFolderImpl> journalfolderResults = JournalArticleResourceLocalServiceUtil.dynamicQuery(queryJournalFolder);		
	return journalfolderResults.get(0);
}

	public List<JournalArticleImpl> getWCByJournalFolderAndType(Long groupId,Long folderId,String structureKey) throws PortalException{
		DynamicQuery queryJournal = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalArticleImpl.class, "journalArticle",PortalClassLoaderUtil.getClassLoader());
		queryJournal.add(RestrictionsFactoryUtil.eq("DDMStructureKey", structureKey));
		queryJournal.add( RestrictionsFactoryUtil.eq("folderId", new Long(folderId)));
		queryJournal.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		List<com.liferay.journal.model.impl.JournalArticleImpl> journalResults =JournalArticleLocalServiceUtil.dynamicQuery(queryJournal);		
		if(journalResults.size()>0){
		return journalResults;
		}
		return new ArrayList<>();
	}
	
	public List<JournalArticle> getWCByJournalFolder(Long groupId,Long folderId) throws PortalException{
		List<JournalArticle> results = JournalArticleLocalServiceUtil.getArticles(groupId, folderId);
		if(results.size()>0){
			return results;
		}
		return new ArrayList<>();
	}
	
	//getWCByJournalFolderAndName
	public List<JournalArticleImpl> getWCByJournalFolderAndName(Long groupId,Long folderId,String structureKey,String namefile) throws PortalException{
		DynamicQuery queryJournal = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalArticleImpl.class, "journalArticle",PortalClassLoaderUtil.getClassLoader());
		queryJournal.add(RestrictionsFactoryUtil.ilike("title", new StringBuilder("%>").append(namefile).append("<%").toString()));
		queryJournal.add( RestrictionsFactoryUtil.eq("folderId", new Long(folderId)));
		queryJournal.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		List<com.liferay.journal.model.impl.JournalArticleImpl> journalResults =JournalArticleLocalServiceUtil.dynamicQuery(queryJournal);		
		if(journalResults.size()>0){
		return journalResults;
		}
		return new ArrayList<>();
	}
	
	
	public List<JournalArticleImpl> getWCByJournalFolderAndTypeAndName(Long groupId,Long folderId,String structureKey,String namefile) throws PortalException{
		DynamicQuery queryJournal = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalArticleImpl.class, "journalArticle",PortalClassLoaderUtil.getClassLoader());
		queryJournal.add(RestrictionsFactoryUtil.ilike("title", new StringBuilder("%>").append(namefile).append("<%").toString()));
		queryJournal.add(RestrictionsFactoryUtil.eq("DDMStructureKey", structureKey));
		queryJournal.add( RestrictionsFactoryUtil.eq("folderId", new Long(folderId)));
		queryJournal.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		List<com.liferay.journal.model.impl.JournalArticleImpl> journalResults =JournalArticleLocalServiceUtil.dynamicQuery(queryJournal);		
		if(journalResults.size()>0){
		return journalResults;
		}
		return new ArrayList<>();
	}

	public List<JournalArticleImpl> getWCByJournalFolderAndName(Long groupId,Long folderId,String namefile) throws PortalException{
		DynamicQuery queryJournal = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalArticleImpl.class, "journalArticle",PortalClassLoaderUtil.getClassLoader());
		queryJournal.add(RestrictionsFactoryUtil.ilike("title", new StringBuilder("%>").append(namefile).append("<%").toString()));
		queryJournal.add( RestrictionsFactoryUtil.eq("folderId", new Long(folderId)));
		queryJournal.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		List<com.liferay.journal.model.impl.JournalArticleImpl> journalResults =JournalArticleLocalServiceUtil.dynamicQuery(queryJournal);		
		if(journalResults.size()>0){
		return journalResults;
		}
		return new ArrayList<>();
	}
	
	public List<Long> journalByBrandFolder(long parentFolder,Long siteID){
		List<Long> listIdFolders = new ArrayList<>();
		DynamicQuery query_journal_folder = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalFolderImpl.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
		query_journal_folder.add( RestrictionsFactoryUtil.eq("parentFolderId", new Long(parentFolder)));
		query_journal_folder.add( RestrictionsFactoryUtil.eq("groupId",new Long(siteID)));
		List<com.liferay.journal.model.impl.JournalFolderImpl> ja_1 = JournalArticleResourceLocalServiceUtil.dynamicQuery(query_journal_folder);
			for (com.liferay.journal.model.impl.JournalFolderImpl journalArticleResourceImpl : ja_1) {
		    	 listIdFolders.add(journalArticleResourceImpl.getFolderId());
				}
		return listIdFolders;
		}
	
	public Long journalByCodeFolder(long parentFolder,String nameFolder,Long siteID){
		DynamicQuery query_journal_folder = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalFolderImpl.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
		query_journal_folder.add(PropertyFactoryUtil.forName("groupId").eq(new Long(siteID)));
		query_journal_folder.add(PropertyFactoryUtil.forName("parentFolderId").eq(new Long(parentFolder)));
		List<com.liferay.journal.model.impl.JournalFolderImpl> ja_1 = JournalArticleResourceLocalServiceUtil.dynamicQuery(query_journal_folder);
		for (com.liferay.journal.model.impl.JournalFolderImpl journalArticleResourceImpl : ja_1) {
	    	 if(journalArticleResourceImpl.getName().equals(nameFolder)){
	    	 return journalArticleResourceImpl.getFolderId();
	    	 
	    	 }
		}
		return parentFolder;

		}
	public JournalArticle getJournalArticleByFolderId(long folderId,String language,Long siteID,String name_structure){
		log.info("Get data folder:"+folderId);
		List<DDMStructure> ddmStructures = new ArrayList<>();
		if(ddmStructures==null || ddmStructures.size() < 0){
		if(getStruct("%>"+name_structure+"<%",siteID).size() > 0){
			ddmStructures= getStruct("%>"+name_structure+"<%",siteID);
		}
		}
		for (DDMStructure ddmStructure : ddmStructures) {
			log.info("Get metadata by structure ID:"+ddmStructure.getStructureId());
			String structureId = ddmStructure.getStructureKey(); // replace with real structure ID
			DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalArticleImpl.class, "journal",PortalClassLoaderUtil.getClassLoader());			
			dynamicQuery.add(PropertyFactoryUtil.forName("groupId").eq(new Long(siteID)));
			dynamicQuery.add(PropertyFactoryUtil.forName("DDMStructureKey").eq(new String(structureId)));
			dynamicQuery.add(PropertyFactoryUtil.forName("folderId").eq(new Long(folderId)));
			    if(JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery).size()>0){
			    	List<com.liferay.journal.model.impl.JournalArticleImpl> ja=JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
			    	  List<JournalArticle> lastest = getLatestVersionArticle(ja,siteID);
					    for (JournalArticle journalArticleImpl : lastest) {
						   	log.info("Get info"+journalArticleImpl);
							return journalArticleImpl;
					    }
			    }
		}
		
		
			return new JournalArticleImpl();    
}
	
	public List<JournalArticle> getLatestVersionArticle(List<JournalArticleImpl> ja,Long siteID) {
		List<JournalArticle> journalList = new ArrayList<JournalArticle>();
			JournalArticle latestArticle ;
			for (JournalArticle journalArticle : ja) {
				try {
					 latestArticle = JournalArticleLocalServiceUtil.getLatestArticle(journalArticle.getResourcePrimKey());
					if (journalList.contains(latestArticle)) {
						continue;
					} else {
						journalList.add(latestArticle);
					}
				} catch (PortalException | SystemException e) {
					e.printStackTrace();
				}
			}
			return journalList;

		}
	
	public List<DDMStructure> getStruct(String nameStructure,Long siteID) {
        DynamicQuery query = DDMStructureLocalServiceUtil.dynamicQuery()
                .add(PropertyFactoryUtil.forName("name").like(nameStructure))
                 .add(PropertyFactoryUtil.forName("groupId").eq(new Long(siteID)));
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
	
	public List<JournalFolderImpl> journalRootFolder(long parentFolder,String nameFolder,Long siteID){
		DynamicQuery query_journal_folder = DynamicQueryFactoryUtil.forClass(com.liferay.journal.model.impl.JournalFolderImpl.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
		query_journal_folder.add(RestrictionsFactoryUtil.eq("name", nameFolder));
		query_journal_folder.add(RestrictionsFactoryUtil.eq("parentFolderId", new Long(parentFolder)));
		query_journal_folder.add(RestrictionsFactoryUtil.eq("groupId",new Long(siteID)));
		List<com.liferay.journal.model.impl.JournalFolderImpl> ja_1 = JournalArticleResourceLocalServiceUtil.dynamicQuery(query_journal_folder);
		return ja_1;
		}

	public Long getFolder(Long siteID,String nameFolder,Long parentFolder){
		List<Long> listIdFolders = new ArrayList<>();
		DynamicQuery folder = DynamicQueryFactoryUtil.forClass(com.liferay.document.library.kernel.model.DLFolder.class, "journalFolder",PortalClassLoaderUtil.getClassLoader());
		folder.add( RestrictionsFactoryUtil.eq("groupId",new Long(siteID)));
		folder.add(RestrictionsFactoryUtil.eq("name", nameFolder));
		folder.add(RestrictionsFactoryUtil.eq("parentFolderId", new Long(parentFolder)));
		List<com.liferay.document.library.kernel.model.DLFolder> Folders = DLFolderLocalServiceUtil.dynamicQuery(folder);
		for (DLFolder dlFolder : Folders) {
			listIdFolders.add(dlFolder.getFolderId());
		}
		return listIdFolders.get(0);
		}
	
	
	public boolean validStructure(Long id){
		for (String structureID : Contants.STRUCTURE_IDS) {
			log.info("structure id uno: "+structureID);
			log.info("structure id dos: "+id);
			if(structureID.equals(String.valueOf(id))) return true;
		}
		return false;
	}
	
	/*Obtiene archivos y folder por nombre y folder actual*/
	public List<DLFileEntry> getFilesByName(Long groupId,Long idCurrentFolder,String namefile){
		DynamicQuery query = DynamicQueryFactoryUtil.forClass(DLFileEntry.class, "DLFileEntry",PortalClassLoaderUtil.getClassLoader());
		query.add(RestrictionsFactoryUtil.ilike("title", new StringBuilder("%").append(namefile).append("%").toString()));
		query.add(PropertyFactoryUtil.forName("folderId").eq(idCurrentFolder));
		query.add(PropertyFactoryUtil.forName("groupId").eq(new Long(groupId)));
		return  DLFileEntryLocalServiceUtil.dynamicQuery(query);
		
	}
	
	
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
