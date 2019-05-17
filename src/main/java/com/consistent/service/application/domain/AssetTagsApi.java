package com.consistent.service.application.domain;

import java.util.ArrayList;
import java.util.List;

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.portal.kernel.exception.PortalException;

public class AssetTagsApi extends QueriesLiferayApi implements com.consistent.service.application.IDomain.IassetTags  {
	
	@Override
	public List<AssetTag> getTagsByname(Long groupId,String name) throws PortalException {
		if(name.length() >= 3){
		return getTags(groupId, name);
		}
		return new ArrayList<>();
		}
}
