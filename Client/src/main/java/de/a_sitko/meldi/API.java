package de.a_sitko.meldi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class API {
    static final String CONNECTION_TEST_PATH = "/is-meldi-server";
    static final String RAISE = "/raise";
    static final String UNRAISE = "/unraise";
    static final String FETCH_POSITION = "/get_position";
    static final float VERSION = 0.1f;
    static final String DELIMITER_READ_ALL = "\\A";
    static final Map<String,String> EMPTY_PARAMS = new HashMap<>();
    private Map<String,String> currentParams = new HashMap<>();
    private boolean connected = false;
    public enum ConnectionType{
        NO, // No Connection at all!
        PERFECT, // Version of the server matches the one of the client!
        NEW_VERSION, // Version of the server is newer than the client!
        OLD_SERVER, // Version of the client is newer than the server!
    }
    public API(){

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
    public void setUserID(String device, String name,String uuid){
        currentParams = new HashMap<>();
        currentParams.put("user", name);
        currentParams.put("device",device);
        currentParams.put("uuid",uuid);
    }
    public boolean raise(boolean status){
        return this.call(Changeables.BASE_URL,status ? RAISE : UNRAISE,currentParams) != null;
    }
    public int fetchPosition(){
        String rt = this.call(Changeables.BASE_URL,FETCH_POSITION,currentParams);
        int position = -1;
        try {
            position = Integer.parseInt(rt.trim());
        }catch(NumberFormatException e){
         e.printStackTrace();
        }
        return position;
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
        try {
            URI r = new URI(base_url + path + querys.toString());
            System.out.println(r.toString());
            InputStream c = r.toURL().openStream();
            Scanner s = new Scanner(c).useDelimiter(DELIMITER_READ_ALL);
            StringBuilder rt = new StringBuilder();
            while(s.hasNext()){
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
