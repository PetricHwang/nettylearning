package com.petric.netty.example.third;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 保存所有与服务器端已经建立好连接的channel对象
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息：" + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });
    }

    /**
     * 客户端与服务器端建立了连接
     *
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 广播告诉其他客户端，xx已经连接了
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 加入\n");
        channelGroup.add(channel);
    }

    /**
     * 连接断开
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 广播告诉其他客户端，xx已经断开连接了
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 离开\n");
//        channelGroup.remove(channel); // netty会自动将断开的客户端移除，无需手工调用它，所有该行代码不用调用

        System.out.println(channelGroup.size());
    }

    /**
     * 连接处于活动状态
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线");
    }

    /**
     * 连接非活动状态
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
        ctx.close();
    }
}
