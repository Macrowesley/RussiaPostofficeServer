//package cc.mrbird.febs.rcs.dao;
//import cc.mrbird.febs.rcs.entity.TransactionMsg;
//import com.alibaba.fastjson.JSON;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
//import static org.springframework.data.mongodb.core.query.Criteria.where;
//import static org.springframework.data.mongodb.core.query.Query.query;
//
//@Component
//public class TransactionMsgDao {
//    @Autowired
//    private MongoOperations mongo;
//    /**
//     * 查询下一个id
//     * @param collectionName 集合名
//     * @return
//     */
//    public Long getNextSequence(String collectionName) {
//        System.out.println("collectionName"+collectionName);
//        TransactionMsg counter = mongo.findAndModify(
////                query(where("_id").is(collectionName)),
//                query(where("_id").is("62456591e06b9c16926fd602")),
//                new Update().inc("seq", 1),
//                options().returnNew(true).upsert(true),
//                TransactionMsg.class);
//        System.out.println("Counter:"+JSON.toJSONString(counter));
////        return counter.getMongoId();
//        return counter.getSeq();
//    }
//}
