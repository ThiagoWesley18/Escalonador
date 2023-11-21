import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/** java Escalonador.java [Argumento 1] [Argumento 2]
 // Argumento 1:
 1 - FCFS
 2 - RR
 3 - SJF
 4 - SRTF
 5 - Prioc
 6 - Priop

 // Argumento 2 - Path do arquivo de entrada com os processos

 **/

class Processo {

    String pid;
    int tempoChegada;
    int duracao;
    int prioridade;
    int tipo;
    int contExec = 0;
    int contEspera = 0;

    public Processo(String pid, int tempoChegada, int duracao, int prioridade,int tipo) {
        this.pid = pid;
        this.tempoChegada = tempoChegada;
        this.duracao = duracao;
        this.prioridade = prioridade;
        this.tipo = tipo;
    }
}

public class Escalonador {
    public static void main(String[] args) {
        List<Processo> processos = new ArrayList<>();
        String[] arg = new String[5];
        String linha;

        try {
            FileInputStream arqEntrada = new FileInputStream(args[1]);
            InputStreamReader input = new InputStreamReader(arqEntrada);
            BufferedReader reader = new BufferedReader(input);

            while ( (linha = reader.readLine() ) != null){
                arg = linha.split(" ");
                processos.add(new Processo(arg[0], Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]) , Integer.parseInt(arg[4])));
                for (String string : arg) {
                    string = null;
                }

            }
            arqEntrada.close();
            reader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int quantum = 2; // Defina o quantum para o algoritmo RR


        String algoritmoEscalonamento = args[0]; // Mude para "FCFS", "RR", "SJF", "SRTF", "Prioc" ou "Priop" conforme necessário

