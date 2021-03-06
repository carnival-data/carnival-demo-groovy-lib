package carnival.demo.library



import carnival.graph.VertexDefinition
import carnival.graph.PropertyDefinition
import carnival.graph.EdgeDefinition

import carnival.core.graph.Core



class LibModel {

    @VertexDefinition
    static enum VX {
        PERSON,

        NAME(
            propertyDefs:[
                PX.FIRST,
                PX.LAST
            ]
        )
    }

    
    @PropertyDefinition
    static enum PX {
        FIRST,
        LAST
    }


    @EdgeDefinition
    static enum EX {
        IS_CALLED(
            domain:[VX.PERSON],
            range:[VX.NAME]
        )
    }

}