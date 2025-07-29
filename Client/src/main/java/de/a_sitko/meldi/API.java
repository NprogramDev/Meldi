/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
package de.a_sitko.meldi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class API {
    /**
     * The connection test path, to get the version and other stats from the server as a csv or something
     */
    static final String CONNECTION_TEST_PATH = "/is-meldi-server";
    /**
     * API Endpoint to raise your hand
     * GET this from a server with ?user&device&uuid to raise your hand
     */
    static final String RAISE = "/raise";
    /**
     * API Endpoint to remove raising your hand
     * GET this from a server with ?user&device&uuid to remove raising your hand
     */
    static final String UNRAISE = "/unraise";
    /**
     * GET the position if raised with params of user&device&uuid
     */
    static final String FETCH_POSITION = "/get_position";
    /**
     * The version of the server to be accepted without a problem
     */
    static final float VERSION = 1.0f;
    /**
     * The read all parameter for a stream to read all bytes
     */
    static final String DELIMITER_READ_ALL = "\\A";
    /**
     * For requests without parameters uses this property
     */
    static final Map<String,String> EMPTY_PARAMS = new HashMap<>();
    /**
     * The current params of this user: Like the user, device, uuid param
     */
    private Map<String,String> currentParams = new HashMap<>();

    /**
     * The connection type based on the version of the server and client and it's relation
     */
    public enum ConnectionType{
        NO, // No Connection at all!
        PERFECT, // Version of the server matches the one of the client!
        NEW_VERSION, // Version of the server is newer than the client!
        OLD_SERVER, // Version of the client is newer than the server!
    }

    /**
     * Does nothing except creating a object
     */
    public API(){

    }

    /**
     * Tests the connection to the server and retrieves the version
     * @return ConnectionType based on the server version and the connection
     */
    public ConnectionType testServerConnection(){
        //Ask the server for the connection
        String result = this.call(Config.getInstance().domain, CONNECTION_TEST_PATH,EMPTY_PARAMS);
        //if not possible => ConnectionType.NO
        if(result == null) return ConnectionType.NO;
        //Split the csv list
        String[] params = result.split(",");
        try{
            // The first param is the version and compare it to the exspected version
            float p = Float.parseFloat(params[0]);
            if(p == VERSION) return ConnectionType.PERFECT;
            if(p > VERSION) return ConnectionType.NEW_VERSION;
            return ConnectionType.OLD_SERVER;
        }catch (Exception e){}
        // Only returns ConnectionType.No if version is not a float
        return ConnectionType.NO;
    }

    /**
     * Set all params the user needs for the server
     * @param device The device id based on the Network Name of the PC
     * @param name The name of the user based on the user folder name
     * @param uuid The Admin uuid to map the users to the admin users
     */
    public void setUserID(String device, String name,String uuid){
        currentParams = new HashMap<>();
        currentParams.put("user", name);
        currentParams.put("device",device);
        currentParams.put("uuid",uuid);
    }

    /**
     * Changes the raise status of the user on the server
     * @param status true = Raise your hand
     * @return if it was successful and not null response
     */
    public boolean raise(boolean status){
        return this.call(Config.getInstance().domain, status ? RAISE : UNRAISE,currentParams) != null;
    }

    /**
     * Get the position of you on the waiting list
     * @return The position, if r < 0 => not on the list
     */
    public int fetchPosition(){
        String rt = this.call(Config.getInstance().domain, FETCH_POSITION,currentParams);
        int position = -1;
        try {
            position = Integer.parseInt(rt.trim());
        }catch(NumberFormatException e){
         e.printStackTrace();
        }
        return position;
    }

    /**
     * Calls a HTTP(S) server with some params and some path
     * @param base_url The Servers Location
     * @param path The path to the resource
     * @param args The url params to be set
     * @return The answer from the server as String or if not possible null
     */
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
