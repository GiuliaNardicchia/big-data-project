# History

Di seguito sono riportati i nomi dei file di *history* generati durante l'esecuzione dei *job* negli ambienti locale e
remoto.
Questi identificatori univoci consentono il tracciamento delle esecuzioni e la verifica dei risultati.
Ogni *job* è stato eseguito su diversi *sample* del dataset, sia nella versione non ottimizzata che in quella
ottimizzata.

- Local:
    - Sample 02:
        - Job Not Optimized: `local-1743585666769`
        - Job Optimized: `local-1743585734663`
    - Sample 16:
        - Job Not Optimized: `local-1743586572039`
        - Job Optimized: `local-1743586933630`
    - Sample 33:
        - Job Not Optimized: `local-1743588504495`
        - Job Optimized: `local-1743588984024`
- Remote:
    - Sample 02:
        - Job Not Optimized: `application_1743584194952_0007`
        - Job Optimized: `application_1743584194952_0008`
    - Sample 16:
        - Job Not Optimized: `application_1743584194952_0009`
        - Job Optimized: `application_1743584194952_0010`
    - Sample 33:
        - Job Not Optimized: `application_1743584194952_0011`
        - Job Optimized: `application_1743584194952_0012`

## History Server

Per avviare lo History Server, aprire un Git Bash nella cartella di installazione di Spark
(`C:\spark-3.5.1-bin-hadoop3`) ed eseguire il seguente comando:

```shell
  bin/spark-class.cmd org.apache.spark.deploy.history.HistoryServer
```

Per visualizzare i file di history, è possibile accedere alla *Spark UI* all'indirizzo: http://localhost:18080.