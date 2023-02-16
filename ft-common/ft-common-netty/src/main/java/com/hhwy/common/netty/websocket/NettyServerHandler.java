package com.hhwy.common.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

//这里 TextWebSocketFrame 类型，表示一个文本帧(frame)
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //定义channle组，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // group 就是充当业务线程池，可以将任务提交到该线程池 指定创建了16个线程
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息 " + msg.text());
        //回复消息
        channelGroup.writeAndFlush(new TextWebSocketFrame("channelRead---" + LocalDateTime.now() + " " + msg.text()+" "+ctx.channel().id().asLongText()));

        //将任务提交到 group线程池
        Future<Object> submit = group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                //模拟耗时5秒的操作
                Thread.sleep(5 * 1000);
                System.out.println("group.submit 的  call 线程是=" + Thread.currentThread().getName());
                channelGroup.writeAndFlush(new TextWebSocketFrame("call---" + LocalDateTime.now() + " " + ctx.channel().id().asLongText()));
                return null;
            }
        });

        /*ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    //输出线程名
                    System.out.println("EchoServerHandler execute 线程是=" + Thread.currentThread().getName());
                    channelGroup.writeAndFlush(new TextWebSocketFrame("channelRead2" + LocalDateTime.now() + " " + ctx.channel().id().asLongText()));
                } catch (Exception ex) {
                    System.out.println("发生异常" + ex.getMessage());
                }
            }
        });*/

    }

    //当web客户端连接后， 触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一
        Channel channel = ctx.channel();
        System.out.println("handlerAdded 被调用" + channel.id().asLongText());
        System.out.println("handlerAdded 被调用" + channel.id().asShortText());
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 " + cause.getMessage());
        ctx.close(); //关闭连接
    }

    public static class Ra implements Runnable{
        @Override
        public void run() {
            while (true){
                if (channelGroup.size()>0) {
                    channelGroup.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now()+""));
                }
                try {
                    Thread.sleep(3 * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
