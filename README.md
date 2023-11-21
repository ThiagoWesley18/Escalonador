# Escalonamento de Processos
**Para executar:**
1. javac *.java
2. java Escalonador.java [Argumento 1] [Argumento 2]  
   - Argumento 1:  
   1 -> FCFS  
   2 -> RR  
   3 -> SJF  
   4 -> SRTF  
   5 -> Prioc  
   6 -> Priop  

   - Argumento 2 - Path do arquivo de entrada com os processos
   - Exemplo: java Escalonador 1 entrada.txt

O sistema operacional ELF possui implementado em seu núcleo seis (6) algoritmos de escalonamento de processo 
(FCFS, RR, SJF, SRTF preemptivo, Prioc e Priop). Desta forma, implemente um (mas podem ser seis) programa(s) (código(s)) 
que ao receber como entrada uma sequência de processos contendo PID, tempo de ingresso na fila de prontos, duração, prioridade 
e tipo (CPU bound, I/O bound ou ambos) os coloque em execução da melhor forma possível. Seu código precisa definir no início quais
algoritmos poderão ser usados (no mínimo dois) e o quantum. Como saída, seu código deve informar a ordem de execução dos processos (PID) 
de acordo com o algoritmo escolhido com base do tipo do processo. Também deve ser fornecido o tempo médio de execução e de espera.
