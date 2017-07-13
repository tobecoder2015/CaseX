package com.autonavi.poi.domain.casex;

import com.autonavi.poi.core.ReflectionVisitor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Data
@Document
public abstract class BaseCase implements AcceptRunner {

    public void accept(ReflectionVisitor visitor) {
        visitor.visit(this);
    }
}
