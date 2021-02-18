package com.med.medservice.Utils;

public class GlobalUrlApi {

    private String UrlAppFolder;
    private String BaseUrl;
    private String HomeUrl;

    public GlobalUrlApi() {
        UrlAppFolder ="http://harisdev.com/medical/";
        BaseUrl ="http://harisdev.com/medical/";
        HomeUrl ="http://harisdev.com/";
    }

    public String getUrlAppFolder() {
        return UrlAppFolder;
    }

    public String getBaseUrl() {
        return BaseUrl;
    }
    public String getHomeUrl() {
        return HomeUrl;
    }


}
