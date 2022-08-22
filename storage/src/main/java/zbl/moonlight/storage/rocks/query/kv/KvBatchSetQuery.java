package zbl.moonlight.storage.rocks.query.kv;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;
import zbl.moonlight.storage.core.Pair;
import zbl.moonlight.storage.core.ResultSet;
import zbl.moonlight.storage.rocks.query.Query;

import java.util.List;

public class KvBatchSetQuery extends Query<List<Pair<byte[], byte[]>>, Void> {
    public KvBatchSetQuery(List<Pair<byte[], byte[]>> queryData, ResultSet<Void> resultSet) {
        super(queryData, resultSet);
    }

    @Override
    public void doQuery(RocksDB db) throws RocksDBException {
        try (final WriteBatch writeBatch = new WriteBatch();
             final WriteOptions writeOptions = new WriteOptions()) {

            for(Pair<byte[], byte[]> pair : queryData) {
                writeBatch.put(pair.left(), pair.right());
            }

            db.write(writeOptions, writeBatch);
        }
    }
}
