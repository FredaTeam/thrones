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