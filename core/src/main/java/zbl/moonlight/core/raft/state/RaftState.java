package zbl.moonlight.core.raft.state;

import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zbl.moonlight.core.raft.log.RaftLog;
import zbl.moonlight.core.raft.log.TermLog;
import zbl.moonlight.core.raft.request.Entry;
import zbl.moonlight.core.socket.client.ServerNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RaftState {
    private static final Logger logger = LogManager.getLogger("RaftState");

    private static final int HEARTBEAT_INTERVAL_MILLIS = 100;
    private static final int ELECTION_MIN_INTERVAL_MILLIS = 150;
    private static final int ELECTION_MAX_INTERVAL_MILLIS = 300;

    private final int ELECTION_INTERVAL_MILLIS;

    public RaftState(Appliable appliable, ServerNode current, List<ServerNode> nodes,
                     String logFilenamePrefix)
            throws IOException {
        stateMachine = appliable;
        currentNode = current;
        allNodes = nodes;
        otherNodes = allNodes.stream().filter((node) -> !node.equals(currentNode))
                .toList();
        raftLog = new RaftLog(logFilenamePrefix + "_index.log",
                logFilenamePrefix + "_data.log");
        termLog = new TermLog(logFilenamePrefix + "_term.log");

        /* 设置随机的选举超时时间 */
        ELECTION_INTERVAL_MILLIS = ((int) (Math.random() *
                (ELECTION_MAX_INTERVAL_MILLIS - ELECTION_MIN_INTERVAL_MILLIS)))
                + ELECTION_MIN_INTERVAL_MILLIS;
        logger.info("[{}] -- ELECTION_INTERVAL_MILLIS is {}.", currentNode, ELECTION_INTERVAL_MILLIS);
    }

    private volatile long heartbeatTimeMillis = System.currentTimeMillis();
    private volatile long electionTimeMillis = System.currentTimeMillis();
    public void resetHeartbeatTime() {
        heartbeatTimeMillis = System.currentTimeMillis();
    }
    public void resetElectionTime() {
        electionTimeMillis = System.currentTimeMillis();
    }
    public boolean isHeartbeatTimeout() {
        return System.currentTimeMillis() - heartbeatTimeMillis
                > HEARTBEAT_INTERVAL_MILLIS;
    }
    public boolean isElectionTimeout() {
        return System.currentTimeMillis() - electionTimeMillis
                > ELECTION_INTERVAL_MILLIS;
    }

    private final List<ServerNode> allNodes;
    /**
     * Raft 集群中的其他节点
     */
    private final List<ServerNode> otherNodes;

    /**
     * 返回 Raft 集群中的其他节点
     * @return 集群中的其他节点
     */
    public List<ServerNode> otherNodes() {
        return otherNodes;
    }
    private final HashSet<ServerNode> votedNodes = new HashSet<>();
    public void setVotedNodeAndCheck(ServerNode serverNode) {
        synchronized (votedNodes) {
            votedNodes.add(serverNode);
            if(votedNodes.size() > (allNodes.size() >> 1)) {
                raftRole = RaftRole.Leader;
                /* 获取所有的 follower 节点 */
                List<ServerNode> followers =  allNodes.stream()
                        .filter((node) -> !node.equals(currentNode)).toList();
                nextIndex.clear();
                matchedIndex.clear();
                /* 初始化 leader 的相关属性 */
                int lastEntryIndex = lastEntryIndex();
                for (ServerNode node : followers) {
                    nextIndex.put(node, lastEntryIndex + 1);
                    matchedIndex.put(node, 0);
                }

                logger.info("{} node get most vote and become [Leader].", currentNode);
            }
        }
    }

    private final ServerNode currentNode;
    public ServerNode currentNode() {
        return currentNode;
    }

    @Setter
    private volatile ServerNode leaderNode;
    public ServerNode leaderNode() {
        return leaderNode;
    }

    @Setter
    private volatile RaftRole raftRole = RaftRole.Follower;
    public RaftRole raftRole() {
        return raftRole;
    }

    /**
     * Raft 日志
     */
    private final RaftLog raftLog;

    /**
     * 返回 raft 日志的最后一个条目
     * @return 最后一个条目
     * @throws IOException IO异常
     */
    public Entry lastEntry() throws IOException {
        return raftLog.lastEntry();
    }

    /**
     * 通过索引值获取该索引值处的日志条目
     * @param index 索引值
     * @return 日志条目
     * @throws IOException IO异常
     */
    public Entry getEntryByIndex(int index) throws IOException {
        return raftLog.getEntryByIndex(index);
    }

    /**
     * 设置最大的有效日志索引值
     * @param index 索引值
     * @throws IOException IO异常
     */
    public void setMaxIndex(int index) throws IOException {
        raftLog.setMaxIndex(index);
    }

    /**
     * 将日志条目添加到日志的尾部
     * @param entry 日志条目
     * @throws IOException IO异常
     */
    public void append(Entry entry) throws IOException {
        raftLog.append(entry);
    }
    public void append(Entry[] entries) throws IOException {
        raftLog.append(entries);
    }
    public Entry[] getEntriesByRange(int begin, int end) throws IOException {
        return raftLog.getEntriesByRange(begin, end);
    }
    public int lastEntryIndex() {
        return 0;
    }

    /**
     * 用来记录当前任期和投票给的节点
     */
    private final TermLog termLog;

    /**
     * 返回当前的任期号
     * @return 当前任期号
     */
    public int currentTerm() throws IOException {
        return termLog.currentTerm();
    }

    /**
     * 设置当前任期号
     * @param term 任期号
     */
    public void setCurrentTerm(int term) throws IOException {
        termLog.setCurrentTerm(term);
    }

    /**
     * 获取投票给的节点
     * @return 投票给的节点
     */
    public ServerNode voteFor() throws IOException {
        return termLog.voteFor();
    }

    public void setVoteFor(ServerNode node) throws IOException {
        termLog.setVoteFor(node);
    }

    /**
     * 已提交的日志索引
     */
    private final AtomicInteger commitIndex = new AtomicInteger(0);

    /**
     * 应用到状态机的日志索引
     */
    private final AtomicInteger lastApplied = new AtomicInteger(0);

    public int commitIndex() {
        return commitIndex.get();
    }
    public void setCommitIndex(int index) {
        commitIndex.set(index);
    }
    public int lastApplied() {
        return lastApplied.get();
    }

    private final ConcurrentHashMap<ServerNode, Integer> nextIndex
            = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ServerNode, Integer> matchedIndex
            = new ConcurrentHashMap<>();
    public ConcurrentHashMap<ServerNode, Integer> nextIndex() {
        return nextIndex;
    }
    public ConcurrentHashMap<ServerNode, Integer> matchedIndex() {
        return matchedIndex;
    }

    /**
     * 检查 commitIndex，如果可以增加，则增加 commitIndex
     */
    public void checkCommitIndex() {
        int n = (allNodes.size() >> 1) + 1;
        int lastEntryIndex = lastEntryIndex();
        for(int i = commitIndex.get() + 1; i < lastEntryIndex; i ++) {
            int count  = 0;
            for(ServerNode node : matchedIndex.keySet()) {
                if(matchedIndex.get(node) > i) {
                    count ++;
                }
            }
            if(count >= n) {
                commitIndex.set(i);
            }
        }
    }

    private final Appliable stateMachine;

    /**
     * 将日志条目应用到状态机
     * @param entries 日志条目
     */
    public void apply(Entry[] entries) {
        stateMachine.apply(entries);
    }
}
