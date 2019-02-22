package com.petric.netty.example.sixth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);

        MyDataInfo.MyMessage myMessage;
        if (random == 0) {
            MyDataInfo.Person person = MyDataInfo.Person.newBuilder()
                    .setName("李世民").setAge(20).setAddress("北京")
                    .build();

            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.PERSON_TYPE).setPerson(person)
                    .build();

        } else if (random == 1) {
            MyDataInfo.Dog dog = MyDataInfo.Dog.newBuilder()
                    .setName("小旺").setAge(3)
                    .build();
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.DOG_TYPE).setDog(dog)
                    .build();

        } else {
            MyDataInfo.Cat cat = MyDataInfo.Cat.newBuilder()
                    .setName("小眯").setCity("北京")
                    .build();
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.CAT_TYPE).setCat(cat)
                    .build();
        }

        ctx.channel().writeAndFlush(myMessage);
    }
}
