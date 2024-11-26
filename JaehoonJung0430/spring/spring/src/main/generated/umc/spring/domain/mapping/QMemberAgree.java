package umc.spring.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberAgree is a Querydsl query type for MemberAgree
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAgree extends EntityPathBase<MemberAgree> {

    private static final long serialVersionUID = -2051556634L;

    public static final QMemberAgree memberAgree = new QMemberAgree("memberAgree");

    public final umc.spring.domain.common.QBaseEntity _super = new umc.spring.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberAgree(String variable) {
        super(MemberAgree.class, forVariable(variable));
    }

    public QMemberAgree(Path<? extends MemberAgree> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberAgree(PathMetadata metadata) {
        super(MemberAgree.class, metadata);
    }

}

