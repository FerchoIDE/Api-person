package com.consistent.service.application.domain;

import java.util.ArrayList;
import java.util.List;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

public class VocabularyApi {

	private AssetCategoryLocalService _category;
	
	public VocabularyApi() {
	AssetCategoryLocalService category = AssetCategoryLocalServiceUtil.getService();
	this._category=category;
		
	}
	
	

	 /**
    * Método getCategoriesByGroupAndVacabularyIdAllLevels
    * @param groupId Id del sítio
    * @param vocabularyId Id del vocabulario a iterar
    * @return <ul>
    *  <li>JSONArray, Todos los objetos dentro del vocabulario</li>
    *  </ul>
    */
	public JSONArray getCategoriesByGroupAndVacabularyIdAllLevels(Long groupId,Long vocabularyId) throws PortalException{		
	    JSONObject categoryObject=null;
		JSONArray categoryArray = JSONFactoryUtil.createJSONArray();
		List<AssetCategory> listCategories = getCategoriesByGroupIdandVocabularyId(groupId, vocabularyId);
		if(listCategories != null && listCategories.size() > 0){
			for (int i = 0; i < listCategories.size(); i++) {
				categoryObject=JSONFactoryUtil.createJSONObject();
				categoryObject.put("category", listCategories.get(i).getName());
				categoryObject.put("key", listCategories.get(i).getPrimaryKey());
				categoryObject.put("key", listCategories.get(i));
				if(getSubCategories(listCategories.get(i).getPrimaryKey()) != null && !getSubCategories(listCategories.get(i).getPrimaryKey()).isNull(0)){
					categoryObject.put("nested", getSubCategories(listCategories.get(i).getPrimaryKey()));
				}
				categoryArray.put(categoryObject);
			}
		}
		return categoryArray;
	}
	 /**
	    * Método getCategoriesByGroupAndVacabularyFirstLevel
	    * @param groupId Id del sítio
	    * @param vocabularyId Id del vocabulario a iterar
	    * @return <ul>
	    *  <li>JSONArray, Obtiene el primer nivel de objetos dentro del vocabulario</li>
	    *  </ul>
	    */
		public JSONArray getCategoriesByGroupAndVacabularyFirstLevel(Long groupId,Long vocabularyId) throws PortalException{		
		    JSONObject categoryObject=null;
			JSONArray categoryArray = JSONFactoryUtil.createJSONArray();
			List<AssetCategory> listCategories = getCategoriesByGroupIdandVocabularyId(groupId, vocabularyId);
			if(listCategories != null && listCategories.size() > 0){
				for (int i = 0; i < listCategories.size(); i++) {
					categoryObject=JSONFactoryUtil.createJSONObject();
					categoryObject.put("category", listCategories.get(i).getName());
					categoryObject.put("key", listCategories.get(i).getPrimaryKey());
					categoryArray.put(categoryObject);
				}
			}	
		return categoryArray;
	}
	
	/* Método getVocabulariesByGroup
		    * @param groupId Id del sítio
		    * @return <ul>
		    *  <li>JSONArray, Obtiene el listado de vocabularios</li>
		    *  </ul>
		    */
	public JSONArray getVocabulariesByGroup(Long groupId) throws PortalException{		
		JSONArray vocabularyArray = JSONFactoryUtil.createJSONArray();
	    JSONObject vocabularyObject=null;
	    List<AssetVocabulary> vocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabularies(groupId);
		for (AssetVocabulary assetVocabulary : vocabulary) {
		vocabularyObject=JSONFactoryUtil.createJSONObject();
		vocabularyObject.put("vocabularyName", assetVocabulary.getName());
		vocabularyObject.put("vocabularykey", assetVocabulary.getVocabularyId());
		vocabularyArray.put(vocabularyObject);
		}
		return vocabularyArray;
	}
	/*
	 * {uuid=a0fa0e84-7f79-bf24-1cba-e35ee2b5d2fc, categoryId=177089, groupId=20142, companyId=20115, userId=20155, userName=Test Test, createDate=Fri Aug 31 04:00:50 GMT 2018, modifiedDate=Fri Aug 31 04:00:50 GMT 2018, parentCategoryId=0, leftCategoryId=573, rightCategoryId=574, name=Place, title=<?xml version='1.0' encoding='UTF-8'?><root available-locales="en_US,es_ES," default-locale="en_US"><Title language-id="es_ES">Place</Title><Title language-id="en_US">Place</Title></root>, description=, vocabularyId=121399, lastPublishDate=null}*/
	
