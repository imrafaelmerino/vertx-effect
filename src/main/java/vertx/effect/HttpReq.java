package vertx.effect;

import fun.optic.Lens;
import fun.optic.Option;
import io.vertx.core.http.RequestOptions;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

 abstract sealed class HttpReq<T extends HttpReq<T>> permits BodyHttpReq, DeleteReq, GetReq, HeadReq, OptionsReq, TraceReq {

    private static final String SSL_FIELD = "ssl";
    private static final String URI_FIELD = "uri";
    private static final String TIMEOUT_FIELD = "timeout";
    private static final String HOST_FIELD = "host";
    private static final String PORT_FIELD = "port";
    private static final String FOLLOW_REDIRECTS_FIELD = "followRedirects";
    private static final String TYPE_FIELD = "type";
    private static final String HEADERS_FIELD = "headers";
    private static final String BODY_FIELD = "body";

    public static final Option<JsObj, JsObj> HEADERS_OPT = JsObj.optional.obj(HEADERS_FIELD);
    public static final Option<JsObj, Long> TIMEOUT_OPT = JsObj.optional.longNum(TIMEOUT_FIELD);
    public static final Option<JsObj, Boolean> FOLLOW_REDIRECT_OPT = JsObj.optional.bool(FOLLOW_REDIRECTS_FIELD);
    public static final Option<JsObj, Integer> PORT_OPT = JsObj.optional.intNum(PORT_FIELD);
    public static final Option<JsObj, String> HOST_OPT = JsObj.optional.str(HOST_FIELD);
    public static final Lens<JsObj, Integer> TYPE_LENS = JsObj.lens.intNum(TYPE_FIELD);
    public static final Option<JsObj, String> URI_OPT = JsObj.optional.str(URI_FIELD);
    public static final Option<JsObj, Boolean> SSL_OPT = JsObj.optional.bool(SSL_FIELD);
    public static final Lens<JsObj, byte[]> BYTES_BODY_LENS = JsObj.lens.binary(BODY_FIELD);

    protected TYPE type;

    public enum TYPE {
        GET(0), POST(1), PUT(2), DELETE(3), OPTIONS(4), HEAD(5), TRACE(6), PATCH(7);
        public final int n;

        TYPE(final int n) {
            this.n = n;
        }
    }

    private JsObj headers = JsObj.empty();
    private Long timeout;
    private Boolean followRedirects;
    private Integer port;
    private String host;
    private String uri;
    private Boolean ssl;

    /**
     * add a value into the given header. It's appended to the end of the header values.
     *
     * @param key   the header name
     * @param value the new header value
     * @return this http req with a new value appended to the end of the specified header
     */
    @SuppressWarnings("unchecked")
    public T addHeader(String key,
                       String value
                      ) {
        JsValue values = headers.get(key);
        if (values.isNothing()) headers = headers.set(key,
                                                      JsArray.of(value)
                                                     );
        else headers = headers.set(key,
                                   values.toJsArray()
                                         .append(JsStr.of(value))
                                  );
        return (T) this;
    }

    /**
     * set a new value into the given header, replacing the existing one if it exists.
     *
     * @param key   the header name
     * @param value the new header value
     * @return this http req with a new value in the specified header
     */
    @SuppressWarnings("unchecked")
    public T setHeader(String key,
                       String value
                      ) {
        headers = headers.set(key,
                              JsArray.of(value)
                             );
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T timeout(final int timeout,
                     final TimeUnit unit
                    ) {
        if (timeout < 0) throw new IllegalArgumentException("timeout < 0");
        this.timeout = requireNonNull(unit).toMillis(timeout);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T ssl(final boolean ssl) {
        this.ssl = ssl;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T followRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T port(final int port) {
        if (port < 0) throw new IllegalArgumentException("port < 0");
        this.port = port;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T host(final String host) {
        if (requireNonNull(host).isEmpty()) throw new IllegalArgumentException("host is empty");
        this.host = host;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T uri(final String uri) {
        if (requireNonNull(uri).isEmpty()) throw new IllegalArgumentException("uri is empty");
        this.uri = uri;
        return (T) this;
    }

    public JsObj createHttpReq() {
        return TYPE_LENS.set.apply(type.n)
                            .andThen(!headers.isEmpty() ? HEADERS_OPT.set.apply(headers) : Function.identity())
                            .andThen(timeout != null ? TIMEOUT_OPT.set.apply(timeout) : Function.identity())
                            .andThen(followRedirects != null ? FOLLOW_REDIRECT_OPT.set.apply(followRedirects) : Function.identity())
                            .andThen(port != null ? PORT_OPT.set.apply(port) : Function.identity())
                            .andThen(host != null ? HOST_OPT.set.apply(host) : Function.identity())
                            .andThen(ssl != null ? SSL_OPT.set.apply(ssl) : Function.identity())
                            .andThen(uri != null ? URI_OPT.set.apply(uri) : Function.identity())
                            .apply(JsObj.empty());
    }

    public static final Function<JsObj, RequestOptions> toReqOptions =
            body -> {
                RequestOptions options = new RequestOptions();
                HOST_OPT.get.apply(body)
                            .ifPresent(options::setHost);

                PORT_OPT.get.apply(body)
                            .ifPresent(options::setPort);

                FOLLOW_REDIRECT_OPT.get.apply(body)
                                       .ifPresent(options::setFollowRedirects);

                SSL_OPT.get.apply(body)
                           .ifPresent(options::setSsl);

                HEADERS_OPT.get.apply(body)
                               .ifPresent(it -> it.keySet()
                                                  .forEach(key -> it.getArray(key)
                                                                    .forEach(value -> options.addHeader(key,
                                                                                                        value.toJsStr().value
                                                                                                       )))
                                         );
                TIMEOUT_OPT.get.apply(body)
                               .ifPresent(options::setTimeout);

                URI_OPT.get.apply(body)
                           .ifPresent(options::setURI);

                return options;
            };
}