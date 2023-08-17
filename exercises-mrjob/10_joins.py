# determine the total purchases per city

from mrjob.job import MRJob

class Joins (MRJob):

	def mapper (self, key, value):
		parts = value.split(',')

		# File 1 - customers
		#   key   = None
		#   value = (customer id, city)
		if len(parts) == 2: # customers table
			id   = parts[0]
			city = parts[1]
			yield id, (city, 'Customers')

		# File 2 - orders
		#   key   = None
		#   value = (customer id, item, amount)
		else: # orders table
			id     = parts[0]
			item   = parts[1]
			amount = parts[2]
			yield id, (amount, 'Orders')

	def reducer (self, key, value_list):
		amount = 0
		for a, b in value_list:
			if b == 'Customers':
				city = a
			else:
				amount = amount + int(a)
		yield city, amount

if __name__ == '__main__':
	Joins.run()