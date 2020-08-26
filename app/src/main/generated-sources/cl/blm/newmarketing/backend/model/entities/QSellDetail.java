package cl.blm.newmarketing.backend.model.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSellDetail is a Querydsl query type for SellDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSellDetail extends EntityPathBase<SellDetail> {

    private static final long serialVersionUID = -2142629959L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSellDetail sellDetail = new QSellDetail("sellDetail");

    public final QProduct product;

    public final QSell sell;

    public final NumberPath<Integer> sellDetailId = createNumber("sellDetailId", Integer.class);

    public final NumberPath<Integer> sellDetailUnits = createNumber("sellDetailUnits", Integer.class);

    public QSellDetail(String variable) {
        this(SellDetail.class, forVariable(variable), INITS);
    }

    public QSellDetail(Path<? extends SellDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSellDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSellDetail(PathMetadata metadata, PathInits inits) {
        this(SellDetail.class, metadata, inits);
    }

    public QSellDetail(Class<? extends SellDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
        this.sell = inits.isInitialized("sell") ? new QSell(forProperty("sell"), inits.get("sell")) : null;
    }

}