        switch (algoritmoEscalonamento) {
            case "1":
                executarFCFS(processos);
                break;
            case "2":
                executarRR(processos, quantum);
                break;
            case "3":
                executarSJF(processos);
                break;
            case "4":
                executarSRTF(processos);
                break;
            case "5":
                executarPrioc(processos);
                break;
            case "6":
                executarPriop(processos);
                break;
            default:
                System.out.println("Algoritmo de escalonamento inválido.");
        }
    }

    public static void executarFCFS(List<Processo> processos) {
        Queue<Processo> filaProntos = new LinkedBlockingQueue<>();
        List<Processo> processosConcluidos = new ArrayList<>();
        int tempoAtual = 0;

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            // Adicione processos à fila de prontos conforme o tempo de chegada
            while (!processos.isEmpty() && processos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processos.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                Processo processoAtual = filaProntos.poll();

                int tempoExecutado =processoAtual.duracao;
                //Math.min(processoAtual.duracao, 1);

                processoAtual.duracao -= tempoExecutado;
                tempoAtual += tempoExecutado;
                processoAtual.contExec += tempoExecutado;

                if (processoAtual.duracao > 0) {
                    filaProntos.add(processoAtual);
                } else {
                    processoAtual.contEspera = tempoAtual - (processoAtual.tempoChegada+processoAtual.contExec);
                    processoAtual.contExec = tempoAtual - processoAtual.tempoChegada;
                    processosConcluidos.add(processoAtual);
                }
            } else {
                tempoAtual++; // Ocioso
            }
        }

        calcularEImprimirMetricas(processosConcluidos);
    }

    public static void executarRR(List<Processo> processos, int quantum) {
        Queue<Processo> filaProntos = new LinkedBlockingQueue<>();
        List<Processo> processosConcluidos = new ArrayList<>();
        int tempoAtual = 0;

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            // Adicione processos à fila de prontos conforme o tempo de chegada
            while (!processos.isEmpty() && processos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processos.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                Processo processoAtual = filaProntos.poll();
                int tempoExecutado = Math.min(quantum, processoAtual.duracao);
                processoAtual.duracao -= tempoExecutado;
                tempoAtual += tempoExecutado;
                processoAtual.contExec += tempoExecutado;

                if (processoAtual.duracao > 0) {
                    filaProntos.add(processoAtual);
                } else {
                    processoAtual.contEspera = tempoAtual - (processoAtual.tempoChegada+processoAtual.contExec);
                    processoAtual.contExec = tempoAtual - processoAtual.tempoChegada;
                    processosConcluidos.add(processoAtual);
                }
            } else {
                tempoAtual++; // Ocioso
            }
        }

        calcularEImprimirMetricas(processosConcluidos);
    }

    public static void executarSJF(List<Processo> processos) {
        Queue<Processo> filaProntos = new PriorityQueue<>(Comparator.comparingInt(p -> p.duracao));
        List<Processo> processosConcluidos = new ArrayList<>();
        int tempoAtual = 0;

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            // Adicione processos à fila de prontos conforme o tempo de chegada
            while (!processos.isEmpty() && processos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processos.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                Processo processoAtual = filaProntos.poll();
                int tempoExecutado = Math.min(processoAtual.duracao, 1);
                processoAtual.duracao -= tempoExecutado;
                tempoAtual += tempoExecutado;
                processoAtual.contExec += tempoExecutado;

                if (processoAtual.duracao > 0) {
                    filaProntos.add(processoAtual);
                } else {
                    processoAtual.contEspera = tempoAtual - (processoAtual.tempoChegada+processoAtual.contExec);
                    processoAtual.contExec = tempoAtual - processoAtual.tempoChegada;
                    processosConcluidos.add(processoAtual);
                }
            } else {
                tempoAtual++; // Ocioso
            }
        }
        calcularEImprimirMetricas(processosConcluidos);
    }

    public static void executarSRTF(List<Processo> processos) {
        Queue<Processo> filaProntos = new PriorityQueue<>(Comparator.comparingInt(p -> p.duracao));
        List<Processo> processosConcluidos = new ArrayList<>();
        int tempoAtual = 0;

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            // Adicione processos à fila de prontos conforme o tempo de chegada
            while (!processos.isEmpty() && processos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processos.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                Processo processoAtual = filaProntos.poll();
                int tempoExecutado = Math.min(processoAtual.duracao, 1);
                processoAtual.duracao -= tempoExecutado;
                tempoAtual += tempoExecutado;
                processoAtual.contExec += tempoExecutado;

                if (processoAtual.duracao > 0) {
                    filaProntos.add(processoAtual);
                } else {
                    processoAtual.contEspera = tempoAtual - (processoAtual.tempoChegada+processoAtual.contExec);
                    processoAtual.contExec = tempoAtual - processoAtual.tempoChegada;
                    processosConcluidos.add(processoAtual);
                }
            } else {
                tempoAtual++; // Ocioso
            }
        }

        calcularEImprimirMetricas(processosConcluidos);
    }

    public static void executarPrioc(List<Processo> processos) {
        Queue<Processo> filaProntos = new PriorityQueue<>(Comparator.comparingInt(p -> -p.prioridade));
        List<Processo> processosConcluidos = new ArrayList<>();
        int tempoAtual = 0;

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            // Adicione processos à fila de prontos conforme o tempo de chegada
            while (!processos.isEmpty() && processos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processos.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                Processo processoAtual = filaProntos.poll();
                int tempoExecutado = processoAtual.duracao;
                processoAtual.duracao -= tempoExecutado;
                tempoAtual += tempoExecutado;
                processoAtual.contExec += tempoExecutado;

                if (processoAtual.duracao > 0) {
                    filaProntos.add(processoAtual);
                } else {
                    processoAtual.contEspera = tempoAtual - (processoAtual.tempoChegada+processoAtual.contExec);
                    processoAtual.contExec = tempoAtual - processoAtual.tempoChegada;
                    processosConcluidos.add(processoAtual);
                }
            } else {
                tempoAtual++; // Ocioso
            }
        }

        calcularEImprimirMetricas(processosConcluidos);
    }

    public static void executarPriop(List<Processo> processos) {
        Queue<Processo> filaProntos = new PriorityQueue<>(Comparator.comparingInt(p -> -p.prioridade));
        List<Processo> processosConcluidos = new ArrayList<>();
        int tempoAtual = 0;

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            // Adicione processos à fila de prontos conforme o tempo de chegada
            while (!processos.isEmpty() && processos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processos.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                Processo processoAtual = filaProntos.poll();
                int tempoExecutado = Math.min(processoAtual.duracao, 1);
                processoAtual.duracao -= tempoExecutado;
                tempoAtual += tempoExecutado;
                processoAtual.contExec += tempoExecutado;

                if (processoAtual.duracao > 0) {
                    filaProntos.add(processoAtual);
                } else {
                    processoAtual.contEspera = tempoAtual - (processoAtual.tempoChegada+processoAtual.contExec);
                    processoAtual.contExec = tempoAtual - processoAtual.tempoChegada;
                    processosConcluidos.add(processoAtual);
                }
            } else {
                tempoAtual++; // Ocioso
            }
        }

        calcularEImprimirMetricas(processosConcluidos);
    }

    public static void calcularEImprimirMetricas(List<Processo> processosConcluidos) {
        double tempoTotalExecucao = 0;
        double tempoTotalEspera = 0;
        System.out.println("Ordem de execução dos processos:");
        for (Processo processo : processosConcluidos) {
            System.out.print(processo.pid + " ");
            tempoTotalExecucao += processo.contExec;
            tempoTotalEspera += processo.contEspera;
        }
        double tempoMedioExecucao = tempoTotalExecucao / processosConcluidos.size();
        double tempoMedioEspera = tempoTotalEspera / processosConcluidos.size();
        System.out.println("\nTempo Médio de Execução: " + tempoMedioExecucao);
        System.out.println("Tempo Médio de Espera: " + tempoMedioEspera);
    }
}