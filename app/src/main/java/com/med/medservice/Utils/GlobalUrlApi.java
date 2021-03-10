package com.med.medservice.Utils;

public class GlobalUrlApi {

    private String UrlAppFolder;
    private String BaseUrl;
    private String newBaseUrl;
    private String HomeUrl;

    public GlobalUrlApi() {
        UrlAppFolder ="http://harisdev.com/medical/";
        BaseUrl ="http://harisdev.com/medical/";
        newBaseUrl ="https://umbrellamd.com/api/";
        HomeUrl ="http://harisdev.com/";
    }

    public String getUrlAppFolder() {
        return UrlAppFolder;
    }

    public String getNewBaseUrl() {
        return newBaseUrl;
    }
    public String getBaseUrl() {
        return BaseUrl;
    }
    public String getHomeUrl() {
        return HomeUrl;
    }


}
