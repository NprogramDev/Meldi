package de.a_sitko.meldi;

public class Student {
    private String name;
    private String device;
    private String original;

    public Student(String original, String device, String name){
        this.original = original;
        this.device = device;
        this.name = name;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getOriginal(){
        return this.original;
    }
}
