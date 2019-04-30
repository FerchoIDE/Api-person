package com.consistent.service.application.IDomain;

import java.util.List;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;

public interface IJournalApi {

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
     *  @throws PortalException 
     */
	public JSONArray getListJournalFolders(Long groupId,String brand,String code_hotel) throws PortalException;
	/**
     * Método getListJournalFoldersByCode
     * @param groupId Id del sítio
     * @param codeBrand codigo carpeta donde se hace la búsqueda
     * @return <ul>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  </ul>
     *  @throws PortalException 
     */
	public JSONArray getListJournalFoldersByCode(Long groupId,Long codeBrand) throws PortalException;
	/**
     * Método getWCByFolder 
     * @param groupId Id del sítio
     * @param folderID codigo carpeta/marca donde se hace la búsqueda
     * @return <ul>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  </ul>
     *  @throws PortalException 
     */
	public List<JournalArticle> getWCByFolder(Long groupId,Long folderId) throws PortalException;
	/**
     * Método createFolderNestedFolderId
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
	 * @throws PortalException 
     */
	public JSONArray createFolderNestedFolderId(Long userId,Long groupId,Long parentFolderId,String name) throws PortalException;
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
	public List<JournalArticle> getWCByName(Long groupId,String brand,String code_hotel,String name) throws PortalException;
	//busqueda recursiva de webcontents x M y CH getAllWCAndJournalFolder((portletGroupId,"AQUA","","AQC");
			/**
		     * Método getWCAndJournalFolder
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
		     *  @throws PortalException 
		     */
	public List<JournalArticle> getWCAndJournalFolder(Long groupId,String brand,String code_hotel) throws PortalException;
	//busqueda recursiva de webcontents x M, CH y tipo getWCAndJournalFolderType((portletGroupId,"AQUA","","AQC");
	/**
     * Método getWCAndJournalFolderType
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
     *  @throws PortalException
     */
	public List<JournalArticle> getWCByJournalFolderAndStructureType(Long groupId,String brand,String code_hotel,String type) throws PortalException;
	/**
     * Método getWCAndJournalFolderByName
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
     *  @throws PortalException 
     */
	public List<JournalArticle> getWCAndJournalFolderByName(Long groupId,String brand,String code_hotel,String name,String type) throws PortalException;
	/**
     * Método searchWebContentByCodeHotel, Búsqueda a primer nivel,si existe devuelve el webcontent,sino un array vacio
     * @param groupId Id del sítio
     * @param folderId Id de la carpeta marca donde se hace la búsqueda
     * @param code Código de hotel para la búsqueda
     * @return <ul>
     *  <li>Resultados de la búsqueda List<JournalArticle></li>
     *  </ul>
     */
	public List<JournalArticle> searchWebContentByCodeHotelFirstLevel(Long groupId,String structureName,String code) throws PortalException;

}
