package utils;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

/**
 * Created by wukat on 22.06.15.
 */
public class HTTPInterceptor extends Action<SimpleResult> {
    @Override
    public F.Promise<SimpleResult> call(Http.Context context) throws Throwable {
        if (context.request().headers().containsKey("X-Forwarded-Ssl")) {
            return delegate.call(context);
        } else {
            return F.Promise.pure(redirect("https://localhost"+context.request().uri()));
        }
    }
}
