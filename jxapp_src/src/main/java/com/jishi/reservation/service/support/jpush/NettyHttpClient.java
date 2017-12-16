package com.jishi.reservation.service.support.jpush;

/**
 * Created by liangxiong on 2017/11/24.
 */
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.connection.HttpProxy;
import cn.jiguang.common.connection.HttpResponseHandler;
import cn.jiguang.common.resp.ResponseWrapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import javax.net.ssl.SSLException;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpClient{
    private static Logger LOG = LoggerFactory.getLogger(NettyHttpClient.class);
    private String _authCode;
    private int _maxRetryTimes;
    private int _readTimeout;
    private Channel _channel;
    private ChannelFuture _channelFuture;
    private Bootstrap b;
    private EventLoopGroup _workerGroup;
    private SslContext _sslCtx;
    private URI _uri;
    private int port = -1;

    public NettyHttpClient(String authCode, HttpProxy proxy, ClientConfig config, URI uri, NettyHttpClient.BaseCallback callback) {
        this._maxRetryTimes = config.getMaxRetryTimes().intValue();
        this._readTimeout = config.getReadTimeout().intValue();
        String message = MessageFormat.format("Created instance with connectionTimeout {0}, readTimeout {1}, maxRetryTimes {2}, SSL Version {3}", config.getConnectionTimeout(), this._readTimeout, this._maxRetryTimes, config.getSSLVersion());
        LOG.info(message);
        this._authCode = authCode;

        try {
            this._sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            this._workerGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this._workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //.handler(new NettyClientInitializer(this._sslCtx, callback, (CountDownLatch)null));
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ChannelHandler[]{_sslCtx.newHandler(socketChannel.alloc()), new HttpClientCodec(), new PushHttpResponseHandler(callback, (CountDownLatch)null)});
                        }
                    });
            this.b = bootstrap;

            this._uri = uri;
            String scheme = _uri.getScheme() == null ? "http" : _uri.getScheme();
            port = _uri.getPort();
            if (port == -1) {
                if ("http".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("https".equalsIgnoreCase(scheme)) {
                    port = 443;
                }
            }

        } catch (SSLException var6) {
            var6.printStackTrace();
        }

    }

    private void comfirmConnected() {
        if (this._channel == null || !this._channel.isOpen() || !this._channel.isActive() || !this._channel.isWritable()) {
            _channelFuture = this.b.connect(_uri.getHost(), port).syncUninterruptibly();
            _channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {

                }
            });
            this._channel = _channelFuture.channel();
        }
    }

    public void sendRequest(HttpMethod method, String content) {

        comfirmConnected();

        DefaultFullHttpRequest request;
        if (null != content) {
          ByteBuf byteBuf = Unpooled.copiedBuffer(content.getBytes(CharsetUtil.UTF_8));
          request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, _uri.getRawPath(), byteBuf);
          request.headers().set(HttpHeaderNames.CONTENT_LENGTH, (long)byteBuf.readableBytes());
        } else {
          request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, _uri.getRawPath());
        }

        request.headers().set(HttpHeaderNames.HOST, _uri.getHost());
        request.headers().set(HttpHeaderNames.AUTHORIZATION, this._authCode);
        request.headers().set("Content-Type", "application/json;charset=utf-8");
        LOG.info("Sending request. " + request);
        LOG.info("Send body: " + content);

        ChannelFuture writeCF = this._channel.writeAndFlush(request);
        System.out.println(writeCF);

        writeCF.addListener(new GenericFutureListener<Future<? super Void>>() {
          @Override
          public void operationComplete(Future<? super Void> future) throws Exception {
              if (!future.isSuccess() && future.cause() != null) {
                future.cause().printStackTrace();
              }
          }
        });

    }

    public void close() {
        if (null != this._channel) {
            this._channel.closeFuture().syncUninterruptibly();
            this._workerGroup.shutdownGracefully();
            this._channel = null;
            this._workerGroup = null;
        }

        System.out.println("Finished request(s) " + new Date());
    }

    public interface BaseCallback extends cn.jiguang.common.connection.NettyHttpClient.BaseCallback {
        void onSucceed(ResponseWrapper var1);
    }

    public static class PushHttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

        private static final Logger LOG = LoggerFactory.getLogger(PushHttpResponseHandler.class);
        private int status;
        private BaseCallback _callback;
        private CountDownLatch _latch;

        public PushHttpResponseHandler(BaseCallback callback, CountDownLatch latch) {
            this._callback = callback;
            this._latch = latch;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            //LOG.info("#0 " + status + "  " + msg.toString());
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse)msg;
                this.status = response.status().code();
                LOG.info("#1 HttpResponse" + status + "  " + response.toString());response.decoderResult().toString();
            }

            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent)msg;
                LOG.info("#2 HttpContent" + content.content().toString());/*
                if (content instanceof LastHttpContent) {
                  LOG.info("#3 HttpContent" + "closing connection");
                  //ctx.close();
                } else {
                  String responseContent = content.content().toString(CharsetUtil.UTF_8);
                  if (null != this._latch) {
                    this._latch.countDown();
                  }

                  if (null != this._callback) {
                    LOG.info("#4 HttpContent" + responseContent);
                    ResponseWrapper responseWrapper = new ResponseWrapper();
                    responseWrapper.responseCode = this.status;
                    responseWrapper.responseContent = responseContent;
                    this._callback.onSucceed(responseWrapper);
                  }
                }*/
            }
        }
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOG.error("error:", cause);

            try {
                ctx.close();
                if (null != this._latch) {
                    this._latch.countDown();
                }
            } catch (Exception var4) {
                LOG.error("close error:", var4);
            }

        }

        public void resetLatch(CountDownLatch latch) {
          this._latch = latch;
        }
    }
}
