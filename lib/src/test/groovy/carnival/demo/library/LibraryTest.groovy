package carnival.demo.library


import spock.lang.Specification
import spock.lang.Shared

import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource

import carnival.core.graph.Core
import carnival.core.graph.CoreGraph
import carnival.core.graph.CoreGraphTinker



class LibraryTest extends Specification {


    ///////////////////////////////////////////////////////////////////////////
    // SHARED FIELDS
    ///////////////////////////////////////////////////////////////////////////

    @Shared CoreGraph coreGraph
    @Shared Graph graph
    @Shared GraphTraversalSource g


    ///////////////////////////////////////////////////////////////////////////
    // TEST SET UP
    ///////////////////////////////////////////////////////////////////////////

    def setupSpec() {
        coreGraph = CoreGraphTinker.create()
        coreGraph.withTraversal { Graph graph, GraphTraversalSource g ->
            coreGraph.initializeGremlinGraph(
                graph, g, 
                this.getClass().getPackage().getName()
            )
        }        
        graph = coreGraph.graph
    }

    def setup() {
        g = coreGraph.traversal()
    }

    def cleanup() {
        if (g) g.close()
    }

    def cleanupSpec() {
        if (graph) graph.close()
    }



    ///////////////////////////////////////////////////////////////////////////
    // TESTS
    ///////////////////////////////////////////////////////////////////////////


    void 'load people'() {
        expect:
        g.V().isa(LibModel.VX.PERSON).count().next() == 0
        g.V().isa(Core.VX.GRAPH_PROCESS).count().next() == 0

        when:
        def libVine = new LibVine()
        def libMethods = new LibMethods(libVine)
        libMethods.method('LoadPeople').call(graph, g)
        g.V().each { println "${it.label()}" }

        then:
        g.V().isa(LibModel.VX.PERSON).count().next() == 2
        g.V()
            .isa(LibModel.VX.PERSON)
            .out(LibModel.EX.IS_CALLED)
            .isa(LibModel.VX.NAME)
            .has(LibModel.PX.FIRST, 'Alice')
        .count().next() == 1
        g.V().isa(Core.VX.GRAPH_PROCESS).count().next() == 1
    }


    def "someLibraryMethod returns true"() {
        setup:
        def lib = new Library()

        when:
        def result = lib.someLibraryMethod()

        then:
        result == true
    }


}
