package com.consistent.service.application.IDomain;

import java.util.List;
import java.util.Map;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;

public interface IJournalApi {

	 
    /**
     * Mï¿½todo getListJournalFoldersByCode
     * @param groupId Id del sï¿½tio
     * @param codeBrand codigo carpeta donde se hace la bï¿½squeda
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
     * Mï¿½todo getWCByFolder
     * @param groupId Id del sï¿½tio
     * @param folderID codigo carpeta/marca donde se hace la bï¿½squeda
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
     * Mï¿½todo createFolderNestedFolderId
     * @param groupId Id del sï¿½tio
     * @param brand Nombre de la carpeta marca donde se hace la bï¿½squeda
     * @param type tipo de archivo a buscar(No se filtra)
     * @param code_hotel Nombre de la carpeta cï¿½difgo de hotel para la bï¿½squeda
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
     * Mï¿½todo getWCByName
     * @param groupId Id del sï¿½tio
     * @param brand Nombre de la carpeta marca donde se hace la bï¿½squeda
     * @param type tipo de archivo a buscar(No se filtra)
     * @param code_hotel Nombre de la carpeta cï¿½difgo de hotel para la bï¿½squeda
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
     * Mï¿½todo getWCAndJournalFolder
     * @param groupId Id del sï¿½tio
     * @param brand Nombre de la carpeta marca donde se hace la bï¿½squeda
     * @param type tipo de archivo a buscar(No se filtra)
     * @param code_hotel Nombre de la carpeta cï¿½difgo de hotel para la bï¿½squeda
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
     * Mï¿½todo getWCAndJournalFolderType
     * @param groupId Id del sï¿½tio
     * @param brand Nombre de la carpeta marca donde se hace la bï¿½squeda
     * @param type tipo de archivo a buscar(No se filtra)
     * @param code_hotel Nombre de la carpeta cï¿½difgo de hotel para la bï¿½squeda
     * @return <ul>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  <li>case 1: code_hotel!=null && brand!=null JSONArray </li>
     *  <li>case 2: brand!=null && code_hotel==null JSONArray </li>
     *  </ul>
     *  @throws PortalException
     */
    public List<JournalArticle> getWCByJournalFolderAndStructureType(Long groupId,String brand,String code_hotel,Long structureId) throws PortalException;
   
  

    public JournalArticle saveWC(String json) throws PortalException;

    public JournalArticle updateWC(String json) throws PortalException;

    /**/



    /*busqueda tosdos los contenidos recursivos de un tipo
    groupid,
    marca,(podria ser opcional)
    code hotel(podria ser opcional)
    tipo de contenido (Id de la estructura) si este es null es sobre todos los contenidos
     */
   //deprecate public List<JournalArticle> getWebcontentRecursiveByType(Long groupId,String marca,String codeHotel,String TypeContent) throws PortalException;
    
    public List<JournalArticle> getWebcontentRecursiveByType(Long groupId,Long folderId,Long structureId) throws PortalException;

    public List<JournalArticle> getWebcontentRecursiveByType(Long groupId,String marca,String codeHotel,Long structureId) throws PortalException;



    /*busqueda todos los contenidos recursivos de un tipo que conincidan en el titulo
    groupid,
    marca,(podria ser opcional)
    code hotel(podria ser opcional)
    tipo de contenido (Id de la estructura) si este es null es sobre todos los contenidos
    titulo del contenido, considerar usar like y no sensible a mayusculas minusculas
     */
   //Deprecate public List<JournalArticle> getWebcontentRecursiveByTitle(Long groupId,String marca,String codeHotel,String title,String contentType) throws PortalException;
    
    
    public List<JournalArticle> getWebcontentRecursiveByTitle(Long groupId,Long folderId,String title,Long structureId) throws PortalException;

    public List<JournalArticle> getWebcontentRecursiveByTitle(Long groupId,String marca,String codeHotel,String title,Long structureId) throws PortalException;


    /*busqueda todos los contenidos recursivos dentro de un folder
    groupid,
    folderid
    tipo de contenido (Id de la estructura) si este es null es sobre todos los contenidos
     */
   
    
    /*Métodos actualizados para recuperar por nombre-usar este*/
    public List<JournalArticle> getWCAndJournalFolderByName(Long groupId,String brand,String code_hotel,String name) throws PortalException;
    /*Métodos actualizados para recuperar por nombre-usar este*/
    
    public List<JournalArticle> getWCAndJournalFolderByName(Long groupId,String brand,String code_hotel,String name,Long structureId) throws PortalException;

    public List<JournalArticle> getWCByFolderId(Long groupId, Long folderId) throws PortalException;

    /*todas las carpetas dentro de
    groupid,
    marca,(podria ser opcional)
    code hotel(podria ser opcional)
     */

    public JSONArray getListJournalFolders(Long groupId,String brand,String code_hotel) throws PortalException;


    /*buscar si un codigo de hotel ya existe, si existe indicar el titulo dle hotel
    code hotel

     */
    public List<JournalArticle> searchWebContentByCodeHotelFirstLevel(Long groupId,String structureName,String code) throws PortalException;
   
    public List<JournalArticle> searchWebContentByCodeHotelFirstLevelByFolder(Long groupId,String structureName,String code,Long folderId) throws PortalException;


    /* un arreglo de listado de marcas
    groupid,
     */

    public JSONArray getListJournalFoldersBrand(Long groupId) throws PortalException;

    /* un arreglo de listado hoteles
    groupid,
    marca
     */
    
    public JSONArray getListJournalFoldersByBrand(Long groupId,Long brand) throws PortalException;
    
   
    public JSONArray getListJournalFoldersByBrand(Long groupId,String brand) throws PortalException;

   
    /*
    Recuperar un contenido y la estructura asociada a este

     */
    public Map<JournalArticle, DDMStructure> getWCandStructureById(Long articleId) throws PortalException;
}
