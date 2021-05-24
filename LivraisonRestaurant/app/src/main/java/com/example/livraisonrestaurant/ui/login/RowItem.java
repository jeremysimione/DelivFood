package com.example.livraisonrestaurant.ui.login;

public class RowItem {

    private String heading;
    private String subHeading;
    private String theFooter;
    private int smallImageName;
    private int bigImageName;

    public void setHeading( String theHeading ) {

        this.heading = theHeading;

    }

    public String getHeading() {

        return this.heading;

    }

    public void setSubHeading( String theSubHeading ) {

        this.subHeading = theSubHeading;

    }

    public String getSubHeading( ) {

        return this.subHeading;

    }

    public void setSmallImageName(int smallName) {

        this.smallImageName = smallName;

    }

    public int getSmallImageName() {

        return this.smallImageName;

    }
    public String getTheFooter(){
        return theFooter;
    }

    public void setTheFooter(String str){
        theFooter = str;
    }

    public void setBigImageName(int bigName) {

        this.bigImageName = bigName;

    }

    public int getBigImageName( ) {

        return this.bigImageName;

    }
}