	 /**
	    * Método getCategories
	    * @param groupId Id del sítio
	    * @return <ul>
	    *  <li>JSONArray, Todos los vocabularios del sitio con sus elementos anidados recursivamente</li>
	    *  </ul>
	    */
	public JSONArray getCategories(Long groupId) throws PortalException{		
		JSONArray vocabularyArray = JSONFactoryUtil.createJSONArray();
	    JSONObject categoryObject=null;
	    JSONObject vocabularyObject=null;
	    List<AssetVocabulary> vocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabularies(groupId);
		for (AssetVocabulary assetVocabulary : vocabulary) {
		JSONArray categoryArray = JSONFactoryUtil.createJSONArray();
		vocabularyObject=JSONFactoryUtil.createJSONObject();
		vocabularyObject.put("vocabularyName", assetVocabulary.getName());
		vocabularyObject.put("vocabularykey", assetVocabulary.getVocabularyId());
		List<AssetCategory> listCategories = assetVocabulary.getCategories();
		if(listCategories != null && listCategories.size() > 0){
			for (int i = 0; i < listCategories.size(); i++) {
				categoryObject=JSONFactoryUtil.createJSONObject();
				System.out.println(listCategories.get(i));
				categoryObject.put("category", listCategories.get(i).getName());
				categoryObject.put("key", listCategories.get(i).getPrimaryKey());
				if(getSubCategories(listCategories.get(i).getPrimaryKey()) != null && !getSubCategories(listCategories.get(i).getPrimaryKey()).isNull(0)){
					categoryObject.put("nested", getSubCategories(listCategories.get(i).getPrimaryKey()));
				}
				categoryArray.put(categoryObject);
				
			}
		}
		vocabularyObject.put("child", categoryArray);
		vocabularyArray.put(vocabularyObject);
		}
		return vocabularyArray;
	}
	
	
	 /**
	    * Método getSubCategories
	    * @param parentCategoryId id de padre de categoria
	    * @return <ul>
	    *  <li>JSONArray, Todos las subcategorias  del sitio con sus elementos anidados recursivamente, sin discriminar ningún valor</li>
	    *  </ul>
	    */
	private JSONArray getSubCategories(long parentCategoryId){
		 JSONArray subCategoryArray = JSONFactoryUtil.createJSONArray();
	    JSONObject subCategoryObject=null;
		List<AssetCategory> listCategories = _category.getChildCategories(parentCategoryId);
		if(listCategories != null && listCategories.size() > 0){
			for (int i = 0; i < listCategories.size(); i++) {
				subCategoryObject=JSONFactoryUtil.createJSONObject();
				subCategoryObject.put("category", listCategories.get(i).getName());
				subCategoryObject.put("key", listCategories.get(i).getPrimaryKey());
				if(getSubCategories(listCategories.get(i).getPrimaryKey()) != null && !getSubCategories(listCategories.get(i).getPrimaryKey()).isNull(0)){
				subCategoryObject.put("child", getSubCategories(listCategories.get(i).getPrimaryKey()));
				}
			
				subCategoryArray.put(subCategoryObject);
			}
		}
		return subCategoryArray;
	}
	
	/********************************Sección de consultas*****************************************/
	@SuppressWarnings("unused")
	private List<AssetCategory> getCategoriesByGroupID(Long groupId){
		List<AssetCategory> listCategories=_category.getAssetCategories(0,_category.getAssetCategoriesCount());
		List<AssetCategory> vlidAsset = new ArrayList<>();
		for (int i = 0; i < listCategories.size(); i++) {
			if(listCategories.get(i).getGroupId()==groupId){
				System.out.println(listCategories.get(i));
				vlidAsset.add(listCategories.get(i));
				
			}
		}
		return vlidAsset;
	}
	
	@SuppressWarnings("unused")
	private List<AssetCategory> getCategoriesByGroupId(Long groupId){
		DynamicQuery categories = DynamicQueryFactoryUtil.forClass(AssetCategory.class, "category",PortalClassLoaderUtil.getClassLoader());
		categories.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		List<AssetCategory> vlidAsset =_category.dynamicQuery(categories);
		return vlidAsset;
	}
	
	
	@SuppressWarnings("unused")
	private List<AssetCategory> getCategoriesByGroupIdandKey(Long groupId,Long categoryId){
		DynamicQuery categories = DynamicQueryFactoryUtil.forClass(AssetCategory.class, "category",PortalClassLoaderUtil.getClassLoader());
		categories.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		categories.add( RestrictionsFactoryUtil.eq("categoryId",new Long(categoryId)));
		List<AssetCategory> vlidAsset =_category.dynamicQuery(categories);
		return vlidAsset;
	}
	
	private List<AssetCategory> getCategoriesByGroupIdandVocabularyId(Long groupId,Long vocabularyId){
		DynamicQuery categories = DynamicQueryFactoryUtil.forClass(AssetCategory.class, "category",PortalClassLoaderUtil.getClassLoader());
		categories.add( RestrictionsFactoryUtil.eq("groupId",new Long(groupId)));
		categories.add( RestrictionsFactoryUtil.eq("vocabularyId",new Long(vocabularyId)));
		List<AssetCategory> vlidAsset =_category.dynamicQuery(categories);
		return vlidAsset;
	}
	
	/*
	 * {uuid=a0fa0e84-7f79-bf24-1cba-e35ee2b5d2fc, categoryId=177089, groupId=20142, companyId=20115, userId=20155, userName=Test Test, createDate=Fri Aug 31 04:00:50 GMT 2018, modifiedDate=Fri Aug 31 04:00:50 GMT 2018, parentCategoryId=0, leftCategoryId=573, rightCategoryId=574, name=Place, title=<?xml version='1.0' encoding='UTF-8'?><root available-locales="en_US,es_ES," default-locale="en_US"><Title language-id="es_ES">Place</Title><Title language-id="en_US">Place</Title></root>, description=, vocabularyId=121399, lastPublishDate=null}*/
	/********************************Sección de consultas*****************************************/

	
}
