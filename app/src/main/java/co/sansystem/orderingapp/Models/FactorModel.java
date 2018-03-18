package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2018-03-17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FactorModel {

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @SerializedName("Name_Kala")
    @Expose
    private String foodName;

    @SerializedName("ID_Kala")
    @Expose
    private String foodCode;

    @SerializedName("ChildForooshKala_TedadAsli")
    @Expose
    private int foodCount;

    @SerializedName("ChildForooshKala_GheymatPaye")
    @Expose
    private String foodPrice;

    public String getSumPriceRow() {
        return SumPriceRow;
    }

    public void setSumPriceRow(String sumPriceRow) {
        SumPriceRow = sumPriceRow;
    }

    @SerializedName("SumPriceRow")
    @Expose
    private String SumPriceRow;

    @SerializedName("Table_Number")
    @Expose
    private String tableNumber;

    @SerializedName("Costumer_Code")
    @Expose
    private String costumerCode;

    @SerializedName("Costumer_Name")
    @Expose
    private String costumerName;

    @SerializedName("Print_Confirm")
    @Expose
    private String printConfirm;

    @SerializedName("ChildForooshKala_SharhKala")
    @Expose
    private String foodExp;

    @SerializedName("User_Id")
    @Expose
    private String userId;

    @SerializedName("Price_Sum")
    @Expose
    private String priceSum;

    @SerializedName("ForooshKalaParent_TypeFact")
    @Expose
    private String vaziatSefaresh;

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCostumerCode() {
        return costumerCode;
    }

    public void setCostumerCode(String costumerCode) {
        this.costumerCode = costumerCode;
    }

    public String getCostumerName() {
        return costumerName;
    }

    public void setCostumerName(String costumerName) {
        this.costumerName = costumerName;
    }

    public String getPrintConfirm() {
        return printConfirm;
    }

    public void setPrintConfirm(String printConfirm) {
        this.printConfirm = printConfirm;
    }

    public String getFoodExp() {
        return foodExp;
    }

    public void setFoodExp(String foodExp) {
        this.foodExp = foodExp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(String priceSum) {
        this.priceSum = priceSum;
    }

    public String getVaziatSefaresh() {
        return vaziatSefaresh;
    }

    public void setVaziatSefaresh(String vaziatSefaresh) {
        this.vaziatSefaresh = vaziatSefaresh;
    }

}