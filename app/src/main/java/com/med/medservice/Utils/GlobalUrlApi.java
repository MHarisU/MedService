package com.med.medservice.Utils;

public class GlobalUrlApi {

    private String UrlAppFolder;
    private String BaseUrl;
    private String newBaseUrl;
    private String newHomeUrl;
    private String HomeUrl;

    public GlobalUrlApi() {
        UrlAppFolder ="http://harisdev.com/medical/";
        BaseUrl ="http://harisdev.com/medical/";
        newBaseUrl ="https://umbrellamd.com/api/";
        newHomeUrl ="https://umbrellamd.com/";
        HomeUrl ="http://harisdev.com/";
    }

    public String getUrlAppFolder() {
        return UrlAppFolder;
    }

    public String getNewBaseUrl() {
        return newBaseUrl;
    }

    public String getNewHomeUrl() {
        return newHomeUrl;
    }

    public String getBaseUrl() {
        return BaseUrl;
    }
    public String getHomeUrl() {
        return HomeUrl;
    }


}
