package com.despegar.p13n.hestia.api.data.model;

import com.despegar.p13n.euler.commons.client.model.UserAction;


/**
 * Represents the event of a user entering to any site home.
 * 
 * @author lbernardi
 * 
 */
public class EnterHomeAction
    extends UserAction {

    /**
     * IATA Code the user.
     */
    private String userOriginCode;


    public static String userOriginCode() {
        return "ip"; // FIXME
    }


    public String getUserOriginCode() {
        return this.userOriginCode;
    }

    public void setUserOriginCode(String userOriginCode) {
        this.userOriginCode = userOriginCode;
    }

}
