package me.theyinspire.projects.grizzle.explorer.data;

import me.theyinspire.projects.grizzle.explorer.dto.Header;
import me.theyinspire.projects.grizzle.explorer.dto.ResultPage;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseService {

    private final EntityManager entityManager;
    private final Map<String, Class<?>> tables;
    private final Map<String, List<Attribute<?, ?>>> attributesMapping;
    private final Map<String, Integer> counts;

    public DatabaseService(final EntityManager entityManager) {
        this.entityManager = entityManager;
        final Metamodel metamodel = entityManager.getMetamodel();
        final Set<EntityType<?>> entities = metamodel.getEntities();
        tables = entities.stream().collect(Collectors.toMap(DatabaseService::getTableName, EntityType::getJavaType));
        attributesMapping = new HashMap<>();
        tables.keySet().forEach(tableName -> {
            final EntityType<?> entity = entityManager.getMetamodel().entity(tables.get(tableName));
            final List<Attribute<?, ?>> attributes = entity.getAttributes().stream()
                                                           .sorted(Comparator.comparing(Attribute::getName))
                                                           .collect(Collectors.toList());
            attributesMapping.put(tableName, attributes);
        });
        counts = new HashMap<>();
        for (String tableName : tableNames()) {
            getCount(tableName);
        }
    }

    public List<String> tableNames() {
        final Metamodel metamodel = entityManager.getMetamodel();
        final Set<EntityType<?>> entities = metamodel.getEntities();
        return entities.stream()
                       .map(DatabaseService::getTableName)
                       .collect(Collectors.toList());
    }

    public ResultPage fetch(String table, int page) {
        final int count = getCount(table);
        final int pageSize = 10;
        final int pages = (int) Math.ceil((double) count / (double) pageSize);
        final int actualPage = smoothPageNumber(page, pages);
        final String sqlString = getSelectStatement(table);
        final Query query = entityManager.createNativeQuery(sqlString);
        query.setFirstResult((actualPage - 1) * pageSize);
        query.setMaxResults(pageSize);
        //noinspection unchecked
        final List<Object[]> list = (List<Object[]>) query.getResultList();
        final List<List<Object>> values = list.stream().map(Arrays::asList).collect(Collectors.toList());
        return new ResultPage(count, actualPage, pageSize, values, pages, sqlString);
    }

    public ResultPage fetchById(String table, Object id) {
        final int count = 1;
        final int pageSize = 10;
        final int pages = (int) Math.ceil((double) count / (double) pageSize);
        final int actualPage = smoothPageNumber(1, pages);
        final String sqlString = getSelectStatementById(table);
        final Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter(1, id);
        query.setFirstResult((actualPage - 1) * pageSize);
        query.setMaxResults(pageSize);
        //noinspection unchecked
        final List<Object[]> list = (List<Object[]>) query.getResultList();
        final List<List<Object>> values = list.stream().map(Arrays::asList).collect(Collectors.toList());
        return new ResultPage(count, actualPage, pageSize, values, pages, sqlString);
    }

    private String getSelectStatement(final String table) {
        return "SELECT "
                + headers(table).stream().map(Header::getName).reduce((a, b) -> a + ", " + b).orElse("*")
                + " FROM "
                + table;
    }

    private String getSelectStatementById(final String table) {
        return "SELECT "
                + headers(table).stream().map(Header::getName).reduce((a, b) -> a + ", " + b).orElse("*")
                + " FROM "
                + table
                + " WHERE id = ?1";
    }

    public List<Header> headers(String table) {
        return attributesMapping.get(table)
                                .stream()
                                .map(attribute -> {
                                    final Header header = new Header();
                                    header.setName(getColumnName(attribute));
                                    header.setAssociation(attribute.isAssociation());
                                    return header;
                                })
                                .filter(header -> header.getName() != null)
                                .map(header -> header.setName(header.getName()
                                                                    .replaceAll("([a-z])([A-Z])", "$1_$2")
                                                                    .toLowerCase()))
                                .collect(Collectors.toList());
    }

    private String getColumnName(final Attribute<?, ?> attribute) {
        return attribute.isAssociation()
                ? (
                attribute.getPersistentAttributeType().name().endsWith("_ONE")
                        ? attribute.getName().concat("_id") : null)
                : attribute.getName();
    }

    private int smoothPageNumber(int page, final int pages) {
        if (page < 1) {
            page = 1;
        }
        if (page > pages) {
            page = pages;
        }
        return page;
    }

    private int getCount(final String table) {
        return counts.computeIfAbsent(table, s -> {
            final Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM " + table);
            return ((BigInteger) query.getSingleResult()).intValue();
        });
    }

    private static String getTableName(final EntityType<?> entity) {
        final String name;
        final Class<?> entityType = entity.getJavaType();
        if (entityType.isAnnotationPresent(Table.class)) {
            name = entityType.getAnnotation(Table.class).name();
        } else {
            name = entity.getName();
        }
        return name;
    }

}
