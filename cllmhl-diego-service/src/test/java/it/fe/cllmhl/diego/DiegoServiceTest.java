package it.fe.cllmhl.diego;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class DiegoServiceTest {

    @Test
    public void testGetDatabases() {
       DiegoService.getDatabases();
    }

    @Test
    public void testGetMetadata() {
        List<DiegoDatabase> lDatabases = DiegoService.getDatabases();
        for (DiegoDatabase lDiegoDatabase : lDatabases) {
            CodeGeneratorBean lCodeGeneratorBean = new CodeGeneratorBean();
            lCodeGeneratorBean.setConnection(lDiegoDatabase.getDatasource());
            DiegoDatabase lDBMetadata = DiegoService.getMetadata(lCodeGeneratorBean);
            Assert.assertNotNull(lDBMetadata);
        }
    }

}
