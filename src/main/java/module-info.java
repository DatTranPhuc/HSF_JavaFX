module javafx.demojavafxs3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires com.microsoft.sqlserver.jdbc;

    // Open packages for reflective access
    opens javafx.demojavafxs3 to javafx.fxml, spring.core, spring.beans, spring.context;
    opens javafx.demojavafxs3.controller to javafx.fxml, spring.core, spring.beans, spring.context;
    opens javafx.demojavafxs3.entity to org.hibernate.orm.core, spring.core;
    opens javafx.demojavafxs3.repository to spring.core, spring.beans, spring.context;
    opens javafx.demojavafxs3.service to spring.core, spring.beans, spring.context;


    exports javafx.demojavafxs3;
}