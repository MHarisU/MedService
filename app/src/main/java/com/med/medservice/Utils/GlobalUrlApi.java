package com.med.medservice.Utils;

public class GlobalUrlApi {

    private String UrlAppFolder;
    private String BaseUrl;
    private String newBaseUrl;
    private String newHomeUrl;
    private String HomeUrl;

    public GlobalUrlApi() {
       // newBaseUrl ="https://umbrellamd.com/api/";
        newBaseUrl ="http://umbrellamd.migmediacompany.com/api/";
       // newHomeUrl ="https://umbrellamd.com/";
        newHomeUrl ="http://umbrellamd.migmediacompany.com/";

        
        UrlAppFolder ="http://harisdev.com/medical/";
        BaseUrl ="http://harisdev.com/medical/";
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
