package de.a_sitko.meldi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Config {
    private static final String SERVER_DOMAIN = "server_domain: ";
    private static final String USER_UUID = "user_uuid: ";
    private static final String SCREEN_POS = "screen_pos: ";
    private static final String ALLOW_WINDOW_MOVE = "allow_window_move: ";
    private static Config inst;
    public static Config getInstance(){
        if(inst == null) inst = new Config();
        return inst;
    }
    public String domain = "";
    public String user_id = "";
    public boolean allow_window_move = true;
    public int[] screen_position = new int[2];

    public Config(){
        try (InputStream input = this.getClass().getResourceAsStream("config.yaml")) {
            if (input == null) {
                System.err.println("File not found in resources.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split(":",2);
                    if(p.length != 2) continue;
                    p[0] = p[0].trim();
                    p[1] = p[1].trim();
                    switch (p[0]){
                        case SERVER_DOMAIN:
                            this.domain = p[1];
                            break;
                        case USER_UUID:
                            this.user_id = p[1];
                            break;
                        case SCREEN_POS:
                            this.screen_position = getIntArray(p[1]);
                            break;
                        case ALLOW_WINDOW_MOVE:
                            this.allow_window_move = getBool(p[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean getBool(String value){
        if(value == null) return false;
        return value.equals("true");
    }
    private int[] getIntArray(String value){
        value = value.replace("[","");
        value = value.replace("]","");
        String[] val = value.split(",");
        return new int[]{Integer.parseInt(val[0]),Integer.parseInt(val[1])};
    }
}
