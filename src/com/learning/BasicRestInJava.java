import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;



import java.net.*;
import javax.script.*;

class BasicRestInJava {
    
    public static boolean getResponse() throws IOException {
		String endPoint = "http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json";
        URL url = new URL(endPoint);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.addRequestProperty("Content-Type", "application/json");
        
        InputStream in=null;
        int code = conn.getResponseCode();
        if(code>=200 && code<299) {
            in = conn.getInputStream();
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuffer str = new StringBuffer();
        str.append(br.readLine());
		System.out.println("\n\n\n RESPONSE BODY = "+str.toString());
        br.close();
        conn.disconnect();
        
        ScriptEngineManager eng = new ScriptEngineManager();
        ScriptEngine scriptEngine = eng.getEngineByName("javascript");
        String strSc = "var objs = JSON.parse('"+str.toString()+"');";
        strSc += "var totals = !objs || objs.length === 0";
        
        try{
            scriptEngine.eval(strSc);
        } catch(Exception ex) {
            System.out.println("\n\n\nException   Exception   Exception="+ex);
        }
        
        if(scriptEngine.get("totals") == null) {
            throw new RuntimeException("total is null");
        }
		boolean responsePresent = !(boolean)scriptEngine.get("totals");
        return !(boolean)scriptEngine.get("totals");
    }

	public static void main(String[] args) {
			BasicRestInJava result = new BasicRestInJava();
			boolean isResponsePresent = false;
			try{
				isResponsePresent = result.getResponse();
			} catch(Exception ex){
				System.out.println("Exception="+ex);
			
			}
		System.out.println("\n\n\n Response Present = "+isResponsePresent);
	}
}