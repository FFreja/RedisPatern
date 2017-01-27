package codec

import com.lambdaworks.redis.codec.RedisCodec
import org.apache.commons.lang3.StringUtils

import java.nio.ByteBuffer

/**
 * Lettuce default to StringCodec if there is no RedisCodec defined
 * Customize Codec with protobuff
 */
class CartCodec implements RedisCodec<GString, byte[]>{

    @Override
    GString decodeKey(ByteBuffer bytes) {
        return Eval.me(new String(bytes))
    }

    @Override
    byte[] decodeValue(ByteBuffer bytes) {
        return getBytes(bytes)
    }

    @Override
    ByteBuffer encodeKey(GString key) {
        if(StringUtils.isEmpty(key)){
            //Todo: exception
        }
        ByteBuffer.wrap(key.bytes)
    }


    ByteBuffer encodeValue(byte[] value) {
        if(value == null){
            //Todo: exception
        }

        return ByteBuffer.wrap(value)
    }

    private static byte[] getBytes(ByteBuffer buffer) {
        byte[] b = new byte[buffer.remaining()]
        buffer.get(b)
        return b
    }

}
