package eu.labrush.traveller;

import eu.labrush.agenetic.Triple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.agenetic.operators.SelectorInterface;
import eu.labrush.agenetic.operators.selection.BiasedWheelSelector;
import eu.labrush.agenetic.operators.selection.WheelAndRandomSelector;
import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.*;
import eu.labrush.traveller.operators.reproduction.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {

		//System.out.println(factory.getProblems());

		//runTests(new String[]{"d15112"});
		//runTests(new String[]{"berlin52", "kroA100", "kroA150", "kroA200", "lin318", "pr439", "rat575", "rat783", "rl1304", "rl1889"});
		//runAWholeBunchOfTests("st70", 10);

		runAWholeBunchOfTests("lin105", 50, loadCombinaisonsToAvoid(null));
		//runPythonAnalysis(runAWholeBunchOfTests("lin105", 50, loadCombinaisonsToAvoid(null)));
		System.out.println("It's all done !");

	}

	private static Triple<String,String, String>[] loadCombinaisonsToAvoid(String filename) {
		ArrayList<Triple<String, String, String>> t = new ArrayList<>();

		if(filename != null) {
			try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
				String line;
				while ((line = br.readLine()) != null) {
					String data[] = line.split("( |	)+");
					t.add(new Triple<>(data[0], data[1], data[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Triple<String,String,String>[] res = new Triple[t.size()];

		for (int i = 0; i < t.size(); i++) {
			res[i] = t.get(i);
		}


		return res ;
	}

	private static void runPythonAnalysis(String filename){

		try {
			String s = "";
			Process p = Runtime.getRuntime().exec("python python_analysis/statistic_check.py " + filename);

			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("\nHere is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e){
			e.printStackTrace();
		}

	}

	/**
	 * Mostly used for a single test in a big (huge ?) sample
	 * @param names
	 */
	private static void runTests(String[] names){
		PointSetFactory factory = new PointSetFactory() ;

		for(String name: names){
			PointSet problem = factory.getSet(name);
			System.out.println(problem.getDesc());

			Nature nature = new Nature(50, 1, 0.5, 0.05, 0.01, problem, new ArcCombination(), new Im(), new WheelAndRandomSelector());
			Logger logger = new Logger("logs/", problem.getName() + "_" + System.currentTimeMillis() + ".csv", nature);

			int i = 0, p = 1000;
			while (nature.getShortest() * 100 > problem.getMinDist() * 103) {
				if (i % p == 0) {
					logger.log(true);
					System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
				}

				nature.evolve(false);
				i++;
			}

			logger.log(true);
			System.out.println("Problem " + name + " génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");
		}
	}

  /**
   * [runAWholeBunchOfTests description]
   * @param  name            the name of the problem
   * @param  number_of_tests number of tests to run for each combinaison
   * @param  avoid           a list of couples not to test
   * @return the name of the file where the results are
   */

	private static String runAWholeBunchOfTests(String name, int number_of_tests, Triple<String, String, String>[] avoid){

		PointSetFactory factory = new PointSetFactory();
		PointSet set = factory.getSet(name);

		MutationInterface[] mutations = new MutationInterface[]{new Cim(), new Im(), new Throas(), new Thrors(), new Twor()};
		CrossoverInterface[] crossover = new CrossoverInterface[]{new ArcCombination(), new AssortiPartiel(), new Cyclic(), new MaximalPreservation(), new Order1(), new Order2(), new Syswerda(), new Uniform() };
		SelectorInterface[] selection = new SelectorInterface[]{new WheelAndRandomSelector(), new BiasedWheelSelector()} ;

		//MutationInterface[] mutations = new MutationInterface[]{new Im(), new Cim()};
		//CrossoverInterface[] crossover = new CrossoverInterface[]{new Order1(), new Order2()};
		//SelectorInterface[] selection = new SelectorInterface[]{new WheelAndRandomSelector()} ;

		String filename = "error";

		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS_yyyy-MM-dd");
			//filename = "logs/" + sdf.format(new Date()) + ".csv" ;
			filename = "logs/"  + System.currentTimeMillis() + ".csv" ;
			PrintWriter logger = new PrintWriter(filename, "UTF-8");

			int total = mutations.length  * crossover.length * selection.length * number_of_tests ;
			int counter = 1 ;
			int limit = 100000 ;

			for (MutationInterface mo: mutations){
				for(CrossoverInterface co: crossover) {
					for (SelectorInterface so : selection) {

						Triple<String, String, String> conf = new Triple<>(
								mo.getClass().getSimpleName(),
								co.getClass().getSimpleName(),
								so.getClass().getSimpleName()
						);

						if(Arrays.asList(avoid).contains(conf)) {
							counter += number_of_tests ;
							System.out.print(counter + " / " + total + " ");
							System.out.println(conf + " avoided !");
							continue;
						}

						for (int i = 0; i < number_of_tests; i++) {
							int result = runTestForConf(set, co, mo, so, limit);

							if (result > limit){
								counter += number_of_tests - i ;
								System.out.print(counter + " / " + total + " ");
								System.out.println(conf + " too long");
								break ;
							}

							String str = "";
							str += set.getName() + ";";
							str += mo.getClass().getSimpleName() + ";";
							str += co.getClass().getSimpleName() + ";";
							str += so.getClass().getSimpleName() + ";";
							str += result;
							str += "\n";

							logger.print(str);

							System.out.print(counter + " / " + total + " ");
							counter++;
						}

						logger.flush();
					}
				}

			}

			logger.print("\n") ;
			logger.flush();
			logger.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return filename ;

	}

	private static int runTestForConf(PointSet problem, CrossoverInterface co, MutationInterface mo, SelectorInterface so, int limit) {
		Nature nature = new Nature(50, 3,0.5, 0.05, 0.01, problem, co, mo, so);

		int i = 0, p = 1000;
		while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
			if(i > limit){
				System.err.println("Too long...");
				break ;
			}

			nature.evolve(false);
			i++;
		}

		System.out.println(
				co.getClass().getSimpleName() + " - " + mo.getClass().getSimpleName() + " - " + so.getClass().getSimpleName() + " - "
				+ problem.getName() + " génération " + i + " : " + nature.getShortest() + " / "  + problem.getMinDist()
		);

		return i ;
	}

}
