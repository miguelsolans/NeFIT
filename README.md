<p align="center">
   <img width="180" height="172" src="https://upload.wikimedia.org/wikipedia/commons/9/93/EEUMLOGO.png">
</p>

# NeFIT

## About
This repository holds the assignment for the curricular unit of Paradigms of Distributed Systems, inserted on the plan of studies of Distributed Systems of Computer Engineering Master Degree at University of Minho.
As such, with this project, we built trading system between manufacturers and importers, by incorporating several technologies such as Dropwizard, 

## Architecture
The final implementation has three distinct servers with different purposes.
- [Frontend](https://github.com/miguelsolans/NeFIT/tree/master/Frontend): Built with Erlang, handles the communication from client to REST catalog and Negotiator 
- [Catalog](https://github.com/miguelsolans/NeFIT/tree/master/catalog) - A REST Server built with Java and Dropwizard with the main purpose of storing information about different importers, manufacturers, product and transactions
- [Nagotiator](https://github.com/miguelsolans/NeFIT/tree/master/Negotiator) - Handles the negotiations of offers between Importers and Manufacturers and vice-versa
- [Client](https://github.com/miguelsolans/NeFIT/tree/master/Client) - Client application for frontend communication

## How to Run?

In order to execute all the servers listed, at the project root directory one must run the following commands

```
$ make
$ cd exec
$ java -jar nefit-1.0.jar
$ make run-frontend
$ java -jar exec/Negotiator-1.0.jar 4001
$ java -jar exec/Client-1.0.jar
```
 
<p align="center"><a href="#">Miguel R. Solans</a></p>
<p align="center"><a href="#">Tifany Silva</a></p>
<p align="center"><a href="#">João Silva</a></p>
<p align="center"><a href="#">Henrique Pereira</a></p>
<p align="center">2019/2020</p>
