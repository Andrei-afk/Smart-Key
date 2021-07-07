package com.example.aplicatie;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ErrorCar
{
    private String carModel;
    private String carBrand;
    private String errorName;
    private String errorText;
    private String email;
    private String todayDate;
public ErrorCar()
{}
    public ErrorCar(String carModel, String carBrand, String errorName, String errorText, String email, String todayDate) {
        this.carModel = carModel;
        this.carBrand = carBrand;
        this.errorName = errorName;
        this.errorText = errorText;
        this.email = email;
        this.todayDate = todayDate;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }
}
