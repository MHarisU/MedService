package com.med.medservice.Utils;

public class GlobalUrlApi {

    private String UrlAppFolder;
    private String BaseUrl;
    private String newBaseUrl;
    private String newHomeUrl;
    private String HomeUrl;

    public GlobalUrlApi() {
       // newBaseUrl ="https://umbrellamd.com/api/";
     //   newBaseUrl ="http://umbrellamd.migmediacompany.com/api/";
        newBaseUrl ="https://umbrellamd-video.com/api/";

       // newHomeUrl ="https://umbrellamd.com/";
      //  newHomeUrl ="http://umbrellamd.migmediacompany.com/";
        newHomeUrl ="https://umbrellamd-video.com/";

        
        UrlAppFolder ="https://ezeearn.co/harisDev_beckup/medical/";
        BaseUrl ="https://ezeearn.co/harisDev_beckup/medical/";
        HomeUrl ="https://ezeearn.co/harisDev_beckup/";
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
