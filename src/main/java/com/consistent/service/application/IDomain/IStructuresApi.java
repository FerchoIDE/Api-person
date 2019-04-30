package com.consistent.service.application.IDomain;

import java.util.List;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.exception.PortalException;

public interface IStructuresApi {

	public List<DDMStructure> getAllStructuresBySite(Long groupId) throws PortalException;
}
