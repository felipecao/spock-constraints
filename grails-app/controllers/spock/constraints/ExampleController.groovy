package spock.constraints

class ExampleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {ExampleCommand cmd ->

        if(cmd?.validate()){
            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            def ret = cmd.f_name ? Example.findAllByName(cmd.f_name, params) : Example.list(params)
            [exampleInstanceList: ret, exampleInstanceTotal: ret.size()]
        }
        else{
            [cmd:cmd, exampleInstanceList:[], exampleInstanceTotal:0]
        }
    }

    def create = {
        def exampleInstance = new Example()
        exampleInstance.properties = params
        return [exampleInstance: exampleInstance]
    }

    def save = {
        def exampleInstance = new Example(params)
        if (exampleInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'example.label', default: 'Example'), exampleInstance.id])}"
            redirect(action: "show", id: exampleInstance.id)
        }
        else {
            render(view: "create", model: [exampleInstance: exampleInstance])
        }
    }

    def show = {
        def exampleInstance = Example.get(params.id)
        if (!exampleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'example.label', default: 'Example'), params.id])}"
            redirect(action: "list")
        }
        else {
            [exampleInstance: exampleInstance]
        }
    }

    def edit = {
        def exampleInstance = Example.get(params.id)
        if (!exampleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'example.label', default: 'Example'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [exampleInstance: exampleInstance]
        }
    }

    def update = {
        def exampleInstance = Example.get(params.id)
        if (exampleInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (exampleInstance.version > version) {
                    
                    exampleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'example.label', default: 'Example')] as Object[], "Another user has updated this Example while you were editing")
                    render(view: "edit", model: [exampleInstance: exampleInstance])
                    return
                }
            }
            exampleInstance.properties = params
            if (!exampleInstance.hasErrors() && exampleInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'example.label', default: 'Example'), exampleInstance.id])}"
                redirect(action: "show", id: exampleInstance.id)
            }
            else {
                render(view: "edit", model: [exampleInstance: exampleInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'example.label', default: 'Example'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def exampleInstance = Example.get(params.id)
        if (exampleInstance) {
            try {
                exampleInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'example.label', default: 'Example'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'example.label', default: 'Example'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'example.label', default: 'Example'), params.id])}"
            redirect(action: "list")
        }
    }
}

class ExampleCommand {
    String f_name

    static constraints = {
        f_name(nullable: true, validator: {val, obj ->
            if(val && val.size() < 3){
                return "error.length.message"
            }
        })
    }
}
