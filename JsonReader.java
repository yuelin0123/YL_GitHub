import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {
	//open and read whole url file.
	private static String getURLFile(String url){
		if (null == url | "" == url ){//check input.
			return null;
		}
		//System.out.println(url);
		String buffer = "";//main file buffer.
	    URL urlFile;//url IO object
	    
		try {
			byte[] b=new byte[1024];//buffer 
			int  byteread=0;//byte counter
			
			
			urlFile = new URL(url);
			InputStream input = urlFile.openStream();
			
			
			while ((byteread = input.read(b)) != -1){
				String reads = new String(b, 0, byteread , "UTF-8"); 
				buffer = buffer + reads;//attach to file buffer.
				}
			
			//System.out.println(buffer);
			input.close();
			return buffer; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;// we return an null here, instead of stack printing.
			//e.printStackTrace();
		}
		//return null;
		
	}
	
	public static void main(String[] args) {
		
		int index 		= 1;
		int hd_flags_true 	= 0;
		int hd_flags_false 	= 0;
	    String urlPath = "http://api.viki.io/v4/videos.json?app=100250a&per_page=10&page=";
	    boolean more = true;
	    while(more == true){
	    	
	    	String urlfile = "";
	    	urlfile = getURLFile(urlPath + index++);
	    	if ( null == urlfile){
	    		more = false;
	    	}
	    	else{
	    		//System.out.println(urlfile);
	    		JSONObject jsonObject = new JSONObject(urlfile);
	    		more = jsonObject.getBoolean("more");
	    		if(more == true){
	    			//System.out.println("has more !");
	    			try{
	    				JSONArray jsonResponse = jsonObject.getJSONArray("response");
	    				boolean gothd = false;
	    				for(int i = 0;i<jsonResponse.length() ;i++){
	    					JSONObject item = jsonResponse.getJSONObject(i);
	    					if (item.has("flags")){
	    						gothd = true;
	    						//assume that item "hd" always in "flag" section.
	    						boolean hd = item.getJSONObject("flags").getBoolean("hd");
	    						if( hd ){
	    	    					hd_flags_true++;
	    	    				}
	    	    				else
	    	    				{
	    	    					hd_flags_false++;
	    	    				}
	    					}
	    				}
	    				if(false == gothd){
	    					//can not find "flags" section in entire arrary.
	    					throw new JSONException(" flags section lost! ") ;
	    				}
	    				
	    			}
	    			catch(JSONException e)
	    			{
	    				System.out.println(e);
	    				more = false;
	    			}
	    		}//if(more == true)
	    	}//if ( null == urlfile) else
	    }// while(more == true)
	    System.out.println("Total " + hd_flags_true +"'hd'flag mark as true." );
	    System.out.println("Total " + hd_flags_false +"'hd'flag mark as false." );
	}

}