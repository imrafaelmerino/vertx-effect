package vertxeffect.core;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.DecodeException;
import jsonvalues.JsArray;
import jsonvalues.MalformedJson;

public class JsArrayMessageCodec implements MessageCodec<JsArray, JsArray> {

  public static final JsArrayMessageCodec INSTANCE = new JsArrayMessageCodec();

  private JsArrayMessageCodec() { }

  @Override
  public JsArray decodeFromWire(int pos,
                                final Buffer buffer
                               ) {

    try {
      int length = buffer.getInt(pos);
      pos += 4;
      return JsArray.parse(buffer.getString(pos,
                                            pos + length
                                           )
                          );
    } catch (MalformedJson malformedJson) {
      throw new DecodeException(malformedJson.getMessage());
    }
  }

  @Override
  public void encodeToWire(final Buffer buffer,
                           final JsArray obj
                          ) {
    byte[] encoded = obj.serialize();
    buffer.appendInt(encoded.length);
    buffer.appendBytes(encoded);
  }

  @Override
  public String name() {
    return "json-array-value";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }

  @Override
  public JsArray transform(final JsArray arr) {
    return arr;
  }
}
