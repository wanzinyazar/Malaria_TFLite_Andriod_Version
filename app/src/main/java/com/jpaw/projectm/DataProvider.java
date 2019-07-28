package com.jpaw.projectm;

public class DataProvider {

    private String name;
    private String paratisized;
    private String uninfected;

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParatisized() {
        return paratisized;
    }

    public void setParatisized(String paratisized) {
        this.paratisized = paratisized;
    }

    public String getUninfected() {
        return uninfected;
    }

    public void setUninfected(String uninfected) {
        this.uninfected = uninfected;
    }

    public DataProvider( String name, String paratisized, String uninfected){

        this.name= name;
        this.paratisized= paratisized;
        this.uninfected= uninfected;

    }

}
