package spock.constraints

class Example {

    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
