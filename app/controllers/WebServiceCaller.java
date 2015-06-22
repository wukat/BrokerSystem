package controllers;

import models.Client;
import models.Offer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.LinkedList;

/**
 * Created by wukat on 22.06.15.
 */
public class WebServiceCaller extends Controller {
    public static Result tadam() throws JAXBException {
        String wsReqDaysToHolidays =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<soap:Envelope  xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ns=\"http://localhost:8080/Hotel_war_exploded/Hotel\" >" +
                        "<soap:Body>" +
                        "<ns:daysToHolidays>" +
                        "<arg0>Hello World</arg0>" +
                        "</ns:daysToHolidays>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";
        String wsReq =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<soap:Envelope  xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ns=\"http://localhost:8080/Hotel_war_exploded/Hotel\" >" +
                        "<soap:Body>" +
                        "<ns:getOffers>" +
                        "</ns:getOffers>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";
        String xml = null;
        xml = getXml("<ns:getOffers>" +
                "</ns:getOffers>", "http://localhost:8080/Hotel_war_exploded/Hotel");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            JAXBContext context = JAXBContext.newInstance(Offer.class);
            Unmarshaller u = context.createUnmarshaller();
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            Node a = document.getDocumentElement().getElementsByTagName("return").item(0);
            for (int i = 0; i < a.getParentNode().getChildNodes().getLength(); i++) {
                document.renameNode(a.getParentNode().getChildNodes().item(i), null, "offer");
            }
            Offer books = (Offer) u.unmarshal(a);
            System.out.println(books);
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
        }
        return ok();
    }

    private static String createXML(String invocation, String ns) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soap:Envelope  xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ns=\"" + ns + "\" >" +
                "<soap:Body>" +
                invocation +
                "</soap:Body>" +
                "</soap:Envelope>";
    }

    private static String getXml(String invocation, String ns) {
        String response = null;
        try {
            response = WS.url(ns + "WS").setHeader("content-type", "application/soap+xml").post(createXML(invocation, ns)).get(1000).getBody();
        } catch (Exception a) {
            Logger.error("Connection refused");
        }
        return response;
    }


    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result synchronize() {
        String email = SessionManagement.getEmail(session());
        Client client = Client.getClientByEmail(email);
        if (Client.isBusinessClient(email) && client.getClientData() != null) {
            if (client.getClientData().getEndpoint() != null) {
                String xml = getXml("<ns:getOffers></ns:getOffers>", client.getClientData().getEndpoint());
                if (xml == null) {
                    flash("error", "Connection refused");
                }
                LinkedList<Offer> offers = Offer.getOffersFromXml(xml);
                if (offers.size() == 0) {
                    flash("info", "Nothing to update");
                } else {
                    if (Offer.updateOrSave(offers, client)) {
                        flash("success", "Offers updated");
                    } else {
                        flash("info", "No changes");
                    }
                }
                return redirect(routes.UserProfile.profile(client.getClientId()));
            } else {
                flash("error", "Endpoint not defined");
            }
        } else {
            flash("error", "Access denied");
        }
        return redirect(routes.Application.index());
    }
}
