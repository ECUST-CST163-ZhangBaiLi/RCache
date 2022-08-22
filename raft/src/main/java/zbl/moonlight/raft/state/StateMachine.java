package zbl.moonlight.raft.state;

import zbl.moonlight.raft.log.RaftLogEntry;
import zbl.moonlight.raft.server.RaftServer;
import zbl.moonlight.socket.client.ServerNode;

import java.util.List;

/**
 * Raft 定义的状态机的接口
 */
public interface StateMachine {

    /**
     * 获取当前集群的所有节点：
     *  如果有 C_OLD_NEW，则返回 C_OLD_NEW
     *  如果没有 C_OLD_NEW，则返回 C
     *
     * @return 当前集群的所有节点
     */
    List<ServerNode> clusterNodes();

    /**
     * 应用日志条目
     * @param entries 日志条目
     */
    void apply(RaftLogEntry[] entries);

    void raftServer(RaftServer server);
}
