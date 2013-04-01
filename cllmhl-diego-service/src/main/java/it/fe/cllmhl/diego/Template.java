package it.fe.cllmhl.diego;

import it.fe.cllmhl.Installation;

import java.io.File;

public enum Template {
    PROJECT("project.stg"),
    DRIVER("driver.stg"),
    ROOT_PACKAGE("rootPackage"),
    POJECT_DIRECTORY("projectDirectory"),
    SUB_DIRECTORIES("subDirectories");

    public static String TEMPLATE_HOME = Installation.getHomeDirectory().concat("diego").concat(File.separator);
    public static Character TEMPLATE_DELIMITER_CHAR = '$';

    String name;
    Template(String name) {
        this.name=name;
    }
}
