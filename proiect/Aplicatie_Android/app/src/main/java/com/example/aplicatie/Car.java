package com.example.aplicatie;

public class Car {
    private String carBrand;
    private String carModel;
    private int year;
    private String carLP;
    private String email;
    private int oilKm;
    private int padsKm;
    private String idBle;

    public Car(String carBrand, String carModel, int year, String carLP, String email, int oilKm, int padsKm, String idBle) {
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.year = year;
        this.carLP = carLP;
        this.email = email;
        this.oilKm = oilKm;
        this.padsKm = padsKm;
        this.idBle = idBle;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCarLP() {
        return carLP;
    }

    public void setCarLP(String carLP) {
        this.carLP = carLP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOilKm() {
        return oilKm;
    }

    public void setOilKm(int oilKm) {
        this.oilKm = oilKm;
    }

    public int getPadsKm() {
        return padsKm;
    }

    public void setPadsKm(int padsKm) {
        this.padsKm = padsKm;
    }

    public String getIdBle() {
        return idBle;
    }

    public void setIdBle(String idBle) {
        this.idBle = idBle;
    }
}
