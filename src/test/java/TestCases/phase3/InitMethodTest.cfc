component initMethod=birth accessors=true {
    property inittedProperly default=false;
    function init() {
        throw "you'd better not call me!";
    }
    function birth() {
        inittedProperly=true
    }
}