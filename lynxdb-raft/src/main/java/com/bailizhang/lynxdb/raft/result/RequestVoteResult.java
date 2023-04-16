package com.bailizhang.lynxdb.raft.result;

import com.bailizhang.lynxdb.core.common.BytesList;
import com.bailizhang.lynxdb.core.common.BytesListConvertible;

import java.nio.ByteBuffer;

import static com.bailizhang.lynxdb.ldtp.request.RaftRpc.REQUEST_VOTE;

public record RequestVoteResult(
        int term,
        byte voteGranted
) implements BytesListConvertible {
    @Override
    public BytesList toBytesList() {
        BytesList bytesList = new BytesList();

        bytesList.appendRawByte(REQUEST_VOTE);
        bytesList.appendRawInt(term);
        bytesList.appendRawByte(voteGranted);

        return bytesList;
    }

    public static RequestVoteResult from(ByteBuffer buffer) {
        int term = buffer.getInt();
        byte voteGranted = buffer.get();

        return new RequestVoteResult(term, voteGranted);
    }
}
