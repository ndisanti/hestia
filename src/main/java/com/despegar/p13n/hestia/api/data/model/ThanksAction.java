package com.despegar.p13n.hestia.api.data.model;

import com.despegar.p13n.euler.commons.client.model.UserAction;



/**
 * The Class ThanksAction.
 *
 * @author jcastro
 * @since Nov 2, 2012
 */
public class ThanksAction
    extends UserAction {

    public static final String IDCRO = "idcro";
    public static final String AGENCY = "agent";

    public int croId() {
        return this.getIntegerFromActionMap(IDCRO).intValue();
    }

    public Boolean agency() {
        String agent = this.getStringFromActionMap(AGENCY, "");
        return "1".equalsIgnoreCase(agent.trim());
    }

    public void agency(boolean agent) {
        this.getActionData().put(AGENCY, agent ? "1" : "0");
    }

}
