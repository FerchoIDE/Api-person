package com.consistent.service.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;

/**
 * @author Fercho
 */
@ApplicationPath("/personalizacion")
@Component(immediate = true, service = Application.class)
public class PersonalizacionApiApplication extends Application {

	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(new com.consistent.service.application.Rest.StructureRest());
		singletons.add(new com.consistent.service.application.Rest.FolderJournalArticleRest());
		return singletons;
	}

	

}