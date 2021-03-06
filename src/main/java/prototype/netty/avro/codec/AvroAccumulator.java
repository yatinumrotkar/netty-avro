package prototype.netty.avro.codec;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

@ChannelPipelineCoverage("one")
public class AvroAccumulator extends OneToOneDecoder {

    private static final List<ByteBuffer> NO_OP = Collections.emptyList();
    private static final Logger logger = Logger.getLogger(AvroAccumulator.class.getName());
    private List<ByteBuffer> request = new ArrayList<ByteBuffer>();

    @Override
    protected Object decode(ChannelHandlerContext context, Channel channel, Object message) throws Exception {
        if (!(message instanceof ByteBuffer)) {
            return message;
        }

        ByteBuffer buffer = (ByteBuffer)message;
        int length = buffer.limit();

        if (length > 0) {
            request.add(buffer);
        }

        logger.log(Level.FINE, format("buffer length: %s%s", length, length == 0 ? " [terminus]" : ""));

        return length == 0 ? request : NO_OP;
    }
}