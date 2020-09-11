package cl.blm.newmarketing.store.jpa.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellType is a Querydsl query type for SellType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSellType extends EntityPathBase<SellType> {

    private static final long serialVersionUID = 836446433L;

    public static final QSellType sellType = new QSellType("sellType");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QSellType(String variable) {
        super(SellType.class, forVariable(variable));
    }

    public QSellType(Path<? extends SellType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellType(PathMetadata metadata) {
        super(SellType.class, metadata);
    }

}

