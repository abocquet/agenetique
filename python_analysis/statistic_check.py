from scipy.special import erfinv
from math import sqrt
import csv
import sys

data = {}

alpha = 0.95
epsilon = erfinv(alpha)

print sys.argv[1]
file = sys.argv[1]

with open(file, 'r') as csvfile:
    reader = csv.reader(csvfile, delimiter=';', quotechar='|')
    for row in reader:
        if len(row) < 5:
            continue

        key = (row[0], row[1], row[2], row[3])

        if not key in data:
            data[key] = []

        data[key].append(float(row[4]))


for k, v in data.items():

    n = len(v)

    m = reduce(lambda x,y: x+y, v) / n
    ecarts = map(lambda x: x*x - m*m, v)
    sigma = reduce(lambda x,y: x+y, ecarts) / n
    sigma = sqrt(sigma)

    intervalle_de_confiance = (int(m - epsilon * sigma / sqrt(n)), int(m + epsilon * sigma / sqrt(n)))

    print(k, n, intervalle_de_confiance)

