"""

Trace des graphes comparaifs des données issues d'un runAWholeBunchOfTest

"""

import pandas
import operator
import numpy as np
from plotly.offline import plot
from plotly.graph_objs import Scatter, Figure, Layout, Bar

def loadFile(filename, chartname):

	colnames = ["problem", "mutation", "crossover", "selection", "generations"] #generations: le nombre de générations pour approcher le résultat à 5%

	data = pandas.read_csv(filename, 'rb', engine='python', delimiter=";", names=colnames)
	gathered = {}

	for index, row in data.iterrows():

		key = row.mutation + " -  " + row.crossover + " - " + row.selection

		if not key in gathered:
			gathered[key] = []

		gathered[key].append(row.generations)

	nb_test = max([len(l) for l in gathered]) # Le nombre de tests effectués
	print(nb_test)
	for l in gathered: # si on a moins d'entrées que le nombre de test, c'est qu'il prenait trop de temps
		if len(gathered[l]) < nb_test:
			gathered[l] = [-1]


	gathered = {k: np.mean(v) for k, v in gathered.items()}
	gathered = sorted(gathered.items(), key=operator.itemgetter(1))

	return Bar(
		x= [row[0] for row in gathered],
		y= [row[1] for row in gathered],
		name = chartname
	)

files = ["lin105", "kro200"]
chart = [ loadFile("../logs/worth_analysis/" + filename + ".csv", filename) for filename in files]

plot(chart, filename='bar.html')
