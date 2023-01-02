package com.bailizhang.lynxdb.socket.server;

public class SocketServerConfig {
    private static final int DEFAULT_CORE_SIZE = 10;
    private static final int DEFAULT_MAX_POOL_SIZE = 20;
    private static final int DEFAULT_KEEP_ALIVE_TIME= 60;
    private static final int DEFAULT_BLOCKING_QUEUE_SIZE = 200;
    private static final String DEFAULT_IO_THREAD_NAME_PREFIX = "Default-IO-Thread-";
    private static final int DEFAULT_BACKLOG = 20;
    private static final int DEFAULT_MESSAGE_PORT = 7263;

    /** IO线程池的核心池大小 */
    private int coreSize = DEFAULT_CORE_SIZE;
    /** IO线程池的最大池大小 */
    private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    /** IO线程池的线程最大存活时间 */
    private int keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    /** IO线程池的阻塞队列大小 */
    private int blockingQueueSize = DEFAULT_BLOCKING_QUEUE_SIZE;
    /** IO线程名称的前缀 */
    private String ioThreadNamePrefix = DEFAULT_IO_THREAD_NAME_PREFIX;
    /** 服务器的端口号 */
    private final int port;
    /** 服务器的最大连接数 */
    private int backlog = DEFAULT_BACKLOG;

    private int messagePort = DEFAULT_MESSAGE_PORT;

    public SocketServerConfig(int port) {
        this.port = port;
    }

    public int coreSize() {
        return coreSize;
    }

    public SocketServerConfig coreSize(int coreSize) {
        this.coreSize = coreSize;
        return this;
    }

    public int maxPoolSize() {
        return maxPoolSize;
    }

    public SocketServerConfig maxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public int keepAliveTime() {
        return keepAliveTime;
    }

    public SocketServerConfig keepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public int blockingQueueSize() {
        return blockingQueueSize;
    }

    public SocketServerConfig blockingQueueSize(int blockingQueueSize) {
        this.blockingQueueSize = blockingQueueSize;
        return this;
    }

    public String ioThreadNamePrefix() {
        return ioThreadNamePrefix;
    }

    public SocketServerConfig ioThreadNamePrefix(String ioThreadNamePrefix) {
        this.ioThreadNamePrefix = ioThreadNamePrefix;
        return this;
    }

    public int port() {
        return port;
    }


    public int backlog() {
        return backlog;
    }

    public SocketServerConfig backlog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    public int messagePort() {
        return messagePort;
    }

    public SocketServerConfig messagePort(int messagePort) {
        this.messagePort = messagePort;
        return this;
    }
}
