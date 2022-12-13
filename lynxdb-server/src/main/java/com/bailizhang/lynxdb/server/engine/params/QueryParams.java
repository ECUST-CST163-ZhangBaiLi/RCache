package com.bailizhang.lynxdb.server.engine.params;

import com.bailizhang.lynxdb.core.utils.BufferUtils;

import java.nio.ByteBuffer;

public record QueryParams (byte method, byte[] content) {
    public static QueryParams parse(ByteBuffer buffer) {
        byte method = buffer.get();
        byte[] content = BufferUtils.getRemaining(buffer);
        return new QueryParams(method, content);
    }
}
