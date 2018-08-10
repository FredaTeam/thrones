# Thrones

# Server Architecture
## RPC Layer

This layer includes the encapsulation of tcp,protocol,coder,decoder and so
on.

## FilterChain Layer

This layer includes functions as follows:

- Load balance
- performance metrics
- advice
- retry mechanism

## Service Layer
This layer includes the dymatic proxy for client 
calling.

## Base on Netty and other serializer framework

Netty which is an advanced TCP network in many project action usage . And Base on Netty 
We will construct a independent message protocol in use to transmit information which contains
the **Message Meta** **Message Body** . 

Use the very popular framework to serialize the information in the TCP delivery , will have
the high performance in action.

## Choice to Disruptor 

## Use Javaassit framework to implement the dynamical proxy  