# Thrones Consumer 

To wrap the linker in framework package and support the thrones protocol or hessian 

# Support 

* Multi attempts 
* Multi protocol
* JDK or Javaassist proxy 
* Load balance 
* trace 

# Resource definition

Define a simple language to describe the service at the remote server 

## BASE

{serviceType}:{serviceInterface}:{signature}:{parameters}

signature is not needed every time 

comma symbol style like 

{parameters} =\> {\[parameter1,type\],\[parameter2,type\]...} 

when assign with annotation then will generate a method to invoke this service 

the service endpoint will be injected from the context 

e.g.

jdk:org.freda.thrones.example.Demoservice:\[name,string\],\[age,int\]

like above then will invoke the remote procedure use jdk serializer 

## schema 

support JDK,Hessian

