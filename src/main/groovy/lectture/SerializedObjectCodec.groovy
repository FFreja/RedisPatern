package lectture

import com.lambdaworks.redis.codec.RedisCodec

import java.nio.ByteBuffer
import java.nio.charset.Charset

class  SerializedObjectCodec implements RedisCodec<String, Object> {

    private Charset charset = Charset.forName("UTF-8");

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return charset.decode(bytes).toString();
    }

    @Override
    public Object decodeValue(ByteBuffer bytes) {
        try {
            byte[] array = new byte[bytes.remaining()];
            bytes.get(array);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
            return is.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return charset.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(Object value) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytes);
            os.writeObject(value);
            return ByteBuffer.wrap(bytes.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }
}

public class ByteBufferCodec implements RedisCodec<ByteBuffer, ByteBuffer> {

    @Override
    public ByteBuffer decodeKey(ByteBuffer bytes) {

        ByteBuffer decoupled = ByteBuffer.allocate(bytes.remaining());
        decoupled.put(bytes);
        return (ByteBuffer) decoupled.flip();
    }

    @Override
    public ByteBuffer decodeValue(ByteBuffer bytes) {

        ByteBuffer decoupled = ByteBuffer.allocate(bytes.remaining());
        decoupled.put(bytes);
        return (ByteBuffer) decoupled.flip();
    }

    @Override
    public ByteBuffer encodeKey(ByteBuffer key) {
        return key.asReadOnlyBuffer();
    }

    @Override
    public ByteBuffer encodeValue(ByteBuffer value) {
        return value.asReadOnlyBuffer();
    }
}
