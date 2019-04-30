package com.consistent.service.application.domain;

import java.util.ArrayList;
import java.util.List;

import com.consistent.service.application.IDomain.IStructuresApi;
import com.consistent.service.application.models.Contants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class StructuresApi implements IStructuresApi {
	private static final Log _log = LogFactoryUtil.getLog(JournalApi.class);

	@Override
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
			_log.info("Estrucruras Validas:"+valid_structures.size() );
			return valid_structures;
			}
			return new ArrayList<>();
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
        	_log.info("Estrucruras Validas:"+valid_structures.size() );
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
        	_log.info("Estrucruras Validas:"+valid_structures.size() );
            return valid_structures;
        }
        return new ArrayList<>();
    }
	
	private boolean validStructure(Long id){
		for (String structureID : Contants.STRUCTURE_IDS) {
			if(structureID.equals(String.valueOf(id))) return true;
		}
		return false;
	}

}
