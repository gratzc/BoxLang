component accessors=true {
    @preAnno
    property name="myProperty" default="myDefaultValue" type=string inject;
    
    /**
     * This is my property
     * @brad wood
     * @luis
     */
    @preanno "myValue" "anothervalue"
    property string anotherprop;

    function init() {
        getMyProperty();
    }
}