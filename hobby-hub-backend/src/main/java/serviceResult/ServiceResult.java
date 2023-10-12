/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serviceResult;

import org.json.JSONObject;

/**
 *
 * @author Zuucker
 */
public class ServiceResult {

    public boolean status;
    public String value;

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", this.status);
        jsonObject.put("value", this.value);

        return jsonObject.toString();
    }

}
