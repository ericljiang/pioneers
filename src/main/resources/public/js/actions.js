/**
 * Defines a client-generated action. Subtypes must exactly match subtypes of
 * Action.java.
 */
class Action {
    constructor() {
        this.type = this.constructor.name;
    }
}

class JoinAction extends Action {
    constructor(color) {
        super();
        this.color = color;
    }
}

class LeaveAction extends Action {
    constructor(color) {
        super();
        this.color = color;
    }
}

class StartAction extends Action {
    constructor() {
        super();
    }
}
