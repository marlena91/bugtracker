```properties

# SPRING_CONFIG_LOCATION=C:\spring\spring-security-demo.properties
spring.datasource.url=jdbc:postgresql://localhost:5436/wsb_bugtracker
spring.datasource.username=wsb
spring.datasource.password=secret

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

spring.messages.basename=languages/messages
spring.messages.encoding=UTF-8
spring.messages.fallback-to-system-locale=true