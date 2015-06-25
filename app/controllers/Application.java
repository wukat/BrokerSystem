package controllers;

import com.lowagie.text.DocumentException;
import org.jose4j.lang.JoseException;
import play.Routes;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.IOException;

public class Application extends Controller {

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        controllers.routes.javascript.Rooms.removeImage(),
                        controllers.routes.javascript.Offers.removeOffer()
                )
        );
    }

    @Transactional
    public static Result index() throws JoseException, IOException, DocumentException {
        return ok(index.render("ok"));
    }
}
