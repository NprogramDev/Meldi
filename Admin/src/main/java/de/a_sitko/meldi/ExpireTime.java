package de.a_sitko.meldi;

public enum ExpireTime {
    ONE_MIN("1 min", 1), FIVE_MIN("5 min",5), TEN_MIN("10 min",10), QUARTER_HOUR("15 min", 15), HALF_HOUR("30 min", 30), LESSON("45 min",45), ONE_HOUR("1h",60), TWO_HOURS("2h",120),DEFAULT("...",0);
    public static ExpireTime ofTime(int time){
        for (ExpireTime value : ExpireTime.values()) {
            if(value.minutes == time) return value;
        }
        return DEFAULT;
    }
    private String title;
    private int minutes;
    private ExpireTime(String title,int minutes){
        this.title = title;
        this.minutes = minutes;
    }
    public String toString(){
        return title;
    }
}
