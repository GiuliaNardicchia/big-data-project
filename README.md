
# Big Data Progetto

I datasets usati per questo progetto sono scaricabili dalla seguente cartella di [OneDrive](https://liveunibo-my.sharepoint.com/:f:/g/personal/giulia_nardicchia_studio_unibo_it/Ei2686kRO3JFrY-4LnImGpwBtge9FRErDnIgvT2h2QB-Pg?e=VrufWl):
- [itineraries-sample02.csv](https://liveunibo-my.sharepoint.com/:x:/g/personal/giulia_nardicchia_studio_unibo_it/Eev55wChE4ZEmziiu4ozDGQBZZEn3NNNhnpaof_O0sbtQw?e=2BCdKv) (*593 MB*)
- [itineraries-sample16.csv](https://liveunibo-my.sharepoint.com/:x:/g/personal/giulia_nardicchia_studio_unibo_it/EfFlDDrsJKhJqLlg3P0CzDgBdFzKS32tefS4x4sNGV2drg?e=4IOvIa) (*4.63 GB*)
- [itineraries-sample33.csv](https://liveunibo-my.sharepoint.com/:x:/g/personal/giulia_nardicchia_studio_unibo_it/EakEQx8NAHFNpzvWU8vraMQB9ji3U9wcCSUCbimpfafAgA?e=s7pXmF) (*9.55 GB*)


### Organizzazione di file e cartelle
- `src/` contiene il codice sorgente ed è suddiviso in linguaggio di programmazione
  - `python/` include i seguenti notebook:
    - [data-exploration.ipynb](src/main/python/understand_dataset/data-exploration.ipynb): dedicato alla comprensione e all'analisi esplorativa dei dati
    - [flight-prices.ipynb](src/main/python/understand_dataset/flight-prices.ipynb): implementa il *job* proposto, sia in versione non ottimizzata e una ottimizzata
  - `scala/`
- `history/` contiene la cronologia dell'esecuzione dei *job*
- `reports/` contiene report generati con Power BI