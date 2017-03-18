package com.baturu.simpleDemo.rpc.exampleImpl;

/**
 * Created by xuran on 16/4/16.
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> I am ok." : " I am ok.";
    }
}
