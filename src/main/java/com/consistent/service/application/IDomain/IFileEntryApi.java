package com.consistent.service.application.IDomain;

import java.io.FileNotFoundException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;

public interface IFileEntryApi {

	public JSONArray saveFile(Long groupId,Long userId,Long folderId,String image,String name,String description,String mimeType) throws FileNotFoundException;
	 /**
     * Método getFilesByCurrentFolderAndName
     * @param groupId Id del sítio
     * @param name filrado por este campo
     * @param currentFolder Nombre de la carpeta marca donde se hace la búsqueda
     * @return <ul>
     *  <li>JSONArray </li>
     *  </ul>
     *  @PortalException
     */
	public JSONArray getFilesByCurrentFolderAndName(Long groupId,Long currentFolder,String name) throws PortalException;
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
	public JSONArray getFilesByName(Long groupId,String brand,String name,String code_hotel) throws PortalException;
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
	public JSONArray getListFolders(Long groupId,String brand,String code_hotel) throws PortalException;
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
	
	public JSONArray getFilesAndFolder(Long groupId,String brand,String type,String code_hotel) throws PortalException;

	
}
