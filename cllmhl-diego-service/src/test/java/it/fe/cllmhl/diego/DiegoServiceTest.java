package it.fe.cllmhl.diego;

import java.util.List;

import junit.framework.Assert;
import junitx.util.PrivateAccessor;

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

    @Test
    public void testGenerateAll() {
        CodeGeneratorBean lCodeGeneratorBean = new CodeGeneratorBean();
        lCodeGeneratorBean.setConnection("POOL");
        DiegoDatabase lDBMetadata = DiegoService.getMetadata(lCodeGeneratorBean);
        DiegoService.generate(lDBMetadata);
    }

    @Test
    public void testGetProjectDrivers() {
        try {
            PrivateAccessor.invoke(DiegoService.class, "getProjectDrivers", null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRootPackage() {
        String lRootPackage = null;
        try {
            lRootPackage = (String) PrivateAccessor.invoke(DiegoService.class, "getRootPackage", null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(lRootPackage);
    }
}
