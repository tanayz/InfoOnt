package com.cs586.dbpedia;

import java.util.HashMap;
import java.util.Map;

public class DBPediaLookUp {

	static Map<String, Map<String,String>> wordTypeUrlMap = new HashMap<String, Map<String,String>>();
	
	public static String getDBPediaUrl(String searchString,String type){
		
		if(type == "ORG"){
			type="Organisation";
		} else if(type == "PER"){
			type="Person";
		} else if(type == "LOC") {
			type = "Place";
		} else if(type == "MISC") {
			//DO SOMETHING STRAMGE HERE !!!!
			type="";
		} else{
			type="";
		}
		
		if(wordTypeUrlMap.containsKey(searchString)){
			Map<String,String> typeUrlMap = wordTypeUrlMap.get(searchString);
			if(typeUrlMap.containsKey(type)){
				//System.out.println("CACHE HIT :D :D :D");
				return typeUrlMap.get(type);
			}else{
				updateWordTypeUrlMap(searchString,type);
			}
		}else{
			wordTypeUrlMap.put(searchString, new HashMap<String,String>());
			Map<String,String> typeUrlMap = wordTypeUrlMap.get(searchString);
			updateWordTypeUrlMap(searchString,type);
			if(typeUrlMap.containsKey(type)){
				//System.out.println("CACHE HIT :D :D :D");
				return typeUrlMap.get(type);
			}
		}
		return "";
	}
	
	private static void updateWordTypeUrlMap(String searchString, String type) {
		Map<String,String> typeUrlMap = wordTypeUrlMap.get(searchString);
		String url = getFirstUrlForSearchWordAndType(searchString,type);
		typeUrlMap.put(type,url);
	}

	private static String getFirstUrlForSearchWordAndType(String searchString, String type) {
		DBpediaLookupClient lookup;
		try {
			lookup = new DBpediaLookupClient(
					"PrefixSearch?QueryString="+searchString+"&QueryClass="+type);
					//searchString);
			for (Map<String, String> bindings : lookup.variableBindings()) {
				String url = bindings.get("URI");
				//System.out.println("DBPediaLookUp # SearchString="+searchString+",Type="+type+" : "+url);
				return url;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		
		String [] key={"Berlin","Mandela","Obama","Bank Of America","Barack","Obama"};
		String [] type={"LOC","PER","MISC","ORG","PER","MISC"};

		for(int i =0 ;i<key.length;i++){
			//System.out.println(getDBPediaUrl(key[i],type[i]));
		}
		
	}
}
