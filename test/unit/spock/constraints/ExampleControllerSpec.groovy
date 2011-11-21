package spock.constraints

import grails.test.*
import grails.plugin.spock.ControllerSpec

class ExampleControllerSpec extends ControllerSpec {

    def setup(){
        mockDomain(Example, [new Example(name: "name1"), new Example(name: "name2")])
    }

    def "when I search for 'name1' I get an instance back"(){
        when:
            def ret = controller.list(new ExampleCommand(f_name: "name1"))

        then:
            !ret.cmd
            1 == ret.exampleInstanceTotal
    }

    def "if I search for something with less than 3 chars, I get an error message"(){
        when:
            def ret = controller.list(new ExampleCommand(f_name: "na"))

        then:
            0 == ret.exampleInstanceTotal
    }

}
