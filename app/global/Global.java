package global; /**
 * Created by wukat on 08.06.15.
 */

import com.lowagie.text.DocumentException;
import models.Client;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.Akka;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.SimpleResult;
import scala.concurrent.duration.Duration;
import views.html.invoice;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.concurrent.TimeUnit;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.notFound;

public class Global extends GlobalSettings {

    public static final JsonWebEncryption jwe = new JsonWebEncryption();

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");
        Key key = new AesKey(ByteUtil.randomBytes(16));
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(20, TimeUnit.SECONDS),
                new Runnable() {
                    public void run() {
                        try {
                            JPA.withTransaction(new play.libs.F.Function0<Void>() {
                                @Override
                                public Void apply() throws Throwable {
                                    for (Client c : JPA.em().createQuery("FROM Client", Client.class).getResultList()) {
                                        if (c.getRole().getRole().equals("business") && c.getClientData() != null && c.getClientData().getEndpoint() != null) {
                                            try {
                                                Document document = XMLResource.load(new ByteArrayInputStream(invoice.render(c).body().getBytes())).getDocument();
                                                ITextRenderer renderer = new ITextRenderer();
                                                renderer.setDocument(document, null);
                                                renderer.layout();
                                                FileOutputStream fos = new FileOutputStream("invoice.pdf");
                                                renderer.createPDF(fos);
                                                fos.close();
                                            } catch (DocumentException | IOException e) {
                                                System.out.println(e);
                                            }
                                            try {
                                                SOAPConnectionFactory factoryS = SOAPConnectionFactory.newInstance();
                                                SOAPConnection connection = factoryS.createConnection();
                                                MessageFactory messageFactory = MessageFactory.newInstance();
                                                SOAPMessage message = messageFactory.createMessage();

                                                SOAPPart soapPart = message.getSOAPPart();
                                                SOAPEnvelope envelope = soapPart.getEnvelope();
                                                SOAPBody body = envelope.getBody();
                                                SOAPBodyElement bodyElement = body.addBodyElement(
                                                        envelope.createName("uploadInvoice", "hotel",
                                                                c.getClientData().getEndpoint()));
                                                SOAPElement el = bodyElement.addChildElement("arg0");

                                                DataHandler handler = new DataHandler(new FileDataSource(new File("/home/wukat/Programming/Broker/invoice.pdf")));
                                                AttachmentPart attachPart = message.createAttachmentPart(handler);
                                                attachPart.addMimeHeader("Content-ID", "10");
                                                el.addChildElement("Include", "xop", "http://www.w3.org/2004/08/xop/include").addAttribute(new QName("href"), "cid:" + attachPart.getContentId());
                                                message.addAttachmentPart(attachPart);
                                                SOAPMessage response = connection.call(message, c.getClientData().getEndpoint() + "WS");
                                            } catch (SOAPException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    return null;
                                }
                            });
                        } catch (Exception th) {
                            System.out.println(th);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
                ,
                Akka.system().dispatcher()
        );
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    @Override
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(notFound(
                views.html.notFound.render(request.uri())
        ));
    }

    @Override
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error) {
        return Promise.<SimpleResult>pure(badRequest(views.html.badUri.render(request.uri())));
    }
}
