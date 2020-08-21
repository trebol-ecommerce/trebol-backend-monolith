package cl.blm.newmarketing.backend.model.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductFamily is a Querydsl query type for ProductFamily
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductFamily extends EntityPathBase<ProductFamily> {

    private static final long serialVersionUID = 1112532989L;

    public static final QProductFamily productFamily = new QProductFamily("productFamily");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QProductFamily(String variable) {
        super(ProductFamily.class, forVariable(variable));
    }

    public QProductFamily(Path<? extends ProductFamily> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductFamily(PathMetadata metadata) {
        super(ProductFamily.class, metadata);
    }

}

