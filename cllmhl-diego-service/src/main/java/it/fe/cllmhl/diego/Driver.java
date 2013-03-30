package it.fe.cllmhl.diego;

import it.fe.cllmhl.util.BaseBean;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class Driver extends BaseBean{
    //template group
    private STGroup templateGroup;
    //template file name
    private String template;
    //name of the generated artifact
    private String fileName;
    //directory where we will generate the artifact relative to projectDirectory
    private String directory;

    public Driver(STGroup pTemplateGroup, String pTemplate, String pFileName, String pDirectory) {
        super();
        templateGroup = pTemplateGroup;
        template = pTemplate;
        fileName = pFileName;
        directory = pDirectory;
    }

    public ST getTemplate() {
        return templateGroup.getInstanceOf(template);
    }
    public String getFileNme() {
        return fileName;
    }
    public String getDirectory() {
        return directory;
    }
}
