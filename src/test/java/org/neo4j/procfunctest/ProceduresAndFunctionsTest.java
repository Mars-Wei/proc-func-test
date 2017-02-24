package org.neo4j.procfunctest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.Iterators;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.test.TestGraphDatabaseFactory;

public class ProceduresAndFunctionsTest {

    private GraphDatabaseService graphDatabaseService;

    @Before
    public void setup() throws KernelException {
        graphDatabaseService = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder()
                .setConfig(GraphDatabaseSettings.procedure_unrestricted, "*")
                .newGraphDatabase();

        Procedures proceduresService = ((GraphDatabaseAPI) graphDatabaseService).getDependencyResolver().resolveDependency(Procedures.class);
        proceduresService.registerProcedure(ProceduresAndFunctions.class);

        // if you skip registration of the function the procedure test goes green
        proceduresService.registerFunction(ProceduresAndFunctions.class);
    }

    @After
    public void teardown() {
        graphDatabaseService.shutdown();
    }

    @Test
    public void shouldProcedureWork() {
        Result result = graphDatabaseService.execute("CALL org.neo4j.procfunctest.sampleProcedure() YIELD value RETURN value");
        Object v = Iterators.single(result.columnAs("value"));
        Assert.assertEquals("hello", v);
    }

    @Test
    public void shouldFunctionWork() {
        Result result = graphDatabaseService.execute("RETURN org.neo4j.procfunctest.sampleFunction() AS value");
        Object v = Iterators.single(result.columnAs("value"));
        Assert.assertEquals("hello", v);
    }
}
