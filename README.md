# Microservices
This repository contains basics to build robust micoroservices applicaiton, during this repository will try to demonestrate knowledge to be shared with whom consern.
a lot of topics will be handled in this repository.

## Services:
 ### product micro-service :  
  basic functionality of catalog service like:
* add product and its information , images ,
* reteive one product or all products,
* delete product
* in future will add sorting criteria and filters and categories to match the well-known ecommerce websites isA.
### email micro-service :
will be updated to be responsible to handle emails notification to customers.

## Utilities
### Mapper Mini Framework:
it is a custome implemented mini framework to enable us from converting dto to entities and vsv.
this mini frame work is implemented using Reflection API.
it works without any cofigurations related to the destination Class. just you have to pass the Class of the destination class.

### Spring Aspect Oriented Programming ( Spring AOP ): 
I have integrted AOP to facilitate the prcess of logging and remove the poiler plate code and the not business code from our Controllers.

### Logging ( log4j2 ) : 
I have integrated with log4j2 as it is more secure than log4j.
used socket appender to log to ELK.

### Elastiksearch, Logstash and Kibana ( ELK ): 
i have installed then locally, and will provide here the configurations and how to install locally: 
and will share with you the logstash.conf file you can find it in the main folder of this project.

### 🚀 Run ELK Environment

#### 1. Elasticsearch  
> **Powerful search engine that stores and queries data in JSON format**

---

##### 📖 What it is:
A distributed, RESTful search and analytics engine.

##### 🎯 Main job:
Stores large amounts of data and enables fast search and analysis.

##### 🔑 Key points:
- Data is stored as **JSON documents**.
- Supports **full-text search**, **structured queries**, **metrics**, and **analytics**.
- **Distributed architecture** — scales horizontally by adding more nodes.
- **Extremely fast** — handles petabytes of data.

##### ▶️ How to start:
```bash
cd "C:\Tools\ELK\bin"
elasticsearch.bat
```

---

#### 2. Logstash  
> **Data processing pipeline that collects, transforms, and enriches data for Elasticsearch**

---

##### 📖 What it is:
A flexible data processing engine that ingests and transforms data from various sources.

##### 🎯 Main job:
Collects data, processes it (filtering, enriching, etc.), and sends it to Elasticsearch or other targets.

##### 🔑 Key points:
- Parses **logs, events, metrics**, and more.
- Can **filter**, **mask sensitive data**, and **enrich** data.
- Supports a wide variety of **input/output plugins** (files, databases, APIs, queues, etc.).
- Think of it as:  
  **Input → Filter → Output**

##### ▶️ How to start:
```bash
cd "C:\Tools\ELK\logstash-8.17.2\bin"
logstash.bat -f C:\Tools\ELK\logstash-8.17.2\logstash.conf
```

---

#### 3. Kibana  
> **Visualization and dashboarding tool for Elasticsearch data**

---

##### 📖 What it is:
A dashboarding and visualization platform that connects to Elasticsearch.

##### 🎯 Main job:
Allows users to explore, analyze, and visualize the data stored in Elasticsearch.

##### 🔑 Key points:
- Create **beautiful charts, tables, maps**, and **dashboards**.
- Build **alerts**, **reports**, and even **machine learning models**.
- Great for **business intelligence** and **real-time monitoring**.
- Fetches and displays data directly from **Elasticsearch**.

##### ▶️ How to start:
```bash
cd "C:\Tools\ELK\kibana-8.17.2\bin"
kibana.bat
```

---

#### 📚 Quick Overview

| Component    | Role                                                 | Start Command |
|--------------|------------------------------------------------------|---------------|
| Elasticsearch| Stores and searches JSON documents                   | `elasticsearch.bat` |
| Logstash     | Collects, processes, and ships data                  | `logstash.bat -f logstash.conf` |
| Kibana       | Visualizes data from Elasticsearch                   | `kibana.bat` |

---

✅ Once all three services are running, open **Kibana** in your browser:  
[http://localhost:5601](http://localhost:5601)


## Topics touched here:
all the following topics will be affected inside this reposatory. even if now or in future.

* clean spring boot archtecture
* using h2 In memory data base.
* Spring JPA
* Log4j confiugrations
* ELK ( Elasticsearch, Logstash and Kibana)
* will dockorize all the microservices
* security
* api gateway
* services registery
* more topics ......
  
