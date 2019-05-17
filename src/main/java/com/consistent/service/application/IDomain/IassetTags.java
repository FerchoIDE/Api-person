package com.consistent.service.application.IDomain;

import java.util.List;

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.portal.kernel.exception.PortalException;

public interface IassetTags {

	public List<AssetTag> getTagsByname(Long groupId,String name) throws PortalException;
}
