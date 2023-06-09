package pe.com.centro.utils;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.User;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utils for {@code multipart/form-data} parsing
 */
@Slf4j
public final class Parser {
    public static <T> void setProperty(T bean, String fieldName, String fieldValue) {
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                if (fieldName.equals(descriptor.getName())) {
                    Class<?> type = descriptor.getPropertyType();
                    Object value = null;
                    if (fieldValue != null) {
                        if (type.isEnum()) {
                            value = Enum.valueOf(type.asSubclass(Enum.class), fieldValue);
                        } else if (type.equals(LocalDate.class)) {
                            value = LocalDate.parse(fieldValue);
                        } else if (type.equals(Instant.class)) {
                            value = Instant.parse(fieldValue);
                        } else if (type.equals(Long.class) || (type.isPrimitive() && type.getName().equals("long"))) {
                            value = Long.parseLong(fieldValue);
                        } else if (type.equals(Integer.class) || (type.isPrimitive() && type.getName().equals("int"))) {
                            value = Integer.parseInt(fieldValue);
                        } else if (type.equals(Boolean.class) || (type.isPrimitive() && type.getName().equals("boolean"))) {
                            value = Boolean.valueOf(fieldValue);
                        } else if (type.equals(List.class) || type.equals(Map.class) || type.equals(Set.class)) {
                            value = Serializer.deserialize(fieldValue, type);
                        } else {
                            value = fieldValue;
                        }
                    }
                    descriptor.getWriteMethod().invoke(bean, value);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            log.error("Error parsing " + bean.getClass().getName() + ", field: " + fieldName, e);
        }
    }

    public static String parseEmails(List<User> users) {
        StringBuilder sb = new StringBuilder();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().getEmail());
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static LocalDateTime now() {
        Instant nowUtc = Instant.now();
        ZoneId asiaLima = ZoneId.of("America/Lima");
        ZonedDateTime nowAsiaLima = ZonedDateTime.ofInstant(nowUtc, asiaLima);
        return nowAsiaLima.toLocalDateTime();
    }

    public static String formatDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    public static LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
    }
}
