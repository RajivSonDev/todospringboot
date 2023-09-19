package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

public class HelloWorldBean {

    private String messgae;

    public HelloWorldBean(String helloWorld) {
        this.messgae=helloWorld;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }


    @Override
    public String toString() {
        return "HelloWorldBean{" +
                "messgae='" + messgae + '\'' +
                '}';
    }

}
