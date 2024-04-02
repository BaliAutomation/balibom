module ac.bali.bom {
    exports ac.bali.bom;
    exports ac.bali.bom.bootstrap;
    exports ac.bali.bom.connectivity;
    exports ac.bali.bom.inventory;
    exports ac.bali.bom.lcsc;
    exports ac.bali.bom.order;
    exports ac.bali.bom.parts;
    exports ac.bali.bom.products;
    exports ac.bali.bom.ui;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.json;
    requires org.apache.polygene.core.api;
    requires org.apache.polygene.core.bootstrap;
    requires java.desktop;
    requires org.apache.polygene.core.spi;
    requires org.apache.polygene.extension.entitystore.file;
    requires org.apache.polygene.extension.indexing.rdf;
    requires org.apache.polygene.extension.entitystore.memory;
    requires org.apache.polygene.library.fileconfig;
    requires org.apache.polygene.library.uowfile;
    requires org.apache.polygene.library.rdf;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    opens ac.bali.bom.bootstrap to org.apache.polygene.core.bootstrap;
    opens ac.bali.bom to org.apache.polygene.core.api, javafx.fxml;
    opens ac.bali.bom.connectivity to org.apache.polygene.core.api;
    opens ac.bali.bom.inventory to org.apache.polygene.core.api;
    opens ac.bali.bom.lcsc to org.apache.polygene.core.api;
    opens ac.bali.bom.order to org.apache.polygene.core.api;
    opens ac.bali.bom.parts to org.apache.polygene.core.api;
    opens ac.bali.bom.products to org.apache.polygene.core.api;
    opens ac.bali.bom.ui to org.apache.polygene.core.api, javafx.fxml;
    exports ac.bali.bom.support;
    opens ac.bali.bom.support to javafx.fxml, org.apache.polygene.core.api;
}