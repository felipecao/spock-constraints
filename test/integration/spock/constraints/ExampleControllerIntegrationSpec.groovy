package spock.constraints

import grails.plugin.spock.IntegrationSpec

/**
 * Created by IntelliJ IDEA.
 * User: Felipe
 * Date: 21/11/11
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
class ExampleControllerIntegrationSpec extends IntegrationSpec {

    def controller

    def setupSpec(){
        new Example(name: "name1").save()
        new Example(name: "name2").save()
    }

    def setup(){
        controller = new ExampleController()
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
            ret.cmd.hasErrors()
    }
}
