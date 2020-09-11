package cl.blm.newmarketing.store.jpa.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductType is a Querydsl query type for ProductType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductType extends EntityPathBase<ProductType> {

    private static final long serialVersionUID = -1577047692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductType productType = new QProductType("productType");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final QProductFamily productFamily;

    public QProductType(String variable) {
        this(ProductType.class, forVariable(variable), INITS);
    }

    public QProductType(Path<? extends ProductType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductType(PathMetadata metadata, PathInits inits) {
        this(ProductType.class, metadata, inits);
    }

    public QProductType(Class<? extends ProductType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.productFamily = inits.isInitialized("productFamily") ? new QProductFamily(forProperty("productFamily")) : null;
    }

}

