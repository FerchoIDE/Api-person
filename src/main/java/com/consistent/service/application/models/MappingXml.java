package com.consistent.service.application.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.liferay.portal.kernel.util.PwdGenerator;

public class MappingXml {
	
	
	
	
	public String parseValues(Map<String, String> values){
	 String result =values.entrySet().stream().map(mapper->{
		 			if(mapper.getValue()!=null && mapper.getKey()!=null)
					return "<dynamic-content language-id=\""+mapper.getKey()+"\">"+setCDATA(mapper.getValue())+"</dynamic-content>";				
		 			else
					return "<dynamic-content language-id=\"es_ES\">"+setCDATA("")+"</dynamic-content>";
		 			}).collect(Collectors.joining());
			
		return result;
	}
	
	public String getXML(String name,String type,Map<String, String> values,String child){
        return    "<dynamic-element name=\""+name+"\" instance-id=\""+getInstance()+"\" type=\""+type+"\" index-type=\"keyword\">"
        		  +parseValues(values)+child+
        		  "</dynamic-element>";

    }
    public String setCDATA(String data){
        return "<![CDATA["+data+"]]>";
    }
    public StringBuilder getInstance(){
     StringBuilder instanceId = new StringBuilder(8);
     String key = PwdGenerator.KEY1 + PwdGenerator.KEY2 + PwdGenerator.KEY3;
     for (int i = 0; i< 8; i++) {
            int pos = (int)Math.floor(Math.random() * key.length());
            instanceId.append(key.charAt(pos));
        }
     return instanceId;
     }
	
    public String mappingObject(List<com.consistent.service.application.models.Input> items){
    	String xml="";
    	for (Input input : items) {
			if(input.equals("Hotel")){
				xml+=getXML(input.name, input.type, input.values, "");
			}
		}
		return xml;
    }
    
}
