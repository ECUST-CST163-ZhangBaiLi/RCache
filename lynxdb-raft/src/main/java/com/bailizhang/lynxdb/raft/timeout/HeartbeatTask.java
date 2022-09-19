package com.bailizhang.lynxdb.raft.timeout;

import com.bailizhang.lynxdb.core.timeout.TimeoutTask;
import com.bailizhang.lynxdb.raft.state.RaftState;

public class HeartbeatTask implements TimeoutTask {
    private final RaftState raftState;

    public HeartbeatTask() {
        raftState = RaftState.getInstance();
    }

    @Override
    public void execute() {
        if(raftState.isLeader()) {
            raftState.sendAppendEntries();
        }
    }
}
