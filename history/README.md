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

## Analisi delle prestazioni

### Confronto dei tempi di esecuzione

Di seguito sono riportati i tempi di esecuzione dei *job* eseguiti in **locale** e su **AWS**, sia nella versione **non
ottimizzata** che **ottimizzata**.

| **Sample** | **Execution Type** | **Not Optimized (min)** | **Optimized (min)** |
|------------|--------------------|-------------------------|---------------------|
| 2          | local              | 0,35                    | 0,33                |
| 2          | remote             | 0,58                    | 0,58                |
| 16         | local              | 4,6                     | 3,7                 |
| 16         | remote             | 2,3                     | 1,8                 |
| 33         | local              | 7,4                     | 7,0                 |
| 33         | remote             | 3,7                     | 2,8                 |

In generale, i *job* ottimizzati hanno mostrato un miglioramento nei tempi, rispetto a quelli non ottimizzati.
Per quanto riguarda i sample piccoli, le differenze di tempo sono minime, mentre per i sample più grandi la differenza
si nota maggiormente.

### Confronto delle esecuzioni

![TODO](/history/img/remote/remote-jobs-no-sample33.png)

![TODO](/history/img/remote/remote-jobs-o-sample33.png)

![TODO](/history/img/remote/remote-stages-no-sample33.png)

![TODO](/history/img/remote/remote-stages-o-sample33.png)

| ![TODO](/history/img/remote/remote-stages-1-dag-no-sample33.png) | ![TODO](/history/img/remote/remote-stages-1-dag-o-sample33.png) |
|------------------------------------------------------------------|-----------------------------------------------------------------|

| ![TODO](/history/img/remote/remote-stages-2-dag-no-sample33.png) | ![TODO](/history/img/remote/remote-stages-2-dag-o-sample33.png) |
|------------------------------------------------------------------|-----------------------------------------------------------------|

![TODO](/history/img/remote/remote-executors-no-sample33.png)

![TODO](/history/img/remote/remote-executors-o-sample33.png)