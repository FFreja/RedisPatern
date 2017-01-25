package codec

import com.lambdaworks.redis.codec.RedisCodec

import java.nio.ByteBuffer

/**
 * Lettuce default to StringCodec if there is no RedisCodec defined
 * Customize Codec with protobuff
 */
class CartCodec implements RedisCodec<String, byte[]>{

    @Override
    String decodeKey(ByteBuffer bytes) {
        return new String(bytes)
    }

    @Override
    byte[] decodeValue(ByteBuffer bytes) {
        return getBytes(bytes)
    }

    @Override
    ByteBuffer encodeKey(String key) {
        ByteBuffer.wrap(key.bytes)
    }


    ByteBuffer encodeValue(byte[] value) {
        if(value == null){
            //Todo: exception
        }

        return ByteBuffer.wrap(value)
    }

    private static byte[] getBytes(ByteBuffer buffer) {
        byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return b;
    }

}
