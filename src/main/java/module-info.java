module home.javafx{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires javafx.graphics;

    requires java.sql;
    requires java.prefs;
    requires webcam.capture;
    requires io;
    requires kernel;
    requires layout;
    requires barcode4j;
    requires java.desktop;
    requires poi.ooxml;
    requires poi.ooxml.schemas;

    opens home to javafx.fxml;
    opens home.Models to javafx.fxml;
    //opens home.Models.MemberSearchModel to javafx.fxml;
    exports home;
    exports home.Models;
}