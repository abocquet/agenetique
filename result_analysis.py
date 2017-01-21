
import csv
data = {}

with open('logs/FullTest # 16:11:05.948_2017-01-20.csv', 'r') as csvfile:
    reader = csv.reader(csvfile, delimiter=';', quotechar='|')
    for row in reader:
        if len(row) < 4:
            continue

        key = (row[0], row[1], row[2])

        # v: somme des resultats, n: nombre de tests
        v, n = 0, 0
        if key in data:
            v, n = data[key]

        data[key] = v + int(row[3]), n + 1

for k in data:
    v, n = data[k]
    v = float(v) / n
    data[k] = v, n

    #Todo: calculer l'intervalle de fluctuation

print(data)