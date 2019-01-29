package br.usp.each.saeg.jaguar.core.cli;

import br.usp.each.saeg.jaguar.core.heuristic.Heuristic;

public class JaguarCli {

    private Heuristic getHeuristic(String heuristic) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (Heuristic) Class.forName(
                "br.usp.each.saeg.jaguar.core.heuristic." + heuristic + "Heuristic").newInstance();
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        System.out.println("Running Jaguar CLI");

        JaguarCli jaguarCli = new JaguarCli();
        Heuristic heuristic = jaguarCli.getHeuristic(args[0]);

        Integer cef = Integer.valueOf(args[1]);
        Integer cnf = Integer.valueOf(args[2]);
        Integer cep = Integer.valueOf(args[3]);
        Integer cnp = Integer.valueOf(args[4]);

        double score = heuristic.eval(cef, cnf, cep, cnp);

        System.out.println("score=" + score);
    }
}
