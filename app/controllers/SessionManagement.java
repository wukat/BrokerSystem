package controllers;

import global.Global;
import org.jose4j.lang.JoseException;
import play.Logger;
import play.mvc.Http;


/**
 * Created by wukat on 08.06.15.
 */
public class SessionManagement {

    private static String[] getParts(Http.Session session) {
        try {
            if (session.containsKey("k")) {
                Global.jwe.setCompactSerialization(session.get("k"));
                return Global.jwe.getPayload().split(" ");
            }
        } catch (JoseException e) {
            System.out.println(e);
            Logger.debug("Encryption failed");
            return null;
        }
        return null;
    }

    private static String getPart(Http.Session session, int i) {
        String[] parts = getParts(session);
        if (parts != null) {
            if (parts.length > i)
                return parts[i];
        }
        return null;
    }

    public static String getEmail(Http.Session session) {
        return getPart(session, 1);
    }

    public static boolean isOk(Http.Session session) {
        String ok = getPart(session, 0);
        if (ok != null && ok.equals("ok")) {
            return true;
        }
        return false;
    }
}
