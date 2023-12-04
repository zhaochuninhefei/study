package com.czhao.test.jdk18;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.spi.InetAddressResolver;
import java.net.spi.InetAddressResolverProvider;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author zhaochun
 */
public class TestInetAddressResolverProvider extends InetAddressResolverProvider {

    @Override
    public InetAddressResolver get(Configuration configuration) {
        return new TestInetAddressResolver(configuration.builtinResolver());
    }

    @Override
    public String name() {
        return "test";
    }

    private record TestInetAddressResolver(InetAddressResolver builtinResolver) implements InetAddressResolver {

        @Override
        public Stream<InetAddress> lookupByName(String host,
                                                LookupPolicy lookupPolicy) throws UnknownHostException {
            System.out.println("lookupByName in TestInetAddressResolver");
            if (Objects.equals(host, "justtest.com")) {
                System.out.println("find justtest.com");
                return Stream.of(InetAddress.getLocalHost());
            }
            return this.builtinResolver.lookupByName(host, lookupPolicy);
        }

        @Override
        public String lookupByAddress(byte[] addr) throws UnknownHostException {
            System.out.println("lookupByAddress in TestInetAddressResolver");
            return this.builtinResolver.lookupByAddress(addr);
        }
    }
}
