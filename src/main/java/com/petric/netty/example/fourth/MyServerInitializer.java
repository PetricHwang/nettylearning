package com.petric.netty.example.fourth;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS)
        // 5秒内客户端没向服务器端发送数据（服务器端没读到任何数据）, 服务器端没向客户端在7秒内端发送数据, 10秒内没检测到读和写
        pipeline.addLast("idleStateHandler", new IdleStateHandler(5, 7, 3, TimeUnit.SECONDS));
        pipeline.addLast("myServerHandler", new MyServerHandler());
    }
}
