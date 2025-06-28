package de.a_sitko.meldi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

public class API {
    static final String CONNECTION_TEST_PATH = "/is-meldi-server";
    static final String RAISE = "/raise";
    static final String UNRAISE = "/unraise";
    static final String ENTRIES_FOR_ID= "/entriesForID";
    static final String EXPIRE_TIME = "/expiresOn";
    static final float VERSION = 0.1f;
    static final String DELIMITER_READ_ALL = "\\A";
    static final Map<String,String> EMPTY_PARAMS = new HashMap<>();
    private Map<String,String> currentParams = new HashMap<>();
    private boolean connected = false;
    public API(){

    }
    public enum ConnectionType{
        NO, // No Connection at all!
        PERFECT, // Version of the server matches the one of the client!
        NEW_VERSION, // Version of the server is newer than the client!
        OLD_SERVER, // Version of the client is newer than the server!
    }
    public ConnectionType testServerConnection(){
        String result = this.call(Changeables.BASE_URL,CONNECTION_TEST_PATH,EMPTY_PARAMS);
        if(result == null) return ConnectionType.NO;
        String[] params = result.split(",");
        try{
            float p = Float.parseFloat(params[0]);
            if(p == VERSION) return ConnectionType.PERFECT;
            if(p > VERSION) return ConnectionType.NEW_VERSION;
            return ConnectionType.OLD_SERVER;
        }catch (Exception e){}
        return ConnectionType.NO;
    }
    public void setUserID(String uuid){
        currentParams = new HashMap<>();
        currentParams.put("uuid",uuid);
    }
    public int getExpireTime(){
        String raw = this.call(Changeables.BASE_URL,EXPIRE_TIME,currentParams);
        if(raw == null){
            return Main.DEFAULT_EXPIRE_TIME_MIN;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return Main.DEFAULT_EXPIRE_TIME_MIN;
        }
    }
    private static Map<String,String> getParam(String url){
        url = url.replace("?","");
        Map<String,String> map = new HashMap<>();
        for (String s : url.split("&")) {
            String[] arg = s.split("=");
            if(arg.length != 2) continue;
            map.put(arg[0],arg[1]);
        }
        return map;
    }
    public List<Student> fetchStudents(){
        String raw = this.call(Changeables.BASE_URL,ENTRIES_FOR_ID,this.currentParams);
        List<Student> rt = new LinkedList<>();
        if(raw == null) return rt;
        for (String s : raw.split("\r\n")) {
            Map<String,String> a = getParam(s);
            if(!a.containsKey("device") || !a.containsKey("user"))continue;
            rt.add(new Student(s,a.get("device"),a.get("user")));
        }
        return rt;
    }
    public void unraise(String a){
        this.call(Changeables.BASE_URL,UNRAISE,"?"+a);
    }
    private String call(String base_url, String path, Map<String,String> args){
        StringBuilder querys = new StringBuilder();
        for(Map.Entry<String,String> entry : args.entrySet()) {
            querys.append("&");
            querys.append(entry.getKey());
            querys.append("=");
            querys.append(entry.getValue());
        }
        if(!querys.isEmpty()) querys.setCharAt(0,'?');
        return call(base_url,path,querys.toString());
    }
    private String call(String base_url, String path, String args){
        try {
            URI r = new URI(base_url + path + args);
            System.out.println(r.toString());
            InputStream c = r.toURL().openStream();
            Scanner s = new Scanner(c).useDelimiter(DELIMITER_READ_ALL);
            StringBuilder rt = new StringBuilder();
            while(s.hasNext()) {
                rt.append(s.next());
            }
            return rt.toString();
        }catch(FileNotFoundException e){
        }catch(MalformedURLException e){
        }catch(IOException e){
        }catch(URISyntaxException e){}

        return null;
    }
}
