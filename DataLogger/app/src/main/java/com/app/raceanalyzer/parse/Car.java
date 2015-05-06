package com.app.raceanalyzer.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("car")
public class Car extends ParseObject {
    public Car() {

    }


    public void setUser(ParseUser currentUser) {
        put("iduser", currentUser);
    }



    /**  engine **/
    public String getEngine() {
        return getString("engine");
    }


    public void setEngine(String engine) {
        put("engine", engine);
    }

    /**  torque **/
    public String getTouque() {
        return getString("touque");
    }


    public void setTouque(String touque) {
        put("touque", touque);
    }

    /**  model **/

    public void setModel(String model) {
        put("Model", model);
    }

    public String getModel() {return getString("model");}


    /**  horse power  **/
    public String getHorsePower_HP() {return getString("horsepower_hp");}
    public void setHorse_power_hp(int power) {put("horsepower_hp", power);}

    public String getHorsePower_round() {return getString("horsepower_round");}
    public void setHorse_power_round(int power) {put("horsepower_round", power);}


    /**  torque power  **/

    public String getTorquePower() {return getString("torque_power");}


    public void setTorque_power(int torque) {put("torque_power", torque);}

    public String getTorqueRound() {return getString("torque_round");}

    public void setTorque_round(int torque) {put("torque_round", torque);}



    /**  RIM  **/

    public String getRimFront() {return getString("rim_front");}

    public void setRimFront(String rim_front) {put("rim_front", rim_front);}

    public String getRimBack() {return getString("rim_back");}

    public void setRimBack(String rim_back) {put("rim_back", rim_back);}


    /**  tires  **/

    public String getTiresFront() {return getString("rim_front");}

    public void setTiresFront(String tires_front) {put("tires_front", tires_front);}

    public String getTiresRear() {return getString("tires_rear");}

    public void setTiresRear(String tires_rear) {put("tires_rear", tires_rear);}


    /**  driveline layout  **/

    public String getDriveLineLayout() {return getString("driveline_layout");}
    public void setDriveLineLayout(String driveLineLayout){put("driveline_layout" , driveLineLayout);}


    /**  weight front  **/
    public int getWeightFront(){return getInt("weight_front");}
    public void setWeightFront(int weight) {put("weight_front" , weight);}

    /**  weight all  **/
    public int getWeightAll(){return getInt("weight_all");}
    public void setWeightAll(int weight) {put("weight_all" , weight);}


    /**  height **/
    public int getHeight(){return getInt("height");}
    public void setHeight(int height) {put("height" , height);}


    /**  wheelbase **/
    public int getWheelbase(){return getInt("wheelbase");}
    public void setWheelbase(int weight) {put("wheelbase" , weight);}

    /**  traction control  **/
    public String getTractionControl(){return getString("traction_control");}
    public void setTractionControl(String mode) {put("traction_control" , mode);}

    /**  oil type  **/
    public String getOilType(){return getString("oil_type");}
    public void setOilType(String oil) {put("oil_type" , oil);}

    /**  oil quantity  **/
    public int getOilQuantity(){return getInt("oil_quantity");}
    public void setOilType(int oil) {put("oil_quantity" , oil);}

}
