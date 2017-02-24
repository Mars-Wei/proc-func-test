package org.neo4j.procfunctest;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;

import java.util.stream.Stream;

public class ProceduresAndFunctions {


    // if you change this to a unprivileged injection (GraphDatabaseService) it works fine
    @Context
    public GraphDatabaseAPI graphDatabaseAPI;

    @Procedure
    public Stream<StringResult> sampleProcedure() {
        System.out.println(graphDatabaseAPI);
        return Stream.of(new StringResult("hello"));
    }

    @UserFunction
    public String sampleFunction() {
        return "hello";
    }

    public static class StringResult {
        public StringResult(String value) {
            this.value = value;
        }

        public String value;
    }
}
