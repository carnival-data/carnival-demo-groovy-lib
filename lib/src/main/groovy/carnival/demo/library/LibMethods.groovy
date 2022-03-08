package carnival.demo.library



import groovy.util.logging.Slf4j
import groovy.transform.ToString

import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import carnival.core.graph.GraphMethods
import carnival.core.graph.GraphMethod



@Slf4j 
class LibMethods implements GraphMethods { 


    LibVine libVine


    /** */
    public LibMethods(LibVine libVine) {
        this.libVine = libVine
    }


    /** */
    class LoadPeople extends GraphMethod {

        void execute(Graph graph, GraphTraversalSource g) {

            def mdt = libVine
                .method('People')
                .call()
            .result

            mdt.data.values().each { rec ->
                log.trace "rec: ${rec}"

                def first = rec.FIRST?.trim()
                def last = rec.LAST?.trim()

                if (!(first || last)) return

                def personV = LibModel.VX.PERSON.instance().create(graph)
                def nameV = LibModel.VX.NAME.instance().withNonNullProperties(
                    LibModel.PX.FIRST, first,
                    LibModel.PX.LAST, last
                ).ensure(graph, g)
                LibModel.EX.IS_CALLED.instance().from(personV).to(nameV).create()
            }

        }

    }


}