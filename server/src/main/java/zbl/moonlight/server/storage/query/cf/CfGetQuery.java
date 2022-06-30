package zbl.moonlight.server.storage.query.cf;

import org.rocksdb.RocksDB;
import zbl.moonlight.server.storage.core.ResultSet;

import java.nio.channels.SelectionKey;

public class CfGetQuery extends CfQuery {
    protected CfGetQuery(SelectionKey selectionKey, byte[] columnFamily, byte[] key) {
        super(selectionKey, columnFamily, key);
    }

    @Override
    public void doQuery(RocksDB db, ResultSet resultSet) {

    }
}
