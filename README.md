# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 50 - Campus Taguspark

Afonso Caetano 82539 afonso.caetano@tecnico.ulisboa.pt

Bruno Santos 82053 bruno.o.santos@tecnico.ulisboa.pt

Ricardo Pereira 82010 ricardo.moreira.pereira@tecnico.ulisboa.pt


Repositório:
[tecnico-softeng-distsys-2015/T_50-project](https://github.com/tecnico-softeng-distsys-2015/T_50-project/)

-------------------------------------------------------------------------------

## Instruções de instalação


### Ambiente

[0] Iniciar sistema operativo

Linux

[1] Iniciar servidores de apoio

JUDDI:
```
...
```


[2] Criar pasta temporária

```
cd Documents
mkdir WebServices
```


[3] Obter código fonte do projeto (versão entregue)

```
fénix
git clone https://github.com/tecnico-softeng-distsys-2015/T_50-project.git
```



[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
cd ws-handlers
mvn clean install
```

-------------------------------------------------------------------------------

### Serviço CA

[1] Construir e executar **servidor**

```
cd ca-ws
mvn clean install
mvn exec:java
```

[2] Construir **cliente** e executar tarefas

```
cd ca-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd transporter-ws
mvn clean install
mvn exec:java
```

[2] Construir e executar **servidor**

```
cd transporter-ws
mvn -Dws.i=2 exec:java
```

[3] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidorBackup**

```
cd broker-ws
mvn clean install
mvn -Dws.i=2 exec:java
```

[2] Construir e executar **servidorPrimário**

```
cd broker-ws
mvn exec:java
```


[3] Construir **cliente** e executar testes

```
cd broker-ws-cli
mvn clean install
```

...

-------------------------------------------------------------------------------
**FIM**
