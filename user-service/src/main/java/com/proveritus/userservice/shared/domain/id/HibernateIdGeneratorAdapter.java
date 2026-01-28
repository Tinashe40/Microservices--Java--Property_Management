package com.proveritus.userservice.shared.domain.id;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * Hibernate adapter for ID generation
 * Adapter Pattern: Adapts our ID generation to Hibernate's interface
 * Dependency Inversion: Depends on IdGenerator abstraction
 */
public class HibernateIdGeneratorAdapter implements IdentifierGenerator {

    private static final String PREFIX_PARAM = "prefix";
    private IdGenerator idGenerator;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        String prefix = params.getProperty(PREFIX_PARAM);

        // Get base generator from service registry or create default
        IdGenerator baseGenerator = new UlidGenerator();

        if (prefix != null && !prefix.isEmpty()) {
            this.idGenerator = new PrefixedIdGenerator(baseGenerator, prefix);
        } else {
            this.idGenerator = baseGenerator;
        }
    }

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session,
            Object object) throws HibernateException {
        return idGenerator.generate();
    }
}
