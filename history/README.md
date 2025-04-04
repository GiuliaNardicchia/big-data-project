# History

Di seguito sono riportati i nomi dei file di *history* generati durante l'esecuzione dei *job* in **locale** e
su **AWS**.
Questi identificatori univoci consentono il tracciamento delle esecuzioni e la verifica dei risultati.
Ogni *job* è stato eseguito su diversi *sample* del dataset, sia nella versione **non ottimizzata** che in quella
**ottimizzata**.

- Sample 02:
    - local - job not optimized: `local-1743585666769`
    - local - job optimized: `local-1743585734663`
    - remote - job not optimized: `application_1743584194952_0007`
    - remote - job optimized: `application_1743584194952_0008`
- Sample 16:
    - local - job not optimized: `local-1743586572039`
    - local - job optimized: `local-1743586933630`
    - remote - job not optimized: `application_1743584194952_0009`
    - remote - job optimized: `application_1743584194952_0010`
- Sample 33:
    - local - job not optimized: `local-1743588504495`
    - local - job optimized: `local-1743588984024`
    - remote - job not optimized: `application_1743584194952_0011`
    - remote - job optimized: `application_1743584194952_0012`

## History Server

Per avviare lo History Server, copiare tutti i file di log e incollarli nella cartella *history*, che si trova
all'interno
della directory di installazione di Spark (`C:\spark-3.5.1-bin-hadoop3\history`).

Successivamente, aprire Git Bash nella directory di installazione di Spark (`C:\spark-3.5.1-bin-hadoop3`) ed eseguire il
seguente comando:

```shell
  bin/spark-class.cmd org.apache.spark.deploy.history.HistoryServer
```

Per visualizzare i file di *history*, è possibile accedere alla *Spark UI* all'indirizzo: http://localhost:18080.

## Analisi delle prestazioni

### Confronto dei tempi di esecuzione

Di seguito sono riportati i tempi di esecuzione dei *job*.

| **Sample** | **Execution Type** | **Not Optimized (min)** | **Optimized (min)** | **Difference (min)** |
|------------|--------------------|-------------------------|---------------------|----------------------|
| 2          | local              | 0,35                    | 0,33                | 0,02                 |
| 2          | remote             | 0,58                    | 0,58                | 0,00                 |
| 16         | local              | 4,6                     | 3,7                 | 0,9                  |
| 16         | remote             | 2,3                     | 1,8                 | 0,5                  |
| 33         | local              | 7,4                     | 7,0                 | 0,4                  |
| 33         | remote             | 3,7                     | 2,8                 | 0,9                  |

In generale, i *job* ottimizzati hanno mostrato un miglioramento nei tempi di esecuzione rispetto a quelli non
ottimizzati.
Per i *sample* di dimensioni ridotte, la differenza nei tempi è trascurabile, mentre all'aumentare della dimensione del
*sample* il beneficio dell'ottimizzazione diventa più evidente, anche se non sempre drastico.

### Confronto delle esecuzioni

Di seguito sono riportate le schermate dei *job* eseguiti in **remoto** relative al *sample* 33, sia nella versione
**non ottimizzata** che nella versione **ottimizzata**.
Le schermate mostrano le informazioni relative ai *job*, agli *stages* e agli *executors*.

#### Stages

L'analisi delle schermate evidenzia una differenza nel numero di stage completati tra le due versioni del job:

- versione non ottimizzata: 5 stage completati e 1 stage saltato;
- versione ottimizzata: 4 stage completati e 1 stage saltato.

L'ottimizzazione ha ridotto il numero di stage, eliminando uno step intermedio.

#### Tasks

Analizzando i task eseguiti per ciascun stage:

- versione non ottimizzata: gli stage contengono 319 task, a eccezione dello stage di salvataggio finale (con 1 task);
- versione ottimizzata: mentre il primo stage mantiene 319 task, gli stage successivi sono stati ridotti a 24 task, a
  eccezione dello stage di salvataggio finale (con 1 task).

#### Shuffle Read e Shuffle Write

Il confronto delle metriche di Shuffle Read e Shuffle Write evidenzia un leggero aumento dello shuffle nella versione
ottimizzata.

Sebbene il job ottimizzato abbia generato un maggiore shuffle, ha comunque ridotto complessivamente i tempi di
esecuzione grazie alla diminuzione del numero di task negli stage successivi al primo.

![jobs page not optimized](/history/img/remote/remote-jobs-no-sample33.png)

![jobs page optimized](/history/img/remote/remote-jobs-o-sample33.png)

![stages page not optimized](/history/img/remote/remote-stages-no-sample33.png)

![stages page optimized](/history/img/remote/remote-stages-o-sample33.png)

| ![stages page DAG 1 not optimized](/history/img/remote/remote-stages-1-dag-no-sample33.png) | ![stages page DAG 1 optimized](/history/img/remote/remote-stages-1-dag-o-sample33.png) |
|---------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|

| ![stages page DAG 2 not optimized](/history/img/remote/remote-stages-2-dag-no-sample33.png) | ![stages page DAG 2 optimized](/history/img/remote/remote-stages-2-dag-o-sample33.png) |
|---------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|