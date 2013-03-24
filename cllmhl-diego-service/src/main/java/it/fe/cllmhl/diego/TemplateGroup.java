package it.fe.cllmhl.diego;

import it.fe.cllmhl.Installation;

import java.io.File;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.STGroupFile;

public enum TemplateGroup {
    DIRECTIVE("directive.stg", null), 
    JAVA(null, "java"), 
    JSP(null, "jsp");

    String TEMPLATE_HOME = Installation.getHomeDirectory().concat(File.separator).concat("template").concat(File.separator);

    private STGroup group;

    TemplateGroup(String fileName, String directory) {
        if (fileName != null) {
            this.group = new STGroupFile(TEMPLATE_HOME.concat(fileName));
        } else {
            this.group = new STGroupDir(TEMPLATE_HOME.concat(directory));
        }
    }

    public ST getInstanceOf(String name) {
        return group.getInstanceOf(name);
    }
}
