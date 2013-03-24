package it.fe.cllmhl.diego;

import it.fe.cllmhl.Installation;

import java.io.File;

public enum Template {
    DIRECTIVE("directive.stg"),
    BEAN("java".concat(File.separator).concat("bean.st"));

    File file;

    Template(String fileName) {
        this.file = new File(Installation.getHomeDirectory().concat(File.separator).concat("template").concat(File.separator).concat(fileName));
    }
}
